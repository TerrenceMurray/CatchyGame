package work.terrencemurray.ui;

import javax.swing.JPanel;

import work.terrencemurray.managers.ImageManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

public class GameOverOverlay extends JPanel {

    private final Image bananaIcon;

    private int score = 0;
    private boolean visible = false;

    public GameOverOverlay() {
        this.setOpaque(false);
        this.bananaIcon = ImageManager.loadImage("/images/fruit_banana.png");
    }

    public void show(int score) {
        this.score = score;
        this.visible = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!visible) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dark overlay
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, getWidth(), getHeight());

        int cy = getHeight() / 2;

        // "GAME OVER" title
        Font titleFont = new Font("Arial", Font.BOLD, 48);
        g2.setFont(titleFont);
        FontMetrics fm = g2.getFontMetrics();
        String title = "GAME OVER";
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        int titleY = cy - 20;

        // Drop shadow
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(title, titleX + 3, titleY + 3);

        // Main title
        g2.setColor(new Color(230, 50, 50));
        g2.drawString(title, titleX, titleY);

        // Score with banana icon
        Font scoreFont = new Font("Arial", Font.BOLD, 22);
        g2.setFont(scoreFont);
        fm = g2.getFontMetrics();
        String scoreText = "x " + score;
        int iconSize = 24;
        int totalWidth = iconSize + 6 + fm.stringWidth(scoreText);
        int scoreX = (getWidth() - totalWidth) / 2;

        if (bananaIcon != null) {
            g2.drawImage(bananaIcon, scoreX, cy + 10, iconSize, iconSize, null);
        }

        int textX = scoreX + iconSize + 6;
        int textY = cy + 10 + fm.getAscent() + (iconSize - fm.getHeight()) / 2;

        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(scoreText, textX + 2, textY + 2);
        g2.setColor(new Color(255, 220, 60));
        g2.drawString(scoreText, textX, textY);
    }
}
