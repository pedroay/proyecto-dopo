package tower;
import Shapes.*;


/**
 * Fearful represents a specific behavior for a Cup.
 * This element only enters the tower if its companion cup is present,
 * and only exits if it is not currently covering its companion.
 */
public class Fearful extends Lid
{	
	/**
	 * The extra shape for the box used to mark de new box
	 */
    private final  Rectangle shapeExtra = new Rectangle();
    
    /**
     * Constructs a Fearful cup with its identifier and associated tower.
     * Sets the fearful state to true upon initialization.
     *
     * @param number The unique numeric identifier for this cup.
     * @param torre  The tower instance where this cup belongs.
     */
    public Fearful(final int number,final Tower torre)
    {
        super(number,torre);
        setIsFearful(true);
    }
    
    /**
     * Constructs a Fearful cup based on an existing cup's properties.
     * Inherits the color and tower from the provided cup instance.
     *
     * @param number The unique numeric identifier for this cup.
     * @param cup    The cup instance to copy attributes from.
     */
    public Fearful(final int number,final Cup cup)
    {
        super(number,cup,cup.getColor(),cup.getTower());  
        setIsFearful(true);
    }
    
    @Override
    public void draw(){
        super.draw();
        shapeExtra.changeColor("Gray");
        shapeExtra.changeSize(height-5,width-10);
        shapeExtra.setP(posy+2,150 -((width-10)/2));
        shapeExtra.makeVisible();
        
    }
    
    @Override
    public void erase(){
        super.erase();
        shapeExtra.makeInvisible();
    }
    
}