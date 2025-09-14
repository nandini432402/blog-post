package com.example.blogNest.domain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT Configuration Properties
 *
 * Centralizes all JWT-related configuration settings that can be
 * overridden via application.properties or environment variables
 */
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {

    /**
     * Secret key for JWT token signing and verification
     * Should be kept secure and never exposed
     */
    private String secret = "blogNestSecretKey2024ForJWTTokenGeneration";

    /**
     * JWT token expiration time in milliseconds
     * Default: 24 hours (86400000 ms)
     */
    private long expiration = 86400000;

    /**
     * Refresh token expiration time in milliseconds
     * Default: 7 days (604800000 ms)
     */
    private long refreshExpiration = 604800000;

    /**
     * JWT token prefix used in Authorization header
     * Default: "Bearer "
     */
    private String tokenPrefix = "Bearer ";

    /**
     * HTTP header name for JWT token
     * Default: "Authorization"
     */
    private String headerName = "Authorization";

    /**
     * Issuer name for JWT tokens
     * Used for token validation
     */
    private String issuer = "BlogNest";

    /**
     * Audience for JWT tokens
     * Used for token validation
     */
    private String audience = "BlogNest-Users";

    // Getters and Setters

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    public void setRefreshExpiration(long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    /**
     * Get expiration time in seconds (for JWT library compatibility)
     */
    public long getExpirationInSeconds() {
        return expiration / 1000;
    }

    /**
     * Get refresh token expiration time in seconds
     */
    public long getRefreshExpirationInSeconds() {
        return refreshExpiration / 1000;
    }
}