package work.terrencemurray.infrastructure.items.factory;

import work.terrencemurray.infrastructure.items.Item;
import work.terrencemurray.infrastructure.items.decorators.ItemWithBoxCollider;
import work.terrencemurray.infrastructure.items.decorators.ItemWithGravity;

public class ItemFactory {
    private Item item;

    public ItemFactory create(Item item) {
        this.item = item;
        return this;
    }

    public ItemFactory reset(Item item) {
        this.item = item;
        return this;
    }

    public ItemFactory addGravity(int fallSpeed) {
        this.item = new ItemWithGravity(item, fallSpeed);
        return this;
    }

    public ItemFactory addBoxCollider(int width, int height) {
        this.item = new ItemWithBoxCollider(item, width, height);
        return this;
    }

    public Item collect() {
        Item result = this.item;
        this.item = null;
        return result;
    }
}
