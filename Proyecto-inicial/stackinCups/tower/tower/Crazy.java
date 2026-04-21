package tower;
import Shapes.*;

/**
 * Represents a "Crazy" element within the tower.
 * Unlike standard elements that cover a cup, this element 
 * positions itself as a base instead.
 */
public class Crazy extends Lid
{
    /**
     * The extra shape for the box used to mark de new box
     */
    private final Rectangle shapeExtra = new Rectangle();

    /**
     * Constructs a Crazy element using tower coordinates.
     * @param number The identification number of the element.
     * @param torre The tower instance where this element is registered.
     */
    public Crazy(final int number,final Tower torre)
    {
        super(number,torre);
        this.isCrazy = true;

    }
    
    /**
     * Constructs a Crazy element associated with a specific cup.
     * * @param number The identification number of the element.
     * @param cup The cup object that this element interacts with.
     */
    public Crazy(final int number,final Cup cup)
    {
        super(number,cup,cup.getColor(),cup.getTower());
        this.isCrazy = true;
    }
    
    /**
     * Renders the Crazy element's visual representation.
     * Overrides the default drawing to show the element as a structural base.
     */
    @Override
    public void draw(){
        super.draw();
        shapeExtra.changeColor("Orange");
        shapeExtra.changeSize(height-5,width-10);
        shapeExtra.setP(posy+2,150 -((width-10)/2));
        shapeExtra.makeVisible();
        
    }


    /**
     * Removes the Crazy element's visual representation from the canvas.
     */
    @Override
    public void makeInvisible(){
        super.makeInvisible();
        shapeExtra.makeInvisible();
    }
}