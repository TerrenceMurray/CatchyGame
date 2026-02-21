package work.terrencemurray.infrastructure.items;

import java.awt.Color;
import java.awt.Graphics;

import work.terrencemurray.infrastructure.position.Point2D;

public class FruitItem extends Item implements Collectable {
    private static final int SIZE = 20;
    private final Color color;

    public FruitItem(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    @Override
    public void render(Graphics g) {
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        g.setColor(color);
        g.fillOval(x, y, SIZE, SIZE);
        g.setColor(color.darker());
        g.drawOval(x, y, SIZE, SIZE);
    }

    @Override
    public Point2D<Integer> getCenter() {
        int centerX = this.currentPosition.getX() + SIZE / 2;
        int centerY = this.currentPosition.getY() + SIZE / 2;
        return new Point2D<Integer>(centerX, centerY);
    }

    @Override
    public void onCollect(CollectFunction action) {
        action.run();
    }

    public int getSize() {
        return SIZE;
    }
}
