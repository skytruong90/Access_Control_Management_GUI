package vsu.se22.team1;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    public AccessManager accessManager = new AccessManager(this);
    public List<Building> buildings = new ArrayList<>();
    public List<Employee> employees = new ArrayList<>();
    /**
     * To prevent unnecessary disk usage, this is compared with the current
     * hash of the Manager each time a write operation occurs.
     * If it has not changed, the write will not take place.
     * @see Persistence#write(Manager, java.io.File)
     */
    transient byte[] lastIOHash;

    public Manager() {}

    /**
     * This constructor is only used when deserializing a Manager.
     */
    protected Manager(List<Building> buildings, List<Employee> employees) {
        this.buildings = buildings;
        this.employees = employees;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public boolean removeBuilding(Building building) {
        return buildings.remove(building);
    }

    /**
     * Removes the given Area from the Manager.
     * @return True if the Area was removed, otherwise false.
     */
    public boolean removeArea(Area area) {
        if(area instanceof Building) {
            return removeBuilding((Building) area);
        } else {
            for(Building b : buildings) {
                if(b.getSuites().contains(area)) {
                    return b.removeSuite((Suite) area);
                } else {
                    for(Suite s : b.getSuites()) {
                        if(s.contains(area)) {
                            return s.removeRoom((Room) area);
                        }
                    }
                }
            }
        }
        return false;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public Employee getEmployee(int id) {
        for(Employee e : employees) {
            if(e.id == id) return e;
        }
        return null;
    }

    public boolean removeEmployee(Employee employee) {
        return employees.remove(employee);
    }

    public boolean removeEmployee(int employeeId) {
        Employee toRemove = null;
        for(Employee e : employees) {
            if(e.id == employeeId) {
                toRemove = e;
                break;
            }
        }
        employees.remove(toRemove);
        return toRemove != null;
    }

    /**
     * @see #lastIOHash
     */
    public boolean hasChangedSinceLastIO() {
        if(lastIOHash == null) {
            return true;
        }
        byte[] hash = Utils.sha1(this);
        for(int i = 0; i < hash.length; i++) {
            if(hash[i] != lastIOHash[i]) return true;
        }
        return false;
    }
}
