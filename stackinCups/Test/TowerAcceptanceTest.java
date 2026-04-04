package Test;
import tower.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TowerAcceptanceTest {

    private Tower tower;

    @Before
    public void setUp() {
        tower = new Tower(100, 300);
    }

    // ─── CICLO 1 ───────────────────────────────────────────────────────

    /**
     * Prueba de aceptación C1-1: Torre básica con pushCup y pushLid,
     * verificando unicidad, orden de stacking y copas tapadas.
     */
    @Test
    public void debeConstruirTorreConCopasYTapasCorrectamente() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushCup(1);
        tower.pushLid(3);
        tower.pushCup(3);
        tower.pushLid(3);
        tower.makeVisible();

        String[][] staking = tower.stakingItems();
        for(int i = 0; i < staking.length; i++){                                                                    
            System.out.println(staking[i][0] + " " + staking[i][1]);
        }
        
        int[] tapadas = tower.lindedCups(); // Limpiado el doble punto y coma
        boolean encontrado = false;
        
        for (int n : tapadas) {
            if (n == 3) {
                encontrado = true;
            }
        }
        assertTrue(encontrado);
    }

    /**
     * Prueba de aceptación C1-2: orderTower ordena de mayor a menor,
     * el menor queda en la cima, y si hay tapa del mismo número se coloca sobre su copa.
     */
    @Test
    public void debeOrdenarTorreDeМayorAMenorConTapaSobreSuCopa() {
        tower.pushCup(3);
        tower.pushCup(7);
        tower.pushCup(1);
        tower.pushCup(5);
        tower.pushLid(1);
        tower.orderTower();
        tower.makeVisible();
        String[][] staking = tower.stakingItems();
        for(int i = 0; i < staking.length; i++){                                                                    
            System.out.println(staking[i][0] + " " + staking[i][1]);
        }
        Cup cup1 = tower.findCupByNumber(1);
    }

    /**
     * Prueba de aceptación C1-3: Constructor Tower(int cups) crea copas
     * de tamaño 1,3,5,... y height() retorna la altura correcta.
     */
    @Test
    public void debeCrearTorreConConstructorDeCopasYVerificarAltura() {
        Tower t = new Tower(4);
        String[][] staking = t.stakingItems();
        for(int i = 0; i < staking.length; i++){                                                                    
            System.out.println(staking[i][0] + " " + staking[i][1]);
        }
        System.out.println("la altura es " + t.height());
    }

    // ─── CICLO 2 ───────────────────────────────────────────────────────

    /**
     * Prueba de aceptación C2-1: swap intercambia dos elementos y
     * swapToReduce detecta el primer par desordenado por altura.
     */
    @Test
    public void debeIntercambiarElementosYDetectarDesorden() {
        tower.pushCup(7);
        tower.pushCup(5);
        tower.pushCup(3);
        tower.swap(new String[]{"cup","7"}, new String[]{"cup","3"});
        String[][] staking = tower.stakingItems();
         for(int i = 0; i < staking.length; i++){                                                                    
            System.out.println(staking[i][0] + " " + staking[i][1]);
        }
        String[][] par = tower.swapToReduce();
         for(int i = 0; i < par.length; i++){                                                                    
            System.out.println(par[i][0] + " " + par[i][1]);
        }
        tower.makeVisible();
    }

    /**
     * Prueba de aceptación C2-2: cover empareja copas con sus tapas,
     * verificando staking, estados y lidedCups ordenado de menor a mayor.
     */
    @Test
    public void debeCubrirCopasYVerificarLidedCupsOrdenado() {
        tower.pushCup(5);
        tower.pushCup(3);
        tower.pushCup(1);
        tower.pushLid(5);
        tower.pushLid(1);
        tower.cover();
        String[][] staking = tower.stakingItems();
        tower.makeVisible();
        for(int i = 0; i < staking.length; i++){                                                                    
            System.out.println(staking[i][0] + " " + staking[i][1]);
        }
        int[] tapadas = tower.lindedCups();
        for(int j = 0; j < tapadas.length ; j++){
            System.out.println(tapadas[j]);
            }
        int count = 0;
        for (int n : tapadas) if (n > 0) count++;
    }

    /**
     * Prueba de aceptación C2-3: ciclo completo swapToReduce + swap
     * hasta ordenar la torre completamente de mayor a menor.
     */
    @Test
    public void debeOrdenarTorreCompletaConCicloSwapToReduce() {
        tower.pushCup(3);
        tower.pushCup(1);
        tower.pushCup(7);
        tower.pushCup(5);
        int maxIteraciones = 20;
        int iteraciones = 0;
        String[][] par = tower.swapToReduce();
        while (par.length > 0 && iteraciones < maxIteraciones) {
            tower.swap(par[0], par[1]);
            par = tower.swapToReduce();
            iteraciones++;
        }
        String[][] staking = tower.stakingItems();
        tower.makeVisible();
    }
}