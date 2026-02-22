package work.terrencemurray.items.decorators;

import java.awt.Graphics;
import java.awt.Image;

import work.terrencemurray.items.Collectable;
import work.terrencemurray.items.Item;
import work.terrencemurray.managers.ImageManager;
import work.terrencemurray.position.Point2D;

public class ItemWithCollectable extends Item implements Collectable {

    public enum ItemType {
        BANANA("/images/fruit_banana.png", 30, 1),
        BAT("/images/bats.png", 64, 4);

        private final Image image;
        private final int size;
        private final int frameCount;
        private final int frameWidth;
        private final int frameHeight;

        ItemType(String path, int size, int frameCount) {
            this.size = size;
            this.image = ImageManager.loadImage(path);
            this.frameCount = frameCount;
            this.frameWidth = image != null ? image.getWidth(null) / frameCount : 0;
            this.frameHeight = image != null ? image.getHeight(null) : 0;
        }

        public Image getImage() {
            return image;
        }

        public int getSize() {
            return size;
        }

        public int getFrameCount() {
            return frameCount;
        }

        public int getFrameWidth() {
            return frameWidth;
        }

        public int getFrameHeight() {
            return frameHeight;
        }
    }

    private static final int ANIMATION_SPEED = 8;

    private final Item wrapped;
    private ItemType type;
    private int animationTick;

    public ItemWithCollectable(Item wrapped, ItemType type) {
        super(wrapped.getCurrentPosition().getX(), wrapped.getCurrentPosition().getY());
        this.wrapped = wrapped;
        this.type = type;
        this.animationTick = 0;
    }

    public Item getWrapped() {
        return wrapped;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
        this.animationTick = 0;
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

        if (type.getFrameCount() > 1) {
            int frame = (animationTick / ANIMATION_SPEED) % type.getFrameCount();
            int sx = frame * type.getFrameWidth();
            g.drawImage(type.getImage(),
                x + size, y, x, y + size,
                sx, 0, sx + type.getFrameWidth(), type.getFrameHeight(),
                null);
            animationTick++;
        } else {
            g.drawImage(type.getImage(), x, y, size, size, null);
        }
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
