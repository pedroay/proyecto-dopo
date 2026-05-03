# Transición a Movimiento Continuo (Pixel a Pixel)

Actualmente, el juego funciona como un tablero de ajedrez: el jugador y los enemigos saltan de una celda entera a otra. Por eso, el jugador nunca puede quedarse a la mitad de una celda y los enemigos hacen "saltos" cada 500 milisegundos.

Para lograr que el movimiento sea fluido y la caja roja (jugador) pueda quedarse entre dos celdas, debemos cambiar la arquitectura del juego de un **movimiento discreto (basado en grilla)** a un **movimiento continuo (basado en coordenadas libres)**.

## User Review Required

> [!WARNING]
> **Cambio Arquitectónico Importante**
> Este cambio requerirá modificar varios archivos centrales del juego (`WorldHG`, `Ball`, `Player`, `GamePanel`). Dejaremos de usar el `Board[][]` para saber dónde están los personajes en todo momento y pasaremos a usar "Cajas de Colisión" (Bounding Boxes) para detectar choques entre ellos y las paredes.
> 
> ¿Estás de acuerdo con reestructurar la lógica de movimiento y colisiones para lograr esta fluidez?

## Proposed Changes

### Dominio (Lógica del Juego)

#### [MODIFY] [Player.java](file:///c:/Users/Pedro%20Ayala/Documents/proyecto-dopo/Proyecto-final/worldHardestGame/src/dominio/Player.java)
- Cambiar las coordenadas enteras (`posx`, `posy`) a `double` (`x`, `y`) para representar la posición en píxeles.
- Añadir variables de velocidad (`velX`, `velY`).

#### [MODIFY] [Enemy.java](file:///c:/Users/Pedro%20Ayala/Documents/proyecto-dopo/Proyecto-final/worldHardestGame/src/dominio/Enemy.java) y [Ball.java](file:///c:/Users/Pedro%20Ayala/Documents/proyecto-dopo/Proyecto-final/worldHardestGame/src/dominio/Ball.java)
- Cambiar las coordenadas a `double` (`x`, `y`).
- Modificar el método `move()` para que no salte de celda en celda, sino que avance unos pocos píxeles en su dirección (`dirX`, `dirY`) multiplicados por una `velocidad`.

#### [MODIFY] [WorldHG.java](file:///c:/Users/Pedro%20Ayala/Documents/proyecto-dopo/Proyecto-final/worldHardestGame/src/dominio/WorldHG.java)
- Extraer al `Player` y los `Enemies` fuera del arreglo bidimensional `board[][]`. El `board` ahora solo contendrá elementos estáticos (Paredes, Zonas Seguras, Metas, Monedas).
- Implementar **AABB (Axis-Aligned Bounding Box)** para la detección de colisiones. En el método `tick()`, verificaremos si los rectángulos de colisión de los enemigos se intersectan con el del jugador.
- Verificar colisiones del jugador con las paredes usando las coordenadas continuas antes de permitirle avanzar.

### Presentación (Interfaz Gráfica)

#### [MODIFY] [WorldHardestGameGUI.java](file:///c:/Users/Pedro%20Ayala/Documents/proyecto-dopo/Proyecto-final/worldHardestGame/src/presentacion/WorldHardestGameGUI.java) (GamePanel)
- Eliminar los dos *timers* distintos (`gameTimer` a 500ms y `renderTimer` a 16ms) y combinarlos en un único `Timer` (bucle de juego) a ~16ms (60 FPS) que actualice la lógica (WorldHG.tick) y repinte la pantalla a la vez.
- Modificar el `KeyListener`. En lugar de mover al jugador en `keyPressed`, este método solo actualizará el estado de los botones (e.g., `isUpPressed = true`). En `keyReleased` se pondrá en `false`. El movimiento real se hará en el bucle principal.
- Ajustar el método de pintado para dibujar al jugador y a los enemigos en sus coordenadas exactas de píxeles (las variables `double x, y` del dominio), eliminando la interpolación visual engañosa que se está usando ahora.

### Niveles

#### [MODIFY] [level1.txt](file:///c:/Users/Pedro%20Ayala/Documents/proyecto-dopo/Proyecto-final/worldHardestGame/src/dominio/levels/level1.txt)
- Cambiar `BV` (Ball Vertical) por `BH` (Ball Horizontal) para que los enemigos se muevan horizontalmente de forma predeterminada, o hacerlo ajustable. (Se puede agrandar el tamaño del mapa aquí añadiendo más puntos `.`).

## Verification Plan

### Manual Verification
- Cargar el nivel 1.
- Comprobar que el jugador rojo se mueve suavemente al mantener presionadas las teclas direccionales y se detiene exactamente al soltar la tecla (pudiendo quedar entre dos casillas).
- Observar a las bolas enemigas moverse rápida y fluidamente de forma horizontal.
- Verificar que al chocar con una pared el jugador se frena y al chocar con un enemigo, muere y reaparece correctamente.
