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

    /**
     * Indica si esta celda es una zona segura (Inicio, Meta o Zona Segura)
     * donde los enemigos no deberían entrar.
     */
    public boolean isSafeZone() {
        for (Object obj : contents) {
            if (obj instanceof Start || obj instanceof Goal || obj instanceof SafeZone) {
                return true;
            }
        }
        return false;
    }
}
