package vsu.se22.team1.gui;

import vsu.se22.team1.Area;
import vsu.se22.team1.AttemptFlag;

public class LogFilter {
    public int employeeId = 0;
    public Area area;
    public long startTimestamp = -1;
    public long endTimestamp = -1;
    public AttemptFlag flag;
    public FilterKey sortBy = FilterKey.TIME;
    public FilterKey filterBy = FilterKey.NONE;
    public boolean sortDescending = false;

    enum FilterKey {
        TIME("Time"), AREA("Area"), EMPLOYEE("Employee"), FLAG("Attempt Flag"), NONE("None");

        private String friendlyName;

        private FilterKey(String friendlyName) {
            this.friendlyName = friendlyName;
        }

        @Override
        public String toString() {
            return friendlyName;
        }

        static FilterKey[] valuesExcludingNone() {
            return new FilterKey[] { TIME, AREA, EMPLOYEE, FLAG };
        }
    }

    public long getStartTimestamp() {
        return startTimestamp == -1 ? 0 : startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp == -1 ? System.currentTimeMillis() : endTimestamp;
    }
}
