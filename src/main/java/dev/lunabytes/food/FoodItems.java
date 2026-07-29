package dev.lunabytes.food;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import java.util.*;


public final class FoodItems {

    public static final int FORCED_FOOD_LEVEL = 4;
    public static final float FORCED_SATURATION = 2.0f;

    private static final Map<String, Item> REGISTERED = new LinkedHashMap<>();

    public static final Set<Item> HANDLED_ITEMS = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());

    private static final Map<Item, FoodDefinition> DEFINITIONS_BY_ITEM = new java.util.IdentityHashMap<>();

    // ------------------------------------------------------------------
    // CONTENT LIST
    // ------------------------------------------------------------------
    public static final List<FoodDefinition> DEFINITIONS = List.of(
            FoodDefinition.withEffect(
                    "milk_bottle",
                    "Milk Bottle",
                    0F,
                    FoodEffect.clearAll(),
                    () -> Items.GLASS_BOTTLE,
                    new FoodRecipe.Shapeless(List.of(
                            IngredientRef.item(Items.MILK_BUCKET),
                            IngredientRef.item(Items.GLASS_BOTTLE),
                            IngredientRef.item(Items.GLASS_BOTTLE),
                            IngredientRef.item(Items.GLASS_BOTTLE),
                            IngredientRef.item(Items.GLASS_BOTTLE)
                    ),4)
            ),
            FoodDefinition.plain(
                    "flour",
                    "Flour",
                    0F,
                    new FoodRecipe.Shapeless(List.of(
                            IngredientRef.item(Items.WHEAT_SEEDS),
                            IngredientRef.item(Items.WHEAT_SEEDS),
                            IngredientRef.item(Items.WHEAT_SEEDS)
                    )),
                    new FoodRecipe.Shapeless(List.of(
                            IngredientRef.item(() -> FoodItems.get("flour_bag"))
                    ), 9)
            ),
            FoodDefinition.plain(
                    "flour_bag",
                    "Flour Bag",
                    0F,
                    new FoodRecipe.Shaped(
                            new String[] {
                                    "###",
                                    "###",
                                    "###"
                            },
                            Map.of(
                                    '#', IngredientRef.item(() -> FoodItems.get("flour"))
                            )
                    )
            ),
            FoodDefinition.plain(
                    "dough",
                    "Dough",
                    0f,
                    new FoodRecipe.Shapeless(List.of(
                            IngredientRef.item(() -> FoodItems.get("flour")),
                            IngredientRef.item(Items.POTION)
                    )),
                    new FoodRecipe.Shapeless(List.of(
                            IngredientRef.item(() -> FoodItems.get("flour")),
                            IngredientRef.item(() -> FoodItems.get("flour")),
                            IngredientRef.item(() -> FoodItems.get("flour")),
                            IngredientRef.tag(ItemTags.EGGS)
                    ), 3)
            ),
            FoodDefinition.plain(
                    "cheese",
                    "Cheese",
                    1.5f,
                    new FoodRecipe.Shapeless(List.of(
                            IngredientRef.item(() -> FoodItems.get("milk_bottle")),
                            IngredientRef.item(Items.BROWN_MUSHROOM),
                            IngredientRef.item(Items.SUGAR)
                    ))
            ),
            FoodDefinition.withEffect(
                    "bread",
                    "Bread",
                    2f,
                    FoodEffect.clearAll(),
                    new FoodRecipe.Cooking(
                            IngredientRef.item(() -> FoodItems.get("dough")),
                            0.35f,
                            200,
                            FoodRecipe.Cooking.CookingType.SMELTING
                    ),
                    new FoodRecipe.Cooking(
                            IngredientRef.item(() -> FoodItems.get("dough")),
                            0.35f,
                            200,
                            FoodRecipe.Cooking.CookingType.CAMPFIRE
                    )
            ),
            FoodDefinition.plain(
                    "naan",
                    "Naan",
                    1f,
                    new FoodRecipe.Cooking(
                            IngredientRef.item(() -> FoodItems.get("dough")),
                            0.35f,
                            100,
                            FoodRecipe.Cooking.CookingType.SMOKING
                    )
            ),
            FoodDefinition.withEffect(
                    "baked_apple",
                    "Baked Apple",
                    2f,
                    FoodEffect.always(MobEffects.REGENERATION,200,1),
                    new FoodRecipe.Cooking(
                            IngredientRef.item(Items.APPLE),
                            0.35f,
                            200,
                            FoodRecipe.Cooking.CookingType.SMELTING
                    ),
                    new FoodRecipe.Cooking(
                            IngredientRef.item(Items.APPLE),
                            0.35f,
                            200,
                            FoodRecipe.Cooking.CookingType.CAMPFIRE
                    )
            ),
            FoodDefinition.withEffect(
                    "baked_golden_apple",
                    "Baked Golden Apple",
                    4f,
                    FoodEffect.always(MobEffects.REGENERATION, 1200, 1),
                    new FoodRecipe.Cooking(
                            IngredientRef.item(Items.GOLDEN_APPLE),
                            0.35f,
                            200,
                            FoodRecipe.Cooking.CookingType.SMELTING
                    ),
                    new FoodRecipe.Cooking(
                            IngredientRef.item(Items.GOLDEN_APPLE),
                            0.35f,
                            200,
                            FoodRecipe.Cooking.CookingType.CAMPFIRE
                    )
            ),
            FoodDefinition.plain(
                    "baked_potato",
                    "Baked Potato",
                    2.5f,
                    new FoodRecipe.Cooking(
                            IngredientRef.item(Items.POTATO),
                            0.35f,
                            200,
                            FoodRecipe.Cooking.CookingType.SMELTING
                    ),
                    new FoodRecipe.Cooking(
                            IngredientRef.item(Items.POTATO),
                            0.35f,
                            200,
                            FoodRecipe.Cooking.CookingType.CAMPFIRE
                    )
            ),
            FoodDefinition.withEffect(
                    "baked_pumpkin",
                    "Baked Pumpkin",
                    1f,
                    FoodEffect.always(MobEffects.RESISTANCE, 200, 1),
                    new FoodRecipe.Cooking(
                            IngredientRef.item(Items.PUMPKIN),
                            0.35f,
                            200,
                            FoodRecipe.Cooking.CookingType.SMELTING
                    ),
                    new FoodRecipe.Cooking(
                            IngredientRef.item(Items.PUMPKIN),
                            0.35f,
                            200,
                            FoodRecipe.Cooking.CookingType.CAMPFIRE
                    )
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
            if (fx.isCleanse()) {
                consumableBuilder.onConsume(new ClearAllStatusEffectsConsumeEffect());
            } else {
                MobEffectInstance instance = fx.toInstance();
                consumableBuilder.onConsume(new ApplyStatusEffectsConsumeEffect(instance, fx.probability()));
            }
        }

        Consumable consumable = consumableBuilder.build();

        List<Component> loreLines = new ArrayList<>();

        int halfHearts = Math.round(def.healHearts() * 2);
        StringBuilder hearts = new StringBuilder();
        hearts.repeat("❤", Math.max(0, halfHearts/2));
        if (halfHearts % 2 == 1) {
            hearts.append("❣");
        }
        if (halfHearts != 0) {
            loreLines.add(Component.literal(hearts.toString())
                    .withStyle(style -> style.withColor(0xFF0000).withItalic(false)));
        }

        for (FoodEffect fx : def.effects()) {
            if (fx.isCleanse()) {
                loreLines.add(Component.literal("\uD83E\uDDEA Removes All Effects")
                        .withStyle(style -> style.withColor(0xA8D8EA).withItalic(false)));
            } else {
                MobEffectInstance instance = fx.toInstance();
                String effectName = instance.getEffect().value().getDisplayName().getString();
                int durationSeconds = instance.getDuration() / 20;
                int minutes = durationSeconds / 60;
                int seconds = durationSeconds % 60;
                String duration = String.format("%d:%02d", minutes, seconds);
                int amplifier = instance.getAmplifier() + 1;

                String icon = getEffectIcon(instance.getEffect());
                String roman = toRoman(amplifier);

                loreLines.add(Component.literal(icon + " " + effectName + roman + "(" + duration + ")")
                        .withStyle(style -> style.withColor(getEffectColor(instance.getEffect())).withItalic(false)));
            }
        }

        ItemLore lore = new ItemLore(loreLines);

        Item.Properties properties = new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, id))
                .food(foodProperties, consumable)
                .component(DataComponents.LORE, lore);

        // FIX: Use usingConvertsTo instead of manually setting USE_REMAINDER
        if (def.useRemainder() != null) {
            properties.usingConvertsTo(def.useRemainder().get());
        }

        return new Item(properties);
    }

    private static String getEffectIcon(net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect) {
        if (effect == MobEffects.STRENGTH) return "⚔";
        if (effect == MobEffects.GLOWING) return "✨";
        if (effect == MobEffects.REGENERATION) return "❤";
        if (effect == MobEffects.RESISTANCE) return "\uD83D\uDEE1";
        if (effect == MobEffects.SPEED) return "⚡";
        if (effect == MobEffects.FIRE_RESISTANCE) return "\uD83D\uDD25";
        return "•";
    }

    private static int getEffectColor(net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect) {
        if (effect == MobEffects.STRENGTH) return 0xFF6B6B;
        if (effect == MobEffects.GLOWING) return 0xFFE66D;
        if (effect == MobEffects.REGENERATION) return 0xE85E8F;
        if (effect == MobEffects.RESISTANCE) return 0x999999;
        if (effect == MobEffects.SPEED) return 0x33CCFF;
        if (effect == MobEffects.FIRE_RESISTANCE) return 0xFF6600;
        return 0xAAAAAA;
    }

    private static String toRoman(int num) {
        return switch (num) {
            case 1 -> " ";
            case 2 -> " II ";
            case 3 -> " III ";
            case 4 -> " IV ";
            case 5 -> " V ";
            default -> String.valueOf(num);
        };
    }
}