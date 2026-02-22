package work.terrencemurray.items;

public interface Collectable {
    public void onCollect(CollectFunction action);

    interface CollectFunction {
        void run();
    }
}
