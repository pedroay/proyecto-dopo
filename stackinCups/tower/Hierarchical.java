package tower;

/**
 * Write a description of class Hierarchical here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hierarchical extends Cup
{
    /**
     * Constructor for objects of class Hierarchical
     */
    public Hierarchical(int inumber,Lid lid)
    {
        super(inumber,lid);
        setDesplazaElementos(true);
    }
    
    public Hierarchical(int inumber,Tower torre){
        super(inumber,torre);
        setDesplazaElementos(true);
    }
}