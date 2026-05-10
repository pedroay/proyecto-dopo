package dominio;

public class SkinPunto extends Punto {
    private String color;

    public SkinPunto(int posx, int posy, String color) {
        super(posx, posy);
        this.color = color;
    }

    public void applyState(Player player) {
        switch(color.toLowerCase()) {
            case "blue": 
                player.setState(new BlueState()); 
                break;
            case "green": 
                player.setState(new GreenState()); 
                break;
            case "red": 
                player.setState(new RedState()); 
                break;
        }
    }
    
    public String getColor() {
        return color;
    }
}
