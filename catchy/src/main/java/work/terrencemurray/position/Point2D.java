package work.terrencemurray.position;

public class Point2D<T extends Number> {
    private T x;
    private T y;

    public Point2D(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    public void setX(T x) {
        this.x = x;
    }

    public void setY(T y) {
        this.y = y;
    }

    public double distanceTo(Point2D<?> p) {
        double dx = this.x.doubleValue() - p.x.doubleValue();
        double dy = this.y.doubleValue() - p.y.doubleValue();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
