
package tower;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;



/**
 * Elements is the abstract base class for all objects in the tower simulation.
 * It defines common physical properties, visibility, and behavioral flags 
 * for specialized elements like Cups and Lids.
 * * @author (Your Name) 
 * @version 2026.04.21
 */
public abstract class Elements {
    /** Width of the element in pixels. */
    protected int width;
    
    /** Color representation of the element. */
    protected String color;
    
    /** Horizontal position on the canvas. */
    protected int posx;
    
    /** Vertical position on the canvas. */
    protected int posy;
    
    /** Current visibility state. */
    protected boolean isVisible;
    
    /** Unique numeric identifier for the element. */
    protected int number;
    
    /** Height of the element in pixels. */
    protected int height;
    
    /** Category type (e.g., "cup", "lid"). */
    protected String type;
    
    /** Reference to the element stacked immediately above this one. */
    protected Elements above;
    
    /** Flag indicating if other elements can be placed inside this one. */
    protected boolean canIn;
    
    /** Reference to the tower container. */
    protected Tower torre;
    
    /** Indicates if the element is allowed to be removed. */
    protected boolean isQuitable;
    
    /** Behavioral flag for elements that can remove lids. */
    protected boolean eliminaTapas;
    
    /** Behavioral flag for elements that can move others upon entry. */
    protected boolean desplazaElementos;
    
    /** Flag for the Crazy specialized behavior. */
    protected boolean isCrazy;
    
    /** Flag for the Fearful specialized behavior. */
    protected boolean isFearful;
    
    /** Flag indicating if the element is a Box. */
    protected boolean isBox;
    
    /** State flag for elements currently contained within a box. */
    protected boolean isInABox;
    
    /** * List of positions where the element cannot be removed.
     * Use List instead of ArrayList to avoid LooseCoupling violations.
     */
    protected ArrayList<Integer> notQuitablePosition;

    /**
     * Constructs an Element with a specific identifier.
     * Initializes default physical properties and behavioral flags.
     *
     * @param number The unique numeric identifier for the element.
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
        isBox = false;
        isInABox = false;
        notQuitablePosition = new ArrayList<>();
    }
    
    /**
    * Calculates the width of the element based on its numeric ID.
    * Uses the formula: ((2 * n) - 1) * 10.
    *
    * @param inumber The number used for the calculation.
    * @return The calculated width as an integer.
    */
    public final int calculateWidth(final int inumber){
        return ((2*inumber)-1)*10;
    }
    
    /**
     * Draws the element on the graphical canvas.
     * Each subclass must implement this method to define its 
     * specific visual representation using shapes.
     */
    public abstract void draw();
    
    /**
     * Erases the element from the graphical canvas.
     * This method should make all shapes associated with the 
     * element invisible or remove them from the screen.
     */
    public abstract void erase();
    public int getNumber(){
        return number;
    }

    public int getWidth() {
        return width;
    }

    public final String getColor() {
        return color;
    }

    /**
     * retorna un color apartir de una lista 
     */
    protected final String randomColor(){
        final String[] colors = {"red","black","blue","yellow","green","magenta"};
        final Random random = new Random();

        final int index = random.nextInt(colors.length);
        return colors[index];
    }

   
    protected void setColor(final String ncolor){
        color=ncolor;
    }

    public int getPosx() {
        return posx;
    }

    public int getPosy() {
        return posy;
    }

    public int getHeight() {
        return height;
    }

    public void setPosx(final int newPosx) {
        posx = newPosx;
    }

    public void setPosy(final int newPosy) {
        posy = newPosy;
    }
    
    /**
     * Updates the coordinates of the element on the canvas.
     * * @param newPosx The new horizontal coordinate.
     * @param newPosy The new vertical coordinate.
     */
    public void setPosition(final int newPosx,final int newPosy) {
        posx = newPosx;
        posy = newPosy;
    }
    
    /**
     * Changes the visibility state to true and renders the element
     * on the graphical interface by calling the draw method.
     */
    public void makeVisible() {
        isVisible = true;
        draw();
    }
    
    /**
     * Changes the visibility state to false and removes the element
     * from the graphical interface by calling the erase method.
     */
    public void makeInvisible() {
        isVisible = false;
        erase();
    }
    
