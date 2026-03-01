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
            if(isInCups(i)){
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
                lids.push(lid);
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
        public Cup popCup(){ 
            if(!cups.isEmpty()){
                Cup tope = top;
                this.top = null;
                cups.remove(tope);
                this.top = cups.get(0);
                for( Cup c:cups){
                    if(c.getCupAbove() ==tope ) c.setCupAbove(null);
                    setNewTop(c);
                }
                tope.makeInvisible();
                isOK = true;
                return tope;
            }
            if(isVisible()){
                JOptionPane.showMessageDialog(null, "No se pudo hacer la acción de popCup");
            }
            isOK = false;
            return null;
            }
        
        /**
         * Remueve una taza específica por número
         */
        public void removeCup(int i){
            if(!cups.isEmpty()){
                Stack<Cup> temp = new Stack();
                Cup rCup = null;
                for (Cup c:cups){
                    int cI = c.getNumber();
                    if(cI == i) {
                       rCup = c;
                       break;
                    }
                }
                if(rCup == null){
                   if(isVisible())JOptionPane.showMessageDialog(null, "No se pudo hacer la acción de removeCup");
                   isOK = false;
                   return;
                }
                cups.remove(rCup);
                for (Cup c: cups){
                    c.setCupInside(null);
                    c.setCupAbove(null);
                    temp.push(c);
                }
                cups.clear();
                top = null;
                for(Cup t:temp){
                    int tI = t.getNumber();
                    pushCup(tI);
                }
                return;
            }
            if(isVisible()){
                JOptionPane.showMessageDialog(null, "No se pudo hacer la acción de removeCup");
 
            }
            isOK = false;
        }
        
        /**
         * Removes a cup identified by its number.
         *
         * If the cup exists, it is removed and the structure is rebuilt
         * to update all relationships and the top cup.
         * If the cup does not exist or the collection is empty,
         * the operation fails.
         *
         * @param i the number of the cup to remove
         */
        public void pushLid(int i){
            if(top != null){
                int topNumber = top.getNumber();
                if(topNumber == i){
                    Lid topLid = top.getCover();
                    top.setState("Covered");
                    topLid.makeVisible();
                    isOK = true;
                    return;
                }
            }
            if(isVisible()){
                JOptionPane.showMessageDialog(null, "No se pudo hacer la acción de pushLid");
 
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
            Cup rLidCup = null;
            for(Cup c : cups){
                int cI = c.getNumber();
                if(cI == i){
                    rLidCup = c;
                    break;
                }
            }
            Lid rLid = rLidCup.getCover();
            rLidCup.setState("covered");
        }
        
        /**
         * Ordena la torre de mayor a menor altura
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
         * Invierte el orden de la torre
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
         * Retorna la altura total de elementos apilados
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
         * Hace visible la torre
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
    }