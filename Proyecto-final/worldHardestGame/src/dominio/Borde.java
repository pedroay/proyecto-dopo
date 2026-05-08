package dominio;

public class Borde implements CellState {
    @Override
    public boolean canHaveObjectOnTop() {
        return false;
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
