package org.example.command;

import org.example.common.ioc.IoC;
import org.example.dto.CommandType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Interpret implements Command {

    private final Map<CommandType, List<Object>> packet;
    private final Queue<Command> game;
    private final Object gameObject;

    public Interpret(Map<CommandType, List<Object>> packet, Queue<Command> game, Object gameObject) {
        this.packet = packet;
        this.game = game;
        this.gameObject = gameObject;
    }

    @Override
    public void execute() {
        var commands = new ArrayList<Command>();
        for (var packetCommand : packet.entrySet()) {
            if (packetCommand.getValue().isEmpty()) {
                commands.add(IoC.resolve(
                    packetCommand.getKey().toString(), gameObject));
            } else {
                commands.add(IoC.resolve(
                    packetCommand.getKey().toString(), gameObject, packetCommand.getValue()));
            }
        }
        game.add(new MacroCommand(commands));
    }
}
