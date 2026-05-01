package dominio;

import java.util.ArrayList;

public class Board extends Objeto {
    private ArrayList<Borde> bordes;
    private ArrayList<Start> starts;
    private ArrayList<Goal> goals;

    public Board() {
        super(0, 0);
        this.bordes = new ArrayList<>();
        this.starts = new ArrayList<>();
        this.goals = new ArrayList<>();
    }
}
