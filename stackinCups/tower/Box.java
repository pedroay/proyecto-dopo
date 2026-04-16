package tower;
import java.util.Random;
import Shapes.*;


/**
 * Write a description of class Box here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Box extends Cup
{
    public Box(int iNumber,Tower torre){
        super(iNumber, torre);
        torre.pushLid(iNumber);
    }
   
}