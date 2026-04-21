package tower;
import Shapes.*;
/**
 * Represents a Lid that covers a Cup in the tower.
 */
public class Lid extends Elements
{
    /** The current state of the lid. */
    private String state;
    /** The cup associated with this lid, if any. */
    private Cup hisCup;
    /** The visual shape of the lid used for drawing. */
    private Rectangle shape;
    
 

    
    /**
     * Main constructor for the Lid class.
     * Creates a new lid based on a unique identifier 
     * number which determines its width.
     * It assigns a random color and automatically 
     * creates a new Cup associated with it.
     * 
     * @param number The unique identifier that determines the lid's size (width).
     * @param torre  The tower this lid belongs to.
     */
    public Lid(final int number,final Tower torre) {
        super(number);
        height = 10;
        posy = 300-height;
        state = "normal";
        color = randomColor();
        this.torre = torre;
        hisCup= new Cup(number,getTower());
        type = "lid";
        super.canIn = false;
    }
    
    /**
     * Overloaded constructor for the Lid class.
     * Creates a new lid from a unique identifier number and an existing Cup.
     * The lid will inherit the color and tower context from the provided Cup.
     * 
     * @param number The unique identifier that determines the lid's size (width).
     * @param cup    The Cup object to which this lid will be associated.
     */
    public Lid(final int number,final Cup cup,final String color, final Tower torre) {
        super(number);
        height = 10;
        posy = 300-height;
        state = "normal";
        this.color = color;
        hisCup= cup;
        type = "lid";
        super.canIn = false;
        this.torre = torre;
    }    
    
    public void setHisCup(final Cup cup) {
        this.hisCup = cup;
    }
    
    /**
     * Gets the cup that this lid covers.
     * 
     * @return a reference to the Cup, or null if not associated with one.
     */
    public Cup getHisCup() {
        return hisCup;
    }

    /**
     * Gets the current state of the lid.
     * 
     * @return the state of the lid.
     */
    public String getState() {
        return state;
    }
 
    /**
     * Sets the state of the lid.
     * 
     * @param nstate the new state to set.
     */
    public void setState(final String nstate) {
        state = nstate;
    }
    
    public void getInfo(){
        final String info="informacion:"+hisCup.getNumber()+", "+width+", "+height+", "+state;
        System.out.println(info);
    }
    
    @Override
    public void draw(){
        shape=new Rectangle();
        shape.changeColor(color);
        shape.changeSize(height,width);
        shape.setP(posy,posx);
        shape.makeVisible();
        }
    
    @Override
    public void erase(){
         if(shape != null){   
         shape.makeInvisible();
         isVisible = false;
        }
    }

   
    
    

    public void push(int i){
        torre.pushLid(i);
    }

    /**
     * Lids do not damage other elements.
     *
     * @param e the element to evaluate
     * @return always false
     */
    @Override
    public boolean canDamage(Elements e) {
        return false;
    }

    /**
     * Lids do not displace other elements.
     *
     * @param e the element to evaluate
     * @return always false
     */
    @Override
    public boolean canDesplace(Elements e) {
        return false;
    }
}
