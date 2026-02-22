package work.terrencemurray.infrastructure.items;

import java.awt.Graphics;
import java.awt.Image;

import work.terrencemurray.infrastructure.managers.ImageManager;
import work.terrencemurray.infrastructure.position.Point2D;

public class CollectableItem extends Item implements Collectable {

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
    private ItemType type;

    public CollectableItem(int x, int y, ItemType type) {
        super(x, y);
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    @Override
    public void render(Graphics g) {
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        int size = type.getSize();
        g.drawImage(type.getImage(), x, y, size, size, null);
    }

    @Override
    public Point2D<Integer> getCenter() {
        int size = type.getSize();
        int centerX = this.currentPosition.getX() + size / 2;
        int centerY = this.currentPosition.getY() + size / 2;
        return new Point2D<Integer>(centerX, centerY);
    }

    @Override
    public void onCollect(CollectFunction action) {
        action.run();
    }

    public int getSize() {
        return type.getSize();
    }
}
