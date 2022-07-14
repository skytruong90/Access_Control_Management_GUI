package vsu.se22.team1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Employee {
    public final UUID uuid;
    public String name;
    public int id;
    public Map<Area, Boolean> access = new HashMap<>();

    public Employee(UUID uuid, String name, int id) {
        this.uuid = uuid;
        this.name = name;
        this.id = id;
    }

    public Employee(String name, int id) {
        this(UUID.randomUUID(), name, id);
    }

    public void setAccessState(Area area, boolean state) {
        access.put(area, state);
    }

    public boolean hasAccess(Area area) {
        if(access.containsKey(area)) return access.get(area);

        for(Area a : access.keySet()) {
            if(a.contains(area)) return access.get(a);
        }
        return false;
    }

    public Set<Area> getAccessibleAreas() {
        Set<Area> accessible = new HashSet<>();
        for(Area area : access.keySet()) {
            if(access.get(area)) accessible.add(area);
        }
        return accessible;
    }

    @Override
    public String toString() {
        return String.format("%s (ID %d)", name, id);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(this.getClass() != obj.getClass()) {
            return false;
        } else {
            return this.uuid.equals(((Area) obj).uuid);
        }
    }
}
