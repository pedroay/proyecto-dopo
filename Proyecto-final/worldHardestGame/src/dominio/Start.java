package dominio;

public class Start implements CellState {
    @Override
    public boolean canHaveObjectOnTop() {
        return true;
    }

    @Override
    public boolean isASafeZone() {
        return true; // Start is also a safe zone
    }

    @Override
    public boolean isAGoal() {
        return false;
    }

    @Override
    public boolean isAStart() {
        return true;
    }
}
