package dev.lunabytes.datagen;

import dev.lunabytes.food.FoodDefinition;
import dev.lunabytes.food.FoodItems;
import dev.lunabytes.food.FoodRecipe;
import dev.lunabytes.food.IngredientRef;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class LunaBytesRecipeProvider extends FabricRecipeProvider {

    public LunaBytesRecipeProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(
            HolderLookup.Provider registries,
            RecipeOutput exporter
    ) {
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);

        return new RecipeProvider(registries, exporter) {
            @Override
            public void buildRecipes() {
                for (FoodDefinition def : FoodItems.DEFINITIONS) {
                    Item result = FoodItems.getOrCreateForDatagen(def.id());

                    if (result == null) {
                        continue;
                    }

                    Map<String, Integer> typeCounts = new HashMap<>();

                    for (FoodRecipe recipe : def.recipes()) {
                        String typeKey = switch (recipe) {
                            case FoodRecipe.None ignored -> "none";
                            case FoodRecipe.Shapeless ignored -> "shapeless";
                            case FoodRecipe.Shaped ignored -> "shaped";
                            case FoodRecipe.Cooking cooking -> cooking.type().name().toLowerCase();
                        };

                        int count = typeCounts.getOrDefault(typeKey, 0);
                        typeCounts.put(typeKey, count + 1);

                        String suffix = typeKey + (count == 0 ? "" : "_" + (count + 1));
                        buildRecipe(def, result, recipe, items, exporter, suffix);
                    }
                }
            }

            private void buildRecipe(
                    FoodDefinition def,
                    Item result,
                    FoodRecipe recipe,
                    HolderGetter<Item> items,
                    RecipeOutput exporter,
                    String suffix
            ) {
                switch (recipe) {
                    case FoodRecipe.None ignored -> {
                    }

                    case FoodRecipe.Shapeless shapeless -> {
                        var builder = ShapelessRecipeBuilder.shapeless(
                                items,
                                RecipeCategory.FOOD,
                                result,
                                shapeless.outputCount()
                        );

                        for (IngredientRef ingredient : shapeless.ingredients()) {
                            addIngredient(builder, ingredient);
                        }

                        builder.unlockedBy(
                                "has_ingredient",
                                has(getUnlockItem(shapeless.ingredients().getFirst()))
                        );

                        builder.save(exporter, recipePath(def.id() + "_" + suffix));
                    }

                    case FoodRecipe.Shaped shaped -> {
                        var builder = ShapedRecipeBuilder.shaped(
                                items,
                                RecipeCategory.FOOD,
                                result,
                                shaped.outputCount()
                        );

                        for (String row : shaped.pattern()) {
                            builder.pattern(row);
                        }

                        for (Map.Entry<Character, IngredientRef> entry : shaped.key().entrySet()) {
                            addKey(builder, entry.getKey(), entry.getValue());
                        }

                        builder.unlockedBy(
                                "has_ingredient",
                                has(getUnlockItem(shaped.key().values().iterator().next()))
                        );

                        builder.save(exporter, recipePath(def.id() + "_" + suffix));
                    }

                    case FoodRecipe.Cooking cooking -> {
                        Ingredient ingredient = toVanillaIngredient(cooking.input());

                        var builder = switch (cooking.type()) {
                            case SMELTING -> SimpleCookingRecipeBuilder.smelting(
                                    ingredient,
                                    RecipeCategory.FOOD,
                                    CookingBookCategory.FOOD,
                                    result,
                                    cooking.experience(),
                                    cooking.cookTimeTicks()
                            );
                            case SMOKING -> SimpleCookingRecipeBuilder.smoking(
                                    ingredient,
                                    RecipeCategory.FOOD,
                                    result,
                                    cooking.experience(),
                                    cooking.cookTimeTicks()
                            );
                            case CAMPFIRE -> SimpleCookingRecipeBuilder.campfireCooking(
                                    ingredient,
                                    RecipeCategory.FOOD,
                                    result,
                                    cooking.experience(),
                                    cooking.cookTimeTicks()
                            );
                        };

                        builder.unlockedBy(
                                "has_ingredient",
                                has(getUnlockItem(cooking.input()))
                        ).save(exporter, recipePath(def.id() + "_" + suffix));
                    }
                }
            }

            // ------------------------------------------------------------------
            // IngredientRef helpers
            // ------------------------------------------------------------------

            private void addIngredient(ShapelessRecipeBuilder builder, IngredientRef ref) {
                switch (ref) {
                    case IngredientRef.OfItem ofItem ->
                            builder.requires(ofItem.item().get());
                    case IngredientRef.OfTag ofTag ->
                            builder.requires(Ingredient.of());
                }
            }

            private void addKey(ShapedRecipeBuilder builder, char symbol, IngredientRef ref) {
                switch (ref) {
                    case IngredientRef.OfItem ofItem ->
                            builder.define(symbol, ofItem.item().get());
                    case IngredientRef.OfTag ofTag ->
                            builder.define(symbol, Ingredient.of());
                }
            }

            private Ingredient toVanillaIngredient(IngredientRef ref) {
                return switch (ref) {
                    case IngredientRef.OfItem ofItem -> Ingredient.of(ofItem.item().get());
                    case IngredientRef.OfTag ofTag -> Ingredient.of();
                };
            }

            private Item getUnlockItem(IngredientRef ref) {
                return switch (ref) {
                    case IngredientRef.OfItem ofItem -> ofItem.item().get();
                    case IngredientRef.OfTag ofTag -> {
                        // Tags can't be used directly in advancement triggers.
                        // Return a representative item or null; Fabric's RecipeProvider
                        // handles tag-based unlocks via has(TagKey) overload below.
                        throw new IllegalStateException(
                                "Cannot use tag for advancement unlock. Use has(TagKey) instead."
                        );
                    }
                };
            }

            private String recipePath(String path) {
                return Identifier.fromNamespaceAndPath("lunabytes", path).toString();
            }
        };
    }

    @Override
    public String getName() {
        return "LunaBytes Recipes";
    }
}