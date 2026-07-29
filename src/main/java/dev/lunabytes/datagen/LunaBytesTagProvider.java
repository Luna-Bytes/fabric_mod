package dev.lunabytes.datagen;

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
        TagAppender<Item> appender = builder(FoodTags.FOODS);

        for (Item item : FoodItems.all().values()) {
            BuiltInRegistries.ITEM.getResourceKey(item).ifPresent(appender::add);
        }

        TagAppender<Item> appender2 = builder(FoodTags.C_FOODS);

        for (Item item : FoodItems.all().values()) {
            BuiltInRegistries.ITEM.getResourceKey(item).ifPresent(appender2::add);
        }
    }
}
