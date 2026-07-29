package dev.lunabytes.client.datagen;

import dev.lunabytes.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;


public class LunaBytesGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(LunaBytesTagProvider::new);
        pack.addProvider(LunaBytesFishingBiomeTagProvider::new);
        pack.addProvider(LunaBytesRecipeProvider::new);
        pack.addProvider(LunaBytesLangProvider::new);
        pack.addProvider(LunaBytesFishingLootProvider::new);
        pack.addProvider(LunaBytesModelProvider::new);
    }
}