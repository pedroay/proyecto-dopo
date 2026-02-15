import java.util.Stack;
import java.util.ArrayList;

/**
 * Write a description of class Tower here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tower
{
    private int width;
    private int height;
    private int minHeight;
    private int maxHeight;
    private boolean isVisible;
    private Stack<Cup> cups;
    private Stack<Lid> lids;
    private boolean isOK;
    
    /**
     * Constructor for objects of class Tower
     */
    public Tower(int nwidth, int nmaxHeight)
    {
        width = nwidth;
        maxHeight = nmaxHeight;
        isVisible = false;
        cups = new Stack<Cup>();
        lids = new Stack<Lid>();
        isOK = true;
    }
    
    /**
     * Agrega una taza al tope de la torre
     */
    public void pushCup(int i){
        boolean alreadyexist = false;
        if(!cups.isEmpty()){
            for (Cup c : cups) {
                if (c.getNumber() == i) {
                    alreadyexist = true;
                    isOK = false;
                    break;
                }
            }
        }
        if (!alreadyexist) {
            Cup ncup = new Cup(i);
            cups.push(ncup);
            Lid nlid=ncup.getCover();
            pushLid(nlid);
            isOK = true;
        }
    }
    
    /**
     * Remueve y retorna la taza del tope
     */
    public Cup popCup()
    {
        if (!cups.isEmpty()) {
            isOK = true;
            return cups.pop();
        } else {
            isOK = false;
            return null;
        }
    }
    
    /**
     * Remueve una taza específica por número
     */
    public void removeCup(int i)
    {
        Stack<Cup> temp = new Stack<Cup>();
        Cup removedCup = null;
        boolean found = false;
        while (!cups.isEmpty()) {
            Cup c = cups.pop();
            if (c.getNumber() != i) {
                temp.push(c);
            } else {
                found = true;
                removedCup=c;
            }
        }
        while (!temp.isEmpty()) {
            cups.push(temp.pop());
        } 
        if (found && removedCup != null) {
            removeLid(removedCup);
        }
        isOK = found;
    }
    
    /**
     * Agrega una tapa al tope de la torre
     */
    public void pushLid(Lid lid)
    {
        if (lid != null) {
            lids.push(lid);
            isOK = true;
        } else {
            isOK = false;
        }
    }
    
    /**
     * Remueve y retorna la tapa del tope
     */
    public Lid popLid()
    {
        if (!lids.isEmpty()) {
            isOK = true;
            return lids.pop();
        } else {
            isOK = false;
            return null;
        }
    }
    
    public void removeLid(Cup removedCup){
        Lid tapaDeLaTaza = removedCup.getCover();
        removeLid(tapaDeLaTaza);
    }
    
    /**
     * Remueve una tapa del stack
     */
    public void removeLid(Lid lid)
    {
        if (lids.remove(lid)) {
            isOK = true;
        } else {
            isOK = false;
        }
    }
    
    /**
     * Ordena la torre de mayor a menor altura
     */
    public void orderTower()
    {
        ArrayList<Cup> temp = new ArrayList<Cup>(cups);
        for (int i = 0; i < temp.size(); i++) {
            for (int j = 0; j < temp.size() - 1; j++) {
                if (temp.get(j).getNumber() < temp.get(j + 1).getNumber()) {
                    Cup aux = temp.get(j);
                    temp.set(j, temp.get(j + 1));
                    temp.set(j + 1, aux);
                }
            }
        }
        cups.clear();
        for (Cup c : temp) {
            cups.push(c);
        }
        System.out.println(cups);
        isOK = true;
    }
    
    /**
     * Invierte el orden de la torre
     */
    public void reverseTower()
    {
        Stack<Cup> temp = new Stack<Cup>();
        while (!cups.isEmpty()) {
            temp.push(cups.pop());
        }
        cups = temp;
        isOK = true;
    }
    
    /**
     * Retorna la altura total de elementos apilados
     */
    public int getHeight()
    {
        int totalHeight = 0;
        for (Cup c : cups) {
            totalHeight += c.getHeight();
        }        
        return totalHeight;
    }
    
    /**
     * Retorna array con números de tazas tapadas
     */
    public int[] lidedCups()
    {
        ArrayList<Integer> covered = new ArrayList<Integer>();
        
        for (Cup c : cups) {
            if (c.isCovered()) {
                covered.add(c.getNumber());
            }
        }
        int[] result = new int[covered.size()];
        for (int i = 0; i < covered.size(); i++) {
            result[i] = covered.get(i);
        }
        return result;
    }
    
    /**
     * Retorna matriz con tipo y número de elementos
     */
    public String[][] stackingItems()
    {
        int totalElements = cups.size() + lids.size();
        String[][] result = new String[totalElements][2];
        
        // Convertir stack a lista para iterar
        ArrayList<Cup> cupList = new ArrayList<Cup>(cups);
        ArrayList<Lid> lidList = new ArrayList<Lid>(lids);
        
        int index = 0;
        
        // Agregar tazas
        for (Cup c : cupList) {
            result[index][0] = "cup";
            result[index][1] = String.valueOf(c.getNumber());
            index++;
        }
        
        // Agregar tapas
        for (Lid l : lidList) {
            result[index][0] = "lid";
            result[index][1] = String.valueOf(l.getHeight());
            index++;
        }
        
        return result;
    }
    
    /**
     * Hace visible la torre
     */
    public void makeVisible()
    {
        isVisible = true;
    }
    
    /**
     * Hace invisible la torre
     */
    public void makeInvisible()
    {
        isVisible = false;
    }
    
    /**
     * Verifica si la torre es visible
     */
    public boolean isVisible()
    {
        return isVisible;
    }
    
    /**
     * Termina el simulador
     */
    public void exit()
    {
        cups.clear();
        lids.clear();
        isVisible = false;
    }
    
    /**
     * Verifica si la última operación fue exitosa
     */
    public boolean isOk()
    {
        return isOK;
    }
    
    /**
     * Retorna el tamaño del stack de tazas
     */
    public int getCupsSize()
    {
        return cups.size();
    }
    
    /**
     * Retorna el tamaño del stack de tapas
     */
    public int getLidsSize()
    {
        return lids.size();
    }
}