package dominio;

import java.util.ArrayList;

public class Board extends Object {
    private CellState state;
    private ArrayList<Object> contents;

    public Board(int posx, int posy) {
        super(posx, posy);
        this.contents = new ArrayList<>();
        this.state = new Empty(); // Default state
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public ArrayList<Object> getContents() {
        return contents;
    }

    public void addObject(Object obj) {
        contents.add(obj);
    }

    public void removeObject(Object obj) {
        contents.remove(obj);
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    // Delegación al estado:
    public boolean isCanHaveObjectOnTop() {
        return state.canHaveObjectOnTop();
    }

    public boolean isSafe() {
        return state.isSafe();
    }

    public boolean isARespawn() {
        return state.isARespawn();
    }
    

    public boolean isAFinish() {
     	return state.isAFinish();
     }

   
}
