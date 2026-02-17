package work.terrencemurray.infrastructure.items;

import java.awt.Color;
import java.awt.Graphics;

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
    public void onCollect(CollectFunction action) {
        action.run();
    }

    public int getSize() {
        return SIZE;
    }
}
