package dominio;

public class Goal implements CellState {
    @Override
    public boolean canHaveObjectOnTop() {
        return true;
    }

    @Override
    public boolean isASafeZone() {
        return true; // Goal is also a safe zone
    }

    @Override
    public boolean isAGoal() {
        return true;
    }

    @Override
    public boolean isAStart() {
        return false;
    }
}
