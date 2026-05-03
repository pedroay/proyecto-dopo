package presentacion;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dominio.Ball;
import dominio.Board;
import dominio.Borde;
import dominio.Goal;
import dominio.Mina;
import dominio.Player;
import dominio.Punto;
import dominio.SafeZone;
import dominio.Start;
import dominio.WorldHG;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import javax.swing.Timer;

public class WorldHardestGameGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel startPanel;
    private MenuWindow menuPanel;
    private GamePanel gamePanel;
    private dominio.WorldHG worldHG;
    private JFileChooser fileChooser = new JFileChooser(".");

    public WorldHardestGameGUI() {
        prepareElements();
        prepareActions();

    }

    private final void prepareElements() {
        // cosas para iniciar la aplicacion
        setTitle("The World’s Hardest Game");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen.getWidth() / 2);
        int height = (int) (screen.getHeight() / 2);
        setSize(width, height);
        setLocationRelativeTo(null);

        // Pantallas
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        menuPanel = new MenuWindow(this);

        prepareElementsStartPanel();

        mainPanel.add(startPanel, "Start");
        mainPanel.add(menuPanel, "MENU");
        add(mainPanel);
    }

    private final void prepareElementsStartPanel() {
        startPanel = new JPanel() {
            private Image backgroundImage;

            {
                // Intentamos cargar la imagen como recurso del proyecto
                try {
                    // La ruta empieza con "/" desde la raíz de src
                    java.net.URL imgURL = getClass().getResource("/presentacion/images/fondo.png");
                    if (imgURL != null) {
                        backgroundImage = javax.imageio.ImageIO.read(imgURL);
                    } else {
                        // Si getResource falla, intentamos la ruta física relativa
                        System.out.println("No se encontró como recurso, intentando ruta física...");
                        java.io.File file = new java.io.File("src/presentacion/images/fondo.png");
                        java.io.File file2 = new java.io.File("worldHardestGame/src/presentacion/images/fondo.png");
                        if (file.exists()) {
                            backgroundImage = javax.imageio.ImageIO.read(file);
                        } else if (file2.exists()) {
                            backgroundImage = javax.imageio.ImageIO.read(file2);
                        } else {
                            System.err.println("¡Error! No se encuentra 'fondo.png' en ninguna ruta.");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar la imagen: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Dibujamos la imagen escalada al tamaño del panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fondo de emergencia si la imagen no carga
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        startPanel.setLayout(new BorderLayout());
        startPanel.setOpaque(false); // Para que se vea el fondo

        // Botón Start
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial Black", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(150, 50));

        // color boton
        startButton.setBackground(new Color(26, 67, 117)); // Un azul brillante, por ejemplo
        startButton.setForeground(Color.WHITE); // Color de la letra
        startButton.setFocusPainted(false); // Quita el cuadrito punteado al hacer clic
        startButton.setBorderPainted(false); // Quita el borde por defecto
        startButton.setContentAreaFilled(true);

        // Efecto hover para hacer más grande el botón
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setFont(new Font("Arial", Font.BOLD, 28));
                startButton.setPreferredSize(new Dimension(170, 60));
                startButton.getParent().revalidate();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setFont(new Font("Arial", Font.BOLD, 24));
                startButton.setPreferredSize(new Dimension(150, 50));
                startButton.getParent().revalidate();
            }
        });

        // Panel para el botón (en la parte inferior)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        startButton.addActionListener(e -> irAlMenu());
        buttonPanel.add(startButton);

        startPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private final void prepareActions() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }

    public void exit() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Desea cerrar la aplicación World Hardest Game?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == 0) {
            setVisible(false);
            dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new WorldHardestGameGUI().setVisible(true);
        });
    }

    private void irAlMenu() {
        cardLayout.show(mainPanel, "MENU");
    }

    public void irAlTablero() {
        // Buscar el archivo del nivel 1
        String[] paths = {
                "src/dominio/levels/level1.txt",
                "worldHardestGame/src/dominio/levels/level1.txt"
        };
        java.io.File levelFile = null;
        for (String path : paths) {
            java.io.File f = new java.io.File(path);
            if (f.exists()) {
                levelFile = f;
                break;
            }
        }
        if (levelFile == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el archivo de nivel.\nBuscado en: src/dominio/levels/level1.txt",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Cargar nivel y crear el juego
            dominio.Level level = dominio.Level.loadFromFile(levelFile.getPath());
            worldHG = new dominio.WorldHG("player");
            worldHG.loadLevel(level);

            // Detener el panel anterior si existe
            if (gamePanel != null) {
                gamePanel.stopTimer();
                mainPanel.remove(gamePanel);
            }

            // Crear nuevo GamePanel y agregarlo al CardLayout
            gamePanel = new GamePanel(worldHG, this);
            mainPanel.add(gamePanel, "GAME");
            cardLayout.show(mainPanel, "GAME");

            // Asegurarse de que el panel reciba el foco para el teclado
            gamePanel.requestFocusInWindow();
            pack(); // Ajustar tamaño de la ventana al tablero
            setLocationRelativeTo(null);

        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar el nivel: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void salvar() {
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this,
                    "Función de GUARDADO en construcción.\nArchivo: " + archivo.getName(),
                    "En desarrollo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void abrir() {
        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this,
                    "Función de APERTURA en construcción.\nArchivo: " + archivo.getName(),
                    "En desarrollo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    class MenuWindow extends JPanel {
        protected WorldHardestGameGUI gui;
        protected JButton newButton;
        protected JButton saveButton;
        protected JButton cancelButton;
        protected JButton loadButton;
        private Image backgroundImage;

        public MenuWindow(WorldHardestGameGUI app) {
            this.gui = app;

            // Intentamos cargar la imagen como recurso del proyecto
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
                    } else {
                        System.err.println("¡Error! No se encuentra 'fondo.png' en ninguna ruta.");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen: " + e.getMessage());
                e.printStackTrace();
            }

            setLayout(new java.awt.GridBagLayout()); // Para centrar los botones
            prepareElementsMenuWindow();
            prepareActionsMenuWindow();
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

        public final void prepareElementsMenuWindow() {
            newButton = new JButton("New");
            saveButton = new JButton("Save");
            loadButton = new JButton("Load");
            cancelButton = new JButton("Cancel");

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new java.awt.GridBagLayout());
            buttonPanel.setOpaque(false);
            java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
            gbc.gridx = 0;
            gbc.insets = new java.awt.Insets(10, 0, 10, 0); // Espacio vertical entre botones

            JButton[] buttons = { newButton, saveButton, loadButton, cancelButton };
            for (JButton btn : buttons) {
                btn.setFont(new Font("Arial Black", Font.BOLD, 20));
                btn.setPreferredSize(new Dimension(150, 40));
                btn.setBackground(new Color(26, 67, 117));
                btn.setForeground(Color.WHITE);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(true);

                // Efecto hover
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btn.setFont(new Font("Arial", Font.BOLD, 24));
                        btn.setPreferredSize(new Dimension(170, 50));
                        btn.getParent().revalidate();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        btn.setFont(new Font("Arial", Font.BOLD, 20));
                        btn.setPreferredSize(new Dimension(150, 40));
                        btn.getParent().revalidate();
                    }
                });

                buttonPanel.add(btn, gbc);
            }

            add(buttonPanel);
        }

        protected final void prepareActionsMenuWindow() {
            newButton.addActionListener(e -> gui.irAlTablero());
            saveButton.addActionListener(e -> gui.salvar());
            loadButton.addActionListener(e -> gui.abrir());
            cancelButton.addActionListener(e -> gui.exit());
        }
    }

    class GamePanel extends JPanel implements KeyListener {

        private static final int CELL_SIZE  = 40;
        private static final int HUD_HEIGHT = 45;
        private static final double MOVE_SPEED = 5.0; // píxeles por frame (16ms)

        private static final Color COLOR_WALL     = new Color(45, 45, 55);
        private static final Color COLOR_EMPTY1   = new Color(200, 210, 230);
        private static final Color COLOR_EMPTY2   = new Color(180, 193, 218);
        private static final Color COLOR_START    = new Color(144, 238, 144);
        private static final Color COLOR_GOAL     = new Color(60, 210, 80);
        private static final Color COLOR_SAFEZONE = new Color(100, 200, 120);
        private static final Color COLOR_PLAYER   = new Color(215, 40, 40);
        private static final Color COLOR_ENEMY    = new Color(30, 100, 200);
        private static final Color COLOR_COIN     = new Color(255, 210, 0);
        private static final Color COLOR_HUD_BG   = new Color(20, 22, 38);

        private final WorldHG worldHG;
        private final WorldHardestGameGUI gui;

        // Posición visual del jugador en píxeles (interpolada para movimiento suave)
        private double playerVisualX, playerVisualY;
        private double playerTargetX, playerTargetY;

        private final Timer gameTimer;   // mueve enemigos cada 500ms
        private final Timer renderTimer; // repinta ~60fps para animación suave

        public GamePanel(WorldHG worldHG, WorldHardestGameGUI gui) {
            this.worldHG = worldHG;
            this.gui = gui;
            setFocusable(true);
            addKeyListener(this);

            // Inicializar posición visual en la celda de inicio
            Player player = worldHG.getPlayer1();
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
                JOptionPane.showMessageDialog(this,
                    "¡Nivel Completo!\nMuertes: " + worldHG.getDeaths(),
                    "Victoria", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        /**
         * Después de un tick de enemigos, si el jugador murió y reapareció lejos,
         * hace snap de la posición visual al nuevo punto de reaparición.
         */
        private void syncPlayerVisualAfterTick() {
            Player player = worldHG.getPlayer1();
            if (player == null) return;
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
            if (Math.abs(dx) < MOVE_SPEED) playerVisualX = playerTargetX;
            else playerVisualX += Math.signum(dx) * MOVE_SPEED;
            if (Math.abs(dy) < MOVE_SPEED) playerVisualY = playerTargetY;
            else playerVisualY += Math.signum(dy) * MOVE_SPEED;
        }

        public void stopTimer() { stopTimers(); }

        private void stopTimers() {
            gameTimer.stop();
            renderTimer.stop();
        }

        // ─── Dibujo ────────────────────────────────────────────────────────────────

        @Override
        public Dimension getPreferredSize() {
            Board[][] board = worldHG.getBoard();
            if (board == null || board.length == 0) return new Dimension(760, 360);
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
            if (board == null) return;
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    drawCell(g2, board[row][col], col * CELL_SIZE, HUD_HEIGHT + row * CELL_SIZE, row, col);
                }
            }
            drawPlayerSmooth(g2); // jugador encima de todo con posición interpolada
        }

        private void drawCell(Graphics2D g2, Board cell, int x, int y, int row, int col) {
            g2.setColor(getCellBgColor(cell, row, col));
            g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            g2.setColor(new Color(0, 0, 0, 25));
            g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            for (Object obj : cell.getContents()) {
                drawObject(g2, obj, x, y);
            }
        }

        private Color getCellBgColor(Board cell, int row, int col) {
            for (Object obj : cell.getContents()) {
                if (obj instanceof Borde)    return COLOR_WALL;
                if (obj instanceof Goal)     return COLOR_GOAL;
                if (obj instanceof Start)    return COLOR_START;
                if (obj instanceof SafeZone) return COLOR_SAFEZONE;
            }
            if (!cell.isCanHaveObjectOnTop()) return COLOR_WALL;
            return ((row + col) % 2 == 0) ? COLOR_EMPTY1 : COLOR_EMPTY2;
        }

        private void drawObject(Graphics2D g2, Object obj, int x, int y) {
            // El jugador se dibuja aparte con posición suavizada
            if (obj instanceof Player) return;

            int m = 5, s = CELL_SIZE - m * 2;

            if (obj instanceof Mina) {
                g2.setColor(new Color(20, 20, 120));
                g2.fillOval(x + m, y + m, s, s);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(x + m, y + m, s, s);

            } else if (obj instanceof Ball) {
                g2.setColor(COLOR_ENEMY);
                g2.fillOval(x + m, y + m, s, s);
                g2.setColor(new Color(10, 60, 160));
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(x + m, y + m, s, s);

            } else if (obj instanceof Punto) {
                int cs = s / 2;
                int cx = x + (CELL_SIZE - cs) / 2;
                int cy = y + (CELL_SIZE - cs) / 2;
                g2.setColor(COLOR_COIN);
                g2.fillOval(cx, cy, cs, cs);
                g2.setColor(new Color(200, 160, 0));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(cx, cy, cs, cs);
            }
        }

        /**
         * Dibuja al jugador usando playerVisualX/Y (posición interpolada),
         * NO la posición lógica del grid. Esto produce el movimiento suave.
         */
        private void drawPlayerSmooth(Graphics2D g2) {
            if (worldHG.getPlayer1() == null) return;
            int m = 5, s = CELL_SIZE - m * 2;
            int px = (int) playerVisualX + m;
            int py = (int) playerVisualY + HUD_HEIGHT + m;
            g2.setColor(COLOR_PLAYER);
            g2.fillRect(px, py, s, s);
            g2.setColor(new Color(150, 20, 20));
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(px, py, s, s);
        }

        // ─── Teclado ───────────────────────────────────────────────────────────────

        @Override
        public void keyPressed(KeyEvent e) {
            Player player = worldHG.getPlayer1();
            if (player == null || worldHG.isTimeUp() || worldHG.isLevelComplete()) return;

            String direction = null;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:     direction = "UP";    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:     direction = "DOWN";  break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:     direction = "LEFT";  break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:     direction = "RIGHT"; break;
            }

            if (direction == null) return;

            int prevX = player.getPosx();
            int prevY = player.getPosy();
            worldHG.movePlayer(player, direction);
            int newX = player.getPosx();
            int newY = player.getPosy();

            if (newX == prevX && newY == prevY) return; // movimiento bloqueado por pared

            double newTargetX = newX * CELL_SIZE;
            double newTargetY = newY * CELL_SIZE;

            // Si el jugador murió y reapareció lejos, hacer snap en vez de animar
            double dist = Math.abs(newTargetX - playerVisualX) + Math.abs(newTargetY - playerVisualY);
            if (dist > CELL_SIZE * 2) {
                playerVisualX = newTargetX;
                playerVisualY = newTargetY;
            }
            playerTargetX = newTargetX;
            playerTargetY = newTargetY;
        }

        @Override public void keyTyped(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }

}
