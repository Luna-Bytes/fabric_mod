package dev.lunabytes.datagen;

import dev.lunabytes.food.FoodDefinition;

import dev.lunabytes.food.FoodItems;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class LunaBytesLangProvider extends FabricLanguageProvider {

    public LunaBytesLangProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registryLookup
    ) {
        super(output, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup,
                                     @NonNull TranslationBuilder translationBuilder) {
        for (FoodDefinition def : FoodItems.DEFINITIONS) {
            translationBuilder.add("item.lunabytes." + def.id(), def.displayName());
        }
    }

}