package work.terrencemurray.infrastructure.ui;

import javax.swing.JPanel;
import javax.swing.Timer;

import work.terrencemurray.infrastructure.items.CollectableItem;
import work.terrencemurray.infrastructure.items.CollectableItem.ItemType;
import work.terrencemurray.infrastructure.items.Item;
import work.terrencemurray.infrastructure.items.ItemPool;
import work.terrencemurray.infrastructure.items.decorators.ItemWithBoxCollider;
import work.terrencemurray.infrastructure.items.decorators.ItemWithGravity;
import work.terrencemurray.infrastructure.items.factory.ItemFactory;
import work.terrencemurray.infrastructure.managers.ImageManager;
import work.terrencemurray.infrastructure.managers.SoundManager;
import work.terrencemurray.infrastructure.player.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Random;
import java.util.Vector;

public class GamePanel extends JPanel {
    private static final int ITEM_COUNT = 5;
    private static final int ITEM_SIZE = 40;
    private static final int ANVIL_SPEED = 4;
    private static final int BANANA_SPEED = 3;

    private final ItemPool<Item> pool;
    private final ItemFactory itemFactory = new ItemFactory();
    private final InfoPanel infoPanel;
    private final Player player;
    private final Random random = new Random();
    private final Timer gameTimer;
    private final SoundManager soundManager = SoundManager.getInstance();
    private Image backgroundImage;
    private int score = 0;
    private boolean gameOver = false;

    public GamePanel(InfoPanel infoPanel) {
        this.infoPanel = infoPanel;
        this.setBackground(new Color(50, 50, 50));

        backgroundImage = ImageManager.loadImage("/images/shreya-das-ntcc-final-year-cartoon-bg.png");

        pool = new ItemPool<>(() -> {
            ItemType type = random.nextBoolean() ? ItemType.BANANA : ItemType.ANVIL;
            return itemFactory
                .create(new CollectableItem(0, 0, type))
                .addBoxCollider(ITEM_SIZE, ITEM_SIZE)
                .addGravity(type == ItemType.ANVIL ? ANVIL_SPEED : BANANA_SPEED)
                .collect();
        }, ITEM_COUNT);

        this.player = new Player();
        this.addKeyListener(this.player);

        gameTimer = new Timer(16, e -> {
            updateItems();
            repaint();
        });
    }

    public void start() {
        soundManager.playClip("gameStart", false);
        soundManager.playClip("background", true);
        spawnItems();
        gameTimer.start();
    }

    public void stop() {
        gameTimer.stop();
    }

    private void spawnItems() {
        for (int i = 0; i < ITEM_COUNT; i++) {
            int x = random.nextInt(Math.max(getWidth() - 20, 1));
            int y = -(random.nextInt(200) + 50);
            pool.acquire(x, y);
        }
    }

    private void respawnItem(ItemWithGravity item) {
        ItemWithBoxCollider itemCollider = (ItemWithBoxCollider) item.getWrapped();
        CollectableItem collectable = (CollectableItem) itemCollider.getWrapped();

        ItemType type = random.nextBoolean() ? ItemType.BANANA : ItemType.ANVIL;
        collectable.setType(type);

        int x = random.nextInt(Math.max(getWidth() - 20, 1));
        int y = -(random.nextInt(300) + 50);
        item.reset(x, y);
        item.setFallSpeed(type == ItemType.ANVIL ? ANVIL_SPEED : BANANA_SPEED);
    }

    private void updateItems() {
        if (gameOver) return;

        Vector<Item> active = pool.getActive();
        for (int i = active.size() - 1; i >= 0; i--) {
            ItemWithGravity item = (ItemWithGravity) active.get(i);
            item.fall();

            // Check collision with player
            ItemWithBoxCollider itemCollider = (ItemWithBoxCollider) item.getWrapped();
            if (itemCollider.intersects(player.getCollider())) {
                CollectableItem collectable = (CollectableItem) itemCollider.getWrapped();
                if (collectable.getType() == ItemType.BANANA) {
                    score += 10;
                    infoPanel.setScore(score);
                    soundManager.playClip("bananaCollect", false);
                } else if (collectable.getType() == ItemType.ANVIL) {
                    infoPanel.getHealthBar().update(-5);
                    soundManager.playClip("anvilHit", false);
                    if (infoPanel.getHealthBar().isDead()) {
                        gameOver = true;
                        soundManager.stopClip("background");
                        soundManager.playClip("gameOver", false);
                        return;
                    }
                }

                respawnItem(item);
                continue;
            }

            // Respawn at top if off screen
            if (item.getCurrentPosition().getY() > getHeight()) {
                respawnItem(item);
            }
        }

        this.player.update(getWidth(), getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Display background
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        // Display falling items
        for (Item item : pool.getActive()) {
            item.render(g2);
        }

        // Display player
        this.player.render(g2);

        // Game over overlay
        if (gameOver) {
            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRect(0, 0, getWidth(), getHeight());

            int centerY = getHeight() / 2;

            // "GAME OVER" with drop shadow
            Font titleFont = new Font("Arial", Font.BOLD, 42);
            g2.setFont(titleFont);
            FontMetrics fm = g2.getFontMetrics();
            String title = "GAME OVER";
            int titleX = (getWidth() - fm.stringWidth(title)) / 2;

            g2.setColor(new Color(0, 0, 0, 120));
            g2.drawString(title, titleX + 3, centerY - 10 + 3);
            g2.setColor(new Color(220, 50, 50));
            g2.drawString(title, titleX, centerY - 10);

            // Final score
            Font scoreFont = new Font("Arial", Font.BOLD, 20);
            g2.setFont(scoreFont);
            fm = g2.getFontMetrics();
            String scoreText = "Final Score: " + score;
            int scoreX = (getWidth() - fm.stringWidth(scoreText)) / 2;
            g2.setColor(new Color(255, 215, 0));
            g2.drawString(scoreText, scoreX, centerY + 30);
        }
    }

    public void setDebugging(boolean flag) {
        for (Item item : pool.getActive()) {
            ItemWithGravity gravity = (ItemWithGravity) item;
            ItemWithBoxCollider collider = (ItemWithBoxCollider) gravity.getWrapped();
            collider.setDebugging(flag);
        }

        ItemWithBoxCollider collider = player.getCollider();
        collider.setDebugging(flag);
    }

    public Player getPlayer() {
        return this.player;
    }
}
