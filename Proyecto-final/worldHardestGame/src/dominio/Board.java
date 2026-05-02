package dominio;

import java.util.ArrayList;

public class Board extends Object {
    private boolean canHaveObjectOnTop;
    private ArrayList<Object> contents;

    public Board(int posx, int posy, boolean canHaveObjectOnTop) {
        super(posx, posy);
        this.canHaveObjectOnTop = canHaveObjectOnTop;
        this.contents = new ArrayList<>();
    }

    public boolean isCanHaveObjectOnTop() {
        return canHaveObjectOnTop;
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
}
