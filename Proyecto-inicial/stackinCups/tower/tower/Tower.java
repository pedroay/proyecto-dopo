package tower;
import java.util.Stack;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/**
 * The Tower class represents the core simulation environment where Elements 
 * (such as Cups and Lids) are stacked according to specific physical rules.
 * It manages the structural integrity, positioning, and validation of 
 * all elements contained within the tower.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tower {
    /** The overall width of the tower canvas. */
    private int width;
    
    /** The current calculated height of the complete tower. */
    private int height;
    
    /** The maximum allowed height or vertical capacity of the tower. */
    private int maxHeight;
    
    /** Indicates whether the tower visually exists or is displayed on the canvas. */
    private boolean isVisible;
    
    /** Collection tracking all Cup elements currently in the tower. */
    private final Stack<Cup> cups;
    
    /** Collection tracking all Lid elements currently in the tower. */
    private final Stack<Lid> lids;
    
    /** Comprehensive collection of all elements in the tower, ordered by insertion. */
    private final Stack<Elements> objects;
    
    /** State flag indicating if the tower's last operation completed 
     * successfully without errors. */
    private boolean isOK;
    
    /** Reference to the topmost Element currently placed in the tower. */
    private Elements top;
    /**
     * Constructs a new Tower with the specified width and maximum height.
     * The tower is initialized as not visible, with empty stacks of cups
     * and lids. The initial state of the tower is considered valid (isOK = true).
     * @param nwidth     The width of the tower
     * @param nmaxHeight The maximum allowed height of the tower
     */
    public Tower(final int nwidth, final int nmaxHeight){
        width = nwidth;
        maxHeight = nmaxHeight;
        isVisible = false;
        cups = new Stack<>();
        lids = new Stack<>();
        objects = new Stack<>();
        isOK = true;
    }
    
    /**
     * constructs a new tower with the specified
     *  number of cups 
     * which thie number will increase
     * in odd numbers "1,3,5,.."
     * 
     * @param number number of cups tha will have the new tower
     */
    public Tower(final int number) {
        cups = new Stack<>();
        lids = new Stack<>();
        objects = new Stack<>();
        isVisible = false;
        for(int j= 1; j <=number; j ++){
            pushCup(j);
        }
        isOK = true;
    }
    
    /**
     * Adds a cup to the top of the element tower.
     * This method validates that the provided ID is positive and unique. 
     * If the tower is empty, the cup becomes the new top.
     *  If the tower contains elements, 
     * the method determines whether to place the cup inside
     *  the current top (if the top 
     * is a larger cup) or above it (if the new cup is larger or the top is a lid).
     * @param i The identification number (and size) of the cup to be added. 
     * Must be a unique positive integer.
     * @return This method does not return a value (void), but it updates the 
     * {@code isOK} flag to true upon successful execution, 
     * or false if an error occurs.
     */
    public final void pushCup(final int number){
        if(cupInTower(number)){return;   }
        final Cup newCup;
        if(isInLids(number)){
            newCup = findCupByNumberOfLid(number);
        }else{
            newCup = new Cup(number,this);
        }
        if(top == null){
            top = newCup;
            cups.push(newCup);
            objects.push(newCup);
            isOK = true;
        }
        else{
            final int sizeTop = top.getWidth();
            final int sizeNewCup = newCup.getWidth();
            if (sizeTop > sizeNewCup && top.isCanIn()){
                setInside((Cup)top,newCup);
                cups.push(newCup);
                objects.push(newCup);
                setNewTop(newCup);
            }
            else if(sizeTop < sizeNewCup || !top.isCanIn()){
                setAbove(top,newCup);
                cups.push(newCup);
                setNewTop(newCup);
                objects.push(newCup);
            }
            isOK = true;
        }
        }
    
    /**
     * Removes the cup located at the top of the tower.
     *
     * This method checks whether the last element stored in the stack of objects
     * is a cup. If it is not a cup, the operation fails and the tower state is
     * marked as invalid. If the tower is visible, an error message is displayed.
     *
     * If the top element is a cup, the method removes it from the internal stacks
     * that store the tower elements and cups. The removed cup is also made invisible.
     *
     * After removal, the method updates all references in the remaining elements:
     * - If any element had the removed cup as its inner element, the reference
     *  is cleared.
     * - If any element had the removed cup above it, that reference is also cleared.
     *
     * Finally, the method recalculates the top element of the tower based on the
     * remaining elements.
     *
     * If the operation completes successfully, the tower structure is updated
     * accordingly. Otherwise, the state flag {@code isOK} is set to {@code false}.
     */
    public void popCup(){
        if(objects.isEmpty() || !"cup".equals(objects.get(objects.size()-1).getType())){
            if(isVisible)showError();
            isOK = false;
        }
        else{
            Elements popeado = objects.get(objects.size()-1);
            if (!popeado.thisIsQuitable()) {
                if(isVisible)showError();
                isOK = false;
                return;
            }
            objects.remove(popeado);
            cups.remove(popeado);
            top = null;
            popeado.makeInvisible();
            for(Elements o:objects){
                if(o.getInside() != null && o.getInside().getNumber() == popeado.getNumber())o.setInside(null);
                if(o.getAbove() != null && o.getAbove().getNumber() == popeado.getNumber())o.setAbove(null);
                setNewTop(o);
            }
        }
    }
    

       
    
    
    /**
     * Removes a specific cup from the tower based on its identifier.
     *
     * The method searches for a cup with the given number and removes it from the
     * tower structure. If the tower has no elements, the operation fails and the
     * state flag {@code isOK} is set to {@code false}. If the tower is visible,
     * an error message is displayed.
     *
     * Only the elements above the removed cup are reconstructed, since the elements
     * below it remain unchanged in position and structure.
     *
     * @param i the identifier (number) of the cup to be removed from the tower
     */
    public void removeCup(int number){
    if(objects.isEmpty() || !isInElements(number,"cup" )|| !findCupByNumber(number).thisIsQuitable()){
        if(isVisible)showError();
        isOK = false;
    }
    else{
        Elements a = findCupByNumber(number);
        makeInvisible();
        objects.remove(a);
        cups.remove(a);
        top = null;
        Stack<Elements> temp = new Stack<>();
        temp.addAll(objects);
        objects.clear();
        cups.clear();
        lids.clear();
        for(final Elements o:temp){
            o.setInside(null);
            o.setAbove(null);
            push(o);
        }
    }
    }
    
    private void push(final Elements element){
        element.push(element.getNumber());
    }

    
    /**
     * Adds a lid to the top of the tower following the stacking rules.
     *
     * The method first verifies whether a lid with the same identifier already
     * exists in the tower or if the provided number is invalid (less than or
     * equal to zero). If so, the operation fails and the tower state is marked
     * as invalid. If the tower is visible, an error message is displayed.
     *
     * If the identifier corresponds to an existing cup, the lid associated
     * with that cup is retrieved. Otherwise, a new lid is created.
     *
     * If the tower is empty, the new lid becomes the top element and is added
     * to the internal stacks of lids and objects.
     *
     * If the tower already contains elements, the method compares the width
     * of the current top element with the width of the new lid in order to
     * determine how it should be placed:
     * If the top element is wider and it is a cup, the lid is placed inside it.
     * If the new lid is wider or equal in width,
     *  it is placed above the current top element.
     * If the top element is a lid and wider than
     *  the new lid, the new lid is stacked above it.         * 
     *
     * When a lid is placed above its corresponding cup (same identifier),
     * the cup is marked as covered by that lid.
     *
     * Finally, the method updates the reference to the top element of the tower
     * if necessary and marks the operation as successful.
     *
     * @param i the identifier (number) of the lid to be added to the tower
     */
    public void pushLid(final int number) {
        if (isInElements(number, "lid") || number <= 0) {
            isOK = false;
            if (isVisible) {
                showError();
            }
            return;
        }
    
        final Lid newLid;        
        if (isInCups(number)) {
            newLid = findLidByNumberOfCup(number);
        } else {
            newLid = new Lid(number,this);
        }
        if (top == null) {
            top = newLid;
            lids.push(newLid);
            objects.push(newLid);
            isOK = true;
            return;
        }
        int sizeTop = top.getWidth();
        int sizeNewLid = newLid.getWidth();
        if (sizeTop > sizeNewLid && top.isCanIn()) {
            setInside((Cup) top, newLid);
            lids.push(newLid);
            objects.push(newLid);
        } else if (sizeTop <= sizeNewLid ) {
            setAbove(top, newLid);
            lids.push(newLid);
            objects.push(newLid);
            if(top.getType().equals("cup") && top.getNumber() == newLid.getNumber())top.setCover(newLid);
            setNewTop(newLid);
        } else if (sizeTop > sizeNewLid && !top.isCanIn()) {
            setAbove(top, newLid);
            lids.push(newLid);
            objects.push(newLid);
            setNewTop(newLid);
        }
        isOK = true;
    }
      
    /**
     * Removes the lid located at the top of the tower.
     *
     * The method verifies whether the last element stored in the stack of objects
     * is a lid. If the top element is not a lid, the operation cannot be performed.
     * In that case, the tower state is marked as invalid ({@code isOK = false}) and,
     * if the tower is visible, an error message is displayed.
     *
     * If the top element is a lid, it is removed from the internal stacks that store
     * lids and general elements. The removed lid is also made invisible.
     *
     * After removing the lid, the method iterates through the remaining elements of
     * the tower and clears any references that pointed to the removed lid, either as
     * an element placed inside another or as an element placed above another.
     *
     * Finally, the method recalculates the top element of the tower based on the
     * vertical positions of the remaining elements.
     *
     * If the operation completes successfully, the internal structure of the tower
     * is updated accordingly.
     */
    public void popLid(){
        if(objects.isEmpty() || !"lid".equals(objects.get(objects.size()-1).getType()) ){
            if(isVisible)showError();
            isOK = false;
        }
        else{
            Elements popeado = objects.get(objects.size()-1);
            if (!popeado.thisIsQuitable()) {
                if(isVisible)showError();
                isOK = false;
                return;
            }
            objects.remove(popeado);
            lids.remove(popeado);
            top = null;
            popeado.makeInvisible();
            for(Elements o:objects){
                if(o.getInside() != null && o.getInside().getNumber() == popeado.getNumber())o.setInside(null);
                if(o.getAbove() != null && o.getAbove().getNumber() == popeado.getNumber())o.setAbove(null);
                setNewTop(o);
            }
        }
    }
    
    
    /**
     * Removes a specific lid from the tower based on its identifier.
     *
     * The method searches for a lid with the given number and removes it from the
     * tower structure. If the tower has no elements, the operation fails and the
     * state flag {@code isOK} is set to {@code false}. If the tower is visible,
     * an error message is displayed.
     *
     * Only the elements above the removed lid are reconstructed, since the elements
     * below it remain unchanged in position and structure.
     *
     * @param i the identifier (number) of the lid to be removed from the tower
     */
     public void removeLid(int i){
    if(objects.isEmpty() || !isInElements(i, "lid") || !findLidByNumber(i).thisIsQuitable() ){
        if(isVisible)showError();
        isOK = false;
    }
     else{
        Elements a = findLidByNumber(i);
        makeInvisible();
        objects.remove(a);
        lids.remove(a);
        top = null;
        Stack<Elements> temp = new Stack<>();
        temp.addAll(objects);
        objects.clear();
        cups.clear();
        lids.clear();
        for(Elements o:temp){
            o.setInside(null);
            o.setAbove(null);
            push(o);
        }
    }
    }
    
    
    
    /**
     * Reorders the tower by arranging its cups and lids according to their identifiers.
     *
     * This method reorganizes the structure of the tower so that cups and lids are
     * stacked following a consistent order based on their numeric identifiers.
     *
     * First, all cups currently in the tower are copied into a temporary list and
     * sorted in descending order of their numbers using a simple bubble sort.
     * The same sorting process is applied to the lids.
     *
     * After sorting, the internal stacks of cups, lids, and objects are cleared,
     * and the reference to the current top element is reset.
     *
     * The tower is then rebuilt from scratch:
     * 
     * Each cup is reinserted using {@code pushCup()} following the sorted order.
    * Each lid is reinserted using {@code pushLid()} so that they are correctly
     * positioned relative to their corresponding cups.
     * 
     *
     * During reconstruction, all previous structural relationships between elements
     * (inside and above references) are cleared to ensure that the tower is rebuilt
     * according to the stacking rules.
     *
     * Finally, the tower is made visible again and the operation is marked as
     * successful by setting {@code isOK = true}.
     */
        public void orderTower(){
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
        ArrayList<Lid> tempo = new ArrayList<Lid>(lids);
        for (int i = 0; i < tempo.size(); i++) {
            for (int j = 0; j < tempo.size() - 1; j++) {
                if (tempo.get(j).getNumber() < tempo.get(j + 1).getNumber()) {
                    Lid aux = tempo.get(j);
                    tempo.set(j, tempo.get(j + 1));
                    tempo.set(j + 1, aux);
                }
            }
        }
        cups.clear();
        lids.clear();
        objects.clear();
        top = null;
        for (Elements c : temp) {
            c.setInside(null);
            c.setAbove(null);
            int cI = c.getNumber();
            pushCup(cI);
        }
        for (int i = tempo.size()-1;i >= 0; i--) {
            Elements c = tempo.get(i);
            c.setInside(null);
            c.setAbove(null);
            int cI = c.getNumber();
            pushLid(cI);
        }
        makeVisible();
        isOK = true;
        }
       
    /**
     * Reverses the order of the elements in the tower.
     *
     * This method reconstructs the tower by reversing the order in which its
     * elements were originally stacked. All current elements of the tower are
     * first copied into a temporary stack. Then, the internal stacks that store
     * the tower elements (objects, cups, and lids) are cleared and the reference
     * to the top element is reset.
     *
     * The elements are then reinserted into the tower in reverse order, starting
     * from the last element of the temporary stack. Before reinserting each
     * element, its structural references (inside and above) are cleared to avoid
     * inconsistencies in the tower structure.
     *
     * Cups are reinserted using {@code pushCup()}, while lids are reinserted
     * using {@code pushLid()}, ensuring that the stacking rules of the tower
     * are respected during reconstruction.
     *
     * As a result, the tower maintains a valid structure but with the order of
     * its elements reversed.
     */    
    public void reverseTower(){
        Stack<Elements> temp = new Stack();
        temp.addAll(objects);
        objects.clear();
        cups.clear();
        lids.clear();
        top = null;
        for(int i = temp.size() -1 ; i >= 0;i--){
            Elements a = temp.get(i);
            a.setInside(null);
            a.setAbove(null);
            push(a);
        }
    }
    
    
    /**
     * return the total height of the tower whci is calculated in the next way
     * we have the postion of the top and we have the max siz eof the canvas which is 300 we rest
     * to 300 the posy
     */
    public int height(){
        int totalHeight;
            if(top != null){
                totalHeight= 300- top.getPosy();
                isOK = true;
                return totalHeight;
            
        }
        isOK = true;
        return totalHeight = 0;
    }
    
    /**
     * Returns the identifiers of the cups that are currently covered by lids.
     *
     * The method iterates through all cups stored in the tower and checks
     * their state. If a cup is marked as "Covered", its identifier (number)
     * is stored in an array.
     *
     * The resulting array contains the numbers of the cups that have a lid
     * placed on them. Cups that are not covered are ignored.
     *
     * @return an array containing the identifiers of the cups that are covered
     *         by lids in the tower
     */
    public int[] lindedCups() {
        int[] copas = new int[cups.size()];
        int index = 0;
        
        for (Cup c : cups) {
            if ("Covered".equals(c.getState())) {
                copas[index] = c.getNumber();
                index++;
            }
        }
        return copas; 
    }
    
    /**
     * Returns a two-dimensional array representing the stored elements,
     * where each row contains the type and quantity of an object.
     *
     * The method iterates through all elements in the collection and
     * extracts their type and number. The type is stored as a String,
     * and the number is converted to a String before being stored.
     *
     * The resulting matrix has as many rows as elements in the collection
     * and exactly two columns:
     * - Column 0: the type of the element
     * - Column 1: the quantity of the element as a String
     *
     * @return a matrix containing the type and quantity of each element
     */
        
    public String[][] stakingItems(){
        String[][] staking = new String[objects.size()][2];
        int index = 0;
        for(Elements o:objects){
            staking[index][0] = o.getType();
            staking[index][1] = Integer.toString(o.getNumber());
            index ++;
        }
        return staking;
    }
    
    /**
     * Swaps the positions of two elements within the structure.
     *
     * The method receives two arrays representing the elements to be swapped
     * and attempts to locate them in the collection. If either element is not found,
     * or both references correspond to the same element, the operation is aborted
     * and the state flag is set to false.
     *
     * To perform the swap, the method temporarily stores all elements in a stack,
     * clears the current structure, and then rebuilds it. During reconstruction,
     * the positions of the two target elements are exchanged while preserving
     * the relative order of the remaining elements.
     *
     * Additionally, all relational references (such as "above" and "inside")
     * are reset before reinserting the elements.
     *
     * @param objeto1 array representing the first element to be swapped
     * @param objeto2 array representing the second element to be swapped
     */
     public void swap(String[] objeto1, String[] objeto2) {
        Elements o1 = buscarElemento(objeto1);
        Elements o2 = buscarElemento(objeto2);
        if(o1 == null || o2 == null || o1 == o2){
            isOK = false;
            return;
        }
        Stack<Elements> temp = new Stack<>();
        temp.addAll(objects);
        objects.clear();
        cups.clear();
        lids.clear();
        top = null;
        for(Elements t:temp){
            t.setAbove(null);
            t.setInside(null);
            int tNumber = t.getNumber();
            if(tNumber != o1.getNumber() && tNumber != o2.getNumber() )push(t);
            else if(tNumber == o1.getNumber() && t != o2){
                push(o2);
            }
            else if(tNumber == o2.getNumber() && t != o1){
                push(o1);
            }
            isOK= true;
        }
    }
    
    /**
     * Reorganizes the structure by placing lids on top of their corresponding cups.
     *
     * The method resets the current structure and clears all relationships between
     * elements (such as "inside" and "above"). It then temporarily stores all elements,
     * cups, and lids in auxiliary stacks to rebuild the structure from scratch.
     *
     * During reconstruction, each cup is reinserted and, if a lid with the same
     * identifier exists, that lid is placed on top of the cup. Lids that do not
     * correspond to any cup are added afterward.
     *
     * This ensures that matching cups and lids are properly paired, while maintaining
     * the rest of the elements in a consistent state.
     */ 
    public void cover() {
        top = null; 
        Stack<Elements> tem = new Stack<>(); 
        Stack<Cup> temp = new Stack<>();
        Stack<Lid> tempo = new Stack<>();
        for (Elements e : objects) {
            e.setInside(null);
            e.setAbove(null);
        }
        tem.addAll(objects);
        temp.addAll(cups);
        tempo.addAll(lids);
        objects.clear();
        cups.clear();
        for (Cup c : temp) {
            int i = c.getNumber();
            pushCup(i);
            Lid f = findLidByNumber(i);
            if (f != null) {
                pushLid(f.getNumber());
                lids.remove(f); 
            }
        }
        for (Lid l : tempo) {
            int j = l.getNumber();
            pushLid(j);
        }
    }
    
    public String[][] swapToReduce() {       
        for (int i = 0; i < objects.size(); i++) {
            Elements e = objects.get(i);        
            for (int j = i + 1; j < objects.size(); j++) {
                Elements k = objects.get(j);        
                if (e.getHeight() < k.getHeight() ) {
                    return new String[][]{
                        {e.getType(), String.valueOf(e.getNumber())},
                        {k.getType(), String.valueOf(k.getNumber())}
                    };
                }
            }
        }
    
        return new String[0][0]; 
    }
    
    /**
    /**
     * Places a cup inside another cup following stacking rules.
     *
     * If the base cup does not already contain a cup inside,
     * the given cup is positioned inside it and linked as its inner cup.
     *
     * If the base already contains a cup, the method checks:
     * - If the inner cup is taller than the cup to stack and is not covered,
     *   the method recursively attempts to place the cup inside that inner cup.
     * - Otherwise, the cup is placed above the inner cup.
     *
     * The method updates the position of the stacked cup accordingly
     * and sets the tower state to valid (isOK = true).
     *
     * @param base    The cup that will contain another cup
     * @param apilar  The cup to be placed inside the base cup
     */
    private void setInside(Cup base, Elements apilar) {
        Elements insideBase = base.getInside();
        int posxApilar = apilar.getPosx(); 
        int posyBase = base.getPosy();
        int baseHeight = base.getHeight();
        int apilarHeight = apilar.getHeight();
      
        if (insideBase == null) {
            apilar.setPosition(posxApilar, posyBase + baseHeight - 10 - apilarHeight);
            base.setInside(apilar);
        } else {
            int insideBaseWidth = insideBase.getWidth();
            int apilarWidth = apilar.getWidth();
            if (insideBaseWidth > apilarWidth && insideBase.getType().equals("cup")) {
                setInside((Cup) insideBase, apilar);
            } else {
                setAbove(insideBase, apilar);
            }
        }
        isOK = true;
    }
    
    
    
    private void setAbove(Elements base, Elements apilar) {
        Elements baseAbove = base.getAbove();
        int posxApilar = apilar.getPosx();
        int posyBase = base.getPosy();
        int apilarHeight = apilar.getHeight();
        
        if (baseAbove == null) {
            base.setAbove(apilar);
            if(apilar.getType().equals("cup") || apilar.getNumber()> base.getNumber())apilar.setPosition(posxApilar, posyBase - apilarHeight);
            else if(apilar.getType().equals("lid") && apilar.getNumber() == base.getNumber()){
            apilar.setPosition(posxApilar, posyBase);
            base.setCover((Lid)apilar);
            }
            else if(apilar.getType().equals("lid") && apilar.getNumber() < base.getNumber())apilar.setPosition(posxApilar, posyBase-10);
        } else {
            int baseAboveHeight = baseAbove.getHeight();
            if (baseAboveHeight > apilarHeight && baseAbove instanceof Cup) {
                setInside((Cup) baseAbove, apilar);
            } else {
                setAbove(baseAbove, apilar);
            }
        }
        isOK = true;
    }
    
    /**
     * Evaluates whether the current top cup must be updated.
     *
     * Compares the vertical position (y-coordinate) of the current top cup
     * with the new cup. If the new cup is positioned higher (i.e., has a
     * smaller y-coordinate value), it becomes the new top cup.
     *
     * @param newCup  The cup to evaluate as a potential new top
     */
    private void setNewTop(Elements newTop){
        if(top == null){
            top = newTop;
            return;
        }
        int posyTop = top.getPosy();
        int posyNewTop = newTop.getPosy();
        if (posyTop >= posyNewTop){
            top = newTop;
        }
    }
    
    /**
     * Checks if an element with the given identifier and type exists in the tower.
     * 
     * Iterates through the collection of all elements currently in the tower
     * and evaluates each element's number and type against the provided parameters.
     * 
     * @param i    The identifier (number) of the element to search for.
     * @param tipo A string representing the type of the element (e.g., "cup" or "lid").
     * @return     {@code true} if an element matching both the identifier and type is found; 
     *             {@code false} otherwise.
     */
    public final boolean isInElements(int i,String tipo){
        for(Elements e : objects){
            if(e.getNumber() == i && e.getType().equals(tipo)){
                return true;
            }
        }
        return false;
    }  
    
    /**
     * Checks if a cup with the given identifier exists in the tower.
     * 
     * Iterates through the stack of cups to determine if any cup matches
     * the provided identifier number.
     * 
     * @param i The identifier (number) of the cup to search for.
     * @return  {@code true} if a matching cup is found; {@code false} otherwise.
     */
    private boolean isInCups(int i){
        for(Cup c : cups){
            if(c.getNumber() == i){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if a lid with the given identifier exists in the tower.
     * 
     * Iterates through the stack of lids to determine if any lid matches
     * the provided identifier number.
     * 
     * @param i The identifier (number) of the lid to search for.
     * @return  {@code true} if a matching lid is found; {@code false} otherwise.
     */
    private boolean isInLids(int i){
        for(Lid l : lids){
            if(l.getNumber() == i){
                return true;
            }
        }
        return false;
    } 
    
    /**
     * Retrieves the cup associated with a specific lid identifier.
     * 
     * Searches through the lids currently in the tower for one matching the 
     * given identifier, and returns its linked cup object.
     * 
     * @param i The identifier number of the lid whose associated cup is sought.
     * @return  The {@code Cup} associated with the specified lid, or {@code null}
     *          if no such lid is found or if it does not have an associated cup.
     */
    public final Cup findCupByNumberOfLid(int i) {
        for (Lid l : lids) {
            if (l.getNumber() == i) {
                return l.getHisCup();
            }
        }
        return null;
    }
    
    /**
     * Retrieves the lid associated with a specific cup identifier.
     * 
     * Searches through the cups currently in the tower for one matching the 
     * given identifier, and returns its linked lid object.
     * 
     * @param i The identifier number of the cup whose associated lid is sought.
     * @return  The {@code Lid} associated with the specified cup, or {@code null}
     *          if no such cup is found or if it does not have an associated lid.
     */
    public Lid findLidByNumberOfCup(int i) {
        for (Cup c : cups) {
            if (c.getNumber() == i) {
                return c.getHisLid();
            }
        }
        return null;
    }
    
    /**
     * Finds and returns a specific cup by its identifier number.
     * 
     * Iterates through the stack of cups and returns the first cup
     * matching the given number.
     * 
     * @param i The identifier number of the cup to locate.
     * @return  The matching {@code Cup} object, or {@code null} if not found.
     */
    public Cup findCupByNumber(int i){
        Cup ca = null;
        for (Cup c : cups) {
            if (c.getNumber() == i) {
                ca = c;
                break;
            }
        }
        return ca;
    }
    
    /**
     * Finds and returns a specific lid by its identifier number.
     * 
     * Iterates through the stack of lids and returns the first lid
     * matching the given number.
     * 
     * @param i The identifier number of the lid to locate.
     * @return  The matching {@code Lid} object, or {@code null} if not found.
     */
    public Lid findLidByNumber(int i){
        Lid la = null;
        for (Lid l : lids) {
            if (l.getNumber() == i) {
                la = l;
                break;
            }
        }
        return la;
    }
    
    /**
     * Displays an error message indicating that the requested action
     * could not be completed and marks the operation as unsuccessful
     * by setting the status flag to false.
     */
    private void showError(){
        JOptionPane.showMessageDialog(null, "No se pudo hacer la acción");
        isOK = false;
    }
    
    public void makeVisible()
    {   for(Elements l:objects){
            int lPosy = l.getPosy();
            if (lPosy >= 0){
                l.makeVisible(); 
            }
        }
        isVisible = true;
        }
    
    public Elements getTop(){
        return top;
    }
    
    public boolean isOK(){
        return isOK;
    }
    
    public Stack<Elements> getObjects(){
        return objects;
    }
    
    public void makeInvisible()
    {
        for(Elements c:objects){
            c.makeInvisible();
        }
        isVisible = false;
    }
    
    /**
     * Searches for and returns a specific element from the tower based on text information.
     * The array must have at least two positions: the first for the element's type
     * (e.g., "cup" or "lid"), and the second for its identifier number.
     * 
     * If the provided array is null or contains fewer than two elements, it invokes
     * the error function. Returns the corresponding {@code Elements} object if found,
     * or {@code null} if the provided type is invalid or the element does not exist.
     *
     * @param objeto A String array where objeto[0] contains the type ("cup", "lid")
     *               and objeto[1] contains the String representation of its identifier number.
     * @return The {@code Elements} object matching the search, or {@code null} if not found.
     */
    private Elements buscarElemento(String[] objeto) {
        if (objeto == null || objeto.length < 2) {
            showError();
        }
    
        String tipo = objeto[0];
        int numero = Integer.parseInt(objeto[1]);
    
        if (tipo.equals("cup")) {
            return findCupByNumber(numero);
        } else if (tipo.equals("lid")) {
            return findLidByNumber(numero);
        } else {
            return null; 
        }
    }
    
    public int findIndexOfACupNumberInStackin(int number){
        int index = -10;
        String numberS = Integer.toString(number);
        String [][] s = stakingItems();
        for(int i = s.length;i >= 0; i--){
           if(s[i][0].equals("cup") && s[i][1].equals(numberS)){
               index = i;
           }
        }
        return index;
    }
    
    /**
     * dice si una copa esta o no en la torres
     */
    private final boolean cupInTower(int i){
        boolean is = false;
        if(isInElements(i,"cup") ||i <=0){
            isOK = false;
            if(isVisible){
                showError();
                }
            is = true;
        }
        return is;
    }

    private void removeLidsBelowCup(Cup cup) {
        if (cup == null || !cup.getEliminaTapas()) return;
        int cupIndex = objects.indexOf(cup);
        if (cupIndex <= 0) return;
        Elements below = objects.get(cupIndex - 1);
        if (!below.getType().equals("lid")) return;
        swap(new String[]{cup.getType(),String.valueOf(cup.getNumber())},new String[]{below.getType(), String.valueOf(below.getNumber())});
        removeLid(below.getNumber());
        removeLidsBelowCup(cup);
    }
    
    /**
     * Displaces elements after an element with the desplaza-flag is inserted.
     * Iterates through all objects; for each element that is not the
     * inserted element, asks whether it can displace the other. If it can,
     * the loop stops. Otherwise, the two elements are swapped.
     *
     * @param elementE the element that must displace others
     */
    private void desplaceElements(Elements elementE) {
        if (elementE == null || !elementE.canDesplace()) return;
        for (Elements e : new java.util.ArrayList<>(objects)) {
            if (e.equals(elementE)) continue;
            boolean cDesplace = elementE.canDesplace(e);
            if (!cDesplace) {
                checkQuitablePosition(e);
                break;
            } else {
                swap(
                    new String[]{e.getType(),       String.valueOf(e.getNumber())},
                    new String[]{elementE.getType(), String.valueOf(elementE.getNumber())}
                );
                System.out.println("Analizando elemento: " + e.getNumber() + " tipo: " + e.getType());
            }
        }
    }
    
    private void checkQuitablePosition(Elements element) {
        int i = objects.indexOf(element);
        if(element.isNotQuitablePosition(i)){
            element.setQuitable(false);
        }
        else element.setQuitable(true);
    }

            
    private Cup createElement(String type, int number) {
        int height = height();
        double nheight = (height +10) / 20.0;
        int heightBox = (int) Math.ceil(nheight);
        switch (type) {
            case "opener": return new Opener(number, this);
            case "cup": return new Cup(number, this);
            case "hierarchical": return new Hierarchical(number, this);
            case "box": return new Box(heightBox, this);
            default:       return new Cup(number, this);
        }
    }
    
    private void linkExistingLid(Cup cup) {
        Lid existing = findLidByNumber(cup.getNumber());
        if (existing != null) {
            existing.setHisCup(cup);
        }
    }
    
    /**
     * Adds a cup of the specified type to the tower.
     * Delegates positioning to {@code pushCupLogica} and then
     * triggers post-insertion behaviour via {@code afterPush}.
     *
     * @param type   the type of cup (e.g. "cup", "opener", "hierarchical")
     * @param number the identifier of the cup
     */
    public void pushCup(String type, int number) {
        if (cupInTower(number)) return;
        Cup newCup = createElement(type, number);
        linkExistingLid(newCup);
        pushCupLogica(newCup);
        afterPush(newCup);
        isOK = true;
    }

    /**
     * Handles the physical insertion of a cup into the tower structure,
     * calculating whether it goes inside or above the current top.
     *
     * @param newCup the cup to insert
     */
    private void pushCupLogica(Cup newCup) {
        if (top == null) {
            top = newCup;
            cups.push(newCup);
            objects.push(newCup);
            return;
        }
        int sizeTop = top.getWidth();
        int sizeNewCup = newCup.getWidth();
        if (sizeTop > sizeNewCup && top.isCanIn()) {
            setInside((Cup) top, newCup);
            cups.push(newCup);
            objects.push(newCup);
            setNewTop(newCup);
        } else if (sizeTop < sizeNewCup || !top.isCanIn()) {
            setAbove(top, newCup);
            cups.push(newCup);
            setNewTop(newCup);
            objects.push(newCup);
        }
    }

    /**
     * Called after a cup has been inserted into the tower.
     * Decides the post-insertion behaviour based on the element's capabilities:
     * - If the element is dangerous, triggers {@code dangerousElement}.
     * - Else if the element can displace others, triggers {@code desplaceElements}.
     *
     * @param element the element that was just inserted
     */
    private void afterPush(Elements element) {
        if (element.isDangerous()) {
            dangerousElement(element);
        } else if (element.canDesplace()) {
            desplaceElements(element);
        } else if (element.thisIsCrazy()) {
            crazyElement(element);
        }
        else if(element.thisIsFearful()){
            validateFearful((Lid) element);
        }
        else if(element.getIsBox()){
            validateBox((Box) element);
        }
    }

    private void validateBox(Box box){
        if (box == null || !box.getIsBox()) return;
        
        objects.remove(box);
        if ("cup".equals(box.getType())) {
            cups.remove(box);
        }
        objects.insertElementAt(box, 0);
        if ("cup".equals(box.getType())) {
            cups.insertElementAt((Cup) box, 0);
        }
        
        if (objects.size() > 1) {
            Elements previousBase = objects.get(1);
            if (box.getWidth() >= previousBase.getWidth()) {
                previousBase.setInside(box);
                previousBase.setAbove(null);
            } else {
                previousBase.setAbove(box);
                previousBase.setInside(null);
            }
            box.setInside(null);
            box.setAbove(null);
        }
        
        box.createHisLid();
        
        for(Elements e : objects){
            if(e != box){
                e.setIsInABox(true);
                e.setQuitable(false);
            }
        }
        
        box.setIsBox(true); 
        setNewTop(objects.lastElement());
    }

    /**
     * crazyElement procedure: removes the added element, reverses the tower,
     * pushes the element back onto the reversed tower (the pure beginning),
     * and reverses the tower back to its original state.
     */
    private void crazyElement(Elements element) {
        if (!element.thisIsCrazy()) return;
        
        objects.remove(element);
        if (element.getType().equals("cup")) {
            cups.remove(element);
        } else {
            lids.remove(element);
        }
        
        top = null;
        for (Elements e : objects) {
            setNewTop(e);
        }
        reverseTower();       
        element.setInside(null);
        element.setAbove(null);
        push(element);     
        reverseTower();
        
        Elements newlyAdded;
        if (element.getType().equals("cup")) {
            newlyAdded = findCupByNumber(element.getNumber());
        } else {
            newlyAdded = findLidByNumber(element.getNumber());
        }
        if (newlyAdded != null) {
            newlyAdded.setIsCrazy(true);
        }
    }

    /**
     * Handles the behaviour of a dangerous element after insertion.
     * Iterates through all objects in the tower; for each element that is
     * not the inserted element itself, checks whether it can damage the other.
     * If it can damage the element, the loop stops. Otherwise, the two
     * elements are swapped and the damaged element (if it is a lid) is popped.
     *
     * @param elementE the dangerous element that was just inserted
     */
    private void dangerousElement(Elements elementE) {
        ArrayList<Elements> copy = new java.util.ArrayList<>(objects);
        for (int i = copy.size() - 1; i >= 0; i--) {
            Elements e = copy.get(i);
            System.out.println("Analizando elemento: " + e.getNumber() + " tipo: " + e.getType());
            if (e.equals(elementE)) continue;
            boolean cDamage = elementE.canDamage(e);
            if (!cDamage) {
                break;
            } else {
                System.out.println("¡Dañando elemento!: " + e.getNumber()+ e.getType());
                swap(
                    new String[]{e.getType(),       String.valueOf(e.getNumber())},
                    new String[]{elementE.getType(), String.valueOf(elementE.getNumber())}
                );
                if (e.getType().equals("lid")) {
                    popLid();
                }
            }
        }
    }
    
     /**
     * Factory method to create different types of lid elements.
     * 
     * Based on the specified type, this method instantiates and returns the 
     * corresponding subclass of Lid. If the type is not recognized 
     * or is specified as "lid", a standard Lid is created.
     * 
     * @param type   A string indicating the specific type of lid to create 
     *               (e.g., "fearful", "crazy", or "lid").
     * @param number The identifier number that defines the size and pairing 
     *               of the lid element.
     * @return       A newly instantiated Lid object or one of its subclasses.
     */
    private Lid createLidElement(String type, int number) {
        switch (type) {
            case "fearful": return new Fearful(number, this);
            case "crazy":   return new Crazy(number, this);
            case "lid":     
            default:        return new Lid(number, this);
        }
    }
    
    /**
     * Links a newly created lid to its corresponding cup, if one already exists in the tower.
     * 
     * @param lid The lid element that needs to be associated with an existing cup.
     */
    private void linkExistingCup(Lid lid) {
        Cup existing = findCupByNumber(lid.getNumber());
        if (existing != null) {
            lid.setHisCup(existing);
        }
    }
    
    /**
     * Validates whether a fearful lid can be placed, and enforces its rules upon insertion.
     * 
     * A fearful lid requires its matching cup to already exist in the tower.
     * If the cup does not exist, the lid is removed, and the operation fails.
     * If the cup exists and is already covered, the lid becomes unable to be removed.
     * 
     * @param lid The newly added lid to validate, which should have fearful properties.
     */
    private void validateFearful(Lid lid) {
        if (lid == null || !lid.thisIsFearful()) return;
        if (!isInElements(lid.getNumber(), "cup")) {
            popLid();
            isOK = false;
            return;
        }
        Cup companion = findCupByNumberOfLid(lid.getNumber());
        if (companion != null && companion.isCovered()) {
            lid.setQuitable(false);
        }
    }
    
    
    
    /**
     * Adds a customized lid of the specified type to the tower.
     * 
     * Instances the corresponding Lid subclass based on the provided type, checks whether 
     * a matching cup exists to link them, and physically stacks it based on size comparisons.
     * It also invokes post-insertion behaviours for specific elements like fearful 
     * or crazy lids.
     * 
     * @param type   The specific type of lid to instantiate (e.g., "fearful", "crazy", "lid").
     * @param number The identifier of the lid.
     */
    public void pushLid(String type, int number) {
        if (isInElements(number, "lid") || number <= 0) {
            isOK = false;
            if (isVisible) showError();
            return;
        }
        Lid newLid = createLidElement(type, number);
        linkExistingCup(newLid);
        if (top == null) {
            top = newLid;
            lids.push(newLid);
            objects.push(newLid);
            afterPush(newLid);
            isOK = true;
            return;
        }
        int sizeTop    = top.getWidth();
        int sizeNewLid = newLid.getWidth();
        if (sizeTop > sizeNewLid && top.isCanIn()) {
            setInside((Cup) top, newLid);
            lids.push(newLid);
            objects.push(newLid);
        } else if (sizeTop <= sizeNewLid) {
            setAbove(top, newLid);
            lids.push(newLid);
            objects.push(newLid);
            if (top.getType().equals("cup") && top.getNumber() == newLid.getNumber()) top.setCover(newLid);
            setNewTop(newLid);
        } else if (sizeTop > sizeNewLid && !top.isCanIn()) {
            setAbove(top, newLid);
            lids.push(newLid);
            objects.push(newLid);
            setNewTop(newLid);
        }
        afterPush(newLid);
        isOK = true;
    }

}
