package Fat;

public enum ClusterState {
    FREE(-1), EOF(111), BAD(222);

    private final int value;

    ClusterState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}