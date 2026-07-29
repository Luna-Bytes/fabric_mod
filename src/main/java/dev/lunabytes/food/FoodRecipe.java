package dev.lunabytes.food;

import java.util.List;
import java.util.Map;

public sealed interface FoodRecipe {

    record None() implements FoodRecipe {}

    record Shapeless(List<IngredientRef> ingredients, int outputCount) implements FoodRecipe {
        public Shapeless(List<IngredientRef> ingredients) {
            this(ingredients, 1);
        }
    }

    record Shaped(String[] pattern, Map<Character, IngredientRef> key, int outputCount) implements FoodRecipe {
        public Shaped(String[] pattern, Map<Character, IngredientRef> key) {
            this(pattern, key, 1);
        }
    }

    record Cooking(IngredientRef input, float experience, int cookTimeTicks, CookingType type) implements FoodRecipe {
        public enum CookingType { SMELTING, SMOKING, CAMPFIRE }
    }
}