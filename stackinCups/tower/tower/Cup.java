package tower;
import Shapes.*;

/**
 * Represents a cup that can be placed in the tower.
 * A cup has a height calculated from its identifier, a color,
 * and can contain other elements inside it or be covered by a lid.
 */

public class Cup extends Elements {
    /**
     * the height of the cup
     */
    private final int height;
    
    /**
     * the state of the cup
     */
    private String state;
    /**
     * the cover of the cup
     */
    private Lid hisCover;
    /**
     * the lid of the cup
     */
    private final Lid hisLid;
    /**
     * the first shape of the cup
     */
    public Rectangle shape1;
    /**
     * the second shape of the cup
     */
    public Rectangle shape2;
    /**
     * the element inside the cup
     */
    private Elements inside;

    /**
     * Constructor for the Cup class associated with an existing lid.
     * Receives color and tower explicitly to avoid calling methods on the
     * Lid parameter (Law of Demeter).
     * Used by subclasses such as {@link Hierarchical} via
     *  {@code super(inumber, lid, color, torre)}.
     *
     * @param inumber The unique identifier for the cup, used to calculate its height.
     * @param lid     The Lid object permanently associated with this cup.
     * @param color   The color to assign to this cup.
     * @param torre   The tower this cup belongs to.
     */
    public Cup(final int inumber, final Lid lid, final String color, final Tower torre) {
        super(inumber);
        height = calculateWidth(inumber);
        state = "noCovered";
        type = "cup";
        this.color = color;
        hisLid = lid;
        posy = 300 - height;
        super.canIn = true;
        this.torre = torre;
        isQuitable = true;
    }

    /**
     * Main constructor for the Cup class.
     * Creates a new independent cup by calculating its base height.
     * It generates a random color for the cup and automatically creates 
     * an associated Lid for it.
     * * @param inumber The unique identifier for the cup,
     *  used to calculate its height.
     */   
    public Cup(final int inumber,final Tower torre) {
        super(inumber);
        height =  calculateWidth(inumber);;
        state = "noCovered";
        type = "cup";
        color = randomColor();
        this.torre = torre;
        hisLid = new Lid(inumber,this);
        posy = 300 - height;
        super.canIn = true; 
    }    
    
    public String getState() {
        return state;
    }
    
    /**
     * returns the cover of a cup
     * 
     * @return hiscover, return a lid
     */
    public Lid getCover() {
        return hisCover;
    }
    
    public void setState(final String nstate) {
        state = nstate;
    }

    
    
   
    /**
     * Returns whether this cup is currently covered by a lid.
     *
     * @return {@code true} if the cup state is "Covered", {@code false} otherwise
     */
    public boolean isCovered() {
        return "Covered".equals(state);
    }
    
    /**
     * Returns a string with the cup's basic information: number, height, and state.
     *
     * @return a formatted string with the cup's identifier, height, and current state
     */
    public String getInfo(){
        final String info="informacion:"+number+", "+height+", "+state;
        System.out.println(info);
        return info;
    }
    
    /**
     * Draws the cup on the canvas using two overlapping rectangles.
     * The outer rectangle uses the cup's color and the inner one is white,
     * creating a hollow cup effect. If the cup is covered, its lid is also drawn.
     */
    @Override
    public void draw(){
        shape1= new Rectangle(height*4);
        shape1.setP(posy,posx);
        shape1.changeColor(color);
        shape2= new Rectangle((height-20)*4);
        shape2.setP(posy,posx);
        shape2.moveHorizontal(10);
        shape2.changeSize(height-10,height-20);
        shape2.changeColor("white");
        shape1.makeVisible();
        shape2.makeVisible(); 
        if(isCovered()){
            hisCover.draw();
        }
    }
    
    /**
     * Erases the cup from the canvas by hiding its shapes and its cover lid, if any.
     */
    @Override
    public void erase(){
        if (shape1 != null) {
            shape1.makeInvisible();
        }
        if (shape2 != null) {
            shape2.makeInvisible();
        }
        if (hisCover != null) {
            hisCover.makeInvisible();
        }
    }
    
    @Override
    public void setInside(final Elements inside){
         this.inside = inside;
    }
    
    @Override
    public Elements getInside(){
        return this.inside;
        }
    
    /**
     * Marks this cup as covered and makes its cover lid visible.
     */
    @Override
    public void cover(){
        final Lid lid = getCover();
        lid.draw();
        setState("Covered");
    }
    
    public Lid getHisLid(){
        return hisLid;
    }
    
    @Override
    public int getHeight() {
        return height;
    }
    
    /**
     * Sets the lid that covers this cup and marks the cup as covered.
     *
     * @param i the lid to assign as the cover of this cup
     */
    @Override
    public void setCover(final Lid lid){
        hisCover = lid;
        setState("Covered");
    }
        

    /**
     * Pushes a cup with the given identifier onto the tower.
     *
     * @param i the identifier of the cup to push
     */
    @Override
    public void push(final int number){
        torre.pushCup(number);
    }
    
    /**
     * set the statequitable of a cup
     * @param value
     */
    public void setIsQuitable(final boolean value){
        isQuitable=value;
    }
    
    public boolean getIsQuitable(){
        return isQuitable;
    }

    /**
     * A Cup cannot damage other elements by default.
     * Opener overrides this by setting eliminaTapas = true.
     *
     * @param e the element to evaluate
     * @return true if this cup can damage {@code e}
     */
    @Override
    public boolean canDamage(final Elements element) {
    	final boolean can ;
        if (eliminaTapas) {
        	can = "lid".equals(element.getType()) && element.getNumber() < this.number;
        }
        else {
        	can = false;
        }
        return can;
    }

    /**
     * A Cup cannot displace other elements by default.
     * Hierarchical overrides this by setting desplazaElementos = true.
     *
     * @param e the element to evaluate
     * @return true if this cup can displace {@code e}
     */
    @Override
    public boolean canDesplace(final Elements element) {
    	final boolean can;
        if (desplazaElementos) {
        	can = element.getNumber() >= this.number;
        }
        else {
        	can = false;
        }
        
        return can;
    }
}