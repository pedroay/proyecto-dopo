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
         * Agrega una taza al tope de la torre
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
                newCup = new Cup(i);
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
                if (sizeTop > sizeNewCup && top.getType().equals("cup")){
                    setInside((Cup)top,newCup);
                    cups.push(newCup);
                    objects.push(newCup);
                    setNewTop(newCup);
                }
                else if(sizeTop < sizeNewCup ){
                    setAbove(top,newCup);
                    cups.push(newCup);
                    setNewTop(newCup);
                    objects.push(newCup);
                }
                isOK = true;
            }
            }
        
        
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
           
        public void removeCup(int i){
            if(objects.isEmpty() ){
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
                    if(o.getType().equals("cup"))pushCup(o.getNumber());
                    else if(o.getType().equals("lid"))pushLid(o.getNumber());
                }
            }
        }
        
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
                newLid = new Lid(i);
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
            if (sizeTop > sizeNewLid && top.getType().equals("cup")) {
                setInside((Cup) top, newLid);
                lids.push(newLid);
                objects.push(newLid);
            } else if (sizeTop <= sizeNewLid ) {
                setAbove(top, newLid);
                lids.push(newLid);
                objects.push(newLid);
                if(top.getType().equals("cup") && top.getNumber() == newLid.getNumber())top.setCover(newLid);
                setNewTop(newLid);
            } else if (sizeTop > sizeNewLid && top.getType().equals("lid")) {
                setAbove(top, newLid);
                lids.push(newLid);
                objects.push(newLid);
                setNewTop(newLid);
            }
            
            isOK = true;
        }
          
        
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
                if (insideBaseWidth > apilarWidth ) {
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
                else if(apilar.getType().equals("lid") || apilar.getNumber() == base.getNumber())apilar.setPosition(posxApilar, posyBase);
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
        
        protected Cup findCupByNumberOfLid(int i) {
            for (Lid l : lids) {
                if (l.getNumber() == i) {
                    return l.getHisCup();
                }
            }
            return null;
        }
        
        private Lid findLidByNumberOfCup(int i) {
            for (Cup c : cups) {
                if (c.getNumber() == i) {
                    return c.getHisLid();
                }
            }
            return null;
        }
        
        private Cup findCupByNumber(int i){
            Cup ca = null;
            for (Cup c : cups) {
                if (c.getNumber() == i) {
                    ca = c;
                    break;
                }
            }
            return ca;
        }
        
        private Lid findLidByNumber(int i){
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
    }
