package work.terrencemurray.infrastructure.ui;

import java.io.InputStream;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame implements KeyListener {
    // Settings
    private int width = 500;
    private int height = 500;
    private Font gamePausedFont;

    // Panels
    private InfoPanel infoPanel;
    private GamePanel gamePanel;

    public GameWindow() {
        // Window configuration
        this.setTitle("Catchy");
        this.setSize(this.width, this.height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        this.loadFont();

        infoPanel = new InfoPanel(gamePausedFont);
        this.add(infoPanel, BorderLayout.NORTH);

        gamePanel = new GamePanel();
        this.add(gamePanel, BorderLayout.CENTER);

        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocus();

        // Display game window
        this.setVisible(true);
        gamePanel.start();
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_SPACE) {
            infoPanel.getHealthBar().update(-10);
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    private void loadFont() {
        try {
            InputStream fontStream = getClass().getResourceAsStream("/fonts/game-paused.otf");
            this.gamePausedFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(24f);
        } catch (Exception e) {
            this.gamePausedFont = new Font("Arial", Font.PLAIN, 24); // fallback
        }
    }
}
