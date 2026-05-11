package dominio;

public class SafeZone implements CellState {
    @Override
    public boolean canHaveObjectOnTop() {
        return true;
    }

    @Override
    public boolean isSafe() {
        return true;
    }

    @Override
    public boolean isARespawn() {
        return true;
    }

    @Override
    public boolean isAFinish() {
     	return false;
     }
}
