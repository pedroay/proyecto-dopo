package dominio;



public abstract class Enemy extends Personaje implements interactWPlayer, CanDamage  {
	
	private static boolean damage = true;
	
    public Enemy(int posx, int posy) {
        super(posx, posy);
    }
    
    public boolean canDamageAPlayer() {
    	return damage;
    }
    
    public abstract void move(Board[][] board);
}
