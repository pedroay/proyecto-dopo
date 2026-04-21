package tower;
import java.util.Random;
import Shapes.*;



public class Box extends Cup
{
    private Rectangle shapeExtra = new Rectangle();

    public Box(int iNumber,Tower torre){
        super(iNumber, torre);
        setIsBox(true);
    }

    @Override
    public void draw(){
        super.draw();
        shapeExtra.changeColor("brown");
        shapeExtra.changeSize(10, width - 10);
        shapeExtra.setP(posy + getHeight() - 10, 150 - ((width - 10) / 2));
        shapeExtra.makeVisible();
    }

    @Override
    public void erase(){
        super.erase();
        if(shapeExtra != null) {
            shapeExtra.makeInvisible();
        }
    }

    @Override
    public void createHisLid(){
        torre.pushLid(number);
    }
}