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
        private boolean isOK;
        private Cup top;
        
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
            isOK = true;
        }
        
        /**
         * Agrega una taza al tope de la torre
         */
        public void pushCup(int i){
            if(isInCups(i) ||i <=0){
                isOK = false;
                if(isVisible){
                    JOptionPane.showMessageDialog(null, "No se pudo hacer la acción de pushCup");
                    }
                return;
            }
            Cup newCup = new Cup(i);
            if(top == null){
                top = newCup;
                cups.push(newCup);
                Lid lid = newCup.getCover();
                isOK = true;
                return;
            }
            else{
                int sizeTop = top.getHeight();
                int sizeNewCup = newCup.getHeight();
                boolean topCover = top.isCovered();
                int posyTop =top.getPosy();
                int posyNewCup = newCup.getPosy();
                if (sizeTop > sizeNewCup && !topCover){
                    setInside(top,newCup);
                    cups.push(newCup);
                    setNewTop(newCup);
                }
                else if(sizeTop < sizeNewCup || topCover){
                    setAbove(top,newCup);
                    cups.push(newCup);
                    setNewTop(newCup);
                }
                isOK = true;
            }
            }
        
            
        private boolean isInCups(int i){
            for(Cup c : cups){
                if(c.getNumber() == i){
                    return true;
                }
            }
            return false;
        }
            
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
        private void setInside(Cup base,Cup apilar){
            Cup insideBase = base.getCupInside();
            int posxApilar = apilar.getPosx();
            int posyBase = base.getPosy();
            int baseHeight = base.getHeight();
            int apilarHeight = apilar.getHeight();
            if(insideBase == null){
                apilar.setPosition(posxApilar,posyBase+baseHeight-10-apilarHeight);
                base.setCupInside(apilar);
            }
            else{
                int insideBaseHeight = insideBase.getHeight();
                boolean insideBaseCover = insideBase.isCovered();
                if(insideBaseHeight > apilarHeight && !insideBaseCover){
                    setInside(insideBase,apilar);    
                }
                else if(insideBaseHeight <= apilarHeight || insideBaseCover){
                    setAbove(insideBase,apilar);
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
        private void setNewTop(Cup newCup){
            if(top == null){
                top = newCup;
                return;
            }
            int posyTop = top.getPosy();
            int posyNewCup = newCup.getPosy();
            if (posyTop > posyNewCup){
                top = newCup;
            }
        }
        
        /**
         * Places a cup on top of another cup following stacking rules.
         *
         * If the base cup does not already have a cup above it,
         * the given cup is positioned directly above the base cup
         * and linked as its upper cup.
         *
         * If there is already a cup above, the method checks:
         * - If the upper cup is taller than the cup to stack and is not covered,
         *   the method attempts to place the cup inside that upper cup.
         * - Otherwise, the method recursively attempts to place the cup
         *   above the upper cup.
         *
         * The position of the stacked cup is updated accordingly,
         * and the tower state is marked as valid (isOK = true).
         *
         * @param base    The cup on which another cup will be placed
         * @param apilar  The cup to be placed above the base cup
         */
        private void setAbove(Cup base, Cup apilar){
           Cup baseAbove = base.getCupAbove(); 
           int posxApilar = apilar.getPosx();
            int posyBase = base.getPosy();
            int apilarHeight = apilar.getHeight();
           if(baseAbove == null){
               base.setCupAbove(apilar);
               apilar.setPosition(posxApilar,posyBase-apilarHeight);
            }
           else{
               int baseAboveHeight = baseAbove.getHeight();
               boolean baseAboveCover = baseAbove.isCovered();
               if(baseAboveHeight > apilarHeight && !baseAboveCover){
                   setInside(baseAbove,apilar);
                }
               else if( baseAboveHeight <= apilarHeight || baseAboveCover){
                   setAbove(baseAbove,apilar);
               }
               }
           isOK = true;
        }
     
       /**
        * Removes and returns the top cup of the structure.
        
         *If there are cups in the collection, the method stores the current top cup,
         *removes it from the stack, and clears any references from other cups that
         *were pointing to it as their "above" cup.
        
        * Then, it recalculates which cup should become the new top by checking all
         *remaining cups. The removed cup is made invisible and the operation is
         *marked as successful.
        
         *If the collection is empty and the structure is visible, a message dialog
         *is displayed indicating that the pop operation could not be performed.
         *In that case, the method returns null and marks the operation as failed.
        */
        public void popCup(){ 
            if(!cups.isEmpty()){
                Cup pop = cups.pop();
                if(pop == top){
                    this.top = null;}
                  for( Cup c:cups){
                    if(c.getCupAbove() == pop)c.setCupAbove(null);
                    if(c.getCupInside() == pop)c.setCupInside(null);
                    setNewTop(c);                  
                }
                pop.makeInvisible();
                isOK = true;
                }
            
            if(isVisible()){
                showError();
            }
            isOK = false;
            }
        
        /**
         * Removes a specific cup identified by its number.
         *
         * The method searches for the cup with the given number. If it exists,
         * the cup is removed from the stack and the structure is rebuilt to
         * maintain correct relationships between the remaining cups.
         * All cups above the removed one are temporarily extracted and then
         * reinserted to preserve the stacking order.
         *
         * If the cup does not exist or the collection is empty, the operation
         * fails and an error message may be displayed if the structure is visible.
         *
         * @param i the number of the cup to be removed
         */
        public void removeCup(int i){
            if(!cups.isEmpty()){
                Stack<Cup> temp = new Stack();
                Cup rCup = findCupByNumber(i);
                if(rCup == null){
                   if(isVisible())
                   showError();
                   return;
                }
                for (int j = cups.size();;j--){
                    Cup c = cups.pop();
                    if(c == rCup)break;
                    c.setCupInside(null);
                    c.setCupAbove(null);
                    temp.push(c);
                }
                cups.remove(rCup);
                top = null;
                for(Cup c:cups){
                    setNewTop(c);
                }
                top.setCupInside(null);
                top.setCupAbove(null);
                for(Cup t:temp){
                    int tI = t.getNumber();
                    pushCup(tI);
                }
                return;
            }
            if(isVisible)showError();
            isOK = false;
        }
        
        /**
         * Searches for a cup by its number.
         *
         * Iterates through the collection of cups and returns the cup
         * whose number matches the given parameter.
         *
         * @param i the number of the cup to search for
         * @return the cup with the specified number, or null if not found
         */
        private Cup findCupByNumber(int i) {
            for (Cup c : cups) {
                if (c.getNumber() == i) {
                    return c;
                }
            }
            return null;
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
        
        
        /**
         * Places a lid on the top cup if its number matches the given value.
         *
         * If the current top cup exists and its number is equal to the
         * specified number, its lid is pushed onto the lids stack,
         * the cup state is updated to "Covered", and the lid becomes visible.
         * Otherwise, an error message is displayed (if visible) and the
         * operation is marked as unsuccessful.
         *
         * @param i the number of the cup that should receive the lid
         */
        public void pushLid(){
            if(top != null){
                    Lid topLid = top.getCover();
                    lids.push(topLid);
                    isOK = true;
                    return;
            }
            if(isVisible()){
                showError();
             }
            isOK = false;
        }
        
        public void pushLid(int i){
            if(!cups.empty()){
                Cup c = findCupByNumber(i);
                Lid lidC = c.getCover();
                lids.push(lidC);
                isOK=true;
                return;
            }
            if(isVisible()){
                showError();
             }
            isOK = false;
        }
        /**
         * Removes the lid from the top cup.
         *
         * If the top cup exists and is covered, its lid is removed
         * and made invisible. The cup state is updated to "noCovered".
         * If there is no top cup or it is not covered, the operation fails
         * and may display an error message if the structure is visible.
         */
        public void popLid(){
            if (top!= null){
                if(top.isCovered()){
                    Lid topLid = top.getCover();
                    topLid.makeInvisible();
                    top.setState("noCovered");
                    lids.pop();
                    isOK = true;
                    return;
                }
                if(isVisible()){
                    JOptionPane.showMessageDialog(null, "No se pudo hacer la acción de popLid");
 
                }
                isOK = false;
            }
            if(isVisible()){
                    JOptionPane.showMessageDialog(null, "No se pudo hacer la acción de popLid");
 
                }
                isOK = false;
        }
    
        
        /**
         * Remueve una tapa del stack
         */
        public void removeLid(int i){
            Stack<Cup> temp = new Stack();
            Cup rLidCup = findCupByNumber(i);
            if(rLidCup.isCovered()){
                Lid rLid = rLidCup.getCover();
                rLid.makeInvisible();
                lids.remove(rLid);
                rLidCup.setState("noCovered");
                while (!cups.isEmpty()) {
                    Cup c = cups.pop();
                    temp.push(c);
                    if(c == rLidCup)break;
                    c.setCupInside(null);
                    c.setCupAbove(null);
                }
                top = null;
                for(Cup c:cups){
                    setNewTop(c);
                }
                if (top != null) {
                    top.setCupInside(null);
                    top.setCupAbove(null);
                } 
        
                for(Cup t:temp){
                    int tI = t.getNumber();
                    pushCup(tI);
                }
                return;
            }
        }
        
        /**
         * Sorts the tower in descending order based on the cup number
         * (from largest to smallest).
         *
         * The method creates a temporary list containing the current cups,
         * applies a bubble sort algorithm to reorder them by number in
         * descending order, clears the original stack, and rebuilds the
         * tower using the sorted cups. During reconstruction, all cup
         * relationships are reset and reassigned.
         *
         * After rebuilding the structure, the tower is made visible and
         * the operation status is marked as successful.
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
            cups.clear();
            top = null;
            for (Cup c : temp) {
                c.setCupInside(null);
                c.setCupAbove(null);
                int cI = c.getNumber();
                pushCup(cI);
            }
            makeVisible();
            isOK = true;
            }
        
        /**
         * Reverses the order of the cups in the tower.
         *
         * The method removes all cups from the current stack, clears their
         * structural relationships, and stores them temporarily in another stack.
         * It then rebuilds the tower by pushing the cups back in reverse order,
         * effectively inverting the original structure.
         *
         * After reconstruction, the operation status is marked as successful.
         */
        public void reverseTower()
        {
            Stack<Cup> temp = new Stack<Cup>();
            top = null;
            while (!cups.isEmpty()) {
                Cup c = cups.pop();
                c.setCupInside(null);
                c.setCupAbove(null);
                temp.push(c);
            }
            for(Cup c: temp){
                int cI = c.getNumber();
                pushCup(cI);
            }
            isOK = true;
        }
        
        /**
         * Calculates and returns the current height of the tower.
         *
         * The height is determined based on the vertical position (posY)
         * of the top cup. If a top cup exists, the height is computed as
         * the difference between a fixed base value (300) and the top cup's
         * Y position. If the tower is empty (top is null), the height is 0.
         *
         * The operation status flag is set to true in both cases.
         *
         * @return the height of the tower; 0 if the tower is empty
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
         * Returns an array containing the numbers of all covered cups.
         *
         * The method iterates through the collection of cups and collects
         * the numbers of those that are currently covered with a lid.
         * It then converts the collected values into an integer array.
         *
         * @return an array with the numbers of the covered cups;
         *         an empty array if no cups are covered
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
         * Returns a two-dimensional array representing the stacking elements
         * in the tower.
         *
         * Each row of the returned array contains two values:
         * - The type of element ("cup" or "lid").
         * - The associated value (cup number or lid height).
         *
         * The method iterates through all cups in the structure and adds
         * their information to the result. If a cup is covered, its lid
         * information is also included immediately after the cup entry.
         *
         * @return a 2D array describing the stacking items in the tower
         */
        public String[][] stackingItems() {
             ArrayList<String[]> temp = new ArrayList<>();
            for (Cup c : cups) {
        
                temp.add(new String[]{
                    "cup",
                    String.valueOf(c.getNumber())
                });
        
                if (c.isCovered()) {
                    Lid lid = c.getCover();
                    temp.add(new String[]{
                        "lid",
                        String.valueOf(lid.getHeight())
                    });
                }
            }
            String[][] result = new String[temp.size()][2];
        
            for (int i = 0; i < temp.size(); i++) {
                result[i] = temp.get(i);
            }
        
            return result;
        }

        
       /**
         * Makes the tower visible.
         *
         * The method iterates through all cups in the structure and makes
         * each cup visible if its vertical position is valid (posY >= 0).
         * After updating the cups, the tower visibility state is set to true.
         */
        public void makeVisible()
        {   for(Cup c:cups){
                int cPosy = c.getPosy();
                if (cPosy >= 0){
                c.makeVisible();
            }
            }
            isVisible = true;
            }
        
        /**
         * Hace invisible la torre
         */
        public void makeInvisible()
        {
            for(Cup c:cups){
                c.makeInvisible();
                isVisible = false;
            }
        }
        
        /**
         * Verifica si la torre es visible
         */
        public boolean isVisible(){   
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
        
        public void drawRule(){
            for (int i=0;i<=maxHeight;i++){
                Rectangle r= new Rectangle();
                r.changeSize(2,10);
                r.setP(i*10,0);
                r.makeVisible();
            }
        }
        
        
        //cilco 2
    }