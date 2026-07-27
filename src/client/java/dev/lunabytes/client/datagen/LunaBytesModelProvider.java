package dev.lunabytes.client.datagen;

import dev.lunabytes.food.FoodDefinition;
import dev.lunabytes.food.FoodItems;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import org.jspecify.annotations.NonNull;

public class LunaBytesModelProvider extends FabricModelProvider {

    public LunaBytesModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(
            @NonNull BlockModelGenerators blockModelGenerators
    ) {
        // No blocks
    }

    @Override
    public void generateItemModels(
            @NonNull ItemModelGenerators itemModelGenerators
    ) {
        for (FoodDefinition def : FoodItems.DEFINITIONS) {

            itemModelGenerators.generateFlatItem(
                    FoodItems.get(def.id()),
                    ModelTemplates.FLAT_ITEM
            );
        }
    }
}