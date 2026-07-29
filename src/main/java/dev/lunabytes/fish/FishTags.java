package dev.lunabytes.fish;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FishTags {
    public static final TagKey<Item> FISH = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath("lunabytes","fish")
    );

    public static final TagKey<Item> C_FISH = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath("c","fish")
    );

    public static final TagKey<Item> C_RAW = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath("c","foods/raw_fish")
    );

    public static final TagKey<Item> TROPICAL = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath("lunabytes","fish/tropical")
    );

    public static final TagKey<Item> PUFFY = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath("lunabytes","fish/puffy")
    );

    public static final TagKey<Item> COD = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath("lunabytes","fish/cod")
    );

    public static final TagKey<Item> SALMON = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath("lunabytes","fish/salmon")
    );

}