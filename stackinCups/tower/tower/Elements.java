package tower;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

 /*+ Write a description of class elements here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Elements
{
	/**
     * The width of the element in pixels.
     */
    protected int width;

    /**
     * The color representation of the element.
     */
    protected String color;

    /**
     * The horizontal position (X-coordinate) of the element on the canvas.
     */
    protected int posx;

    /**
     * The vertical position (Y-coordinate) of the element on the canvas.
     */
    protected int posy;

    /**
     * Indicates whether the element is currently visible on the screen.
     */
    protected boolean isVisible;

    /**
     * The identification number or size index of the element.
     */
    protected int number;

    /**
     * The vertical dimension (height) of the element.
     */
    protected int height;

    /**
     * The specific type or category of the element.
     */
    protected String type;

    /**
     * Reference to the element positioned directly above this one.
     */
    protected Elements above;

    /**
     * Determines if another element can be placed inside this one.
     */
    protected boolean canIn;

    /**
     * Reference to the tower instance that contains this element.
     */
    protected Tower torre;

    /**
     * Indicates if the element can be removed from its current position.
     */
    protected boolean isQuitable;

    /**
     * Specifies if this element has the ability to remove lids.
     */
    protected boolean eliminaTapas;

    /**
     * Specifies if this element can displace other elements in the stack.
     */
    protected boolean desplazaElementos;

    /**
     * Flag to identify if the element behaves according to "Crazy" logic.
     */
    protected boolean isCrazy;

    /**
     * Flag to identify if the element behaves according to "Fearful" logic.
     */
    protected boolean isFearful;

    /**
     * List of positions where the element cannot be removed.
     */
    protected List<Integer> notQuitablePosition;

    /**
     * Indicates if the element is classified as a Box.
     */
    protected boolean isBox;

    /**
     * Indicates if this element is currently contained inside a box.
     */
    protected boolean isInABox;
    
    /**
     * Initializes a new Element with default properties based on its size number.
     * Calculates the initial width and sets coordinates, visibility, 
     * and behavioral flags to their default starting values.
     * * @param number The identification number used to calculate 
     * the width and define the element's size.
     */
    public Elements(final int number){
        width = calculateWidth(number);
        posx = 150 -(width/2);
        posy = 0;
        isVisible = false;
        this.number = number;
        eliminaTapas = false;
        desplazaElementos = false;
        isCrazy = false;
        isFearful = false;
        isQuitable = true;
        notQuitablePosition = new ArrayList<>();
        isBox = false;
        isInABox = false;
    }
    
    /**
     * Calculates the width of an element based on its 
     * specific numeric identifier.
     * The formula uses the identifier to determine 
     * a proportional size in pixels.
     *
     * @param inumber The numeric identifier used 
     * as the base for the calculation.
     * @return The calculated width of the element 
     * in pixels.
     */
    public final int calculateWidth(final int inumber){
        return ((2*inumber)-1)*10;
    }
    
    /**
     * Draws the element on the canvas.
     * This method must be implemented by subclasses to define 
     * the specific graphical representation of the object.
     */
    public abstract void draw();
    
    /**
     * Erases the element from the canvas.
     * This method must be implemented by subclasses to 
     * remove or hide their graphical representation.
     */
    public abstract void erase();
    public  int getNumber(){
        return number;
    }
    
    public int getWidth() {
        return width;
    }
    
    public String getColor() {
        return color;
    }
    
    /**
     * Returns a random color string from a predefined list.
     * @return A string representing a color (e.g., "red", "blue").
     */
    protected String randomColor(){
        final String[] colors = {"red","black","blue","yellow","green","magenta"};
        final Random random = new Random();

        final int index = random.nextInt(colors.length);
        return colors[index];
    }
    
    /**
     * Updates the current color of the element with 
     * a new specified color string.
     * * @param ncolor The new color to be applied 
     * to the element.
     */
    protected void changeColor(final String ncolor){
        color=ncolor;
    }
    
    public  int getPosx() {
        return posx;
    }
    
    public int getPosy() {
        return posy;
    }
    
    public int getHeight() {
        return height;
    }
    
    
    public  void setPosx(final int newPosx) {
        posx = newPosx;
    }

    public void setPosy(final int newPosy) {
        posy = newPosy;
    }
    
    /**Updates the position of the element to the 
     * specified coordinates on the canvas.
     * @param newPosx The new horizontal coordinate 
      * (X position) for the element.
      * @param newPosy The new vertical coordinate 
      * (Y position) for the element.
     */
    public void setPosition(final int newPosx, final int newPosy) {
        posx = newPosx;
        posy = newPosy;
    }
    
    /**
     * Changes the visibility of the element to true 
     * and renders it on the canvas.
     */
    public void makeVisible() {
        isVisible = true;
        draw();
    }
    
    /**
     * Changes the visibility of the element to false 
     * and removes it from the canvas.
     */
    public void makeInvisible() {
        isVisible = false;
        erase();
    }
    
    public boolean hasBeenVisible() {
        return isVisible;
    }
    
    public String getType(){
        return type;
    }
    
    public Elements getAbove(){
        return this.above;
    }
    
        public void setAbove(Elements above){
        this.above = above;
    }
    
        public  Elements getInside(){
        return null;
    }
    
    public void setCover(Lid i){}
    
    public  void setInside(Elements i){}
    
    public void cover(){}
    
    public  boolean isCanIn(){
        return canIn;
    }
    
    public  Tower getTower(){
        return torre;
    }
    
    public boolean getEliminaTapas() {
        return eliminaTapas;
    }
    
    public void setEliminaTapas(boolean value) {
        eliminaTapas = value;
    }
    
    public boolean getDesplazaElementos() {
        return desplazaElementos;
    }
    
    public final void setDesplazaElementos(boolean value) {
        desplazaElementos = value;
    }

    public boolean isDangerous() {
        return eliminaTapas;
    }

    public boolean canDesplace() {
        return desplazaElementos;
    }

    /**
     * Returns whether this element can damage the given target element.
     * Subclasses override this to define specific damage rules.
     *
     * @param e the element to evaluate
     * @return true if this element can damage {@code e}
     */
    public abstract boolean canDamage(Elements e);

    /**
     * Returns whether this element can displace the given target element.
     * Subclasses override this to define specific displacement rules.
     *
     * @param e the element to evaluate
     * @return true if this element can displace {@code e}
     */
    public abstract boolean canDesplace(Elements e);

    public abstract void push(int i);

    public boolean thisIsQuitable() {
        return isQuitable;
    }

    public void setQuitable(boolean value) {
        isQuitable = value;
    }

    public boolean thisIsCrazy() {
        return isCrazy;
    }

    public void setIsCrazy(boolean value) {
        isCrazy = value;
    }

    public boolean thisIsFearful() {
        return isFearful;
    }

    public void setIsFearful(boolean value) {
        isFearful = value;
    }

    public final void setNotQuitablePosition(int i){
        notQuitablePosition.add(i);
    }

    public boolean isNotQuitablePosition(int i){
        return notQuitablePosition.contains(i);
    }

    public List<Integer> getNotQuitablePosition(){
        return notQuitablePosition;
    }
    public final void setIsBox(boolean value) {
        isBox = value;
    }

    public boolean thisIsBox() {
        return isBox;
    }

    public void setIsInABox(boolean value) {
        isInABox = value;
    }

    public boolean thisIsInABox() {
        return isInABox;
    }
    public void createHisLid(){}
}