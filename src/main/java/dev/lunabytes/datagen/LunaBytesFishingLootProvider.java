package dev.lunabytes.datagen;

import dev.lunabytes.fish.FishItems;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;

import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Climate-tiered fishing pools + a full override of vanilla's gameplay/fishing
 * table, converted from the source datapack. Biome grouping is done through
 * the tags in LunaBytesFishingBiomeTagProvider - same approach the original
 * datapack used (data/minecraft/tags/worldgen/biome/*.json).
 *
 * One fix vs. the source: its root fishing.json pointed BOTH the
 * freshwater_hot_wet AND freshwater_hot_dry entries at the #freshwater_hot_dry
 * tag (copy-paste bug), so jungle/bamboo_jungle biomes never actually rolled
 * any fish. Fixed below to check #freshwater_hot_wet where it should.
 */
public class LunaBytesFishingLootProvider extends SimpleFabricLootTableSubProvider {

    private static final ResourceKey<LootTable> FISHING =
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("minecraft", "gameplay/fishing"));

    private final CompletableFuture<HolderLookup.Provider> registryLookup;

    public LunaBytesFishingLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.FISHING);
        this.registryLookup = registryLookup;
    }

    public static ResourceKey<LootTable> fishingKey(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("lunabytes", "fishing/" + path));
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> out) {
        HolderLookup.Provider registries = registryLookup.join();
        HolderGetter<Biome> biomes = registries.lookupOrThrow(Registries.BIOME);

        out.accept(fishingKey("freshwater_cold"),
                fiveTier("humpback_whitefish", "alaska_blackfish", "spoonhead_sculpin", "northern_pike", "siberian_sturgeon"));
        out.accept(fishingKey("freshwater_cool"),
                fiveTier("striped_perch", "crappie", "walleye", "gar", "muskellunge"));
        out.accept(fishingKey("freshwater_temperate"),
                fiveTier("bluegill", "crappie", "carp", "catfish", "bass"));
        out.accept(fishingKey("freshwater_hot_dry"),
                fiveTier("mediterranean_killifish", "freshwater_pufferfish", "lamprey", "tunisian_barb", "european_eel"));
        out.accept(fishingKey("freshwater_hot_wet"),
                fiveTier("guppy", "bujurqui", "piranha", "armoured_catfish", "arapaima"));

        out.accept(fishingKey("saltwater_cold"),
                fiveTier("cod", "salmon", "herring", "wolffish", "skate"));
        out.accept(fishingKey("saltwater_cool"),
                fiveTier("cod", "salmon", "black_seabass", "monkfish", "opah"));
        out.accept(fishingKey("saltwater_temperate"),
                fiveTier("anchovy", "shad", "flying_fish", "sturgeon", "swordfish"));
        out.accept(fishingKey("saltwater_warm"),
                fiveTier("tropical_fish", "rainbow_wrasse", "gurnard", "flounder", "swordfish"));
        out.accept(fishingKey("saltwater_hot"),
                fiveTier("tropical_fish", "pufferfish", "mahi_mahi", "painted_moray", "oarfish"));

        out.accept(fishingKey("swamps"),
                fiveTier("guppy", "bujurqui", "lamprey", "flounder", "arapaima"));

        out.accept(fishingKey("deep_dark"), LootTable.lootTable().withPool(
                pool()
                        .add(fishEntry("echo_fish", 7))
                        .add(LootItem.lootTableItem(Items.SCULK_VEIN).setWeight(20))
                        .add(LootItem.lootTableItem(Items.ECHO_SHARD).setWeight(1))
        ));

        out.accept(fishingKey("pale_garden"), LootTable.lootTable().withPool(
                pool()
                        .add(fishEntry("pale_fish", 7))
                        .add(LootItem.lootTableItem(Items.PALE_HANGING_MOSS).setWeight(20))
                        .add(LootItem.lootTableItem(Items.RESIN_CLUMP).setWeight(1))
        ));

        out.accept(fishingKey("sulfur_caves"), LootTable.lootTable().withPool(
                pool()
                        .add(LootItem.lootTableItem(Items.BONE).setWeight(10))
                        .add(LootItem.lootTableItem(Items.GUNPOWDER))
        ));

        out.accept(fishingKey("junk"), buildJunkTable());
        out.accept(fishingKey("treasure"), buildTreasureTable());

        out.accept(FISHING, buildRootTable(biomes));
    }

    /** Full override of minecraft:gameplay/fishing, matching the source pack's structure/weights. */
    private LootTable.Builder buildRootTable(HolderGetter<Biome> biomes) {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .setBonusRolls(ConstantValue.exactly(0))
                .add(nested("junk", 10))
                .add(nested("treasure", 5))
                .add(biomeGated("freshwater_cold", 85, biomes, LunaBytesFishingBiomeTagProvider.FRESHWATER_COLD))
                .add(biomeGated("freshwater_cool", 85, biomes, LunaBytesFishingBiomeTagProvider.FRESHWATER_COOL))
                .add(biomeGated("freshwater_temperate", 85, biomes, LunaBytesFishingBiomeTagProvider.FRESHWATER_TEMPERATE))
                .add(biomeGated("freshwater_hot_wet", 85, biomes, LunaBytesFishingBiomeTagProvider.FRESHWATER_HOT_WET))
                .add(biomeGated("freshwater_hot_dry", 85, biomes, LunaBytesFishingBiomeTagProvider.FRESHWATER_HOT_DRY))
                .add(biomeGated("saltwater_cold", 85, biomes, LunaBytesFishingBiomeTagProvider.SALTWATER_COLD))
                .add(biomeGated("saltwater_cool", 85, biomes, LunaBytesFishingBiomeTagProvider.SALTWATER_COOL))
                .add(biomeGated("saltwater_temperate", 85, biomes, LunaBytesFishingBiomeTagProvider.SALTWATER_TEMPERATE))
                .add(biomeGated("saltwater_warm", 85, biomes, LunaBytesFishingBiomeTagProvider.SALTWATER_WARM))
                .add(biomeGated("saltwater_hot", 85, biomes, LunaBytesFishingBiomeTagProvider.SALTWATER_HOT))
                .add(biomeGated("swamps", 85, biomes, LunaBytesFishingBiomeTagProvider.SWAMPS))
                .add(biomeGatedSingle("deep_dark", 85, biomes, net.minecraft.world.level.biome.Biomes.DEEP_DARK))
                .add(biomeGatedSingle("sulfur_caves", 85, biomes, ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("minecraft", "sulfur_caves"))))
                .add(biomeGated("freshwater_temperate", 85, biomes,
                        net.minecraft.world.level.biome.Biomes.DRIPSTONE_CAVES, net.minecraft.world.level.biome.Biomes.LUSH_CAVES))
                .add(biomeGatedSingle("pale_garden", 85, biomes, net.minecraft.world.level.biome.Biomes.PALE_GARDEN));

        return LootTable.lootTable().withPool(pool);
    }

    private LootPoolEntryContainer.Builder<?> nested(String path, int weight) {
        return NestedLootTable.lootTableReference(fishingKey(path)).setWeight(weight);
    }

    @SafeVarargs
    private LootPoolEntryContainer.Builder<?> biomeGated(String path, int weight, HolderGetter<Biome> biomes,
                                                         ResourceKey<Biome>... explicitBiomes) {
        HolderSet<Biome> set = HolderSet.direct(biomes::getOrThrow, explicitBiomes);
        return nested(path, weight).when(LocationCheck.checkLocation(
                LocationPredicate.Builder.location().setBiomes(set)));
    }

    private LootPoolEntryContainer.Builder<?> biomeGated(String path, int weight, HolderGetter<Biome> biomes, TagKey<Biome> tag) {
        HolderSet<Biome> set = biomes.getOrThrow(tag);
        return nested(path, weight).when(LocationCheck.checkLocation(
                LocationPredicate.Builder.location().setBiomes(set)));
    }

    private LootPoolEntryContainer.Builder<?> biomeGatedSingle(String path, int weight, HolderGetter<Biome> biomes, ResourceKey<Biome> biome) {
        return biomeGated(path, weight, biomes, biome);
    }

    private LootTable.Builder buildJunkTable() {
        LootPool.Builder pool = pool()
                .add(LootItem.lootTableItem(Items.LILY_PAD).setWeight(17))
                .add(LootItem.lootTableItem(Items.LEATHER).setWeight(10))
                .add(LootItem.lootTableItem(Items.BONE).setWeight(10))
                .add(LootItem.lootTableItem(Items.GLASS_BOTTLE).setWeight(10))
                .add(LootItem.lootTableItem(Items.STRING).setWeight(5))
                .add(LootItem.lootTableItem(Items.FISHING_ROD).setWeight(2))
                .add(LootItem.lootTableItem(Items.BOWL).setWeight(10))
                .add(LootItem.lootTableItem(Items.STICK).setWeight(5))
                .add(LootItem.lootTableItem(Items.INK_SAC))
                .add(LootItem.lootTableItem(Items.TRIPWIRE_HOOK).setWeight(10));

        pool.add(LootItem.lootTableItem(FishItems.get(FishItems.AXOLOTL_ID)).setWeight(10));

        return LootTable.lootTable().withPool(pool);
    }

    private LootTable.Builder buildTreasureTable() {
        LootPool.Builder pool = pool()
                .add(LootItem.lootTableItem(Items.FISHING_ROD).setWeight(1))
                .add(LootItem.lootTableItem(Items.BOOK).setWeight(3))
                .add(LootItem.lootTableItem(Items.NAUTILUS_SHELL).setWeight(4))
                .add(LootItem.lootTableItem(Items.EMERALD).setWeight(3));

        return LootTable.lootTable().withPool(pool);
    }

    private LootPoolEntryContainer.Builder<?> fishEntry(String id, int weight) {
        return LootItem.lootTableItem(FishItems.get(id)).setWeight(weight);
    }

    private LootTable.Builder fiveTier(String a, String b, String c, String d, String e) {
        return LootTable.lootTable().withPool(
                pool()
                        .add(fishEntry(a, 15))
                        .add(fishEntry(b, 15))
                        .add(fishEntry(c, 8))
                        .add(fishEntry(d, 3))
                        .add(fishEntry(e, 1))
        );
    }

    private LootPool.Builder pool() {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .setBonusRolls(ConstantValue.exactly(0));
    }
}