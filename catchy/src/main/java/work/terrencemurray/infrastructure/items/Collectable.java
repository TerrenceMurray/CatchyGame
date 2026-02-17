package work.terrencemurray.infrastructure.items;


public interface Collectable {
    public void onCollect(CollectFunction action);

    interface CollectFunction {
        void run();
    }
}
