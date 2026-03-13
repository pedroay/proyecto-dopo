     import java.util.Stack;
    import java.util.ArrayList;
    import javax.swing.JOptionPane;

    
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
        private int maxHeight;
        private boolean isVisible;
        private Stack<Cup> cups;
        private Stack<Lid> lids;
        private Stack<Elements> objects;
        private boolean isOK;
        private Elements top;
        /**
         * Constructs a new Tower with the specified width and maximum height.
         *
         * The tower is initialized as not visible, with empty stacks of cups
         * and lids. The initial state of the tower is considered valid (isOK = true).
         *
         * @param nwidth     The width of the tower
         * @param nmaxHeight The maximum allowed height of the tower
         */
        public Tower(int nwidth, int nmaxHeight){
            width = nwidth;
            maxHeight = nmaxHeight;
            isVisible = false;
            cups = new Stack<Cup>();
            lids = new Stack<Lid>();
            objects = new Stack<Elements>();
            isOK = true;
        }
        
        /**
         * constructs a new tower with the specified number of cups which thie number will increase
         * in odd numbers "1,3,5,.."
         * 
         * @param i number of cups tha will have the new tower
         */
        public Tower(int i) {
            cups = new Stack<Cup>();
            lids = new Stack<Lid>();
            objects = new Stack<Elements>();
            isVisible = false;
            for(int j= 1; j <=i; j ++){
                int k = j*2-1;
                pushCup(k);
            }
            isOK = true;
        }
        
        /**
         * Adds a cup to the top of the element tower.
         *
         * This method validates that the provided ID is positive and unique. 
         * If the tower is empty, the cup becomes the new top. If the tower contains elements, 
         * the method determines whether to place the cup inside the current top (if the top 
         * is a larger cup) or above it (if the new cup is larger or the top is a lid).
         * 
         *
         * @param i The identification number (and size) of the cup to be added. 
         * Must be a unique positive integer.
         * @return This method does not return a value (void), but it updates the 
         * {@code isOK} flag to true upon successful execution, or false if an error occurs.
         */
        public void pushCup(int i){
            if(isInElements(i,"cup") ||i <=0){
                isOK = false;
                if(isVisible){
                    showError();
                    }
                return;
            }
            Cup newCup;

            if(isInLids(i)){
                newCup = findCupByNumberOfLid(i);
            }else{
                newCup = new Cup(i,this);
            }
            if(top == null){
                top = newCup;
                cups.push(newCup);
                objects.push(newCup);
                isOK = true;
                return;
            }
            else{
                int sizeTop = top.getWidth();
                int sizeNewCup = newCup.getWidth();
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
         * - If any element had the removed cup as its inner element, the reference is cleared.
         * - If any element had the removed cup above it, that reference is also cleared.
         *
         * Finally, the method recalculates the top element of the tower based on the
         * remaining elements.
         *
         * If the operation completes successfully, the tower structure is updated
         * accordingly. Otherwise, the state flag {@code isOK} is set to {@code false}.
         */
        public void popCup(){
            if(!objects.get(objects.size()-1).getType().equals("cup")){
                if(isVisible)showError();
                isOK = false;
            }
            else{
                Elements  popeado = objects.get(objects.size()-1);
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
         * When a cup is removed, the tower is temporarily cleared and then rebuilt
         * with the remaining elements in order to maintain the correct stacking rules.
         * During this reconstruction:
         * <ul>
         * <li>All references between elements (inside and above) are cleared.</li>
         * <li>Each remaining element is reinserted using {@code pushCup()} or {@code pushLid()}.</li>
         * </ul>
         *
         * The removed cup is also deleted from the internal stacks that store cups
         * and general elements.
         *
         * @param i the identifier (number) of the cup to be removed from the tower
         */
        public void removeCup(int i){
            if(objects.isEmpty() || !isInElements(i,"cup") ){
                if(isVisible)showError();
                isOK = false;
            }
            else{
                Elements a = findCupByNumber(i);
                makeInvisible();
                objects.remove(a);
                cups.remove(a);
                top = null;
                Stack<Elements> temp = new Stack();
                temp.addAll(objects);
                objects.clear();
                cups.clear();
                lids.clear();
                for(Elements o:temp){
                    o.setInside(null);
                    o.setAbove(null);
                    push((Elements)o);
                }
            }
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
         * If the new lid is wider or equal in width, it is placed above the current top element.
         * If the top element is a lid and wider than the new lid, the new lid is stacked above it.         * 
         *
         * When a lid is placed above its corresponding cup (same identifier),
         * the cup is marked as covered by that lid.
         *
         * Finally, the method updates the reference to the top element of the tower
         * if necessary and marks the operation as successful.
         *
         * @param i the identifier (number) of the lid to be added to the tower
         */
        public void pushLid(int i) {
            if (isInElements(i, "lid") || i <= 0) {
                isOK = false;
                if (isVisible) {
                    showError();
                }
                return;
            }
        
            Lid newLid;        
            if (isInCups(i)) {
                newLid = findLidByNumberOfCup(i);
            } else {
                newLid = new Lid(i,this);
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
            if(!objects.get(objects.size()-1).getType().equals("lid") ){
                if(isVisible)showError();
                isOK = false;
            }
            else{
                Elements  popeado = objects.get(objects.size()-1);
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
                public void removeLid(int i){
            if(objects.isEmpty() ){
                if(isVisible)showError();
                isOK = false;
            }
             else{
                Elements a = findLidByNumber(i);
                makeInvisible();
                objects.remove(a);
                lids.remove(a);
                top = null;
                Stack<Elements> temp = new Stack();
                temp.addAll(objects);
                objects.clear();
                cups.clear();
                lids.clear();
                for(Elements o:temp){
                    o.setInside(null);
                    o.setAbove(null);
                    if(o.getType().equals("cup"))pushCup(o.getNumber());
                    else if(o.getType().equals("lid"))pushLid(o.getNumber());
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
         * <ul>
         * <li>Each cup is reinserted using {@code pushCup()} following the sorted order.</li>
         * <li>Each lid is reinserted using {@code pushLid()} so that they are correctly
         * positioned relative to their corresponding cups.</li>
         * </ul>
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
                if(a.getType().equals("cup"))pushCup(a.getNumber());
                else if(a.getType().equals("lid"))pushLid(a.getNumber());
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
        
         public void swap(String[] objeto1, String[] objeto2) {
            Elements o1 = buscarElemento(objeto1);
            Elements o2 = buscarElemento(objeto2);
            if(o1 == null || o2 == null || o1 == o2){
                isOK = false;
                return;
            }
            Stack<Elements> temp = new Stack();
            temp.addAll(objects);
            objects.clear();
            cups.clear();
            lids.clear();
            top = null;
            for(Elements t:temp){
                t.setAbove(null);
                t.setInside(null);
                int tNumber = t.getNumber();
                if(tNumber != o1.getNumber() && tNumber != o2.getNumber() && t.getType().equals("cup"))pushCup(tNumber);
                else if(tNumber != o1.getNumber() && tNumber != o2.getNumber() && t.getType().equals("lid"))pushLid(tNumber);
                else if(tNumber == o1.getNumber() && t != o2){
                    if(o2.getType().equals("cup"))pushCup(o2.getNumber());
                    else if(o2.getType().equals("lid"))pushLid(o2.getNumber());
                }
                else if(tNumber == o2.getNumber() && t != o1){
                    if(o1.getType().equals("cup"))pushCup(o1.getNumber());
                    else if(o1.getType().equals("lid"))pushLid(o1.getNumber());
                }
                isOK= true;
            }
        }
        
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
        
         protected boolean isInElements(int i,String tipo){
            for(Elements e : objects){
                if(e.getNumber() == i && e.getType().equals(tipo)){
                    return true;
                }
            }
            return false;
        }  
        
         private boolean isInCups(int i){
            for(Cup c : cups){
                if(c.getNumber() == i){
                    return true;
                }
            }
            return false;
        }
        
         private boolean isInLids(int i){
            for(Lid l : lids){
                if(l.getNumber() == i){
                    return true;
                }
            }
            return false;
        } 
        
        public Cup findCupByNumberOfLid(int i) {
            for (Lid l : lids) {
                if (l.getNumber() == i) {
                    return l.getHisCup();
                }
            }
            return null;
        }
        
        public Lid findLidByNumberOfCup(int i) {
            for (Cup c : cups) {
                if (c.getNumber() == i) {
                    return c.getHisLid();
                }
            }
            return null;
        }
        
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
    }
