package work.terrencemurray.managers;

import work.terrencemurray.items.Item;
import work.terrencemurray.items.ItemFactory;
import work.terrencemurray.items.ItemPool;
import work.terrencemurray.items.decorators.ItemWithBoxCollider;
import work.terrencemurray.items.decorators.ItemWithCollectable;
import work.terrencemurray.items.decorators.ItemWithCollectable.ItemType;
import work.terrencemurray.items.decorators.ItemWithGravity;

import java.awt.Graphics2D;
import java.util.Random;
import java.util.Vector;

public class ItemManager {

    private static final int ITEM_COUNT = 5;
    private static final int ITEM_SIZE = 40;
    private static final int BAT_SPEED = -5;
    private static final int BANANA_SPEED = -3;
    private static final int MIN_ZIGZAG_AMP = 10;
    private static final int MAX_ZIGZAG_AMP = 30;
    private static final double MIN_ZIGZAG_FREQ = 0.03;
    private static final double MAX_ZIGZAG_FREQ = 0.06;

    private final ItemPool<Item> pool;
    private final ItemFactory itemFactory = new ItemFactory();
    private final Random random = new Random();

    public ItemManager() {
        pool = new ItemPool<>(() -> {
            ItemType type = random.nextBoolean() ? ItemType.BANANA : ItemType.BAT;
            int speed = type == ItemType.BAT ? BAT_SPEED : BANANA_SPEED;
            int amp = randomZigzagAmplitude();
            double freq = randomZigzagFrequency();
            return itemFactory
                .create(new Item(0, 0))
                .addCollectable(type)
                .addBoxCollider(ITEM_SIZE, ITEM_SIZE)
                .addGravity(speed, amp, freq)
                .collect();
        }, ITEM_COUNT);
    }

    public void spawnItems(int panelWidth, int panelHeight) {
        for (int i = 0; i < ITEM_COUNT; i++) {
            int x = panelWidth + random.nextInt(200) + 50;
            int y = random.nextInt(Math.max(panelHeight - ITEM_SIZE, 1));
            pool.acquire(x, y);
        }
    }

    public Vector<ItemType> update(ItemWithBoxCollider playerCollider, int panelWidth, int panelHeight) {
        Vector<ItemType> collisions = new Vector<>();

        Vector<Item> active = pool.getActive();
        for (int i = active.size() - 1; i >= 0; i--) {
            ItemWithGravity item = (ItemWithGravity) active.get(i);
            item.fall();

            ItemWithBoxCollider itemCollider = unwrapCollider(item);
            if (itemCollider.intersects(playerCollider)) {
                collisions.add(unwrapCollectable(item).getType());
                respawnItem(item, panelWidth, panelHeight);
                continue;
            }

            if (item.getCurrentPosition().getX() < -ITEM_SIZE) {
                respawnItem(item, panelWidth, panelHeight);
            }
        }

        return collisions;
    }

    public void render(Graphics2D g2) {
        for (Item item : pool.getActive()) {
            item.render(g2);
        }
    }

    public void setDebugging(boolean flag) {
        for (Item item : pool.getActive()) {
            unwrapCollider((ItemWithGravity) item).setDebugging(flag);
        }
    }

    private void respawnItem(ItemWithGravity item, int panelWidth, int panelHeight) {
        ItemWithCollectable collectable = unwrapCollectable(item);

        ItemType type = random.nextBoolean() ? ItemType.BANANA : ItemType.BAT;
        collectable.setType(type);

        int x = panelWidth + random.nextInt(200) + 50;
        int y = random.nextInt(Math.max(panelHeight - ITEM_SIZE, 1));
        item.reset(x, y);
        item.setHorizontalSpeed(type == ItemType.BAT ? BAT_SPEED : BANANA_SPEED);
        item.setZigzagAmplitude(randomZigzagAmplitude());
        item.setZigzagFrequency(randomZigzagFrequency());
    }

    private int randomZigzagAmplitude() {
        return MIN_ZIGZAG_AMP + random.nextInt(MAX_ZIGZAG_AMP - MIN_ZIGZAG_AMP + 1);
    }

    private double randomZigzagFrequency() {
        return MIN_ZIGZAG_FREQ + random.nextDouble() * (MAX_ZIGZAG_FREQ - MIN_ZIGZAG_FREQ);
    }

    private ItemWithBoxCollider unwrapCollider(ItemWithGravity item) {
        return (ItemWithBoxCollider) item.getWrapped();
    }

    private ItemWithCollectable unwrapCollectable(ItemWithGravity item) {
        return (ItemWithCollectable) unwrapCollider(item).getWrapped();
    }
}
