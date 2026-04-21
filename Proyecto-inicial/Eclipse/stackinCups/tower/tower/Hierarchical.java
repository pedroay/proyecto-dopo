package tower;
import Shapes.*;

/**
 * Write a description of class Hierarchical here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hierarchical extends Cup
{
    private Rectangle shapeExtra = new Rectangle();

    /**
     * Constructor for objects of class Hierarchical
     */
    public Hierarchical(int inumber,Lid lid)
    {
        super(inumber,lid);
        setDesplazaElementos(true);
        setNotQuitablePosition(0);
    }
    
    public Hierarchical(int inumber,Tower torre){
        super(inumber,torre);
        setDesplazaElementos(true);
    }

    @Override
    public void draw(){
        super.draw();
        shapeExtra.changeColor("darkGreen");
        shapeExtra.changeSize(10, width - 10);
        shapeExtra.setP(posy + getHeight() - 2, 150 - ((width - 10) / 2));
        shapeExtra.makeVisible();
    }

    @Override
    public void erase(){
        super.erase();
        if(shapeExtra != null) {
            shapeExtra.makeInvisible();
        }
    }

    @Override
    public boolean canDamage(Elements e) {
        return false;
    }

    @Override
    public boolean canDesplace(Elements e) {
        boolean canDesplace = false;
        if(e.isQuitable() && e.getNumber() <this.number){
            canDesplace = true;
        }
        return canDesplace;
    }
}