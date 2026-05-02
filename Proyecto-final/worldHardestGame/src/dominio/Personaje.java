package dominio;

public abstract class Personaje extends Object implements canMove {
    public Personaje(int posx, int posy) {
        super(posx, posy);
    }
    
    public void moveUp() {
    	super.setPosy(super.getPosy()-1);
    }
    
    public void moveDown() {
    	super.setPosy(super.getPosy()+1);
    }
	
	public void moveRight() {
		super.setPosx(super.getPosx()+1);
	}
	
	public void moveLeft(){
		super.setPosx(super.getPosx()-1);
	}
}
