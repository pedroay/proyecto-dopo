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

public class SkinSelectionPanel extends JPanel {
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
                gui.irASeleccionModo();
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

