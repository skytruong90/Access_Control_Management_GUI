package vsu.se22.team1;

import java.util.ArrayList;
import java.util.List;

public class AccessManager {
    private Manager manager;
    private List<AccessAttempt> history = new ArrayList<>();

    public AccessManager(Manager manager) {
        this.manager = manager;
    }

    public void logAttempt(Employee e, Area a) {
        history.add(new AccessAttempt(a, e.id, e.hasAccess(a) ? AttemptFlag.SUCCESS : AttemptFlag.DENIED));
    }

    public void logAttempt(int id, Area a) {
        Employee employee = null;
        for(Employee e : manager.employees) {
            if(e.id == id) {
                employee = e;
                break;
            }
        }
        if(employee == null) {
            history.add(new AccessAttempt(a, id, AttemptFlag.SECURITY_ALERT));
        } else {
            logAttempt(employee, a);
        }
    }

    public List<AccessAttempt> getHistory() {
        return history;
    }

    public List<AccessAttempt> getHistory(Area area) {
        List<AccessAttempt> list = new ArrayList<>();
        for(AccessAttempt a : history) {
            if(a.area.equals(area)) {
                list.add(a);
            }
        }
        return list;
    }

    public List<AccessAttempt> getHistory(int employeeId) {
        List<AccessAttempt> list = new ArrayList<>();
        for(AccessAttempt a : history) {
            if(a.employeeId == employeeId) {
                list.add(a);
            }
        }
        return list;
    }

    public List<AccessAttempt> getHistory(Employee employee) {
        return getHistory(employee.id);
    }

    public List<AccessAttempt> getHistory(AttemptFlag flag) {
        List<AccessAttempt> list = new ArrayList<>();
        for(AccessAttempt a : history) {
            if(a.flag == flag) {
                list.add(a);
            }
        }
        return list;
    }

    public List<AccessAttempt> getHistoryBetween(long start, long end) {
        List<AccessAttempt> list = new ArrayList<>();
        for(AccessAttempt a : history) {
            if(a.timestamp >= start && a.timestamp <= end) {
                list.add(a);
            }
        }
        return list;
    }

    public List<AccessAttempt> getHistoryBetween(long start, long end, AttemptFlag flag) {
        List<AccessAttempt> list = new ArrayList<>();
        for(AccessAttempt a : history) {
            if(a.flag == flag && a.timestamp >= start && a.timestamp <= end) {
                list.add(a);
            }
        }
        return list;
    }

    public List<AccessAttempt> getHistoryBetween(long start, long end, Area area) {
        List<AccessAttempt> list = new ArrayList<>();
        for(AccessAttempt a : history) {
            if(a.area.equals(area) && a.timestamp >= start && a.timestamp <= end) {
                list.add(a);
            }
        }
        return list;
    }

    protected void addAll(List<AccessAttempt> accessHistory) {
        history.addAll(accessHistory);
    }
}
