package dev.lunabytes.food;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public final class FoodItems {

    public static final int FORCED_FOOD_LEVEL = 4;
    public static final float FORCED_SATURATION = 2.0f;

    private static final Map<String, Item> REGISTERED = new LinkedHashMap<>();

    /** Identity set of every Item this handler registered, checked by ForceHungerOnConsumeMixin. */
    public static final Set<Item> HANDLED_ITEMS = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());

    private static final Map<Item, FoodDefinition> DEFINITIONS_BY_ITEM = new java.util.IdentityHashMap<>();

    // ------------------------------------------------------------------
    // CONTENT LIST
    // ------------------------------------------------------------------
    public static final List<FoodDefinition> DEFINITIONS = List.of(

            FoodDefinition.withEffect(
                    "glow_jam",
                    "Glow Jam",
                    1f,
                    FoodEffect.always(MobEffects.GLOWING, 100, 0),
                    new FoodRecipe.Shapeless(List.of(
                            () -> Items.GLOW_BERRIES,
                            () -> Items.GLOW_BERRIES,
                            () -> Items.SUGAR,
                            () -> Items.GLASS_BOTTLE
                    ))
            ),
            FoodDefinition.withEffect(
                    "glow_bread",
                    "Glow Bread",
                    1.2f,
                    FoodEffect.always(MobEffects.GLOWING, 300, 0),
                    new FoodRecipe.Shapeless(List.of(
                            () -> FoodItems.get("glow_jam"),
                            () -> Items.BREAD
                    ))
            )
    );

    public static Item getOrCreateForDatagen(String id) {
        Identifier identifier =
                Identifier.fromNamespaceAndPath("lunabytes", id);

        return BuiltInRegistries.ITEM
                .getOptional(identifier)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Missing item: " + identifier
                        )
                );
    }

    private FoodItems() {}

    public static FoodDefinition getDefinition(Item item) {
        return DEFINITIONS_BY_ITEM.get(item);
    }

    public static void registerAll() {
        for (FoodDefinition def : DEFINITIONS) {
            Identifier id = Identifier.fromNamespaceAndPath("lunabytes", def.id());

            Item item = build(def, id);

            Registry.register(BuiltInRegistries.ITEM, id, item);

            REGISTERED.put(def.id(), item);
            HANDLED_ITEMS.add(item);
            DEFINITIONS_BY_ITEM.put(item, def);
        }

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(output -> {
            for (Item item : REGISTERED.values()) {
                output.accept(item);
            }
        });
    }

    public static Item get(String id) {
        return REGISTERED.get(id);
    }

    public static Map<String, Item> all() {
        return REGISTERED;
    }

    private static Item build(FoodDefinition def, Identifier id) {
        FoodProperties foodProperties = new FoodProperties.Builder()
                .nutrition(FORCED_FOOD_LEVEL)
                .saturationModifier(FORCED_SATURATION / (FORCED_FOOD_LEVEL * 2f))
                .alwaysEdible()
                .build();

        Consumable.Builder consumableBuilder = Consumable.builder()
                .consumeSeconds(def.eatSeconds());
        for (FoodEffect fx : def.effects()) {
            MobEffectInstance instance = fx.toInstance();
            consumableBuilder.onConsume(new ApplyStatusEffectsConsumeEffect(instance, fx.probability()));
        }
        Consumable consumable = consumableBuilder.build();

        Item.Properties properties = new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, id))
                .food(foodProperties, consumable);

        return new Item(properties);
    }
}
