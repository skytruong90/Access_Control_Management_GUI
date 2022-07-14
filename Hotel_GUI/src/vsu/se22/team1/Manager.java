package vsu.se22.team1;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    public AccessManager accessManager = new AccessManager(this);
    public List<Building> buildings = new ArrayList<>();
    public List<Employee> employees = new ArrayList<>();

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
}
