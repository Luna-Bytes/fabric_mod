package dev.lunabytes.datagen;

import dev.lunabytes.food.FoodDefinition;
import dev.lunabytes.food.FoodItems;
import dev.lunabytes.food.FoodRecipe;

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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import org.jspecify.annotations.NullMarked;

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
                    Item result = FoodItems.get(def.id());

                    if (result == null) {
                        continue;
                    }

                    switch (def.recipe()) {
                        case FoodRecipe.None ignored -> {
                        }

                        case FoodRecipe.Shapeless shapeless -> {
                            var builder = ShapelessRecipeBuilder.shapeless(
                                    items,
                                    RecipeCategory.FOOD,
                                    result,
                                    shapeless.outputCount()
                            );

                            for (Item ingredient : shapeless.ingredients()) {
                                builder.requires(ingredient);
                            }

                            builder.unlockedBy(
                                    "has_ingredient",
                                    has(shapeless.ingredients().getFirst())
                            );

                            builder.save(exporter);
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

                            for (Map.Entry<Character, Item> entry : shaped.key().entrySet()) {
                                builder.define(entry.getKey(), entry.getValue());
                            }

                            Item unlock = shaped.key().values().iterator().next();

                            builder.unlockedBy(
                                    "has_ingredient",
                                    has(unlock)
                            );

                            builder.save(exporter);
                        }

                        case FoodRecipe.Cooking cooking -> {
                            switch (cooking.type()) {
                                case SMELTING -> SimpleCookingRecipeBuilder.smelting(
                                                Ingredient.of(cooking.input()),
                                                RecipeCategory.FOOD,
                                                CookingBookCategory.FOOD,
                                                result,
                                                cooking.experience(),
                                                cooking.cookTimeTicks()
                                        )
                                        .unlockedBy("has_ingredient", has(cooking.input()))
                                        .save(exporter);

                                case SMOKING -> SimpleCookingRecipeBuilder.smoking(
                                                Ingredient.of(cooking.input()),
                                                RecipeCategory.FOOD,
                                                result,
                                                cooking.experience(),
                                                cooking.cookTimeTicks()
                                        )
                                        .unlockedBy("has_ingredient", has(cooking.input()))
                                        .save(exporter);

                                case CAMPFIRE -> SimpleCookingRecipeBuilder.campfireCooking(
                                                Ingredient.of(cooking.input()),
                                                RecipeCategory.FOOD,
                                                result,
                                                cooking.experience(),
                                                cooking.cookTimeTicks()
                                        )
                                        .unlockedBy("has_ingredient", has(cooking.input()))
                                        .save(exporter);
                            }
                        }
                    }
                }
            }
        };
    }

    @Override
    public String getName() {
        return "LunaBytes Recipes";
    }
}