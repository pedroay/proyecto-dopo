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
    
    public Elements(int number,Tower torre){
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
        this.torre = torre;
    }
    
    public final int calculateWidth(int inumber){
        return ((2*inumber)-1)*10;
    }
    
    public abstract void draw();
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
    
    public  int getPosx() {
        return posx;
    }
    
    public int getPosy() {
        return posy;
    }
    
    public int getHeight() {
        return height;
    }
    
    public  void setPosx(int newPosx) {
        posx = newPosx;
    }

    public void setPosy(int newPosy) {
        posy = newPosy;
    }
    
    public void setPosition(int newPosx, int newPosy) {
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
    
    public boolean isVisible() {
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

    public boolean isQuitable() {
        return isQuitable;
    }

    public void setQuitable(boolean value) {
        isQuitable = value;
    }

    public boolean isCrazy() {
        return isCrazy;
    }

    public void setIsCrazy(boolean value) {
        isCrazy = value;
    }

    public boolean isFearful() {
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

    public ArrayList<Integer> getNotQuitablePosition(){
        return notQuitablePosition;
    }
    public final void setIsBox(boolean value) {
        isBox = value;
    }

    public boolean isBox() {
        return isBox;
    }

    public void setIsInABox(boolean value) {
        isInABox = value;
    }

    public boolean isInABox() {
        return isInABox;
    }
    public void createHisLid(){}
}