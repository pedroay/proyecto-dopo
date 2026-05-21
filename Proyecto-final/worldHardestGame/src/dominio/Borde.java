package dominio;

public class Borde implements CellState {
    @Override
    public boolean canHaveObjectOnTop() {
        return false;
    }

    @Override
    public boolean isSafe() {
        return false;
    }


    @Override
    public boolean isARespawn() {
        return false;
    }
    
    @Override
   public boolean isAFinish() {
    	return false;
    }
}
