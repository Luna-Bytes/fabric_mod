package dev.lunabytes.food;

import java.util.List;

public record FoodDefinition(
        String id,
        String displayName,
        float healHearts,
        float eatSeconds,
        List<FoodEffect> effects,
        FoodRecipe recipe
) {

    public static FoodDefinition plain(
            String id,
            String displayName,
            float healHearts,
            FoodRecipe recipe
    ) {
        return new FoodDefinition(
                id,
                displayName,
                healHearts,
                1.6f,
                List.of(),
                recipe
        );
    }

    public static FoodDefinition withEffect(
            String id,
            String displayName,
            float healHearts,
            FoodEffect effect,
            FoodRecipe recipe
    ) {
        return new FoodDefinition(
                id,
                displayName,
                healHearts,
                1.6f,
                List.of(effect),
                recipe
        );
    }
}