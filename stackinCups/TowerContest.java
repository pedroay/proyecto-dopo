
/**
 * Write a description of class TowerContest here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TowerContest
{
    public void solve(int n, int h){
        int nh = h * 10;
        int maxAltura = n*n*10;
        if(nh > maxAltura){
            return ;
        }
        Tower torre = new Tower(n);
        int actual =torre.height();
        for(int i = n;i>= 1;i--){
            int altura = torre.findCupByNumber(i).getHeight();
            int reduccion = altura - 20;
            if(actual - reduccion >= nh){
                actual = actual - reduccion;
                //aqui toca buscar el que es inmediatamente menor para meterlo dentro 
            }
        }
    }
}