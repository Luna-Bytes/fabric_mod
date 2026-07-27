package dev.lunabytes.food;

import java.util.List;

public record FoodDefinition(
        String id,
        float eatSeconds,
        float healHearts,
        List<FoodEffect> effects,
        FoodRecipe recipe
) {
    public static FoodDefinition plain(
            String id,
            float healHearts,
            FoodRecipe recipe
    ) {
        return new FoodDefinition(id, 1.6f, healHearts, List.of(), recipe);
    }

    public static FoodDefinition withEffect(
            String id,
            float healHearts,
            FoodEffect effect,
            FoodRecipe recipe
    ) {
        return new FoodDefinition(id, 1.6f, healHearts, List.of(effect), recipe);
    }
}