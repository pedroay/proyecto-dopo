package tower;


/**
 * Write a description of class Crazy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Crazy extends Lid
{
    /**
     * Constructor for objects of class Fearful
     */
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
}