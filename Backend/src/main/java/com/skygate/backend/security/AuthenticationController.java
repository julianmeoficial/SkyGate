package com.skygate.backend.security;

import com.skygate.backend.model.dto.response.ApiResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public AuthenticationController(JwtTokenProvider tokenProvider, CustomUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> login(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Login attempt for user: {}", loginRequest.getUsername());

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

            String token = tokenProvider.generateToken(loginRequest.getUsername());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", loginRequest.getUsername());
            response.put("type", "Bearer");

            logger.info("Login successful for user: {}", loginRequest.getUsername());

            return ResponseEntity.ok(ApiResponseDTO.success("Login successful", response));

        } catch (Exception e) {
            logger.error("Login failed for user: {}", loginRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error("Invalid credentials"));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> validateToken(@RequestBody TokenValidationRequest request) {
        try {
            String token = request.getToken();

            if (tokenProvider.validateToken(token)) {
                String username = tokenProvider.extractUsername(token);

                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("username", username);

                return ResponseEntity.ok(ApiResponseDTO.success("Token is valid", response));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponseDTO.error("Invalid token"));
            }
        } catch (Exception e) {
            logger.error("Token validation failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error("Token validation failed"));
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class TokenValidationRequest {
        private String token;

        public TokenValidationRequest() {
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
