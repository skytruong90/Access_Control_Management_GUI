package vsu.se22.team1;

import java.util.UUID;

public abstract class Area {
    /**
     * The UUID is never seen by the end user. It exists only
     * to differentiate Areas that have the same name and/or code.
     */
    public final UUID uuid;
    public String name;
    public int code;

    public Area(UUID uuid, String name, int code) {
        this.uuid = uuid;
        this.name = name;
        this.code = code;
    }

    public Area(String name, int code) {
        this(UUID.randomUUID(), name, code);
    }

    @Override
    public String toString() {
        return String.format("%s (%02d)", name, code);
    }

    public boolean contains(Area other) {
        return other.equals(this);
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
            return this.uuid.equals(((Area) obj).uuid);
        }
    }
}
