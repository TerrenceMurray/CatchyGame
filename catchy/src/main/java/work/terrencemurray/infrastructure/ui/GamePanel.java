package work.terrencemurray.infrastructure.ui;

import javax.swing.JPanel;
import javax.swing.Timer;

import work.terrencemurray.infrastructure.items.FruitItem;
import work.terrencemurray.infrastructure.items.ItemPool;
import work.terrencemurray.infrastructure.items.decorators.ItemWithGravity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

public class GamePanel extends JPanel {
    private static final int ITEM_COUNT = 5;
    private static final Color[] FRUIT_COLORS = {
        new Color(220, 40, 40),   // red
        new Color(255, 165, 0),   // orange
        new Color(255, 220, 0),   // yellow
        new Color(80, 200, 80),   // green
        new Color(140, 80, 200)   // purple
    };

    private final ItemPool<ItemWithGravity> pool;
    private final Random random = new Random();
    private final Timer gameTimer;

    public GamePanel() {
        this.setBackground(new Color(50, 50, 50));

        pool = new ItemPool<>(() -> {
            Color color = FRUIT_COLORS[random.nextInt(FRUIT_COLORS.length)];
            FruitItem fruit = new FruitItem(0, 0, color);
            return new ItemWithGravity(fruit, 2 + random.nextInt(3));
        }, ITEM_COUNT);

        gameTimer = new Timer(16, e -> {
            updateItems();
            repaint();
        });
    }

    public void start() {
        spawnItems();
        gameTimer.start();
    }

    public void stop() {
        gameTimer.stop();
    }

    private void spawnItems() {
        for (int i = 0; i < ITEM_COUNT; i++) {
            int x = random.nextInt(Math.max(getWidth() - 20, 1));
            int y = -random.nextInt(200);
            pool.acquire(x, y);
        }
    }

    private void updateItems() {
        Vector<ItemWithGravity> active = pool.getActive();
        for (int i = active.size() - 1; i >= 0; i--) {
            ItemWithGravity item = active.get(i);
            item.fall();

            // Respawn at top if off screen
            if (item.getCurrentPosition().getY() > getHeight()) {
                int x = random.nextInt(Math.max(getWidth() - 20, 1));
                int y = -random.nextInt(60);
                item.reset(x, y);
                item.setFallSpeed(2 + random.nextInt(3));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (ItemWithGravity item : pool.getActive()) {
            item.render(g);
        }
    }
}
