package tower;
import java.util.Random;
import Shapes.*;

/**
 * Write a description of class Crazy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Crazy extends Lid
{
    private Rectangle shapeExtra = new Rectangle();

    public Crazy(int number,Tower torre)
    {
        super(number,torre);
        setIsCrazy(true);
    }
    
    public Crazy(int number,Cup cup)
    {
        super(number,cup);
        setIsCrazy(true);
    }
    
    @Override
    public void draw(){
        super.draw();
        shapeExtra.changeColor("Orange");
        shapeExtra.changeSize(height-5,width-10);
        shapeExtra.setP(posy+2,150 -((width-10)/2));
        shapeExtra.makeVisible();
        
    }

    @Override
    public void makeInvisible(){
        super.makeInvisible();
        shapeExtra.makeInvisible();
    }
}