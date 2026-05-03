package presentacion;

import dominio.Ball;
import dominio.Board;
import dominio.Borde;
import dominio.Enemy;
import dominio.Goal;
import dominio.Mina;
import dominio.Player;
import dominio.Punto;
import dominio.SafeZone;
import dominio.Start;
import dominio.WorldHG;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements KeyListener {

    private static final int CELL_SIZE = 40;
    private static final int HUD_HEIGHT = 45;

    // Colores del tablero
    private static final Color COLOR_WALL      = new Color(45, 45, 55);
    private static final Color COLOR_EMPTY1    = new Color(200, 210, 230);
    private static final Color COLOR_EMPTY2    = new Color(180, 193, 218);
    private static final Color COLOR_START     = new Color(144, 238, 144);
    private static final Color COLOR_GOAL      = new Color(60, 210, 80);
    private static final Color COLOR_SAFEZONE  = new Color(100, 200, 120);
    private static final Color COLOR_PLAYER    = new Color(215, 40, 40);
    private static final Color COLOR_ENEMY     = new Color(30, 100, 200);
    private static final Color COLOR_COIN      = new Color(255, 210, 0);
    private static final Color COLOR_HUD_BG    = new Color(20, 22, 38);

    private final WorldHG worldHG;
    private final Timer gameTimer;
    private final WorldHardestGameGUI gui;

    public GamePanel(WorldHG worldHG, WorldHardestGameGUI gui) {
        this.worldHG = worldHG;
        this.gui = gui;
        setFocusable(true);
        addKeyListener(this);

        // Tick cada 500ms: mueve enemigos y verifica colisiones
        gameTimer = new Timer(500, e -> onTick());
        gameTimer.start();
    }

    private void onTick() {
        if (worldHG.isTimeUp()) {
            gameTimer.stop();
            repaint();
            JOptionPane.showMessageDialog(this,
                "¡Se acabó el tiempo!\nMuertes: " + worldHG.getDeaths(),
                "Game Over", JOptionPane.ERROR_MESSAGE);
            return;
        }
        worldHG.tick();
        repaint();
        if (worldHG.isLevelComplete()) {
            gameTimer.stop();
            JOptionPane.showMessageDialog(this,
                "¡Nivel Completo!\nMuertes: " + worldHG.getDeaths(),
                "Victoria", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void stopTimer() {
        gameTimer.stop();
    }

    @Override
    public Dimension getPreferredSize() {
        Board[][] board = worldHG.getBoard();
        if (board == null || board.length == 0) return new Dimension(760, 360);
        return new Dimension(board[0].length * CELL_SIZE, board.length * CELL_SIZE + HUD_HEIGHT);
    }

    // ─── Dibujo ────────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawHUD(g2);
        drawBoard(g2);
    }

    private void drawHUD(Graphics2D g2) {
        g2.setColor(COLOR_HUD_BG);
        g2.fillRect(0, 0, getWidth(), HUD_HEIGHT);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 15));
        g2.drawString(worldHG.getInfo(), 12, 28);
    }

    private void drawBoard(Graphics2D g2) {
        Board[][] board = worldHG.getBoard();
        if (board == null) return;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                int x = col * CELL_SIZE;
                int y = HUD_HEIGHT + row * CELL_SIZE;
                drawCell(g2, board[row][col], x, y, row, col);
            }
        }
    }

    private void drawCell(Graphics2D g2, Board cell, int x, int y, int row, int col) {
        // Fondo de la celda según su tipo estático
        g2.setColor(getCellBgColor(cell, row, col));
        g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

        // Línea de grilla sutil
        g2.setColor(new Color(0, 0, 0, 25));
        g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);

        // Objetos dinámicos
        for (Object obj : cell.getContents()) {
            drawObject(g2, obj, x, y);
        }
    }

    private Color getCellBgColor(Board cell, int row, int col) {
        // Revisamos el contenido estático para el color de fondo
        for (Object obj : cell.getContents()) {
            if (obj instanceof Borde)    return COLOR_WALL;
            if (obj instanceof Goal)     return COLOR_GOAL;
            if (obj instanceof Start)    return COLOR_START;
            if (obj instanceof SafeZone) return COLOR_SAFEZONE;
        }
        if (!cell.isCanHaveObjectOnTop()) return COLOR_WALL;
        // Patrón tablero de ajedrez para celdas vacías
        return ((row + col) % 2 == 0) ? COLOR_EMPTY1 : COLOR_EMPTY2;
    }

    private void drawObject(Graphics2D g2, Object obj, int x, int y) {
        int m = 5; // margen
        int s = CELL_SIZE - m * 2;

        if (obj instanceof Player) {
            g2.setColor(COLOR_PLAYER);
            g2.fillRect(x + m, y + m, s, s);
            g2.setColor(new Color(150, 20, 20));
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(x + m, y + m, s, s);

        } else if (obj instanceof Mina) {
            // Mina: punto oscuro con borde
            g2.setColor(new Color(20, 20, 120));
            g2.fillOval(x + m, y + m, s, s);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(x + m, y + m, s, s);

        } else if (obj instanceof Ball) {
            // Ball: círculo azul
            g2.setColor(COLOR_ENEMY);
            g2.fillOval(x + m, y + m, s, s);
            g2.setColor(new Color(10, 60, 160));
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x + m, y + m, s, s);

        } else if (obj instanceof Punto) {
            // Moneda: círculo amarillo centrado, más pequeño
            int cs = s / 2;
            int cx = x + (CELL_SIZE - cs) / 2;
            int cy = y + (CELL_SIZE - cs) / 2;
            g2.setColor(COLOR_COIN);
            g2.fillOval(cx, cy, cs, cs);
            g2.setColor(new Color(200, 160, 0));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(cx, cy, cs, cs);
        }
        // Borde/Start/Goal/SafeZone se dibujan como color de fondo, no como objeto
    }

    // ─── Teclado ───────────────────────────────────────────────────────────────

    @Override
    public void keyPressed(KeyEvent e) {
        Player player = worldHG.getPlayer1();
        if (player == null || worldHG.isTimeUp() || worldHG.isLevelComplete()) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:    worldHG.movePlayer(player, "UP");    break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:    worldHG.movePlayer(player, "DOWN");  break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:    worldHG.movePlayer(player, "LEFT");  break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:    worldHG.movePlayer(player, "RIGHT"); break;
        }
        repaint();
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
