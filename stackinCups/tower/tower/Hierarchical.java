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
	/**
	 * The extra shape for the box used to mark de new box
	 */
    private final Rectangle shapeExtra = new Rectangle(); 
    /**
     * Constructor for objects of class Hierarchical
     * @param inumber: number which we identify a cup
     * @param lid:is the lid of the cup
     */
    public Hierarchical(final int inumber,final Lid lid)
    {
        super(inumber,lid,lid.getColor(),lid.getTower());
        setDesplazaElementos(true);
        setNotQuitablePosition(0);
    }
    
    /**
     * Create the cup and link himself with a tower
     * @param inumber:number which we identify a cup
     * @param torre:tower where the cup will apil
     */
    public Hierarchical(final int inumber,final Tower torre){
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
    public boolean canDamage(Elements element) {
        return false;
    }

    @Override
    public boolean canDesplace(final Elements element) {
        boolean canDesplace = false;
        if(element.thisIsQuitable() && element.getNumber() <this.number){
            canDesplace = true;
        }
        return canDesplace;
    }
}