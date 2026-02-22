package work.terrencemurray.ui;

import javax.swing.JPanel;
import javax.swing.Timer;

import work.terrencemurray.items.CollectableItem;
import work.terrencemurray.items.CollectableItem.ItemType;
import work.terrencemurray.items.Item;
import work.terrencemurray.items.ItemPool;
import work.terrencemurray.items.decorators.ItemWithBoxCollider;
import work.terrencemurray.items.decorators.ItemWithGravity;
import work.terrencemurray.items.ItemFactory;
import work.terrencemurray.managers.ImageManager;
import work.terrencemurray.managers.SoundManager;
import work.terrencemurray.player.Player;

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

    // Game tuning
    private static final int ITEM_COUNT = 5;
    private static final int ITEM_SIZE = 40;
    private static final int ANVIL_SPEED = 4;
    private static final int BANANA_SPEED = 3;
    private static final int SCORE_PER_BANANA = 10;
    private static final int ANVIL_DAMAGE = 5;

    // Dependencies
    private final ItemPool<Item> pool;
    private final ItemFactory itemFactory = new ItemFactory();
    private final InfoPanel infoPanel;
    private final Player player;
    private final Random random = new Random();
    private final Timer gameTimer;
    private final SoundManager soundManager = SoundManager.getInstance();
    private final Image backgroundImage;

    // State
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

        gameTimer = new Timer(16, e -> {
            updateItems();
            repaint();
        });
    }

    // -- Lifecycle --

    public void start() {
        soundManager.playClip("gameStart", false);
        soundManager.playClip("background", true);
        spawnItems();
        gameTimer.start();
    }

    public void stop() {
        gameTimer.stop();
    }

    // -- Game Logic --

    private void spawnItems() {
        for (int i = 0; i < ITEM_COUNT; i++) {
            int x = random.nextInt(Math.max(getWidth() - 20, 1));
            int y = -(random.nextInt(200) + 50);
            pool.acquire(x, y);
        }
    }

    private void respawnItem(ItemWithGravity item) {
        CollectableItem collectable = unwrapCollectable(item);

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

            ItemWithBoxCollider itemCollider = unwrapCollider(item);
            if (itemCollider.intersects(player.getCollider())) {
                CollectableItem collectable = (CollectableItem) itemCollider.getWrapped();

                if (collectable.getType() == ItemType.BANANA) {
                    collectBanana();
                } else if (collectable.getType() == ItemType.ANVIL) {
                    hitByAnvil();
                    if (gameOver) return;
                }

                respawnItem(item);
                continue;
            }

            if (item.getCurrentPosition().getY() > getHeight()) {
                respawnItem(item);
            }
        }

        this.player.update(getWidth(), getHeight());
    }

    private void collectBanana() {
        score += SCORE_PER_BANANA;
        infoPanel.setScore(score);
        soundManager.playClip("bananaCollect", false);
    }

    private void hitByAnvil() {
        infoPanel.getHealthBar().update(-ANVIL_DAMAGE);
        soundManager.playClip("anvilHit", false);

        if (infoPanel.getHealthBar().isDead()) {
            gameOver = true;
            soundManager.stopClip("background");
            soundManager.playClip("gameOver", false);
        }
    }

    // -- Rendering --

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

        for (Item item : pool.getActive()) {
            item.render(g2);
        }

        this.player.render(g2);

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

    // -- Helpers --

    private ItemWithBoxCollider unwrapCollider(ItemWithGravity item) {
        return (ItemWithBoxCollider) item.getWrapped();
    }

    private CollectableItem unwrapCollectable(ItemWithGravity item) {
        return (CollectableItem) unwrapCollider(item).getWrapped();
    }

    public void setDebugging(boolean flag) {
        for (Item item : pool.getActive()) {
            unwrapCollider((ItemWithGravity) item).setDebugging(flag);
        }
        player.getCollider().setDebugging(flag);
    }

    public Player getPlayer() {
        return this.player;
    }
}
