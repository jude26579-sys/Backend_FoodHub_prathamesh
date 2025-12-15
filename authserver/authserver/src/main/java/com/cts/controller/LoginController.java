package com.cts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/login")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8082", "http://localhost:8083", "http://localhost:8084"}, allowCredentials = "true")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    public static class LoginRequest {
        public String email;
        public String password;

        public LoginRequest() {}
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    @PostMapping("/customer")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return authenticateUserAndCreateSession(request.email, request.password, "ROLE_CUSTOMER", httpRequest);
    }

    @PostMapping("/vendor")
    public ResponseEntity<?> loginVendor(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return authenticateUserAndCreateSession(request.email, request.password, "ROLE_VENDOR", httpRequest);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return authenticateUserAndCreateSession(request.email, request.password, "ROLE_ADMIN", httpRequest);
    }

    @PostMapping
    public ResponseEntity<?> loginGeneric(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        // Generic endpoint that auto-detects role from database
        return authenticateUserAndCreateSessionAutoRole(request.email, request.password, httpRequest);
    }

    private ResponseEntity<?> authenticateUserAndCreateSession(String email, String password, String expectedRole, HttpServletRequest request) {
        try {
            System.out.println("=== Login attempt for: " + email + " with role: " + expectedRole + " ===");
            
            // Authenticate using email as username
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );

            System.out.println("Authentication successful for: " + email);
            System.out.println("User authorities: " + authentication.getAuthorities());

            // Create a new security context
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            
            // Set in SecurityContextHolder for current thread
            SecurityContextHolder.setContext(securityContext);
            
            System.out.println("SecurityContext set in SecurityContextHolder");

            // Store the security context in the session so it persists across requests
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            
            // Make sure session is marked as valid
            session.setAttribute("SPRING_SECURITY_LAST_USERNAME", email);

            System.out.println("Session created with ID: " + session.getId());
            System.out.println("Security context stored in session");
            System.out.println("Session timeout: " + session.getMaxInactiveInterval() + " seconds");

            // Return success response
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Authentication successful");
            response.put("email", email);
            String roleString = expectedRole.replace("ROLE_", "").toLowerCase();
            response.put("role", roleString);
            response.put("sessionId", session.getId());
            
            // Generate authorize URL based on role
            String authorizeUrl;
            if (roleString.equals("admin")) {
                authorizeUrl = "http://localhost:9001/oauth2/authorize?client_id=adminclient&response_type=code&scope=read%20write&redirect_uri=http://localhost:8084/auth/callback&state=xyz";
            } else if (roleString.equals("vendor")) {
                authorizeUrl = "http://localhost:9001/oauth2/authorize?client_id=vendorclient&response_type=code&scope=read%20write&redirect_uri=http://localhost:8084/auth/callback&state=xyz";
            } else {
                // Default to customer
                authorizeUrl = "http://localhost:9001/oauth2/authorize?client_id=customerclient&response_type=code&scope=read%20write&redirect_uri=http://localhost:8084/auth/callback&state=xyz";
            }
            response.put("authorizeUrl", authorizeUrl);

            System.out.println("=== Login successful, returning response ===");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("=== Login failed for email: " + email + " ===");
            System.err.println("Error type: " + e.getClass().getName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            
            String errorDetail = e.getMessage();
            if (e.getMessage() == null) {
                errorDetail = "Authentication failed";
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                    "error", "Invalid email or password",
                    "details", errorDetail,
                    "errorType", e.getClass().getSimpleName()
                ));
        }
    }

    private ResponseEntity<?> authenticateUserAndCreateSessionAutoRole(String email, String password, HttpServletRequest request) {
        try {
            System.out.println("=== Generic Login attempt for: " + email + " (role will be auto-detected) ===");
            
            // Authenticate using email as username
            // CustomUserDetailsService will check Admin, then Customer, then Vendor
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );

            System.out.println("Authentication successful for: " + email);
            System.out.println("User authorities: " + authentication.getAuthorities());

            // Extract the actual role from authentication
            String actualRole = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .findFirst()
                .orElse("ROLE_CUSTOMER");
            
            System.out.println("Actual role detected: " + actualRole);

            // Create a new security context
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            
            // Set in SecurityContextHolder for current thread
            SecurityContextHolder.setContext(securityContext);
            
            System.out.println("SecurityContext set in SecurityContextHolder");

            // Store the security context in the session so it persists across requests
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            
            // Make sure session is marked as valid
            session.setAttribute("SPRING_SECURITY_LAST_USERNAME", email);

            System.out.println("Session created with ID: " + session.getId());
            System.out.println("Security context stored in session");

            // Return success response with ACTUAL role from database
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Authentication successful");
            response.put("email", email);
            String roleString = actualRole.replace("ROLE_", "").toLowerCase();
            response.put("role", roleString);
            response.put("sessionId", session.getId());
            
            System.out.println("=== Login successful with actual role: " + roleString + " ===");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("=== Generic Login failed for email: " + email + " ===");
            System.err.println("Error type: " + e.getClass().getName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            
            String errorDetail = e.getMessage();
            if (e.getMessage() == null) {
                errorDetail = "Authentication failed";
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                    "error", "Invalid email or password",
                    "details", errorDetail,
                    "errorType", e.getClass().getSimpleName()
                ));
        }
    }
}

