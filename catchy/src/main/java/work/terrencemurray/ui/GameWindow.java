package work.terrencemurray.ui;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame implements KeyListener {

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    private boolean debugging = false;

    private InfoPanel infoPanel;
    private GameOverOverlay gameOverOverlay;
    private GamePanel gamePanel;

    public GameWindow() {
        this.setTitle("Catchy");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JLayeredPane layeredPane = new JLayeredPane();

        infoPanel = new InfoPanel();
        gameOverOverlay = new GameOverOverlay();
        gamePanel = new GamePanel(infoPanel, gameOverOverlay);

        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(infoPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(gameOverOverlay, JLayeredPane.MODAL_LAYER);

        // Keep all panels sized to fill the window
        layeredPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Dimension size = layeredPane.getSize();
                gamePanel.setBounds(0, 0, size.width, size.height);
                infoPanel.setBounds(0, 0, size.width, size.height);
                gameOverOverlay.setBounds(0, 0, size.width, size.height);
            }
        });

        this.setContentPane(layeredPane);

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
}
