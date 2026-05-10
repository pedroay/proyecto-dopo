package dominio;

public abstract class Hero extends Personaje {
	
	private State skin;
	private double size;
	
    public Hero(int posx, int posy, double heroVelocity) {
        super(posx, posy);
        this.heroVelocity = heroVelocity;
    }
    
    
}
