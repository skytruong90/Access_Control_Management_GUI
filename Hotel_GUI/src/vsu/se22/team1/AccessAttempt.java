package vsu.se22.team1;

public class AccessAttempt {
    public long timestamp;
    public Area area;
    public int employeeId;
    public AttemptFlag flag;

    public AccessAttempt(long timestamp, Area area, int employeeId, AttemptFlag flag) {
        this.timestamp = timestamp;
        this.area = area;
        this.employeeId = employeeId;
        this.flag = flag;
    }

    public AccessAttempt(Area area, int employeeId, AttemptFlag flag) {
        this(System.currentTimeMillis(), area, employeeId, flag);
    }

    public String toString() {
        return String.format("%s: Employee #%d accessed %s [%s]", Utils.DATE_FORMAT.format(timestamp), employeeId,
                area.toString(), flag.toString());
    }
}
