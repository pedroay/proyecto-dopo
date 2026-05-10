package dominio;

public class Goal implements CellState {
    @Override
    public boolean canHaveObjectOnTop() {
        return true;
    }

    @Override
    public boolean isSafe() {
        return true; // Goal is also a safe zone
    }

    @Override
    public boolean isARespawn() {
        return false;
    }
    
    @Override
    public boolean isAFinish() {
     	return true;
     }
}
