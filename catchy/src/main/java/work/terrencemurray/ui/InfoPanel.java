package work.terrencemurray.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class InfoPanel extends JPanel {

    private HealthBar healthBar;
    private JLabel currentScoreValueLabel;

    public InfoPanel(Font font) {
        this.setLayout(new BorderLayout(10, 0));
        this.setBackground(new Color(25, 25, 30));
        this.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        buildHealthBar();
        buildScoreSection(font);
    }

    private void buildHealthBar() {
        healthBar = new HealthBar(150, 18, 100);
        JPanel healthContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        healthContainer.setOpaque(false);

        JLabel hpLabel = new JLabel("HP");
        hpLabel.setFont(new Font("Arial", Font.BOLD, 13));
        hpLabel.setForeground(new Color(200, 60, 60));

        healthContainer.add(hpLabel);
        healthContainer.add(healthBar);
        this.add(healthContainer, BorderLayout.WEST);
    }

    private void buildScoreSection(Font font) {
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2));
        scorePanel.setOpaque(false);

        JLabel scoreLabel = new JLabel("SCORE");
        scoreLabel.setFont(font.deriveFont(14f));
        scoreLabel.setForeground(new Color(120, 120, 130));

        currentScoreValueLabel = new JLabel("0");
        currentScoreValueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        currentScoreValueLabel.setForeground(new Color(255, 220, 60));

        scorePanel.add(scoreLabel);
        scorePanel.add(currentScoreValueLabel);
        this.add(scorePanel, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Subtle gradient background
        GradientPaint bg = new GradientPaint(0, 0, new Color(30, 30, 35), 0, getHeight(), new Color(20, 20, 25));
        g2.setPaint(bg);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Bottom accent line
        GradientPaint accent = new GradientPaint(
            0, getHeight() - 2, new Color(80, 140, 220, 0),
            getWidth() / 2, getHeight() - 2, new Color(80, 140, 220, 120));
        g2.setPaint(accent);
        g2.fillRect(0, getHeight() - 2, getWidth(), 2);
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public void setScore(int score) {
        currentScoreValueLabel.setText(String.valueOf(score));
    }
}
