package dominio;

import java.util.ArrayList;

public abstract class Objeto {
    private int posx;
    private int posy;
    private ArrayList<String> colideWith;

    public Objeto(int posx, int posy) {
        this.posx = posx;
        this.posy = posy;
        this.colideWith = new ArrayList<>();
    }

    public boolean canColideW(Objeto obj) {
        // TODO: Implementar lógica
        return false;
    }
}
