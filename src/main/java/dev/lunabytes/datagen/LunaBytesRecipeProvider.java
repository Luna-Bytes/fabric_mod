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
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

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

                    for (FoodRecipe recipe : def.recipes()) {
                        buildRecipe(def, result, recipe, items, exporter);
                    }
                }
            }

            private void buildRecipe(
                    FoodDefinition def,
                    Item result,
                    FoodRecipe recipe,
                    HolderGetter<Item> items,
                    RecipeOutput exporter
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

                        for (Supplier<Item> ingredient : shapeless.ingredients()) {
                            builder.requires(ingredient.get());
                        }

                        builder.unlockedBy(
                                "has_ingredient",
                                has(shapeless.ingredients().getFirst().get())
                        );

                        builder.save(exporter, recipePath(def.id() + "_shapeless"));
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

                        for (Map.Entry<Character, Supplier<Item>> entry : shaped.key().entrySet()) {
                            builder.define(entry.getKey(), entry.getValue().get());
                        }

                        Supplier<Item> unlock = shaped.key().values().iterator().next();

                        builder.unlockedBy(
                                "has_ingredient",
                                has(unlock.get())
                        );

                        builder.save(exporter, recipePath(def.id() + "_shaped"));
                    }

                    case FoodRecipe.Cooking cooking -> {
                        String suffix = switch (cooking.type()) {
                            case SMELTING -> "_smelting";
                            case SMOKING -> "_smoking";
                            case CAMPFIRE -> "_campfire";
                        };

                        switch (cooking.type()) {
                            case SMELTING -> SimpleCookingRecipeBuilder.smelting(
                                            Ingredient.of(cooking.input().get()),
                                            RecipeCategory.FOOD,
                                            CookingBookCategory.FOOD,
                                            result,
                                            cooking.experience(),
                                            cooking.cookTimeTicks()
                                    )
                                    .unlockedBy("has_ingredient", has(cooking.input().get()))
                                    .save(exporter, recipePath(def.id() + suffix));

                            case SMOKING -> SimpleCookingRecipeBuilder.smoking(
                                            Ingredient.of(cooking.input().get()),
                                            RecipeCategory.FOOD,
                                            result,
                                            cooking.experience(),
                                            cooking.cookTimeTicks()
                                    )
                                    .unlockedBy("has_ingredient", has(cooking.input().get()))
                                    .save(exporter, recipePath(def.id() + suffix));

                            case CAMPFIRE -> SimpleCookingRecipeBuilder.campfireCooking(
                                            Ingredient.of(cooking.input().get()),
                                            RecipeCategory.FOOD,
                                            result,
                                            cooking.experience(),
                                            cooking.cookTimeTicks()
                                    )
                                    .unlockedBy("has_ingredient", has(cooking.input().get()))
                                    .save(exporter, recipePath(def.id() + suffix));
                        }
                    }
                }
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