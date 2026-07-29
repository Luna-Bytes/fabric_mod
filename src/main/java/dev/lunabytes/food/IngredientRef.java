package dev.lunabytes.food;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public sealed interface IngredientRef {
    record OfItem(Supplier<Item> item) implements IngredientRef {}
    record OfTag(TagKey<Item> tag) implements IngredientRef {}

    static IngredientRef item(Supplier<Item> item) {
        return new OfItem(item);
    }

    static IngredientRef item(Item item) {
        return new OfItem(() -> item);
    }

    static IngredientRef tag(TagKey<Item> tag) {
        return new OfTag(tag);
    }
}