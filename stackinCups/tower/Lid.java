package tower;
import java.util.Random;
import Shapes.*;
/**
 * Write a description of class Lib here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Lid extends Elements
{

    private String state;
    private Cup hisCup;
    private Rectangle shape;
    private final static boolean canIN = false;
    private boolean isFearful;
    private boolean isCrazy;
    
    /**
     * Constructor principal de la clase Lid.
     * Crea una nueva tapa basándose en un número identificador. Este número determina 
     * su ancho. Además, le asigna un color aleatorio y crea automáticamente una taza (Cup)
     * asociada a ella.
     * * @param number El número identificador único que determina el tamaño (ancho) de la tapa.
     */
    public Lid(int number,Tower torre) {
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
     * Constructor sobrecargado de la clase Lid.
     * Crea una nueva tapa a partir de un número identificador y una taza (Cup) ya existente.
     * La tapa heredará el color de la taza proporcionada.
     * * @param number El número identificador único que determina el tamaño (ancho) de la tapa.
     * @param cup El objeto Cup al que se asociará esta tapa y del cual tomará su color.
     */
    public Lid(int number,Cup cup) {
        super(number);
        height = 10;
        posy = 300-height;
        state = "normal";
        color = cup.getColor();
        hisCup= cup;
        type = "lid";
        super.canIn = false;
        this.torre = cup.getTower();
    }    
    
    public void setHisCup(Cup cup) {
        this.hisCup = cup;
    }
    
    /**
     * Obtiene la taza que cubre esta tapa
     * 
     * @return referencia a Cup si está cubriendo, null en caso contrario
     */
    public Cup getHisCup() {
        return hisCup;
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
     * Establece el estado de la tapa
     * 
     * @param nstate nuevo estado 
     */
    public void setState(String nstate) {
        state = nstate;
    }
    
    public void getInfo(){
        String info="informacion:"+hisCup.getNumber()+", "+width+", "+height+", "+state;
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
    
    public boolean getIsFearful() {
        return isFearful;
    }
    
    public void setIsFearful(boolean value) {
        isFearful=value;
    }
    
    public boolean getIsCrazy() {
        return isCrazy;
    }
    
    public void setIsCrazy(boolean value) {
        isCrazy=value;
    }
    
    public void setAbove(Cup above){
        this.above = above;
    }
    
    public void setAbove(Lid above){
        this.above = above;
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