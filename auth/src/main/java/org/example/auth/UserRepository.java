package org.example.auth;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserRepository {
    private static final Map<String, Long> users = new ConcurrentHashMap<>();

    public void add(String userName, Long gameId) {
        if (users.containsKey(userName)) {
            throw new IllegalArgumentException("User register to other game");
        }
        users.put(userName, gameId);
    }

    public Optional<Long> getBy(String userName) {
        return Optional.of(users.get(userName));
    }
}
