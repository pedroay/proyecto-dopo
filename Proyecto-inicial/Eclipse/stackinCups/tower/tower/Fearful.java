package tower;
import java.util.Random;
import Shapes.*;


/**
 * Write a description of class Fearful here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Fearful extends Lid
{
    private Rectangle shapeExtra = new Rectangle();
    
    public Fearful(int number,Tower torre)
    {
        super(number,torre);
        setIsFearful(true);
    }
    
    public Fearful(int number,Cup cup)
    {
        super(number,cup);  
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