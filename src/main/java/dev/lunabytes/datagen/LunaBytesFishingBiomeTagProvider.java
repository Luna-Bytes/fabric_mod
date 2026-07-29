package dev.lunabytes.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class LunaBytesFishingBiomeTagProvider extends FabricTagsProvider<Biome> {

    public static final TagKey<Biome> FRESHWATER_COLD = create("freshwater_cold");
    public static final TagKey<Biome> FRESHWATER_COOL = create("freshwater_cool");
    public static final TagKey<Biome> FRESHWATER_TEMPERATE = create("freshwater_temperate");
    public static final TagKey<Biome> FRESHWATER_HOT_WET = create("freshwater_hot_wet");
    public static final TagKey<Biome> FRESHWATER_HOT_DRY = create("freshwater_hot_dry");

    public static final TagKey<Biome> SALTWATER_COLD = create("saltwater_cold");
    public static final TagKey<Biome> SALTWATER_COOL = create("saltwater_cool");
    public static final TagKey<Biome> SALTWATER_TEMPERATE = create("saltwater_temperate");
    public static final TagKey<Biome> SALTWATER_WARM = create("saltwater_warm");
    public static final TagKey<Biome> SALTWATER_HOT = create("saltwater_hot");

    public static final TagKey<Biome> SWAMPS = create("swamps");

    public LunaBytesFishingBiomeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.BIOME, registryLookupFuture);
    }

    private static TagKey<Biome> create(String path) {
        return TagKey.create(Registries.BIOME, net.minecraft.resources.Identifier.fromNamespaceAndPath("lunabytes", "fishing/" + path));
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        builder(FRESHWATER_COLD)
                .add(Biomes.FROZEN_RIVER)
                .add(Biomes.SNOWY_PLAINS)
                .add(Biomes.SNOWY_TAIGA)
                .add(Biomes.SNOWY_BEACH)
                .add(Biomes.ICE_SPIKES);

        builder(FRESHWATER_COOL)
                .add(Biomes.TAIGA)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                .add(Biomes.BIRCH_FOREST)
                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .add(Biomes.WINDSWEPT_HILLS)
                .add(Biomes.WINDSWEPT_FOREST)
                .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                .add(Biomes.STONY_SHORE)
                .add(Biomes.MEADOW);

        builder(FRESHWATER_TEMPERATE)
                .add(Biomes.FOREST)
                .add(Biomes.FLOWER_FOREST)
                .add(Biomes.DARK_FOREST)
                .add(Biomes.PLAINS)
                .add(Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.RIVER)
                .add(Biomes.BEACH)
                .add(Biomes.SWAMP) // base river/lake biomes
                .add(Biomes.MANGROVE_SWAMP);

        builder(FRESHWATER_HOT_WET)
                .add(Biomes.JUNGLE)
                .add(Biomes.SPARSE_JUNGLE)
                .add(Biomes.BAMBOO_JUNGLE);

        builder(FRESHWATER_HOT_DRY)
                .add(Biomes.SAVANNA)
                .add(Biomes.SAVANNA_PLATEAU)
                .add(Biomes.WINDSWEPT_SAVANNA)
                .add(Biomes.BADLANDS)
                .add(Biomes.ERODED_BADLANDS)
                .add(Biomes.WOODED_BADLANDS)
                .add(Biomes.DESERT);

        builder(SALTWATER_COLD)
                .add(Biomes.FROZEN_OCEAN)
                .add(Biomes.DEEP_FROZEN_OCEAN);

        builder(SALTWATER_COOL)
                .add(Biomes.COLD_OCEAN)
                .add(Biomes.DEEP_COLD_OCEAN);

        builder(SALTWATER_TEMPERATE)
                .add(Biomes.OCEAN)
                .add(Biomes.DEEP_OCEAN)
                .add(Biomes.LUKEWARM_OCEAN)
                .add(Biomes.DEEP_LUKEWARM_OCEAN);

        builder(SALTWATER_WARM)
                .add(Biomes.WARM_OCEAN);

        builder(SALTWATER_HOT)
                .add(Biomes.WARM_OCEAN);

        builder(SWAMPS)
                .add(Biomes.SWAMP)
                .add(Biomes.MANGROVE_SWAMP);
    }
}