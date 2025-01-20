package org.example.auth;

import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class JwtCreate {

    private static final Logger logger = LoggerFactory.getLogger(JwtCreate.class);
    private static final RsaJsonWebKey rsaJsonWebKey;

    static {
        try {
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
            rsaJsonWebKey.setKeyId("k1");
        } catch (JoseException e) {
            logger.error("Generate error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String newJwt(String userId, Long gameId) throws JoseException {
        JwtClaims claims = getJwtClaims(userId, gameId);

        JsonWebSignature jws = new JsonWebSignature();

        jws.setPayload(claims.toJson());

        logger.info("Format: {}", rsaJsonWebKey.getPublicKey().getFormat());
        logger.info("Algorithm: {}", rsaJsonWebKey.getPublicKey().getAlgorithm());

        String publicKeyJwkString = rsaJsonWebKey.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY);
        logger.info("Public key: {}", publicKeyJwkString);

        jws.setKey(rsaJsonWebKey.getPrivateKey());

        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        return jws.getCompactSerialization();
    }

    public String getPublicKey() {
        return  rsaJsonWebKey.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY);
    }

    private static JwtClaims getJwtClaims(String userId, Long gameId) {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer("auth-service");  // who creates the token and signs it
        claims.setAudience("game-service"); // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
        claims.setSubject(userId); // the subject/principal is whom the token is about
        claims.setClaim("gameId", gameId);
        List<String> groups = Arrays.asList("group-one", "other-group", "group-three");
        claims.setStringListClaim("groups", groups); // multi-valued claims work too and will end up as a JSON array
        return claims;
    }
}
