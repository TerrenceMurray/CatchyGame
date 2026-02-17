package work.terrencemurray.infrastructure.items;

import work.terrencemurray.infrastructure.position.Point2D;
import java.awt.Graphics;

public abstract class Item {
    protected Point2D<Integer> currentPosition;

    public Item(int x, int y) {
        this.currentPosition = new Point2D<>(x, y);
    }

    public Point2D<Integer> getCurrentPosition() {
        return this.currentPosition;
    }

    public void reset(int x, int y) {
        this.currentPosition = new Point2D<>(x, y);
    }

    public abstract void render(Graphics g);
}