    /**
     * say if a element is visible
     * @return
     */
    public boolean thisIsVisible() {
        return isVisible;
    }

    public String getType(){
        return type;
    }

    public Elements getAbove(){
        return this.above;
    }

    public void setAbove(final Elements above){
        this.above = above;
    }
    
    /**
     * Retrieves the element currently contained inside this element.
     * * @return The Elements instance located inside, or null if empty.
     */
    public abstract Elements getInside();
    
    /**
     * Sets a specific lid to cover this element.
     * * @param lid The Lid instance that will cover this element.
     */
    public abstract void setCover(Lid lid);
    
    /**
     * Places an element inside this one.
     * * @param element The Elements instance to be placed inside.
     */
    public abstract void setInside(Elements element);
    
    /**
     * Marks this cup as covered and makes its cover lid visible.
     */
    public abstract void cover();

    public boolean isCanIn(){
        return canIn;
    }
    
    /**
     * returns the tower
     * @return
     */
    public final Tower getTower(){
        return torre;
    }

    public boolean getEliminaTapas() {
        return eliminaTapas;
    }

    public final void setEliminaTapas(final boolean value) {
        eliminaTapas = value;
    }

    public boolean getDesplazaElementos() {
        return desplazaElementos;
    }

    public final void setDesplazaElementos(final boolean value) {
        desplazaElementos = value;
    }
    
    /**
     * tell us if a element is dangerous
     * @return eliminaTapas
     */
    public boolean isDangerous() {
        return eliminaTapas;
    }
    
    /**
     * tell us if a element can desplace
     * @return
     */
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
    public abstract boolean canDamage(Elements element);

    /**
     * Returns whether this element can displace the given target element.
     * Subclasses override this to define specific displacement rules.
     *
     * @param e the element to evaluate
     * @return true if this element can displace {@code e}
     */
    public abstract boolean canDesplace(Elements element);
    
    /**
     * push a element in a towe with his number
     * @param number
     */
    public abstract void push(int number);
    
    /**
     * tell us if a element is quitable
     * @return
     */
    public boolean thisIsQuitable() {
        return isQuitable;
    }
    
    /**
     * set if a element is quitable
     * @param value
     */
    public void setQuitable(final boolean value) {
        isQuitable = value;
    }
    /**
     * return if a element is crazy
     * @return isCrazy
     */
    public boolean thisIsCrazy() {
        return isCrazy;
    }

    public void setIsCrazy(final boolean value) {
        isCrazy = value;
    }
    
    /**
     * return if a element is fearful
     * @return
     */
    public boolean thisIsFearful() {
        return isFearful;
    }
    
    /**
     * set if a element is a fearful
     * @param value
     */
    public final void setIsFearful(final boolean value) {
        isFearful = value;
    }

    /**
     * Adds a position to the list of positions where this element cannot be removed.
     *
     * @param i The position index to be added.
     */
    public final void setNotQuitablePosition(final int position){
        notQuitablePosition.add(position);
    }

    /**
     * Checks if the element cannot be removed from a specific position.
     *
     * @param i The position index to check.
     * @return true if the position is in the not quitable list, false otherwise.
     */
    public boolean isNotQuitablePosition(final int position){
        return notQuitablePosition.contains(position);
    }

    /**
     * Retrieves the list of positions where the element cannot be removed.
     *
     * @return A list containing the not quitable positions.
     */
    public List<Integer> getNotQuitablePosition(){
        return notQuitablePosition;
    }

    public boolean getIsBox() {
        return isBox;
    }

    /**
     * Sets whether this element functions as a Box.
     *
     * @param value true if the element is a Box, false otherwise.
     */
    public final void setIsBox(final boolean value) {
        isBox = value;
    }

    /**
     * Checks if this element is currently inside a Box.
     *
     * @return true if the element is enclosed in a Box, false otherwise.
     */
    public boolean thisIsInABox() {
        return isInABox;
    }

    /**
     * Sets the state indicating whether this element is inside a Box.
     *
     * @param value true if the element is inside a Box, false otherwise.
     */
    public void setIsInABox(final boolean value) {
        isInABox = value;
    }
}