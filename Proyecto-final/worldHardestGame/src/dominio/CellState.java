package dominio;

public interface CellState {
    boolean canHaveObjectOnTop();
    boolean isSafe();
    boolean isARespawn();
    boolean isAFinish();
}
