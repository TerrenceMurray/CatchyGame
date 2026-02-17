package work.terrencemurray.infrastructure.items.decorators;

import work.terrencemurray.infrastructure.items.Item;
import work.terrencemurray.infrastructure.position.Point2D;
import java.awt.Graphics;

public class ItemWithGravity extends Item {
    private final Item wrapped;
    private int fallSpeed;

    public ItemWithGravity(Item wrapped, int fallSpeed) {
        super(wrapped.getCurrentPosition().getX(), wrapped.getCurrentPosition().getY());
        this.wrapped = wrapped;
        this.fallSpeed = fallSpeed;
    }

    public void fall() {
        Point2D<Integer> pos = wrapped.getCurrentPosition();
        pos.setY(pos.getY() + fallSpeed);
        this.currentPosition = pos;
    }

    public int getFallSpeed() {
        return fallSpeed;
    }

    public void setFallSpeed(int fallSpeed) {
        this.fallSpeed = fallSpeed;
    }

    public Item getWrapped() {
        return wrapped;
    }

    @Override
    public void reset(int x, int y) {
        wrapped.reset(x, y);
        this.currentPosition = wrapped.getCurrentPosition();
    }

    @Override
    public void render(Graphics g) {
        wrapped.render(g);
    }
}
