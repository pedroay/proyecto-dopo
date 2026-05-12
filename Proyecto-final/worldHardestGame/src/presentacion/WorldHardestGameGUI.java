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

import dominio.Board;
import dominio.Player;
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
    private SkinSelectionPanel skinPanel;
    private GamePanel gamePanel;
    private dominio.WorldHG worldHG;
    private JFileChooser fileChooser = new JFileChooser(".");
    private int currentLevel = 1;
    private String selectedSkin = "red"; // Default skin

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
        skinPanel = new SkinSelectionPanel(this);

        prepareElementsStartPanel();

        mainPanel.add(startPanel, "Start");
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(skinPanel, "SKIN");
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

    public void irASeleccionSkin() {
        cardLayout.show(mainPanel, "SKIN");
    }

    public void setSelectedSkin(String skin) {
        this.selectedSkin = skin;
    }

    public void irAlTablero() {
        irAlTablero(1);
    }

    public void irAlTablero(int numNivel) {
        this.currentLevel = numNivel;
        String fileName = "level" + numNivel + ".txt";

        // Buscar el archivo del nivel dinámicamente
        String[] paths = {
                "src/dominio/levels/" + fileName,
                "worldHardestGame/src/dominio/levels/" + fileName
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
            if (numNivel > 1) {
                JOptionPane.showMessageDialog(this,
                        "¡Felicidades! Has completado todos los niveles disponibles.",
                        "Juego Terminado", JOptionPane.INFORMATION_MESSAGE);
                irAlMenu();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el archivo de nivel: " + fileName,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        try {
            // Cargar nivel y crear el juego
            dominio.Level level = dominio.Level.loadFromFile(levelFile.getPath());
            worldHG = new dominio.WorldHG("player");
            worldHG.loadLevel(level);

            // Apply the selected skin to the player
            if (worldHG.getPlayer1() != null) {
                dominio.PlayerState skinState;
                switch (selectedSkin) {
                    case "blue":
                        skinState = new dominio.BlueState();
                        break;
                    case "green":
                        skinState = new dominio.GreenState();
                        break;
                    default:
                        skinState = new dominio.RedState();
                        break;
                }
                worldHG.getPlayer1().setState(skinState);
            }

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
            newButton.addActionListener(e -> gui.irASeleccionSkin());
            saveButton.addActionListener(e -> gui.salvar());
            loadButton.addActionListener(e -> gui.abrir());
            cancelButton.addActionListener(e -> gui.exit());
        }
    }

    // ─── Skin Selection Panel ──────────────────────────────────────────────────

    class SkinSelectionPanel extends JPanel {
        private final WorldHardestGameGUI gui;
        private Image backgroundImage;
        private String hoveredSkin = null;

        public SkinSelectionPanel(WorldHardestGameGUI gui) {
            this.gui = gui;
            setLayout(new BorderLayout());

            // Load background image
            try {
                java.net.URL imgURL = getClass().getResource("/presentacion/images/fondo.png");
                if (imgURL != null) {
                    backgroundImage = javax.imageio.ImageIO.read(imgURL);
                } else {
                    java.io.File file = new java.io.File("src/presentacion/images/fondo.png");
                    java.io.File file2 = new java.io.File("worldHardestGame/src/presentacion/images/fondo.png");
                    if (file.exists())
                        backgroundImage = javax.imageio.ImageIO.read(file);
                    else if (file2.exists())
                        backgroundImage = javax.imageio.ImageIO.read(file2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Title
            JLabel title = new JLabel("Selecciona tu Skin", SwingConstants.CENTER);
            title.setFont(new Font("Arial Black", Font.BOLD, 28));
            title.setForeground(Color.WHITE);
            title.setOpaque(false);
            title.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 0, 10, 0));
            add(title, BorderLayout.NORTH);

            // Skin cards panel
            JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
            cardsPanel.setOpaque(false);
            cardsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 40, 20, 40));

            cardsPanel.add(createSkinCard("red", "Clásico", Color.RED,
                    "Velocidad: Normal", "Tamaño: Normal", "Muere al contacto"));
            cardsPanel.add(createSkinCard("blue", "Veloz", Color.BLUE,
                    "Velocidad: Alta", "Tamaño: Pequeño", "Muere al contacto"));
            cardsPanel.add(createSkinCard("green", "Resistente", new Color(0, 180, 0),
                    "Velocidad: Normal", "Tamaño: Grande", "Resiste 1 golpe"));

            add(cardsPanel, BorderLayout.CENTER);

            // Back button
            JPanel bottomPanel = new JPanel();
            bottomPanel.setOpaque(false);
            JButton backButton = new JButton("Volver");
            backButton.setFont(new Font("Arial Black", Font.BOLD, 16));
            backButton.setPreferredSize(new Dimension(120, 40));
            backButton.setBackground(new Color(120, 40, 40));
            backButton.setForeground(Color.WHITE);
            backButton.setFocusPainted(false);
            backButton.setBorderPainted(false);
            backButton.addActionListener(e -> gui.irAlMenu());
            bottomPanel.add(backButton);
            bottomPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 20, 0));
            add(bottomPanel, BorderLayout.SOUTH);
        }

        private JPanel createSkinCard(String skinId, String skinName, Color skinColor,
                String stat1, String stat2, String stat3) {
            JPanel card = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Card background with transparency
                    boolean isHovered = skinId.equals(hoveredSkin);
                    int alpha = isHovered ? 200 : 150;
                    g2.setColor(new Color(30, 30, 50, alpha));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                    // Border
                    if (isHovered) {
                        g2.setColor(skinColor);
                        g2.setStroke(new BasicStroke(3));
                    } else {
                        g2.setColor(new Color(100, 100, 130));
                        g2.setStroke(new BasicStroke(1));
                    }
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);

                    // Player preview square
                    int previewSize = 50;
                    int px = (getWidth() - previewSize) / 2;
                    int py = 30;
                    g2.setColor(skinColor);
                    g2.fillRect(px, py, previewSize, previewSize);
                    g2.setColor(skinColor.darker());
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRect(px, py, previewSize, previewSize);

                    // Skin name
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial Black", Font.BOLD, 18));
                    java.awt.FontMetrics fm = g2.getFontMetrics();
                    int textX = (getWidth() - fm.stringWidth(skinName)) / 2;
                    g2.drawString(skinName, textX, py + previewSize + 30);

                    // Stats
                    g2.setFont(new Font("Arial", Font.PLAIN, 13));
                    g2.setColor(new Color(200, 200, 220));
                    fm = g2.getFontMetrics();
                    String[] stats = { stat1, stat2, stat3 };
                    int startY = py + previewSize + 55;
                    for (int i = 0; i < stats.length; i++) {
                        int sx = (getWidth() - fm.stringWidth(stats[i])) / 2;
                        g2.drawString(stats[i], sx, startY + i * 20);
                    }
                }
            };
            card.setOpaque(false);
            card.setPreferredSize(new Dimension(180, 250));
            card.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hoveredSkin = skinId;
                    card.getParent().repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hoveredSkin = null;
                    card.getParent().repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    gui.setSelectedSkin(skinId);
                    gui.irAlTablero();
                }
            });

            return card;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(20, 22, 38));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    class GamePanel extends JPanel implements KeyListener {

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
                JOptionPane.showMessageDialog(this,
                        "¡Nivel " + gui.currentLevel + " Completo!\nMuertes: " + worldHG.getDeaths(),
                        "Victoria", JOptionPane.INFORMATION_MESSAGE);

                // Cargar el siguiente nivel automáticamente
                gui.irAlTablero(gui.currentLevel + 1);
            }
        }

        /**
         * Después de un tick de enemigos, si el jugador murió y reapareció lejos,
         * hace snap de la posición visual al nuevo punto de reaparición.
         */
        private void syncPlayerVisualAfterTick() {
            Player player = worldHG.getPlayer1();
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

            drawPlayer(g2); // jugador encima de todo con su posición real
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
        private void drawPlayer(Graphics2D g2) {
            dominio.Player player = worldHG.getPlayer1();
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
            Player player = worldHG.getPlayer1();
            if (player == null || worldHG.isTimeUp() || worldHG.isLevelComplete())
                return;

            String direction = null;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    direction = "UP";
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    direction = "DOWN";
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    direction = "LEFT";
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    direction = "RIGHT";
                    break;
            }

            if (direction == null)
                return;

            double prevX = player.getPosx();
            double prevY = player.getPosy();
            worldHG.movePlayerContinuous(player, direction);
            double newX = player.getPosx();
            double newY = player.getPosy();

            if (newX == prevX && newY == prevY)
                return; // movimiento bloqueado por pared

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

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

}
