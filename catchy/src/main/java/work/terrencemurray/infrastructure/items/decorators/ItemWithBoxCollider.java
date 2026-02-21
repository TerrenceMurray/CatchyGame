package work.terrencemurray.infrastructure.items.decorators;

import java.awt.Color;
import java.awt.Graphics;

import work.terrencemurray.infrastructure.items.Item;
import work.terrencemurray.infrastructure.position.Point2D;

public class ItemWithBoxCollider extends Item {
    private final Item wrapped;
    private final BoundingBox boundingBox;
    private int width, height;
    private boolean isDebugging;

    public class BoundingBox {
        private final Item item;
        
        public BoundingBox (Item item) {
            this.item = item;
        }

        public int getLeft() { return item.getCenter().getX() - width / 2; }
        public int getRight() { return item.getCenter().getX() + width / 2; }
        public int getTop() { return item.getCenter().getY() - height / 2; }
        public int getBottom() { return item.getCenter().getY() + height / 2; }
    }
    
    public ItemWithBoxCollider(Item wrapped, int width, int height) {
        super(wrapped.getCurrentPosition().getX(), wrapped.getCurrentPosition().getY());
        this.wrapped = wrapped;
        this.width = width;
        this.height = height;

        this.boundingBox = new BoundingBox(wrapped);
        this.isDebugging = false;
    }

    public void setDebugging(boolean flag) {
        this.isDebugging = flag;
    }

    private void drawBoundingBox(Graphics g) {
        Point2D<Integer> center = this.wrapped.getCenter();
        int x = center.getX() - this.width / 2;
        int y = center.getY() - this.height / 2;

        g.setColor(Color.RED);
        g.drawRect(x, y, this.width, this.height);
    }

    public boolean intersects(ItemWithBoxCollider other) {
        BoundingBox a = this.boundingBox;
        BoundingBox b = other.boundingBox;

        if (a.getRight() < b.getLeft())
            return false;

        if (a.getLeft() > b.getRight())
            return false;

        if (a.getBottom() < b.getTop())
            return false;

        if (a.getTop() > b.getBottom())
            return false;

        return true;
    }

    @Override
    public Point2D<Integer> getCurrentPosition() {
        return wrapped.getCurrentPosition();
    }

    @Override
    public Point2D<Integer> getCenter() {
        return wrapped.getCenter();
    }

    @Override
    public void reset(int x, int y) {
        wrapped.reset(x, y);
    }

    public void render(Graphics g) {
        wrapped.render(g);
        if (this.isDebugging) {
            this.drawBoundingBox(g);
        }
    }
}
