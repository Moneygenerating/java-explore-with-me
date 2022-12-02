package ewm.request.model;

public enum Status {
    PENDING,
    CONFIRMED,
    REJECTED,
    CANCELED;

    public static Status from(String state) {
        for (Status value : Status.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }
}
