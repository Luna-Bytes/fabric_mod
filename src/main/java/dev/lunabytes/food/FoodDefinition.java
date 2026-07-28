package dev.lunabytes.food;

import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public record FoodDefinition(
        String id,
        String displayName,
        float healHearts,
        float eatSeconds,
        List<FoodEffect> effects,
        List<FoodRecipe> recipes,
        Supplier<Item> useRemainder
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
                List.of(recipes),
                null
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
                recipes,
                null
        );
    }

    public static FoodDefinition plain(
            String id,
            String displayName,
            float healHearts,
            Supplier<Item> useRemainder,
            FoodRecipe... recipes
    ) {
        return new FoodDefinition(
                id,
                displayName,
                healHearts,
                1.6f,
                List.of(),
                List.of(recipes),
                useRemainder
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
                List.of(recipes),
                null
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
                recipes,
                null
        );
    }

    public static FoodDefinition withEffect(
            String id,
            String displayName,
            float healHearts,
            FoodEffect effect,
            Supplier<Item> useRemainder,
            FoodRecipe... recipes
    ) {
        return new FoodDefinition(
                id,
                displayName,
                healHearts,
                1.6f,
                List.of(effect),
                List.of(recipes),
                useRemainder
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
                List.of(recipes),
                null
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
                recipes,
                null
        );
    }

    public static FoodDefinition withEffects(
            String id,
            String displayName,
            float healHearts,
            List<FoodEffect> effects,
            Supplier<Item> useRemainder,
            FoodRecipe... recipes
    ) {
        return new FoodDefinition(
                id,
                displayName,
                healHearts,
                1.6f,
                effects,
                List.of(recipes),
                useRemainder
        );
    }
}