package work.terrencemurray.ui;

import javax.swing.JPanel;
import javax.swing.Timer;

import work.terrencemurray.items.decorators.ItemWithCollectable.ItemType;
import work.terrencemurray.managers.ImageManager;
import work.terrencemurray.managers.ItemManager;
import work.terrencemurray.managers.SoundManager;
import work.terrencemurray.player.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Vector;

public class GamePanel extends JPanel {

    private static final int SCORE_PER_BANANA = 10;
    private static final int ANVIL_DAMAGE = 5;

    private final ItemManager itemManager;
    private final InfoPanel infoPanel;
    private final Player player;
    private final Timer gameTimer;
    private final SoundManager soundManager = SoundManager.getInstance();
    private final Image backgroundImage;

    private int score = 0;
    private boolean gameOver = false;

    public GamePanel(InfoPanel infoPanel) {
        this.infoPanel = infoPanel;
        this.setBackground(new Color(50, 50, 50));

        backgroundImage = ImageManager.loadImage("/images/shreya-das-ntcc-final-year-cartoon-bg.png");
        itemManager = new ItemManager();
        player = new Player();

        gameTimer = new Timer(16, e -> {
            update();
            repaint();
        });
    }

    public void start() {
        soundManager.playClip("gameStart", false);
        soundManager.playClip("background", true);
        itemManager.spawnItems(getWidth());
        gameTimer.start();
    }

    public void stop() {
        gameTimer.stop();
        gameOver = true;
        soundManager.stopClip("background");
        soundManager.playClip("gameOver", false);
    }

    private void update() {
        Vector<ItemType> collisions = itemManager.update(
            player.getCollider(), getWidth(), getHeight());

        for (ItemType type : collisions) {
            if (type == ItemType.BANANA) {
                score += SCORE_PER_BANANA;
                infoPanel.setScore(score);
                soundManager.playClip("bananaCollect", false);
            } else if (type == ItemType.ANVIL) {
                infoPanel.getHealthBar().update(-ANVIL_DAMAGE);
                soundManager.playClip("anvilHit", false);
                if (infoPanel.getHealthBar().isDead()) {
                    stop();
                    return;
                }
            }
        }

        player.update(getWidth(), getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        itemManager.render(g2);
        player.render(g2);

        if (gameOver) {
            renderGameOverOverlay(g2);
        }
    }

    private void renderGameOverOverlay(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, getWidth(), getHeight());

        int centerY = getHeight() / 2;

        Font titleFont = new Font("Arial", Font.BOLD, 42);
        g2.setFont(titleFont);
        FontMetrics fm = g2.getFontMetrics();
        String title = "GAME OVER";
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;

        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(title, titleX + 3, centerY - 10 + 3);
        g2.setColor(new Color(220, 50, 50));
        g2.drawString(title, titleX, centerY - 10);

        Font scoreFont = new Font("Arial", Font.BOLD, 20);
        g2.setFont(scoreFont);
        fm = g2.getFontMetrics();
        String scoreText = "Final Score: " + score;
        int scoreX = (getWidth() - fm.stringWidth(scoreText)) / 2;
        g2.setColor(new Color(255, 215, 0));
        g2.drawString(scoreText, scoreX, centerY + 30);
    }

    public void setDebugging(boolean flag) {
        itemManager.setDebugging(flag);
        player.getCollider().setDebugging(flag);
    }

    public Player getPlayer() {
        return player;
    }
}
