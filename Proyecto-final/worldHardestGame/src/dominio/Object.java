package dominio;

import java.util.ArrayList;

public abstract class Object {
    private int posx;
    private int posy;
    private ArrayList<String> colideWith;

    public Object(int posx, int posy) {
        this.posx = posx;
        this.posy = posy;
        this.colideWith = new ArrayList<>();
    }

    public boolean canColideW(Object obj) {
        return colideWith.contains(obj);
    }

    public void setPosx(int newPosx){
        posx = newPosx;
    }

    public void setPosy(int newPosy){
        posx = newPosy;
    }
    
    public int getPosx() {
    	return posx;
    }
    
    public int getPosy() {
    	return posy;
    }
    
    public void addColideWith(Object newObject){
    	
    }
}
