package work.terrencemurray.infrastructure.ui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

public class InfoPanel extends JPanel {

    private HealthBar healthBar;
    private JLabel currentScoreLabel;
    private JLabel currentScoreValueLabel;

    public InfoPanel(Font font) {
        this.setLayout(new BorderLayout(10, 0));
        this.setBackground(new Color(30, 30, 30));
        this.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        buildHealthBar();
        buildScoreSection(font);
    }

    private void buildHealthBar() {
        healthBar = new HealthBar(180, 20, 50);
        JPanel healthContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        healthContainer.setOpaque(false);
        healthContainer.add(healthBar);
        this.add(healthContainer, BorderLayout.WEST);
    }

    private void buildScoreSection(Font font) {
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.X_AXIS));
        scorePanel.setOpaque(false);

        currentScoreLabel = new JLabel("SCORE ");
        currentScoreLabel.setFont(font.deriveFont(16f));
        currentScoreLabel.setForeground(new Color(150, 150, 150));

        currentScoreValueLabel = new JLabel("0");
        currentScoreValueLabel.setFont(font.deriveFont(24f));
        currentScoreValueLabel.setForeground(new Color(255, 215, 0));

        scorePanel.add(currentScoreLabel);
        scorePanel.add(currentScoreValueLabel);
        this.add(scorePanel, BorderLayout.EAST);
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public void setScore(int score) {
        currentScoreValueLabel.setText(String.valueOf(score));
    }
}
