package presentacion;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WorldHardestGameGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel startPanel;

    public WorldHardestGameGUI() {
        prepareElements();
        prepareActions();
        
    }

    private void prepareElements() {
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
        
        prepareElementsStartPanel();
        
        mainPanel.add(startPanel, "Start");
        add(mainPanel);
    }

    private void prepareElementsStartPanel() {
        startPanel = new JPanel() {
            private Image backgroundImage;
            
            { 
                // Intentamos cargar la imagen como recurso del proyecto
                try {
                    // La ruta empieza con "/" desde la raíz de src
                    java.net.URL imgURL = getClass().getResource("/presentacion/images/fondo.png");
                    if (imgURL != null) {
                        backgroundImage = new ImageIcon(imgURL).getImage();
                    } else {
                        // Si getResource falla, intentamos la ruta física relativa
                        System.out.println("No se encontró como recurso, intentando ruta física...");
                        java.io.File file = new java.io.File("src/presentacion/images/fondo.png");
                        if (file.exists()) {
                            backgroundImage = new ImageIcon(file.getAbsolutePath()).getImage();
                        } else {
                            System.err.println("¡Error! No se encuentra 'fondo.png' en ninguna ruta.");
                        }
                    }
                } catch (Exception e) {
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
        
        // Título
        JLabel titleLabel = new JLabel("World's Hardest Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE); // Texto blanco para que resalte
        startPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Botón Start
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(150, 50));
        
        // Panel para el botón (en la parte inferior)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        
        startPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void prepareActions() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }

    private void exit() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Desea cerrar la aplicación EasySokoban?",
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
}
