package work.terrencemurray.ui;

import java.io.InputStream;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame implements KeyListener {

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    private Font gameFont;
    private boolean debugging = false;

    private InfoPanel infoPanel;
    private GamePanel gamePanel;

    public GameWindow() {
        this.setTitle("Catchy");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        loadFont();

        infoPanel = new InfoPanel(gameFont);
        this.add(infoPanel, BorderLayout.NORTH);

        gamePanel = new GamePanel(infoPanel);
        this.add(gamePanel, BorderLayout.CENTER);

        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocus();

        this.setVisible(true);
        gamePanel.start();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            debugging = !debugging;
            gamePanel.setDebugging(debugging);
        }

        gamePanel.getPlayer().keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        gamePanel.getPlayer().keyReleased(e);
    }

    public void keyTyped(KeyEvent e) {}

    private void loadFont() {
        try {
            InputStream fontStream = getClass().getResourceAsStream("/fonts/game-paused.otf");
            gameFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(24f);
        } catch (Exception e) {
            gameFont = new Font("Arial", Font.PLAIN, 24);
        }
    }
}
