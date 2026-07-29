package dev.lunabytes.food;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FoodTags {
    public static final TagKey<Item> FOODS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath("lunabytes","foods")
    );

    public static final TagKey<Item> C_FOODS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath("c","foods")
    );
}