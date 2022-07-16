package vsu.se22.team1;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    public AccessManager accessManager = new AccessManager(this);
    public List<Building> buildings = new ArrayList<>();
    public List<Employee> employees = new ArrayList<>();
    transient byte[] lastIOHash;

    public Manager() {}

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

    public boolean removeArea(Area area) {
        if(area instanceof Building) {
            return removeBuilding((Building) area);
        } else {
            for(Building b : buildings) {
                if(b.getSuites().contains(area)) {
                    b.removeSuite((Suite) area);
                } else {
                    for(Suite s : b.getSuites()) {
                        if(s.contains(area)) {
                            s.removeRoom((Room) area);
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

    public String listBuildings() {
        StringBuilder sb = new StringBuilder();
        for(Building b : buildings) {
            sb.append(b.toString());
            sb.append("\n");
            for(Suite s : b.getSuites()) {
                sb.append("\u00A0\u00A0");
                sb.append(s.toString());
                sb.append("\n");
                for(Room r : s.getRooms()) {
                    sb.append("\u00A0\u00A0\u00A0\u00A0");
                    sb.append(r.toString());
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

    public String listEmployees() {
        StringBuilder sb = new StringBuilder();
        for(Employee e : employees) {
            sb.append(e.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

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
