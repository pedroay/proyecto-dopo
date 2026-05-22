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
import dominio.WorldHGException;

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
    private GameModeSelectionPanel modePanel;
    private GamePanel gamePanel;
    private dominio.WorldHG worldHG;
    private JFileChooser fileChooser = new JFileChooser(".");
    private int currentLevel = 1;
    private String selectedSkin = "red";
    private String selectedMode = "solo";
    private java.io.File archivoActual;

    public WorldHardestGameGUI() {
        prepareElements();
        prepareActions();

    }
    
    public int getCurrentLevel() {
    	return currentLevel;
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
        modePanel = new GameModeSelectionPanel(this);

        prepareElementsStartPanel();

        mainPanel.add(startPanel, "Start");
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(skinPanel, "SKIN");
        mainPanel.add(modePanel, "MODE");
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

    public void irAlMenu() {
        cardLayout.show(mainPanel, "MENU");
    }

    public void irASeleccionSkin() {
        cardLayout.show(mainPanel, "SKIN");
    }

    public void irASeleccionModo() {
        cardLayout.show(mainPanel, "MODE");
    }

    public void setSelectedSkin(String skin) {
        this.selectedSkin = skin;
    }

    public void setSelectedMode(String mode) {
        this.selectedMode = mode;
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
            worldHG = new dominio.WorldHG(this.selectedMode);
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
                worldHG.getPlayer1().setOriginalState(skinState);
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
            try {
            	worldHG.saveAs(archivo);
            	archivoActual = archivo;
                JOptionPane.showMessageDialog(this,
                    "Juego guardado correctamente en:\n" + archivo.getName(),
                    "Guardado Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
            	
            }
            catch(dominio.WorldHGException e){}
        }
    }

    public void abrir() {
        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = fileChooser.getSelectedFile();
            try {
                worldHG = WorldHG.open(archivo);
                archivoActual = archivo;
                
                if (worldHG.getLevel() != null) {
                    this.currentLevel = worldHG.getLevel().getLevelNumber();
                }
                if (gamePanel != null) {
                    gamePanel.stopTimer();
                    mainPanel.remove(gamePanel);
                }
                gamePanel = new GamePanel(worldHG, this);
                mainPanel.add(gamePanel, "GAME");
                cardLayout.show(mainPanel, "GAME");

                // Asegurarse de que el panel reciba el foco para el teclado
                gamePanel.requestFocusInWindow();
                pack(); // Ajustar tamaño de la ventana al tablero
                setLocationRelativeTo(null);

                JOptionPane.showMessageDialog(this,
                    "Juego cargado correctamente desde:\n" + archivo.getName(),
                    "Apertura Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (dominio.WorldHGException e) {
                JOptionPane.showMessageDialog(this,
                    e.getMessage(), "Error al abrir", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

    