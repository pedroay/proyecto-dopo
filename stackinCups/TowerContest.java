import java.util.ArrayList;
import javax.swing.JOptionPane;
/**
 * Write a description of class TowerContest here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TowerContest
{   
        /**
     * Generates a string representation of a valid arrangement of cups
     * whose total visible height equals the given value h.
     *
     * The method calls an auxiliary function that returns a list of cup
     * identifiers (represented as integers). Each identifier corresponds
     * to a cup whose height is calculated using the formula (2*i - 1).
     *
     * If the auxiliary method returns an empty list, it means that no valid
     * combination exists to achieve the desired height, and the method
     * returns the string "Impossible".
     *
     * Otherwise, the method builds a string by converting each identifier
     * into its corresponding height and concatenating them separated by spaces.
     *
     * @param n the number of cups available
     * @param h the desired total height of the tower
     * @return a string containing the heights of the selected cups separated
     *         by spaces, or "Impossible" if no valid solution exists
     */
    public String solve(int n, int h){
        ArrayList<Integer> respuesta = auxiliar(n,h);
        String result = "";
        if(respuesta.size() == 0){
            return "Impossible";
        }
        for(int r:respuesta){
            int altura = r*2-1;
            result += " " + altura + " ";
        }
        result = result.trim();

        return result;
    }
    
    /**
     * Simulates the construction of the cup tower based on the desired height.
     *
     * The method uses an auxiliary function to obtain a list of cup identifiers
     * that achieve the target height h. Each identifier represents a cup, where
     * its actual height is determined externally.
     *
     * If no valid combination exists, a message dialog is displayed indicating
     * that the solution is impossible.
     *
     * Otherwise, the method creates a new tower and inserts each selected cup
     * into it. Finally, the tower is made visible to display the resulting
     * configuration.
     *
     * @param n the number of cups available
     * @param h the desired total height of the tower
     */
    public void simulate(int n, int h){
        ArrayList<Integer> respuesta = auxiliar(n,h);
        Tower torre = new Tower(0);
        if(respuesta.size() == 0){
            JOptionPane.showMessageDialog(null, "Impossible");
        }
        for(int r:respuesta){
            torre.pushCup(r);
        }
        torre.makeVisible();
    }
    
    /**
     * Computes a list of cup identifiers that form a tower with the desired height.
     *
     * The method attempts to reduce the height of an initially constructed tower
     * until it matches the target value h. To improve precision and avoid working
     * with decimals, all height values are scaled by a factor of 10.
     *
     * First, it validates whether the desired height is achievable based on the
     * maximum possible height. If not, it returns an empty list.
     *
     * Then, a tower is initialized with n cups. The method repeatedly performs
     * swap operations between elements in order to reduce the height of the tower.
     * This process continues until the desired height is reached or no further
     * reductions are possible.
     *
     * If it is not possible to reach the exact height, an empty list is returned.
     * Otherwise, the method extracts the identifiers of the cups in the final
     * configuration and stores them in a list.
     *
     * @param n the number of cups available
     * @param h the desired total height of the tower
     * @return a list of integers representing the identifiers of the cups that
     *         achieve the target height, or an empty list if no solution exists
     */
    private ArrayList<Integer> auxiliar(int n, int h){
        ArrayList<Integer> respuesta = new ArrayList<Integer>();
        int nh = h * 10;
        int maxAltura = n*n*10;
        if(nh > maxAltura || h<=0){
            return respuesta;
        }
        Tower torre = new Tower(n);
        while (torre.height() != nh) {
            String[][] par = torre.swapToReduce();
            if (par.length == 0) {
                return respuesta;
            }
            torre.swap(par[0], par[1]);
        }
        String[][] items = torre.stakingItems();
        String result = "";
        for (int i = 0; i < items.length; i++) {
            int num=Integer.parseInt(items[i][1]);
            respuesta.add(num);
        }
        result = result.trim();
        return respuesta;
        
    }
    
    public Tower auxiliar2(int n, int h){
        ArrayList<Integer> respuesta = auxiliar(n,h);
        Tower torre = new Tower(0);
        if(respuesta.size() == 0){
            JOptionPane.showMessageDialog(null, "Impossible");
        }
        for(int r:respuesta){
            torre.pushCup(r);
        }
        return torre;
    }
}