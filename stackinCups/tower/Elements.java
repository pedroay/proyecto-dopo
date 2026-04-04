package tower;
import java.util.Random;
/**
 * Write a description of class elements here.
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
    
    public Elements(int number){
        width = calculateWidth(number);
        posx = 150 -(width/2);
        posy = 0;
        isVisible = false;
        this.number = number;
    }
    
    public int calculateWidth(int inumber){
        return ((2*inumber)-1)*10;
    }
    
    public abstract void draw();
    public abstract void erase();
    public int getNumber(){
        return number;
    }
    
    public int getWidth() {
        return width;
    }
    
        /**
     * Obtiene el color del elemento como cadena de texto
     * 
     * @return nombre del color (ej: "red", "blue", etc)
     */
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
    
        /**
     * Obtiene la posición X de la tapa
     * 
     * @return coordenada X en píxeles
     */
    public int getPosx() {
        return posx;
    }
    
        /**
     * Obtiene la posición Y de la tapa
     * 
     * @return coordenada Y en píxeles
     */
    public int getPosy() {
        return posy;
    }
    
     /**
     * Obtiene la altura de la tapa
     * 
     * @return altura siempre retorna 1 (1 cm)
     */
    public int getHeight() {
        return height;
    }
    
        /**
     * Establece la posición X de la tapa
     * 
     * @param newPosx nueva coordenada X en píxeles
     */
    public void setPosx(int newPosx) {
        posx = newPosx;
    }
        /**
     * Establece la posición Y de la tapa
     * 
     * @param newPosy nueva coordenada Y en píxeles
     */
    public void setPosy(int newPosy) {
        posy = newPosy;
    }
    
       /**
     * Establece la posición X y Y de la tapa
     * 
     * @param newPosx nueva coordenada X en píxeles
     * @param newPosy nueva coordenada Y en píxeles
     */
    public void setPosition(int newPosx, int newPosy) {
        posx = newPosx;
        posy = newPosy;
    }
    
        /**
     * Hace visible la taza en pantalla
     */
    public void makeVisible() {
        isVisible = true;
        draw();
    }
    
        /**
     * Hace invisible la taza (la oculta)
     */
    public void makeInvisible() {
        isVisible = false;
        erase();
    }
    
        /**
     * Verifica si la taza es visible
     * 
     * @return true si es visible, false en caso contrario
     */
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
    
        public Elements getInside(){
        return null;
    }
    
    public void setCover(Lid i){}
    
    public void setInside(Elements i){}
    
    public void cover(){}
    
    public boolean isCanIn(){
        return canIn;
    }
    
    
}