package dominio;
import java.util.ArrayList;
public class WorldHG {
    private Level level;
    private String direction; 
    private ArrayList<Board> boards;
    private Player player1;
    private Player player2;
    private String modalidad;

    public WorldHG(String modalidad) {
        this.modalidad = modalidad;
        this.boards = new ArrayList<>();
        
    }
    
    private void logicaModalidad1() {
        if("player".equals(modalidad)) {
            this.player1 = new Player("Juan");
        }
    }
    public void movePlayer() {
        // TODO: Implementar lógica
    }

    public String getInfo() {
        // TODO: Implementar lógica
        return null;
    }

    public String saveGame() {
        // TODO: Implementar lógica
        return null;
    }

    public void importGame(String file) {
        // TODO: Implementar lógica
    }
}
