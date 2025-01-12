package org.example.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class AuthenticateFilter extends GenericFilterBean {

    private static final String AUTH = "Authorization";
    private static final String BEARER = "Bearer ";

    private final String publicKey;

    public AuthenticateFilter(String authServerUrl) {
        this.publicKey = loadPublicKey(authServerUrl);
    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        var token = httpRequest.getHeader(AUTH).substring(BEARER.length());

        try {
            validateToken(token);
        } catch (MalformedClaimException e) {
            throw new AuthenticateException(e.getMessage());
        }

        chain.doFilter(request, response);
    }

    private String loadPublicKey(String authServerUrl) {
        var restTemplate = new RestTemplate();
        return restTemplate.getForObject(authServerUrl, String.class);
    }

    private void validateToken(String token) throws MalformedClaimException {
        Key key = new HmacKey(publicKey.getBytes(StandardCharsets.UTF_8));
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
            .setRequireExpirationTime() // the JWT must have an expiration time
            .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
            .setRequireSubject() // the JWT must have a subject claim
            .setExpectedIssuer("auth-service") // whom the JWT needs to have been issued by
            .setExpectedAudience("game-service") // to whom the JWT is intended for
            .setVerificationKey(key) // verify the signature with the public key
            .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, // which is only RS256 here
                    AlgorithmIdentifiers.RSA_USING_SHA256))
            .build(); // create the JwtConsumer instance

        try {
            //  Validate the JWT and process it to the Claims
            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            System.out.println("JWT validation succeeded! " + jwtClaims);
        } catch (InvalidJwtException e) {
            // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
            // Hopefully with meaningful explanations(s) about what went wrong.
            System.out.println("Invalid JWT! " + e);

            // Programmatic access to (some) specific reasons for JWT invalidity is also possible
            // should you want different error handling behavior for certain conditions.

            // Whether or not the JWT has expired being one common reason for invalidity
            if (e.hasExpired()) {
                System.out.println("JWT expired at " + e.getJwtContext().getJwtClaims().getExpirationTime());
            }

            // Or maybe the audience was invalid
            if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)) {
                System.out.println("JWT had wrong audience: " + e.getJwtContext().getJwtClaims().getAudience());
            }
        }
    }
}