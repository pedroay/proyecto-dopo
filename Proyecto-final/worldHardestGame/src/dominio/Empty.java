package dominio;

public class Empty implements CellState {
    @Override
    public boolean canHaveObjectOnTop() {
        return true;
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
