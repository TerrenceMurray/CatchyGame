package work.terrencemurray.ui;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class HealthBar extends JPanel {

    private static final Color FILL_HIGH = new Color(50, 205, 50);
    private static final Color FILL_HIGH_DARK = new Color(30, 140, 30);
    private static final Color FILL_MID = new Color(255, 180, 0);
    private static final Color FILL_MID_DARK = new Color(200, 120, 0);
    private static final Color FILL_LOW = new Color(220, 40, 40);
    private static final Color FILL_LOW_DARK = new Color(150, 20, 20);
    private static final Color BACKGROUND_COLOR = new Color(40, 40, 40);
    private static final Color BORDER_COLOR = new Color(80, 80, 80);

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

        // Inset background
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRoundRect(0, 0, barWidth, barHeight, 8, 8);

        // Health fill with gradient
        int fillWidth = getFillWidth();
        if (fillWidth > 0) {
            double pct = (double) currentHealth / maxHealth;
            Color top, bottom;
            if (pct > 0.5) {
                top = FILL_HIGH;
                bottom = FILL_HIGH_DARK;
            } else if (pct > 0.25) {
                top = FILL_MID;
                bottom = FILL_MID_DARK;
            } else {
                top = FILL_LOW;
                bottom = FILL_LOW_DARK;
            }

            GradientPaint gradient = new GradientPaint(0, 1, top, 0, barHeight - 1, bottom);
            g2.setPaint(gradient);
            g2.fillRoundRect(1, 1, fillWidth, barHeight - 2, 6, 6);

            // Glass shine on top half
            g2.setColor(new Color(255, 255, 255, 60));
            g2.fillRoundRect(2, 2, fillWidth - 2, (barHeight - 4) / 2, 4, 4);
        }

        // Border
        g2.setColor(BORDER_COLOR);
        g2.drawRoundRect(0, 0, barWidth - 1, barHeight - 1, 8, 8);
    }
}
