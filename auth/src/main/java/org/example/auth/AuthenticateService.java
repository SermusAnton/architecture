package org.example.auth;

import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AuthenticateService {

    private final UserRepository userRepository;
    private final JwtCreate jwtCreate;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private AtomicLong longValue = new AtomicLong(100);

    public AuthenticateService(UserRepository userRepository, JwtCreate jwtCreate) {
        this.userRepository = userRepository;
        this.jwtCreate = jwtCreate;
    }

    public Optional<String> getToken(String userName, Long gameId) {
        return userRepository.getBy(userName)
            .filter(registerGameId -> registerGameId.equals(gameId))
            .map(registerGameId -> {
                try {
                    return jwtCreate.newJwt(userName, gameId);
                } catch (JoseException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            });
    }

    public Long createGame(List<String> userNames) {
        var gameId = nextIdentifier();
        userNames.forEach(user -> userRepository.add(user, gameId));
        return gameId;
    }

    public String getPublicKey() {
        return jwtCreate.getPublicKey();
    }

    private long nextIdentifier() {
        return longValue.getAndIncrement();
    }
}
