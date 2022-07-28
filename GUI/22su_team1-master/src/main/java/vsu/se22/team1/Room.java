package vsu.se22.team1;

import java.util.UUID;

public class Room extends Area {

    public Room(int number) {
        super("Room", number);
    }

    protected Room(UUID uuid, int number) {
        super(uuid, "Room", number);
    }

    @Override
    public String toString() {
        return String.format("%s %03d", I18n.get("room.name"), code);
    }
}
