
/**
 * Write a description of class TowerContest here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TowerContest
{
    public String solve(int n, int h){
        int nh = h * 10;
        int maxAltura = n*n*10;
        if(nh > maxAltura || h<=0){
            return "Imposible";
        }
        Tower torre = new Tower(n);
        while (torre.height() > nh) {
            String[][] par = torre.swapToReduce();
            if (par.length == 0) {
                return "Imposible";
            }
            torre.swap(par[0], par[1]);
        }
        
        String[][] items = torre.stakingItems();
        String result = "";
        for (int i = 0; i < items.length; i++) {
            int num=Integer.parseInt(items[i][1]);
            int altura=num*2-1;
            result += " " + altura + "\n";
        }
        result = result.trim();
        System.out.println(result);
        //torre.makeVisible();
        return result;
    }
    public void simulate(int n, int h){
        String result=solve(n,h);
        if(result=="Imposible"){ 
        }
    
    }
}