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

public class WorldHardestGameGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel startPanel;
    private MenuWindow menuPanel;
    private JFileChooser fileChooser = new JFileChooser(".");

    public WorldHardestGameGUI() {
        prepareElements();
        prepareActions();
        
    }

    private final void prepareElements() {
        // cosas para iniciar la aplicacion
        setTitle("The World’s Hardest Game");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen.getWidth()/2);
        int height = (int) (screen.getHeight()/2);
        setSize(width,height);
        setLocationRelativeTo(null);

        //Pantallas
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
        
        //color boton
        startButton.setBackground(new Color(26, 67, 117)); // Un azul brillante, por ejemplo
        startButton.setForeground(Color.WHITE);            // Color de la letra
        startButton.setFocusPainted(false);               // Quita el cuadrito punteado al hacer clic
        startButton.setBorderPainted(false);              // Quita el borde por defecto
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
            JOptionPane.QUESTION_MESSAGE
        );

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
        JOptionPane.showMessageDialog(this, "Función ir al tablero en construcción.");
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

    class MenuWindow extends JPanel{
        protected WorldHardestGameGUI gui;
        protected JButton newButton;
        protected JButton saveButton;
        protected JButton cancelButton;
        protected JButton loadButton;
        private Image backgroundImage;
        
        public MenuWindow(WorldHardestGameGUI app){
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
        
        public final void prepareElementsMenuWindow(){
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
            
            JButton[] buttons = {newButton, saveButton, loadButton, cancelButton};
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
}
