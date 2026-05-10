package dominio;

public class Start implements CellState {
    @Override
    public boolean canHaveObjectOnTop() {
        return true;
    }

    @Override
    public boolean isSafe() {
        return true; // Start is also a safe zone
    }
    
    @Override
	public boolean isARespawn() {
    	return true;
    }
    public boolean isAFinish() {
    	return false;
    }
}
