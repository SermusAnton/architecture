package org.example.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final AuthenticateService authenticateService;

    public AuthController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping("/game")
    public Long createGame(@RequestBody List<String> userNames) {
        return authenticateService.createGame(userNames);
    }


    @GetMapping("/game/{id}/token")
    public String getToken(@PathVariable Long id, String userName) {
        return authenticateService.getToken(userName, id)
            .orElseThrow(AuthError::new);
    }

    @GetMapping("/publickey")
    public String getPublicKey() {
        return authenticateService.getPublicKey();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class AuthError extends RuntimeException {
    }


}
