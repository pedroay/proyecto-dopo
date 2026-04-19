package tower;
import java.util.Random;
import Shapes.*;

/**
 * Write a description of class Cup here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class Cup extends Elements {
    
    private int height;
    private String state;
    private Lid cover;
    private final Lid hisLid;
    public Rectangle shape1;
    public Rectangle shape2;
    private Elements inside;
    
    /**
     * Constructor de la clase Cup asociado a una tapa existente.
     * Crea una nueva taza calculando su altura en base al identificador numérico.
     * Se asocia directamente con la tapa (Lid) proporcionada y toma el color de esta.
     * * @param inumber El identificador único de la taza, utilizado para calcular su altura.
     * @param lid El objeto Lid (tapa) que cubrirá esta taza.
     */
    public Cup(int inumber,Lid lid) {
        super(inumber);
        height = calculateWidth(inumber);
        state = "noCovered";
        type = "cup";
        color = lid.getColor();
        hisLid = lid;
        posy = 300 - height;
        super.canIn = true;
        torre = lid.getTower();
        isQuitable=true;
    }
    
    /**
     * Constructor principal de la clase Cup.
     * Crea una nueva taza independiente calculando su altura base. Genera un color 
     * aleatorio para la taza y crea automáticamente una tapa (Lid) asociada a ella.
     * * @param inumber El identificador único de la taza, utilizado para calcular su altura.
     */     
    public Cup(int inumber,Tower torre) {
        super(inumber);
        height =  calculateWidth(inumber);;
        state = "noCovered";
        type = "cup";
        color = randomColor();
        this.torre = torre;
        hisLid = new Lid(inumber,this);
        posy = 300 - height;
        super.canIn = true; 
        isQuitable=true;
    }    
    
    /**
     * Obtiene el estado actual de la taza
     * 
     * @return estado actual de la taza sobre su covertura
     */
    public String getState() {
        return state;
    }
    
    /**
     * Obtiene la tapa que cubre esta taza
     * 
     * @return la tapa (Lid)
     */
    public Lid getCover() {
        return cover;
    }
    
    /**
     * Establece el estado de la taza
     * 
     * @param nstate nuevo estado (ej: "normal", ", "selected", "stacked")
     */
    public void setState(String nstate) {
        state = nstate;
    }

    
    /**
     * Establece la altura de la taza
     * 
     * @param newHeight nueva altura en cm
     */
    private void setHeight(int newHeight) {
        height = newHeight;
    }
    
   
    /**
     * Verifica si la taza está cubierta por una tapa
     * 
     * @return true si tiene una tapa, false si tiene otro estado
     */
    public boolean isCovered() {
        return state=="Covered";
    }
    
    public String getInfo(){
        String info="informacion:"+number+", "+height+", "+state;
        System.out.println(info);
        return info;
    }
    
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
            cover.draw();
        }
    }
    
    @Override
    public void erase(){
        if (shape1 != null) {
            shape1.makeInvisible();
        }
        if (shape2 != null) {
            shape2.makeInvisible();
        }
        if (cover != null) {
            cover.makeInvisible();
        }
    }
    
    public void setInside(Elements inside){
         this.inside = inside;
    }
    
    @Override
    public Elements getInside(){
        return this.inside;
        }
        
    public void cover(){
        Lid l = getCover();
        l.isVisible();
        setState("Covered");
    }
    
    public Lid getHisLid(){
        return hisLid;
    }
    
    public int getHeight() {
        return height;
    }
    
    @Override
    public void setCover(Lid i){
        cover = i;
        setState("Covered");
        }
        
    public void push(int i){
        torre.pushCup(i);
    }
    
    public void setIsQuitable(boolean value){
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
    public boolean canDamage(Elements e) {
        if (!eliminaTapas) return false;
        return e.getType().equals("lid") && e.getNumber() < this.number;
    }

    /**
     * A Cup cannot displace other elements by default.
     * Hierarchical overrides this by setting desplazaElementos = true.
     *
     * @param e the element to evaluate
     * @return true if this cup can displace {@code e}
     */
    @Override
    public boolean canDesplace(Elements e) {
        if (!desplazaElementos) return false;
        return e.getNumber() >= this.number;
    }
}