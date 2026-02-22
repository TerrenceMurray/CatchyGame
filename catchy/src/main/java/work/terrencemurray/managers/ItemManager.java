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
    private static final int ANVIL_SPEED = 4;
    private static final int BANANA_SPEED = 3;

    private final ItemPool<Item> pool;
    private final ItemFactory itemFactory = new ItemFactory();
    private final Random random = new Random();

    public ItemManager() {
        pool = new ItemPool<>(() -> {
            ItemType type = random.nextBoolean() ? ItemType.BANANA : ItemType.ANVIL;
            return itemFactory
                .create(new Item(0, 0))
                .addCollectable(type)
                .addBoxCollider(ITEM_SIZE, ITEM_SIZE)
                .addGravity(type == ItemType.ANVIL ? ANVIL_SPEED : BANANA_SPEED)
                .collect();
        }, ITEM_COUNT);
    }

    public void spawnItems(int panelWidth) {
        for (int i = 0; i < ITEM_COUNT; i++) {
            int x = random.nextInt(Math.max(panelWidth - 20, 1));
            int y = -(random.nextInt(200) + 50);
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
                respawnItem(item, panelWidth);
                continue;
            }

            if (item.getCurrentPosition().getY() > panelHeight) {
                respawnItem(item, panelWidth);
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

    private void respawnItem(ItemWithGravity item, int panelWidth) {
        ItemWithCollectable collectable = unwrapCollectable(item);

        ItemType type = random.nextBoolean() ? ItemType.BANANA : ItemType.ANVIL;
        collectable.setType(type);

        int x = random.nextInt(Math.max(panelWidth - 20, 1));
        int y = -(random.nextInt(300) + 50);
        item.reset(x, y);
        item.setFallSpeed(type == ItemType.ANVIL ? ANVIL_SPEED : BANANA_SPEED);
    }

    private ItemWithBoxCollider unwrapCollider(ItemWithGravity item) {
        return (ItemWithBoxCollider) item.getWrapped();
    }

    private ItemWithCollectable unwrapCollectable(ItemWithGravity item) {
        return (ItemWithCollectable) unwrapCollider(item).getWrapped();
    }
}
