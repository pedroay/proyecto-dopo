package tower;


/**
 * Write a description of class Fearful here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Fearful extends Lid
{
    /**
     * Constructor for objects of class Fearful
     */
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
    
}