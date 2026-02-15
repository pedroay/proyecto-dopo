
/**
 * Write a description of class Lib here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Lid
{

    private int width;
    private String color;
    private static int height = 1;
    private int posx;
    private int posy;
    private String state;
    private boolean isVisible;
    private Cup cup;
    private Rectangle shape;
    /**
     * Constructor de la clase Lid
     * Crea una nueva tapa con ancho y color especificados
     * 
     * @param width ancho de la tapa en píxeles
     * @param color color de la tapa
     */
    public Lid(int nwidth, String ncolor, Cup ncup) {
        width = nwidth;
        posx = 0;
        posy = 0;
        state = "normal";
        isVisible = false;
        color = ncolor;
        cup=ncup;
    }
    
    /**
     * Obtiene el ancho de la tapa
     * 
     * @return ancho en píxeles
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Obtiene la taza que cubre esta tapa
     * 
     * @return referencia a Cup si está cubriendo, null en caso contrario
     */
    public Cup getCup() {
        return cup;
    }
    
    /**
     * Obtiene el color de la tapa como cadena de texto
     * 
     * @return nombre del color (ej: "red", "blue", etc)
     */
    public String getColor() {
        return color;
    }
    
    public void changeColor(String ncolor){
        color=ncolor;
    }
    
    /**
     * Obtiene la altura de la tapa
     * 
     * @return altura siempre retorna 1 (1 cm)
     */
    public int getHeight() {
        return height;  // Siempre retorna 1
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
     * Obtiene el estado actual de la tapa
     * 
     * @return estado
     */
    public String getState() {
        return state;
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
     * Establece el estado de la tapa
     * 
     * @param nstate nuevo estado 
     */
    public void setState(String nstate) {
        state = nstate;
    }
    
    /**
     * Hace visible la taza en pantalla
     */
    public void makeVisible() {
        isVisible = true;
    }
    
    /**
     * Hace invisible la taza (la oculta)
     */
    public void makeInvisible() {
        isVisible = false;
    }
    
    /**
     * Verifica si la taza es visible
     * 
     * @return true si es visible, false en caso contrario
     */
    public boolean isVisible() {
        return isVisible;
    }
    
    public void getInfo(){
        String info="informacion:"+cup.getNumber()+", "+width+", "+height+", "+state;
        System.out.println(info);
    }
    
    public void draw(){
        shape=new Rectangle();
        shape.changeColor(color);
        shape.changeSize(height*10,width);
        posx=cup.shape1.getXP();
        posy=cup.shape1.getYP();
        shape.setP(posy,posx);
        shape.makeVisible();
        }
    
    public void erase(){
        shape.makeInvisible();
    }
}