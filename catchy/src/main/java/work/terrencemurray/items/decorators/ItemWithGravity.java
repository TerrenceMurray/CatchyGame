package work.terrencemurray.items.decorators;

import work.terrencemurray.items.Item;
import work.terrencemurray.position.Point2D;
import java.awt.Graphics;

public class ItemWithGravity extends Item {
    private final Item wrapped;
    private int horizontalSpeed;
    private int zigzagAmplitude;
    private double zigzagFrequency;
    private int baseY;
    private int tick;

    public ItemWithGravity(Item wrapped, int horizontalSpeed, int zigzagAmplitude, double zigzagFrequency) {
        super(wrapped.getCurrentPosition().getX(), wrapped.getCurrentPosition().getY());
        this.wrapped = wrapped;
        this.horizontalSpeed = horizontalSpeed;
        this.zigzagAmplitude = zigzagAmplitude;
        this.zigzagFrequency = zigzagFrequency;
        this.baseY = wrapped.getCurrentPosition().getY();
        this.tick = 0;
    }

    public void fall() {
        Point2D<Integer> pos = wrapped.getCurrentPosition();
        pos.setX(pos.getX() + horizontalSpeed);
        tick++;
        int y = baseY + (int) (zigzagAmplitude * Math.sin(zigzagFrequency * tick));
        pos.setY(y);
    }

    @Override
    public Point2D<Integer> getCurrentPosition() {
        return wrapped.getCurrentPosition();
    }

    public int getHorizontalSpeed() {
        return horizontalSpeed;
    }

    public void setHorizontalSpeed(int horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
    }

    public void setBaseY(int baseY) {
        this.baseY = baseY;
    }

    public void setZigzagAmplitude(int zigzagAmplitude) {
        this.zigzagAmplitude = zigzagAmplitude;
    }

    public void setZigzagFrequency(double zigzagFrequency) {
        this.zigzagFrequency = zigzagFrequency;
    }

    public Item getWrapped() {
        return wrapped;
    }

    @Override
    public void reset(int x, int y) {
        wrapped.reset(x, y);
        this.baseY = y;
        this.tick = 0;
    }

    @Override
    public Point2D<Integer> getCenter() {
        return wrapped.getCenter();
    }

    @Override
    public void render(Graphics g) {
        wrapped.render(g);
    }
}
