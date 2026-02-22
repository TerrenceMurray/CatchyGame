package work.terrencemurray.items.decorators;

import java.awt.Graphics;
import java.awt.Image;

import work.terrencemurray.items.Collectable;
import work.terrencemurray.items.Item;
import work.terrencemurray.managers.ImageManager;
import work.terrencemurray.position.Point2D;

public class ItemWithCollectable extends Item implements Collectable {

    public enum ItemType {
        BANANA("/images/fruit_banana.png", 30),
        ANVIL("/images/anvil-64x64-black.png", 45);

        private final Image image;
        private final int size;

        ItemType(String path, int size) {
            this.size = size;
            this.image = ImageManager.loadImage(path);
        }

        public Image getImage() {
            return image;
        }

        public int getSize() {
            return size;
        }
    }

    private final Item wrapped;
    private ItemType type;

    public ItemWithCollectable(Item wrapped, ItemType type) {
        super(wrapped.getCurrentPosition().getX(), wrapped.getCurrentPosition().getY());
        this.wrapped = wrapped;
        this.type = type;
    }

    public Item getWrapped() {
        return wrapped;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public int getSize() {
        return type.getSize();
    }

    @Override
    public void onCollect(CollectFunction action) {
        action.run();
    }

    @Override
    public void render(Graphics g) {
        int x = wrapped.getCurrentPosition().getX();
        int y = wrapped.getCurrentPosition().getY();
        int size = type.getSize();
        g.drawImage(type.getImage(), x, y, size, size, null);
    }

    @Override
    public Point2D<Integer> getCenter() {
        int size = type.getSize();
        int centerX = wrapped.getCurrentPosition().getX() + size / 2;
        int centerY = wrapped.getCurrentPosition().getY() + size / 2;
        return new Point2D<>(centerX, centerY);
    }

    @Override
    public Point2D<Integer> getCurrentPosition() {
        return wrapped.getCurrentPosition();
    }

    @Override
    public void reset(int x, int y) {
        wrapped.reset(x, y);
    }
}
