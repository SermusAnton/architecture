package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип команды")
public enum CommandType {

    MOVE("Move"),
    SET_VELOCITY("Set velocity"),
    ROTATE("Rotate");

    private final String name;

    CommandType(String newName) {
        name = newName;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
