package org.example.controller;

import org.example.service.CommandService;
import org.example.common.polling.Game;
import org.example.dto.PacketCommandDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/game", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {

    private final CommandService commandService;

    public GameController(CommandService commandService) {
        this.commandService = commandService;
    }

    @GetMapping("/{id}")
    public Game findById(@PathVariable long id) {
        return Game.GameHolder.HOLDER_INSTANCE;
    }

    @PostMapping("/")
    public void create(@RequestBody PacketCommandDto packetCommandDto) {
        commandService.create(packetCommandDto);
    }

}
