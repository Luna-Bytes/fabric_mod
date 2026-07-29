package dev.lunabytes.datagen;

import dev.lunabytes.fish.FishItems;
import dev.lunabytes.fish.FishTags;
import dev.lunabytes.food.FoodItems;
import dev.lunabytes.food.FoodTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class LunaBytesTagProvider extends FabricTagsProvider.ItemTagsProvider {


    public LunaBytesTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        TagAppender<Item> food_appender = builder(FoodTags.FOODS);
        TagAppender<Item> cfood_appender = builder(FoodTags.C_FOODS);

        for (Item item : FoodItems.all().values()) {
            BuiltInRegistries.ITEM.getResourceKey(item).ifPresent(food_appender::add);
            BuiltInRegistries.ITEM.getResourceKey(item).ifPresent(cfood_appender::add);
        }

        TagAppender<Item> fish_appender = builder(FishTags.FISH);
        TagAppender<Item> cfish_appender = builder(FishTags.C_FISH);
        TagAppender<Item> raw_appender = builder(FishTags.C_RAW);

        for (Item item : FishItems.all().values()) {
            BuiltInRegistries.ITEM.getResourceKey(item).ifPresent(fish_appender::add);
            BuiltInRegistries.ITEM.getResourceKey(item).ifPresent(cfish_appender::add);
            BuiltInRegistries.ITEM.getResourceKey(item).ifPresent(raw_appender::add);
        }

    }
}
