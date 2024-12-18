package org.example.service;

import org.example.command.Command;
import org.example.command.Interpret;
import org.example.common.ioc.IoC;
import org.example.dto.PacketCommandDto;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service
public class CommandService {
    public void create(PacketCommandDto packetCommandDto) {
        var game = IoC.<Queue<Command>>resolve(
            String.format("Game%s.Deque", packetCommandDto.getGameId())
        );
        var gameObject = IoC.resolve(
            "Game Object", packetCommandDto.getGameObjectId()
        );
        game.add(new Interpret(packetCommandDto.getCommands(), game, gameObject));
    }
}
