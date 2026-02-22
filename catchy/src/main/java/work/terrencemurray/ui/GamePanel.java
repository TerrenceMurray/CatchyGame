package work.terrencemurray.ui;

import javax.swing.JPanel;
import javax.swing.Timer;

import work.terrencemurray.items.decorators.ItemWithCollectable.ItemType;
import work.terrencemurray.managers.ImageManager;
import work.terrencemurray.managers.ItemManager;
import work.terrencemurray.managers.SoundManager;
import work.terrencemurray.player.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Vector;

public class GamePanel extends JPanel {

    private static final int SCORE_PER_BANANA = 1;
    private static final int BAT_DAMAGE = 5;

    private final ItemManager itemManager;
    private final InfoPanel infoPanel;
    private final GameOverOverlay gameOverOverlay;
    private final Player player;
    private final Timer gameTimer;
    private final SoundManager soundManager = SoundManager.getInstance();
    private final Image backgroundImage;

    private int score = 0;

    public GamePanel(InfoPanel infoPanel, GameOverOverlay gameOverOverlay) {
        this.infoPanel = infoPanel;
        this.gameOverOverlay = gameOverOverlay;
        this.setBackground(new Color(50, 50, 50));

        backgroundImage = ImageManager.loadImage("/images/shreya-das-ntcc-final-year-cartoon-bg.png");
        itemManager = new ItemManager();
        player = new Player();

        gameTimer = new Timer(16, e -> {
            update();
            repaint();
            infoPanel.repaint();
        });
    }

    public void start() {
        soundManager.playClip("gameStart", false);
        soundManager.playClip("background", true);
        itemManager.spawnItems(getWidth(), getHeight());
        gameTimer.start();
    }

    public void stop() {
        gameTimer.stop();
        soundManager.stopClip("background");
        soundManager.playClip("gameOver", false);
        gameOverOverlay.show(score);
    }

    private void update() {
        Vector<ItemType> collisions = itemManager.update(
            player.getCollider(), getWidth(), getHeight()
        );

        for (ItemType type : collisions) {
            player.onCatch();
            if (type == ItemType.BANANA) {
                score += SCORE_PER_BANANA;
                infoPanel.setScore(score);
                soundManager.playClip("bananaCollect", false);
            } else if (type == ItemType.BAT) {
                infoPanel.updateHealth(-BAT_DAMAGE);
                soundManager.playClip("batHit", false);
                if (infoPanel.isDead()) {
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
    }

    public void setDebugging(boolean flag) {
        itemManager.setDebugging(flag);
        player.getCollider().setDebugging(flag);
    }

    public Player getPlayer() {
        return player;
    }
}
