package presentacion;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import dominio.Board;
import dominio.Player;
import dominio.WorldHG;

public class GamePanel extends JPanel implements KeyListener {

    private static final int CELL_SIZE = 40;
    private static final int HUD_HEIGHT = 45;
    private static final double MOVE_SPEED = 6.0; // píxeles por frame (16ms)

    private static final Color COLOR_WALL = new Color(45, 45, 55);
    private static final Color COLOR_EMPTY1 = new Color(200, 210, 230);
    private static final Color COLOR_EMPTY2 = new Color(180, 193, 218);
    private static final Color COLOR_START = new Color(144, 238, 144);
    private static final Color COLOR_GOAL = new Color(60, 210, 80);
    private static final Color COLOR_SAFEZONE = new Color(100, 200, 120);
    private static final Color COLOR_HUD_BG = new Color(20, 22, 38);

    private final WorldHG worldHG;
    private final WorldHardestGameGUI gui;

    // Posición visual del jugador en píxeles (interpolada para movimiento suave)
    private double playerVisualX, playerVisualY;
    private double playerTargetX, playerTargetY;

    private final Timer gameTimer; // mueve enemigos cada 500ms
    private final Timer renderTimer; // repinta ~60fps para animación suave
    private final java.util.Set<Integer> keysPressed = new java.util.HashSet<>();

    public GamePanel(WorldHG worldHG, WorldHardestGameGUI gui) {
        this.worldHG = worldHG;
        this.gui = gui;
        setFocusable(true);
        setLayout(null);
        addKeyListener(this);

        // Botón de menú
        JButton menuBtn = new JButton("Menu");
        menuBtn.setFont(new Font("Arial", Font.BOLD, 12));
        menuBtn.setBackground(new Color(150, 40, 40));
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setFocusPainted(false);
        menuBtn.addActionListener(e -> {
            stopTimers();
            gui.irAlMenu();
        });
        int width = (worldHG.getBoard() != null && worldHG.getBoard().length > 0) ? worldHG.getBoard()[0].length * CELL_SIZE : 760;
        menuBtn.setBounds(width - 90, 8, 80, 28);
        add(menuBtn);

        // Inicializar posición visual en la celda de inicio
        Player player = worldHG.getPlayer(1);
        if (player != null) {
            playerVisualX = player.getPosx() * CELL_SIZE;
            playerVisualY = player.getPosy() * CELL_SIZE;
            playerTargetX = playerVisualX;
            playerTargetY = playerVisualY;
        }

        // Timer de lógica: ticks de enemigos y tiempo
        gameTimer = new Timer(500, e -> onTick());
        gameTimer.start();

        // Timer de render: interpola posición del jugador y repinta
        renderTimer = new Timer(16, e -> {
            handleInput();
            onTick();
            updatePlayerAnimation();
            repaint();
        });
        renderTimer.start();
    }

    // ─── Lógica ────────────────────────────────────────────────────────────────

    private void onTick() {
        if (worldHG.isTimeUp()) {
            stopTimers();
            repaint();
            JOptionPane.showMessageDialog(this,
                    "¡Se acabó el tiempo!\nMuertes: " + worldHG.getDeaths(),
                    "Game Over", JOptionPane.ERROR_MESSAGE);
            return;
        }
        worldHG.tick();

        // Sincronizar posición visual por si el jugador murió durante el tick
        syncPlayerVisualAfterTick();

        if (worldHG.isLevelComplete()) {
            stopTimers();
            repaint();
            String msg = "¡Nivel " + gui.getCurrentLevel() + " Completo!\nMuertes: " + worldHG.getDeaths();
            if (worldHG.getWinner() != null) {
                msg = "¡" + worldHG.getWinner() + " ha ganado!\n" + msg;
            }
            JOptionPane.showMessageDialog(this,
                    msg,
                    "Victoria", JOptionPane.INFORMATION_MESSAGE);

            // Cargar el siguiente nivel automáticamente
            gui.irAlTablero(gui.getCurrentLevel() + 1);
        }
    }

    /**
     * Después de un tick de enemigos, si el jugador murió y reapareció lejos,
     * hace snap de la posición visual al nuevo punto de reaparición.
     */
    private void syncPlayerVisualAfterTick() {
        Player player = worldHG.getPlayer(1);
        if (player == null)
            return;
        double newTargetX = player.getPosx() * CELL_SIZE;
        double newTargetY = player.getPosy() * CELL_SIZE;
        double dist = Math.abs(newTargetX - playerVisualX) + Math.abs(newTargetY - playerVisualY);
        if (dist > CELL_SIZE * 2) {
            // Reapareció lejos → snap inmediato
            playerVisualX = newTargetX;
            playerVisualY = newTargetY;
        }
        playerTargetX = newTargetX;
        playerTargetY = newTargetY;
    }

