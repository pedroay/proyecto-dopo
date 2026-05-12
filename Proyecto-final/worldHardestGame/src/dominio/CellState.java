package dominio;

public interface CellState extends java.io.Serializable {
    boolean canHaveObjectOnTop();

    boolean isSafe();

    boolean isARespawn();

    boolean isAFinish();
}
