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
        setNotQuitablePosition(0);
    }
    
    public Hierarchical(int inumber,Tower torre){
        super(inumber,torre);
        setDesplazaElementos(true);
    }

    @Override
    public boolean canDamage(Elements e) {
        return false;
    }

    @Override
    public boolean canDesplace(Elements e) {
        boolean canDesplace = false;
        if(e.isQuitable() && e.getNumber() <this.number){
            canDesplace = true;
        }
        return canDesplace;
    }
}