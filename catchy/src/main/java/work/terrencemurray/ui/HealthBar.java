package work.terrencemurray.ui;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class HealthBar extends JPanel {

    // Colors
    private static final Color FILL_COLOR = new Color(220, 40, 40);
    private static final Color FILL_LOW_COLOR = new Color(220, 140, 30);
    private static final Color BACKGROUND_COLOR = new Color(60, 60, 60);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);

    // State variables
    private int barWidth;
    private int barHeight;
    private int maxHealth;
    private int currentHealth;

    public HealthBar(int barWidth, int barHeight, int maxHealth) {
        this.barWidth = barWidth;
        this.barHeight = barHeight;
        this.maxHealth = maxHealth;
        this.currentHealth = this.maxHealth;
        this.setPreferredSize(new Dimension(barWidth, barHeight));
        this.setOpaque(false);
    }


    private int getFillWidth() {
        return (int)((barWidth - 2) * ((double) currentHealth / maxHealth));
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public void update(int interval) {
        if (interval + currentHealth > this.maxHealth) {
            this.currentHealth = this.maxHealth;
            return;
        }

        if (interval + currentHealth < 0) {
            this.currentHealth = 0;
            return;
        }

        this.currentHealth += interval;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRoundRect(0, 0, barWidth, barHeight, 6, 6);

        // Health fill
        double healthPercent = (double) currentHealth / maxHealth;
        g2.setColor(healthPercent > 0.3 ? FILL_COLOR : FILL_LOW_COLOR);
        g2.fillRoundRect(1, 1, getFillWidth(), barHeight - 2, 5, 5);

        // Border
        g2.setColor(BORDER_COLOR);
        g2.drawRoundRect(0, 0, barWidth - 1, barHeight - 1, 6, 6);
    }
}
