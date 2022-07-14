package vsu.se22.team1;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Suite extends Area {
    private List<Room> rooms = new ArrayList<>();

    public Suite(String name, int code) {
        super(name, code);
    }

    protected Suite(UUID uuid, String name, int code) {
        super(uuid, name, code);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public boolean removeRoom(Room room) {
        return rooms.remove(room);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public boolean contains(Area other) {
        if(other instanceof Building) return false;
        for(Room room : getRooms()) {
            if(room.equals(other)) return true;
        }
        return false;
    }
}
