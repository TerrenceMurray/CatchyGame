package work.terrencemurray.items;

import java.util.Vector;
import java.util.function.Supplier;

public class ItemPool<T extends Item> {
    private final Vector<T> available;
    private final Vector<T> active;
    private final Supplier<T> factory;

    public ItemPool(Supplier<T> factory, int initialSize) {
        this.factory = factory;
        this.available = new Vector<>();
        this.active = new Vector<>();

        for (int i = 0; i < initialSize; i++) {
            available.add(factory.get());
        }
    }

    public T acquire(int x, int y) {
        T item;
        if (!available.isEmpty()) {
            item = available.remove(available.size() - 1);
        } else {
            item = factory.get();
        }
        item.reset(x, y);
        active.add(item);
        return item;
    }

    public void release(T item) {
        active.remove(item);
        available.add(item);
    }

    public Vector<T> getActive() {
        return active;
    }

    public int activeCount() {
        return active.size();
    }

    public int availableCount() {
        return available.size();
    }
}
