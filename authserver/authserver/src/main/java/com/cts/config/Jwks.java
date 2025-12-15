package com.cts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

@Configuration
public class Jwks {
	
	// Static RSA key - same key across all server restarts
	private static RSAKey rsaKey;
	
	static {
		try {
			// Generate key once and reuse it
			rsaKey = new RSAKeyGenerator(2048)
					.keyID("auth-server-key")
					.generate();
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate RSA key", e);
		}
	}
	
	@Bean
	public RSAKey rsaKey() throws Exception{
		return rsaKey;
	}
	
}
			
		
	


