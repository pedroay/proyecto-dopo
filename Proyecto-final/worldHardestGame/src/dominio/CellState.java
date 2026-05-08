package dominio;

public interface CellState {
    boolean canHaveObjectOnTop();
    boolean isASafeZone();
    boolean isAGoal();
    boolean isAStart();
}
