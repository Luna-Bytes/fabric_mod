package dev.lunabytes.food;

import java.util.List;

public record FoodDefinition(
        String id,
        String displayName,
        float healHearts,
        float eatSeconds,
        List<FoodEffect> effects,
        List<FoodRecipe> recipes
) {

    public static FoodDefinition plain(
            String id,
            String displayName,
            float healHearts,
            FoodRecipe... recipes
    ) {
        return new FoodDefinition(
                id,
                displayName,
                healHearts,
                1.6f,
                List.of(),
                List.of(recipes)
        );
    }

    public static FoodDefinition plain(
            String id,
            String displayName,
            float healHearts,
            List<FoodRecipe> recipes
    ) {
        return new FoodDefinition(
                id,
                displayName,
                healHearts,
                1.6f,
                List.of(),
                recipes
        );
    }

    public static FoodDefinition withEffect(
            String id,
            String displayName,
            float healHearts,
            FoodEffect effect,
            FoodRecipe... recipes
    ) {
        return new FoodDefinition(
                id,
                displayName,
                healHearts,
                1.6f,
                List.of(effect),
                List.of(recipes)
        );
    }

    public static FoodDefinition withEffect(
            String id,
            String displayName,
            float healHearts,
            FoodEffect effect,
            List<FoodRecipe> recipes
    ) {
        return new FoodDefinition(
                id,
                displayName,
                healHearts,
                1.6f,
                List.of(effect),
                recipes
        );
    }

    public static FoodDefinition withEffects(
            String id,
            String displayName,
            float healHearts,
            List<FoodEffect> effects,
            FoodRecipe... recipes
    ) {
        return new FoodDefinition(
                id,
                displayName,
                healHearts,
                1.6f,
                effects,
                List.of(recipes)
        );
    }

    public static FoodDefinition withEffects(
            String id,
            String displayName,
            float healHearts,
            List<FoodEffect> effects,
            List<FoodRecipe> recipes
    ) {
        return new FoodDefinition(
                id,
                displayName,
                healHearts,
                1.6f,
                effects,
                recipes
        );
    }
}