    /**
     * Mueve la posición visual del jugador hacia el target a velocidad constante.
     * Produce el efecto de deslizamiento entre celdas.
     */
    private void updatePlayerAnimation() {
        double dx = playerTargetX - playerVisualX;
        double dy = playerTargetY - playerVisualY;
        if (Math.abs(dx) < MOVE_SPEED)
            playerVisualX = playerTargetX;
        else
            playerVisualX += Math.signum(dx) * MOVE_SPEED;
        if (Math.abs(dy) < MOVE_SPEED)
            playerVisualY = playerTargetY;
        else
            playerVisualY += Math.signum(dy) * MOVE_SPEED;
    }

    public void stopTimer() {
        stopTimers();
    }

    private void stopTimers() {
        gameTimer.stop();
        renderTimer.stop();
    }

    // ─── Dibujo ────────────────────────────────────────────────────────────────

    @Override
    public Dimension getPreferredSize() {
        Board[][] board = worldHG.getBoard();
        if (board == null || board.length == 0)
            return new Dimension(760, 360);
        return new Dimension(board[0].length * CELL_SIZE, board.length * CELL_SIZE + HUD_HEIGHT);
    }

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
        if (board == null)
            return;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                drawCell(g2, board[row][col], col * CELL_SIZE, HUD_HEIGHT + row * CELL_SIZE, row, col);
            }
        }

        // Dibujar enemigos que viven fuera del Board[][]
        for (dominio.Enemy enemy : worldHG.getEnemies()) {
            drawObject(g2, enemy, (int) enemy.getX(), HUD_HEIGHT + (int) enemy.getY());
        }

        drawPlayer(g2, worldHG.getPlayer(1)); // jugador encima de todo con su posición real
        try {
	        if (worldHG.getPlayer(2) != null) {
	            drawPlayer(g2, worldHG.getPlayer(2));
	        }
        }catch(Exception e){
        	
        }
    }

    private void drawCell(Graphics2D g2, Board cell, int x, int y, int row, int col) {
        g2.setColor(getCellBgColor(cell, row, col));
        g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        g2.setColor(new Color(0, 0, 0, 25));
        g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        for (dominio.Object obj : cell.getContents()) {
            drawObject(g2, obj, x, y);
        }
    }

    private Color getCellBgColor(Board cell, int row, int col) {
        if (!cell.isCanHaveObjectOnTop())
            return COLOR_WALL;
        if (cell.isARespawn())
            return COLOR_START;
        if (cell.isAFinish())
            return COLOR_GOAL;
        if (cell.isSafe())
            return COLOR_SAFEZONE;

        return ((row + col) % 2 == 0) ? COLOR_EMPTY1 : COLOR_EMPTY2;
    }

    private void drawObject(Graphics2D g2, dominio.Object obj, int x, int y) {
        if (!obj.isVisible() || obj.isPlayer()) {
            return;
        }

        float ratio = obj.getDrawSizeRatio();
        // A ratio of 1.0 corresponds to a full cell with a 5px margin.
        int baseSize = CELL_SIZE - 10;
        int s = (int) (baseSize * ratio);

        // Compute centered offsets within the cell
        int cx = x + (CELL_SIZE - s) / 2;
        int cy = y + (CELL_SIZE - s) / 2;

        g2.setColor(obj.getPrimaryColor());
        g2.fillOval(cx, cy, s, s);

        g2.setColor(obj.getBorderColor());
        g2.setStroke(new BasicStroke(obj.getStrokeWidth()));
        g2.drawOval(cx, cy, s, s);
    }

    /**
     * Dibuja al jugador usando su posición lógica real (x, y).
     * Esto garantiza sincronización perfecta con la colisión.
     */
    private void drawPlayer(Graphics2D g2, dominio.Player player) {
        if (player == null)
            return;

        // Tamaño dinámico del jugador según su estado
        double stateSize = player.getState().getSize();
        // Calcula el margen para centrar el cuadro dentro de su celda
        int m = (int) ((CELL_SIZE - stateSize) / 2);
        int px = (int) player.getX() + m;
        int py = (int) player.getY() + HUD_HEIGHT + m;
        int s = (int) stateSize;

        // Color dinámico del jugador
        g2.setColor(player.getState().getColor());
        g2.fillRect(px, py, s, s);
        g2.setColor(player.getState().getColor().darker());
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(px, py, s, s);
    }

    // ─── Teclado ───────────────────────────────────────────────────────────────

    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.add(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove(e.getKeyCode());
    }

    private void handleInput() {
        Player p1 = worldHG.getPlayer(1);
        Player p2;
        try {
        	 p2 = worldHG.getPlayer(2);
        }catch(Exception e) {
        	p2 = null;
        }
        if (worldHG.isTimeUp() || worldHG.isLevelComplete())
            return;

        if (p1 != null) {
            if (keysPressed.contains(KeyEvent.VK_W)) worldHG.movePlayerContinuous(p1, "UP");
            if (keysPressed.contains(KeyEvent.VK_S)) worldHG.movePlayerContinuous(p1, "DOWN");
            if (keysPressed.contains(KeyEvent.VK_A)) worldHG.movePlayerContinuous(p1, "LEFT");
            if (keysPressed.contains(KeyEvent.VK_D)) worldHG.movePlayerContinuous(p1, "RIGHT");

            if (p2 == null) {
                if (keysPressed.contains(KeyEvent.VK_UP)) worldHG.movePlayerContinuous(p1, "UP");
                if (keysPressed.contains(KeyEvent.VK_DOWN)) worldHG.movePlayerContinuous(p1, "DOWN");
                if (keysPressed.contains(KeyEvent.VK_LEFT)) worldHG.movePlayerContinuous(p1, "LEFT");
                if (keysPressed.contains(KeyEvent.VK_RIGHT)) worldHG.movePlayerContinuous(p1, "RIGHT");
            }
        }

        if (p2 != null) {
            if (keysPressed.contains(KeyEvent.VK_UP)) worldHG.movePlayerContinuous(p2, "UP");
            if (keysPressed.contains(KeyEvent.VK_DOWN)) worldHG.movePlayerContinuous(p2, "DOWN");
            if (keysPressed.contains(KeyEvent.VK_LEFT)) worldHG.movePlayerContinuous(p2, "LEFT");
            if (keysPressed.contains(KeyEvent.VK_RIGHT)) worldHG.movePlayerContinuous(p2, "RIGHT");
        }
    }


}

