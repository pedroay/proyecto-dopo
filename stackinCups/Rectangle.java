import java.awt.*;

/**
 * A rectangle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes (Modified)
 * @version 1.0  (15 July 2000)()
 */


 
public class Rectangle{

    public static int EDGES = 4;
    
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
    private int perimeter;

    /**
     * Create a new rectangle at default position with default color.
     */
    public Rectangle(){
        height = 30;
        width = 40;
        xPosition = 70;
        yPosition = 15;
        color = "magenta";
        isVisible = false;
        perimeter = calculatePerimeter();
    }
    
    /**
     * Create a new rectangle based in its perimeter
     */
    public Rectangle(int per){
        height = per/4;
        width = per/4;
        xPosition = 70;
        yPosition = 15;
        color = "magenta";
        isVisible = false;
        perimeter = per;
    }
    
    public void mirror(){
        Rectangle mirror;
        mirror = new Rectangle();
        mirror.changeSize(height,width);
        mirror.setXP(xPosition-width);
        mirror.setYP(yPosition);
        if (isVisible){
            mirror.makeVisible();
        }
        draw();
    }
    
    public int getXP(){  //esto lo volvi public para poder crear la cabeza en una posición
        return xPosition;
    }
    public int getYP(){  //esto lo volvi public para poder crear la cabeza en una posición
        return yPosition;
    }
    
    public void setXP(int nPosx){  //esto lo volvi public para poder crear la cabeza en una posición
        xPosition = nPosx;
    }
    
    public void setYP(int nPosy){ //esto lo volvi public para poder crear la cabeza en una posición
        yPosition = nPosy;
    }
    
    public void setP(int nPosy,int nPosx){
        xPosition = nPosx;   
        yPosition = nPosy;
    }
    
    /**
     * It calculates the Rectangle's perimeter.
     */
    private int calculatePerimeter(){
        perimeter=(height*2)+(width*2);
        return perimeter;
    }
    
    /**
     * Make this rectangle visible. If it was already visible, do nothing.
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    
    /**
     * Make this rectangle invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible(){
        this.erase();
        isVisible = false;
    }
    
    /**
     * Move the rectangle a few pixels to the right.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Move the rectangle a few pixels to the left.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Move the rectangle a few pixels up.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Move the rectangle a few pixels down.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Move the rectangle horizontally.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Move the rectangle vertically.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Slowly move the rectangle horizontally.
     * @param distance the desired distance in pixels
     */
    public void slowMoveHorizontal(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    /**
     * Slowly move the rectangle vertically.
     * @param distance the desired distance in pixels
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * Change the size to the new size
     * @param newHeight the new height in pixels. newHeight must be >=0.
     * @param newWidht the new width in pixels. newWidth must be >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        calculatePerimeter();
        draw();
    }
    
    /**
     * Change the size in a 100% keeping the proportion.
     * @param z it determines if the size increases or decreaces.
     */
    public void zoom(char z){
        if(z=='-'){
            changeSize(height/2, width/2);
        }else if(z=='+'){
            changeSize(height*2, width*2);
        }
    }
    
    /**
     * Change the color. 
     * @param color the new color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta" and "black".
     */
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }

    /**
     * Draw the rectangle with current specifications on screen.
     */

    public void draw() { //esto lo volvi public para poder crear la cabeza en una posición
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, 
                                       width, height));
            canvas.wait(10);
        }
    }

    /**
     * Erase the rectangle on screen.
     */
    private void erase(){
        Canvas canvas = Canvas.getCanvas();
        canvas.erase(this);
        
    }
    
    /**
     * Move the rectangle in n times
     * @param int times in the canvas
     */
    public void walk(int times,int position){
        if(times > 0){
            int minimoves = position / times;
            for(int i = 0;i<times;i++){
                slowMoveHorizontal(minimoves);
                draw();
            }
        }
    }
    
    
}


