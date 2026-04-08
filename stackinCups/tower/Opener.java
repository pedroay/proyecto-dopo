package tower;


/**
 * Write a description of class Opener here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Opener extends Cup
{
    public Opener(int inumber,Lid lid)
    {
        super(inumber,lid);
        setEliminaTapas(true);
    }
    
    public Opener(int inumber,Tower torre){
        super(inumber,torre);
        setEliminaTapas(true);
    }
}