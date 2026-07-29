package dev.lunabytes.fish;

import dev.lunabytes.food.FoodEffect;

import java.util.List;
import java.util.Locale;

public record FishDefinition(String id, String displayName, FishRarity rarity, List<FoodEffect> effects) {

    public static FishDefinition of(String id, FishRarity rarity) {
        return new FishDefinition(id, titleCase(id), rarity, List.of());
    }

    public static FishDefinition of(String id, String displayName, FishRarity rarity) {
        return new FishDefinition(id, displayName, rarity, List.of());
    }

    public static FishDefinition withEffects(String id, FishRarity rarity, List<FoodEffect> effects) {
        return new FishDefinition(id, titleCase(id), rarity, effects);
    }

    private static String titleCase(String id) {
        String[] parts = id.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            if (!sb.isEmpty()) sb.append(' ');
            sb.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1).toLowerCase(Locale.ROOT));
        }
        return sb.toString();
    }
}