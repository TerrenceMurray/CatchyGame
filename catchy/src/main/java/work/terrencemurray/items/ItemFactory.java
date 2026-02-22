package work.terrencemurray.items;

import work.terrencemurray.items.decorators.ItemWithBoxCollider;
import work.terrencemurray.items.decorators.ItemWithCollectable;
import work.terrencemurray.items.decorators.ItemWithCollectable.ItemType;
import work.terrencemurray.items.decorators.ItemWithGravity;

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

    public ItemFactory addCollectable(ItemType type) {
        this.item = new ItemWithCollectable(item, type);
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
