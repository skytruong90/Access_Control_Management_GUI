package vsu.se22.team1;

public enum AttemptFlag {
    SUCCESS(1), DENIED(0), SECURITY_ALERT(2);

    public final int code;

    private AttemptFlag(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return I18n.get("attempt-flag." + name().toLowerCase());
    }

    public static AttemptFlag fromCode(int code) {
        for(AttemptFlag a : values()) {
            if(a.code == code) return a;
        }
        throw new RuntimeException("Invalid AttemptFlag code");
    }
}
