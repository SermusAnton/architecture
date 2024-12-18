package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashMap;
import java.util.List;

@Schema(description = "Пакет команд")
public class PacketCommandDto {

    @NotNull
    private Long gameId;
    @NotNull
    private Long gameObjectId;
    @NotNull
    private LinkedHashMap<CommandType, List<Object>> commands;

    public Long getGameId() {
        return gameId;
    }

    public Long getGameObjectId() {
        return gameObjectId;
    }

    public LinkedHashMap<CommandType, List<Object>> getCommands() {
        return commands;
    }
}
