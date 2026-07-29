package dev.lunabytes.client.datagen;

import dev.lunabytes.datagen.LunaBytesLangProvider;
import dev.lunabytes.datagen.LunaBytesRecipeProvider;
import dev.lunabytes.datagen.LunaBytesTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;


public class LunaBytesGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(LunaBytesTagProvider::new);
        pack.addProvider(LunaBytesRecipeProvider::new);
        pack.addProvider(LunaBytesLangProvider::new);
        pack.addProvider(LunaBytesModelProvider::new);
    }
}