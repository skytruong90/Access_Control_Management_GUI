package vsu.se22.team1;

public enum AttemptFlag {
    SUCCESS(1, "Success"), DENIED(0, "Denied"), SECURITY_ALERT(2, "Security Alert");

    public final int code;
    private String friendlyName;

    private AttemptFlag(int code, String friendlyName) {
        this.code = code;
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }

    public static AttemptFlag fromFlag(int code) {
        for(AttemptFlag a : values()) {
            if(a.code == code) return a;
        }
        throw new RuntimeException("Invalid AttemptFlag code");
    }
}
