package dev.lunabytes.fish;


public enum FishRarity {
    COMMON(1, 0xAAAAAA),    // gray
    UNCOMMON(2, 0x55FF55),  // green
    RARE(3, 0x55FFFF),      // aqua
    EPIC(4, 0xFF55FF);      // light_purple

    private final int stars;
    private final int color;

    FishRarity(int stars, int color) {
        this.stars = stars;
        this.color = color;
    }

    public String starText() {
        return "⭐".repeat(stars);
    }

    public int color() {
        return color;
    }
}