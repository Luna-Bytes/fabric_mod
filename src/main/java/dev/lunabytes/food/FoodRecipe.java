package dev.lunabytes.food;

import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public sealed interface FoodRecipe {

    record None() implements FoodRecipe {}

    record Shapeless(List<Supplier<Item>> ingredients, int outputCount) implements FoodRecipe {
        public Shapeless(List<Supplier<Item>> ingredients) {
            this(ingredients, 1);
        }
    }

    record Shaped(String[] pattern, Map<Character, Supplier<Item>> key, int outputCount) implements FoodRecipe {
        public Shaped(String[] pattern, Map<Character, Supplier<Item>> key) {
            this(pattern, key, 1);
        }
    }

    record Cooking(Supplier<Item> input, float experience, int cookTimeTicks, CookingType type) implements FoodRecipe {
        public enum CookingType { SMELTING, SMOKING, CAMPFIRE }
    }
}