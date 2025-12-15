package com.cts.clients;
 
import java.net.Authenticator;
 
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
 
import feign.RequestInterceptor;
import feign.RequestTemplate;
 
@Component
public class FeignClientInterceptor implements RequestInterceptor{
 
	@Override
	public void apply(RequestTemplate template) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			System.out.println("🔐 [FeignInterceptor] Current Authentication: " + (auth != null ? auth.getName() : "NULL"));
			
			if (auth instanceof JwtAuthenticationToken jwtAuth) {
				String token = jwtAuth.getToken().getTokenValue();
				template.header("Authorization", "Bearer " + token);
				System.out.println("✅ [FeignInterceptor] JWT token added to request");
			} else {
				System.out.println("⚠️ [FeignInterceptor] Authentication is not JwtAuthenticationToken: " + (auth != null ? auth.getClass().getName() : "NULL"));
			}
		} catch (Exception e) {
			System.out.println("❌ [FeignInterceptor] Error adding JWT token: " + e.getMessage());
			e.printStackTrace();
		}
	}
}