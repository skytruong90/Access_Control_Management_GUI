package vsu.se22.team1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Employee {
    /**
     * The UUID is never seen by the end user. It exists only to
     * differentiate Employees that have the same name and/or code.
     */
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

    public boolean inheritsAccess(Area area) {
        return !access.containsKey(area);
    }

    public void clearAccessState(Area a) {
        access.remove(a);
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
        } else if(obj == null) {
            return false;
        } else if(this.getClass() != obj.getClass()) {
            return false;
        } else {
            return this.uuid.equals(((Employee) obj).uuid);
        }
    }
}
