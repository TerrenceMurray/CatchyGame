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

public class InfoPanel extends JPanel {

    private static final int HUD_MARGIN = 12;
    private static final int BANANA_ICON_SIZE = 20;

    private final HealthBar healthBar;
    private final Image bananaIcon;

    private int score = 0;

    public InfoPanel() {
        this.setOpaque(false);
        this.setLayout(null);

        this.bananaIcon = ImageManager.loadImage("/images/fruit_banana.png");

        this.healthBar = new HealthBar(120, 14, 100);
        this.healthBar.setBounds(HUD_MARGIN, HUD_MARGIN, 120, 14);
        this.add(healthBar);
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public void setScore(int score) {
        this.score = score;
        repaint();
    }

    public int getScore() {
        return score;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        renderBananaCount(g2);
    }

    private void renderBananaCount(Graphics2D g2) {
        Font countFont = new Font("Arial", Font.BOLD, 16);
        g2.setFont(countFont);
        FontMetrics fm = g2.getFontMetrics();
        String text = "x " + score;
        int textWidth = fm.stringWidth(text);

        int totalWidth = BANANA_ICON_SIZE + 4 + textWidth;
        int x = getWidth() - HUD_MARGIN - totalWidth;
        int y = HUD_MARGIN;

        // Banana icon
        if (bananaIcon != null) {
            g2.drawImage(bananaIcon, x, y, BANANA_ICON_SIZE, BANANA_ICON_SIZE, null);
        }

        // Count text with drop shadow
        int textX = x + BANANA_ICON_SIZE + 4;
        int textY = y + fm.getAscent() + (BANANA_ICON_SIZE - fm.getHeight()) / 2;

        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(text, textX + 1, textY + 1);
        g2.setColor(Color.WHITE);
        g2.drawString(text, textX, textY);
    }
}
