package tower;
import Shapes.*;

/**
 * This class represents a box that encloses the entire tower.
 * Once created, this box cannot be opened or closed.
 */
public class Box extends Cup
{	
	/**
	 * The extra shape for the box used to mark de new box
	 */
    private final Rectangle shapeExtra = new Rectangle(); 
    
    /**
     * this contructor creat a box
     * @param iNumber: is the number which we indentify the cup
     * @param torre: is the tower where the box will apile
     */
    public Box(final int iNumber,final Tower torre){
        super(iNumber, torre);
        setIsBox(true);
    }

    /**
     * Draws the box on the screen.
     * This method overrides the draw method of the Cup class.
     */
    @Override
    public void draw(){
        super.draw();
        shapeExtra.changeColor("brown");
        shapeExtra.changeSize(10, width - 10);
        shapeExtra.setP(posy + getHeight() - 10, 150 - ((width - 10) / 2));
        shapeExtra.makeVisible();
    }

    /**
     * Erases the box from the screen.
     * This method overrides the erase method of the Cup class.
     */
    @Override
    public void erase(){
        super.erase();
        if(shapeExtra != null) {
            shapeExtra.makeInvisible();
        }
    }

    /**
     * Creates the lid for the box.
     * This method overrides the createHisLid method of the Cup class.
     */

    public void createHisLid(){
        torre.pushLid(number);
    }
}