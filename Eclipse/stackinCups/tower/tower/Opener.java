package tower;

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

    @Override
    public boolean canDamage(Elements e) {
        return e.getType().equals("lid") && e.isQuitable();
    }

    @Override
    public boolean canDesplace(Elements e) {
        return false;
    }
}