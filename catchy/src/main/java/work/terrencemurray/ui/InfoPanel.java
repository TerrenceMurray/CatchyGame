package work.terrencemurray.ui;

import javax.swing.JPanel;

import work.terrencemurray.managers.ImageManager;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

public class InfoPanel extends JPanel {

    private static final int MAX_HEALTH = 100;
    private static final int HUD_MARGIN = 12;
    private static final int BAR_WIDTH = 120;
    private static final int BAR_HEIGHT = 14;
    private static final int BANANA_ICON_SIZE = 20;

    private final Image bananaIcon;

    private int score = 0;
    private int health = MAX_HEALTH;

    public InfoPanel() {
        this.setOpaque(false);
        this.bananaIcon = ImageManager.loadImage("/images/fruit_banana.png");
    }

    public void setScore(int score) {
        this.score = score;
        repaint();
    }

    public void updateHealth(int delta) {
        this.health = Math.max(0, Math.min(MAX_HEALTH, health + delta));
        repaint();
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getScore() {
        return score;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        renderHealthBar(g2);
        renderBananaCount(g2);
    }

    private void renderHealthBar(Graphics2D g2) {
        int x = HUD_MARGIN;
        int y = HUD_MARGIN;

        // Background
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(x, y, BAR_WIDTH, BAR_HEIGHT, 8, 8);

        // Fill
        double pct = (double) health / MAX_HEALTH;
        int fillWidth = (int) ((BAR_WIDTH - 2) * pct);
        if (fillWidth > 0) {
            Color top, bottom;
            if (pct > 0.5) {
                top = new Color(70, 210, 70);
                bottom = new Color(40, 160, 40);
            } else if (pct > 0.25) {
                top = new Color(255, 190, 30);
                bottom = new Color(210, 140, 10);
            } else {
                top = new Color(230, 55, 55);
                bottom = new Color(170, 30, 30);
            }

            GradientPaint fill = new GradientPaint(0, y + 1, top, 0, y + BAR_HEIGHT - 1, bottom);
            g2.setPaint(fill);
            g2.fillRoundRect(x + 1, y + 1, fillWidth, BAR_HEIGHT - 2, 6, 6);

            // Glass shine
            g2.setColor(new Color(255, 255, 255, 50));
            g2.fillRoundRect(x + 2, y + 2, fillWidth - 2, (BAR_HEIGHT - 4) / 2, 4, 4);
        }

        // Border
        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, BAR_WIDTH - 1, BAR_HEIGHT - 1, 8, 8);
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
