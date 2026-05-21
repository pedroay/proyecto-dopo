package presentacion;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
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

public class MenuWindow extends JPanel {
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

