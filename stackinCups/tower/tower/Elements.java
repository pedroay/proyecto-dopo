package tower;
import java.util.Random;
import java.util.ArrayList;

 /*+ Write a description of class elements here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Elements
{
    protected int width;
    protected String color;
    protected int posx;
    protected int posy;
    protected boolean isVisible;
    protected int number;
    protected int height;
    protected String type;
    protected Elements above;
    protected boolean canIn;
    protected Tower torre;
    protected boolean isQuitable;
    protected boolean eliminaTapas;
    protected boolean desplazaElementos;
    protected boolean isCrazy;
    protected boolean isFearful;
    protected ArrayList<Integer> notQuitablePosition;
    protected boolean isBox;
    protected boolean isInABox;
    
    public Elements(int number){
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
        notQuitablePosition = new ArrayList<Integer>();
        isBox = false;
        isInABox = false;
    }
    
    public int calculateWidth(int inumber){
        return ((2*inumber)-1)*10;
    }
    
    public abstract void draw();
    public abstract void erase();
    public final int getNumber(){
        return number;
    }
    
    public final int getWidth() {
        return width;
    }
    
    public final String getColor() {
        return color;
    }
    
    /**
     * retorna un color apartir de una lista 
     */
    protected String randomColor(){
        String[] colors = {"red","black","blue","yellow","green","magenta"};
        Random random = new Random();

        int index = random.nextInt(colors.length);
        return colors[index];
    }
    
    protected void changeColor(String ncolor){
        color=ncolor;
    }
    
    public final int getPosx() {
        return posx;
    }
    
    public final int getPosy() {
        return posy;
    }
    
    public final int getHeight() {
        return height;
    }
    
    public final void setPosx(int newPosx) {
        posx = newPosx;
    }

    public final void setPosy(int newPosy) {
        posy = newPosy;
    }
    
    public final void setPosition(int newPosx, int newPosy) {
        posx = newPosx;
        posy = newPosy;
    }
    
    public void makeVisible() {
        isVisible = true;
        draw();
    }
    
    public void makeInvisible() {
        isVisible = false;
        erase();
    }
    
    public final boolean isVisible() {
        return isVisible;
    }
    
    public final String getType(){
        return type;
    }
    
    public final Elements getAbove(){
        return this.above;
    }
    
        public final void setAbove(Elements above){
        this.above = above;
    }
    
        public final Elements getInside(){
        return null;
    }
    
    public final void setCover(Lid i){}
    
    public final void setInside(Elements i){}
    
    public void cover(){}
    
    public final boolean isCanIn(){
        return canIn;
    }
    
    public final Tower getTower(){
        return torre;
    }
    
    public final boolean getEliminaTapas() {
        return eliminaTapas;
    }
    
    public final void setEliminaTapas(boolean value) {
        eliminaTapas = value;
    }
    
    public final boolean getDesplazaElementos() {
        return desplazaElementos;
    }
    
    public final void setDesplazaElementos(boolean value) {
        desplazaElementos = value;
    }

    public final boolean isDangerous() {
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

    public final boolean isQuitable() {
        return isQuitable;
    }

    public final void setQuitable(boolean value) {
        isQuitable = value;
    }

    public final boolean isCrazy() {
        return isCrazy;
    }

    public final void setIsCrazy(boolean value) {
        isCrazy = value;
    }

    public final boolean isFearful() {
        return isFearful;
    }

    public final void setIsFearful(boolean value) {
        isFearful = value;
    }

    public final void setNotQuitablePosition(int i){
        notQuitablePosition.add(i);
    }

    public final boolean isNotQuitablePosition(int i){
        return notQuitablePosition.contains(i);
    }

    public final ArrayList<Integer> getNotQuitablePosition(){
        return notQuitablePosition;
    }
    public final void setIsBox(boolean value) {
        isBox = value;
    }

    public final boolean isBox() {
        return isBox;
    }

    public final void setIsInABox(boolean value) {
        isInABox = value;
    }

    public final boolean isInABox() {
        return isInABox;
    }
    public void createHisLid(){}
}