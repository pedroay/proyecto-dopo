package tower;

/**
 * Represents an Opener cup in the tower.
 * An Opener has the special ability to remove (damage) lids from the tower.
 */
public class Opener extends Cup
{
    /**
     * Constructor for the Opener class associated with an existing lid.
     * 
     * @param inumber The unique identifier for the opener, 
     * used to calculate its height.
     * @param lid     The Lid object permanently
     *  associated with this opener.
     */
    public Opener(final int inumber,final Lid lid)
    {
        super(inumber,lid,lid.getColor(),lid.getTower());
        setEliminaTapas(true);
    }
    
    /**
     * Main constructor for the Opener class.
     * 
     * @param inumber The unique identifier for the opener,
     *  used to calculate its height.
     * @param torre   The tower this opener belongs to.
     */
    public Opener(final int inumber,final Tower torre){
        super(inumber,torre);
        setEliminaTapas(true);
    }

    /**
     * Determines if this opener can damage (remove) another element.
     * An opener can only damage lids that are quitable.
     * 
     * @param e The element to evaluate.
     * @return true if the element is a quitable lid, false otherwise.
     */
    @Override
    public boolean canDamage(final Elements element) {
        return "lid".equals(element.getType()) && element.thisIsQuitable();
    }

    /**
     * Determines if this opener can displace another element.
     * Openers do not have the ability to displace elements.
     * 
     * @param e The element to evaluate.
     * @return always false.
     */
    @Override
    public boolean canDesplace(Elements element) {
        return false;
    }
}