// ─── Game Mode Selection Panel ─────────────────────────────────────────────

class GameModeSelectionPanel extends JPanel {
    protected WorldHardestGameGUI gui;
    protected JButton soloButton;
    protected JButton pvpButton;
    protected JButton pveButton;
    protected JButton backButton;
    private Image backgroundImage;

    public GameModeSelectionPanel(WorldHardestGameGUI app) {
        this.gui = app;

        try {
            java.net.URL imgURL = getClass().getResource("/presentacion/images/fondo.png");
            if (imgURL != null) {
                backgroundImage = javax.imageio.ImageIO.read(imgURL);
            } else {
                java.io.File file = new java.io.File("src/presentacion/images/fondo.png");
                java.io.File file2 = new java.io.File("worldHardestGame/src/presentacion/images/fondo.png");
                if (file.exists()) {
                    backgroundImage = javax.imageio.ImageIO.read(file);
                } else if (file2.exists()) {
                    backgroundImage = javax.imageio.ImageIO.read(file2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new java.awt.BorderLayout());
        
        JLabel title = new JLabel("Selecciona el Modo de Juego", SwingConstants.CENTER);
        title.setFont(new Font("Arial Black", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(title, java.awt.BorderLayout.NORTH);

        prepareElementsModeWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public final void prepareElementsModeWindow() {
        soloButton = new JButton("Solo");
        pvpButton = new JButton("Jugador vs Jugador");
        pveButton = new JButton("Jugador vs Máquina");
        backButton = new JButton("Volver");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new java.awt.GridBagLayout());
        buttonPanel.setOpaque(false);
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new java.awt.Insets(10, 0, 10, 0);

        JButton[] buttons = { soloButton, pvpButton, pveButton, backButton };
        for (JButton btn : buttons) {
            btn.setFont(new Font("Arial Black", Font.BOLD, 20));
            btn.setPreferredSize(new Dimension(280, 50));
            if (btn == backButton) {
                btn.setBackground(new Color(120, 40, 40));
            } else {
                btn.setBackground(new Color(26, 67, 117));
            }
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(true);

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setFont(new Font("Arial", Font.BOLD, 22));
                    btn.setPreferredSize(new Dimension(300, 60));
                    btn.getParent().revalidate();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setFont(new Font("Arial", Font.BOLD, 20));
                    btn.setPreferredSize(new Dimension(280, 50));
                    btn.getParent().revalidate();
                }
            });

            buttonPanel.add(btn, gbc);
        }

        add(buttonPanel, java.awt.BorderLayout.CENTER);

        soloButton.addActionListener(e -> { gui.setSelectedMode("solo"); gui.irAlTablero(); });
        pvpButton.addActionListener(e -> { gui.setSelectedMode("pvp"); gui.irAlTablero(); });
        pveButton.addActionListener(e -> { gui.setSelectedMode("pve"); gui.irAlTablero(); });
        backButton.addActionListener(e -> gui.irASeleccionSkin());
    }
}

