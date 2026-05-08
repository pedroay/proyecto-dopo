package dominio;

public class Empty implements CellState {
    @Override
    public boolean canHaveObjectOnTop() {
        return true;
    }

    @Override
    public boolean isASafeZone() {
        return false;
    }

    @Override
    public boolean isAGoal() {
        return false;
    }

    @Override
    public boolean isAStart() {
        return false;
    }
}
