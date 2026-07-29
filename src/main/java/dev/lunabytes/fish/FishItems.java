package dev.lunabytes.fish;

import dev.lunabytes.food.FoodDefinition;
import dev.lunabytes.food.FoodEffect;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class FishItems {

    public static final int FORCED_FOOD_LEVEL = 4;
    public static final float FORCED_SATURATION = 2.0f;

    /** Key used for the axolotl entry in all()/get() - it is NOT in DEFINITIONS, see registerAxolotl(). */
    public static final String AXOLOTL_ID = "axolotl";
    public static final String AXOLOTL_DISPLAY_NAME = "Axolotl";

    private static final List<FoodEffect> PUFFERFISH_EFFECTS = List.of(
            FoodEffect.always(MobEffects.POISON, 1200, 3),
            FoodEffect.always(MobEffects.HUNGER, 300, 2),
            FoodEffect.always(MobEffects.DARKNESS, 300, 1) //TODO: Replace with Confusion once implemented
    );

    private static final Map<String, Item> REGISTERED = new LinkedHashMap<>();
    private static final Map<Item, FishDefinition> DEFINITIONS_BY_ITEM = new java.util.IdentityHashMap<>();

    // ------------------------------------------------------------------
    // (axolotl is handled separately in registerAxolotl() - it isn't food,
    // it spawns the entity, and it never had a rarity indicator in the
    // original datapack)
    // ------------------------------------------------------------------
    public static final List<FishDefinition> COD_DEFINITIONS = List.of(
            FishDefinition.of("striped_perch", FishRarity.COMMON),
            FishDefinition.of("spoonhead_sculpin", FishRarity.UNCOMMON),
            FishDefinition.of("shad", FishRarity.COMMON),
            FishDefinition.of("flying_fish", FishRarity.UNCOMMON),
            FishDefinition.of("rainbow_wrasse", FishRarity.COMMON),
            FishDefinition.of("piranha", FishRarity.UNCOMMON),
            FishDefinition.of("echo_fish", FishRarity.UNCOMMON),
            FishDefinition.of("pale_fish", FishRarity.UNCOMMON),
            FishDefinition.of("crappie", FishRarity.COMMON),
            FishDefinition.of("cod", FishRarity.COMMON),
            FishDefinition.of("bujurqui", FishRarity.COMMON),
            FishDefinition.of("mediterranean_killifish", FishRarity.COMMON),
            FishDefinition.of("gurnard", FishRarity.UNCOMMON),
            FishDefinition.of("guppy", FishRarity.COMMON),
            FishDefinition.of("lamprey", FishRarity.UNCOMMON),
            FishDefinition.of("herring", FishRarity.UNCOMMON),
            FishDefinition.of("bluegill", FishRarity.COMMON),
            FishDefinition.of("anchovy", FishRarity.COMMON),
            FishDefinition.of("humpback_whitefish", FishRarity.COMMON),
            FishDefinition.of("alaska_blackfish", FishRarity.COMMON),
            FishDefinition.of("blind_minnow", FishRarity.COMMON),
            FishDefinition.of("blind_cave_fish", FishRarity.COMMON)
    );

    public static final List<FishDefinition> PUFFER_DEFINITIONS = List.of(
            FishDefinition.withEffects("pufferfish", FishRarity.COMMON, PUFFERFISH_EFFECTS),
            FishDefinition.withEffects("freshwater_pufferfish", FishRarity.COMMON, PUFFERFISH_EFFECTS)
    );

    public static final List<FishDefinition> TROPICAL_DEFINITIONS = List.of(
            FishDefinition.of("tropical_fish", FishRarity.COMMON)
    );

    public static final List<FishDefinition> SALMON_DEFINITIONS = List.of(
            FishDefinition.of("wolffish", FishRarity.RARE),
            FishDefinition.of("gar", FishRarity.RARE),
            FishDefinition.of("walleye", FishRarity.UNCOMMON),
            FishDefinition.of("flounder", FishRarity.RARE),
            FishDefinition.of("tunisian_barb", FishRarity.RARE),
            FishDefinition.of("european_eel", FishRarity.EPIC),
            FishDefinition.of("catfish", FishRarity.RARE),
            FishDefinition.of("swordfish", FishRarity.EPIC),
            FishDefinition.of("carp", FishRarity.UNCOMMON),
            FishDefinition.of("sturgeon", FishRarity.RARE),
            FishDefinition.of("painted_moray", FishRarity.RARE),
            FishDefinition.of("black_seabass", FishRarity.UNCOMMON),
            FishDefinition.of("opah", FishRarity.EPIC),
            FishDefinition.of("bass", FishRarity.EPIC),
            FishDefinition.of("monkfish", FishRarity.RARE),
            FishDefinition.of("oarfish", FishRarity.EPIC),
            FishDefinition.of("armoured_catfish", FishRarity.RARE),
            FishDefinition.of("northern_pike", FishRarity.RARE),
            FishDefinition.of("skate", FishRarity.EPIC),
            FishDefinition.of("muskellunge", FishRarity.EPIC),
            FishDefinition.of("arapaima", FishRarity.EPIC),
            FishDefinition.of("mahi_mahi", FishRarity.UNCOMMON),
            FishDefinition.of("siberian_sturgeon", FishRarity.EPIC),
            FishDefinition.of("salmon", FishRarity.COMMON)
    );

    public static final List<FishDefinition> DEFINITIONS = Stream.of(
            COD_DEFINITIONS,
            PUFFER_DEFINITIONS,
            TROPICAL_DEFINITIONS,
            SALMON_DEFINITIONS
    ).flatMap(List::stream).toList();

    public static void registerAll() {
        for (FishDefinition def : DEFINITIONS) {
            Identifier id = Identifier.fromNamespaceAndPath("lunabytes", def.id());
            Item item = build(def, id);

            Registry.register(BuiltInRegistries.ITEM, id, item);
            REGISTERED.put(def.id(), item);
            DEFINITIONS_BY_ITEM.put(item, def);
        }

        registerAxolotl();

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(output -> {
            for (Map.Entry<String, Item> entry : REGISTERED.entrySet()) {
                if (!entry.getKey().equals(AXOLOTL_ID)) {
                    output.accept(entry.getValue());
                }
            }
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(output ->
                output.accept(REGISTERED.get(AXOLOTL_ID))
        );
    }

    private static void registerAxolotl() {
        Identifier id = Identifier.fromNamespaceAndPath("lunabytes", "axolotl");

        Item.Properties properties = new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, id))
                .spawnEgg(EntityTypes.AXOLOTL);


        Item item = new SpawnEggItem(properties);

        Registry.register(BuiltInRegistries.ITEM, id, item);
        REGISTERED.put(AXOLOTL_ID, item);
    }


    public static Item get(String id) {
        return REGISTERED.get(id);
    }

    public static Map<String, Item> all() {
        return REGISTERED;
    }

    public static FishDefinition getDefinition(Item item) {
        return DEFINITIONS_BY_ITEM.get(item);
    }

    private static Item build(FishDefinition def, Identifier id) {
        FoodProperties foodProperties = new FoodProperties.Builder()
                .nutrition(FORCED_FOOD_LEVEL)
                .saturationModifier(FORCED_SATURATION / (FORCED_FOOD_LEVEL * 2f))
                .alwaysEdible()
                .build();

        Consumable.Builder consumableBuilder = Consumable.builder();

        for (FoodEffect fx : def.effects()) {
            MobEffectInstance instance = fx.toInstance();
            consumableBuilder.onConsume(new ApplyStatusEffectsConsumeEffect(instance, fx.probability()));
        }

        Consumable consumable = consumableBuilder.build();

        List<Component> loreLines = new ArrayList<>();
        loreLines.add(Component.literal(def.rarity().starText())
                .withStyle(style -> style.withColor(def.rarity().color()).withItalic(false)));

        ItemLore lore = new ItemLore(loreLines);

        Item.Properties properties = new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, id))
                .food(foodProperties, consumable)
                .component(DataComponents.LORE, lore);

        return new Item(properties);
    }
}