package dominio;

public class Player extends Hero {
    private String nombre;
    private int respawnX;
    private int respawnY;

    public Player(String nombre, int posx, int posy) {
        super(posx, posy);
        this.nombre = nombre;
        this.respawnX = posx;
        this.respawnY = posy;
    }

    public void setRespawnPoint(int x, int y) {
        this.respawnX = x;
        this.respawnY = y;
    }

    public int getRespawnX() {
        return respawnX;
    }

    public int getRespawnY() {
        return respawnY;
    }

    public String getNombre() {
        return nombre;
    }
}
