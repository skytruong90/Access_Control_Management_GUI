package vsu.se22.team1;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Building extends Area {
    private List<Suite> suites = new ArrayList<>();

    public Building(String name, int code) {
        super(name, code);
    }

    protected Building(UUID uuid, String name, int code) {
        super(uuid, name, code);
    }

    public void addSuite(Suite suite) {
        suites.add(suite);
    }

    public boolean removeSuite(Suite suite) {
        return suites.remove(suite);
    }

    public List<Suite> getSuites() {
        return suites;
    }

    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        for(Suite suite : suites) {
            rooms.addAll(suite.getRooms());
        }
        return rooms;
    }

    public boolean contains(Area other) {
        for(Suite suite : getSuites()) {
            if(suite.equals(other) || suite.contains(other)) return true;
        }
        return false;
    }
}
