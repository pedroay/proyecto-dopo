import java.util.Random;

/**
 * Write a description of class Cup here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class Cup {
    private int number;
    private int height;
    private int posx;
    private int posy;
    private String state;
    private String color;
    private boolean isVisible;
    private Lid cover;
    public Rectangle shape1;
    public Rectangle shape2;
    private Cup inside;
    private Cup above;
    
    /**
     * Constructor de la clase Cup
     * Crea una nueva taza con un número identificador
     * 
     * @param number identificador único de la taza
     */
    public Cup(int inumber) {
        number = inumber;
        height = calculateHeight(inumber);
        //hay que calcular min y max dependiendo del numero
        state = "noCovered"; //opciones: Covered, noCovered 
                             // no es booleano porque 
                             //da posibilidad a extender despúes
        color = randomColor();
        isVisible = false;
        cover = new Lid(height,color,this);
        posx = 150 - (height/2);
        posy = 300 - height;
            
    }
    
    private String randomColor(){
        String[] colors = {"red","black","blue","yellow","green","magenta"};
        Random random = new Random();

        int index = random.nextInt(colors.length);
        return colors[index];
    }
    
    public int calculateHeight(int inumber){
        return ((2*inumber)-1)*10;
    }
    
    /**
     * Obtiene el número identificador de la taza
     * 
     * @return número identificador
     */
    public int getNumber() {
        return number;
    }
    
    /**
     * Obtiene la altura de la taza
     * 
     * @return altura en centímetros
     */
    public int getHeight() {
        return height;
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
     * Obtiene la posición X de la taza
     * 
     * @return coordenada X en píxeles
     */
    public int getPosx() {
        return posx;
    }
    
    /**
     * Obtiene la posición Y de la taza
     * 
     * @return coordenada Y en píxeles
     */
    public int getPosy() {
        return posy;
    }
    
    /**
     * Obtiene el color de la taza
     * 
     * @return color en formato RGB entero
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Cambia el color de la taza usando código RGB
     * 
     * @param ncolor nuevo color
     */
    public void changeColor(String ncolor) {
        color = ncolor;
        cover.changeColor(ncolor);
    }
    
    /**
     * Establece la posición de la taza en pantalla
     * 
     * @param posx coordenada X en píxeles
     * @param posy coordenada Y en píxeles
     */
    public void setPosition(int posx, int posy) {
        this.posx = posx;
        this.posy = posy;
    }
    
    /**
     * Establece el estado de la taza
     * 
     * @param nstate nuevo estado (ej: "normal", "covered", "selected", "stacked")
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
    
    private void draw(){
        shape1= new Rectangle(height*4);
        shape1.setP(posy,posx);
        shape1.changeColor(color);
        shape2= new Rectangle((height-20)*4);
        shape2.setP(posy,posx);
        shape2.moveHorizontal(10);
        shape2.changeSize(height-10,height-20);//altura,ancho
        shape2.changeColor("white");
        shape1.makeVisible();
        shape2.makeVisible(); 
        if(isCovered()){
            cover.draw();
        }
    }
    
    private void erase(){
        shape1.makeInvisible();
        shape2.makeInvisible();
        cover.makeInvisible();
    }
    
    public void setCupInside(Cup nCup){
         inside = nCup;
    }
    
    public void setCupAbove(Cup nCup){
         above =nCup;
    }
    
    public Cup getCupInside(){
        return this.inside;
    }
    
    public Cup getCupAbove(){
        return this.above;
    }
}