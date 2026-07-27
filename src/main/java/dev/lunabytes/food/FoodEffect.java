package dev.lunabytes.food;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

/**
 * One status effect applied when a food item is eaten.
 *
 * @param effect      the vanilla or modded mob effect to apply
 * @param durationTicks how long the effect lasts (20 ticks = 1 second)
 * @param amplifier   0 = level I, 1 = level II, etc.
 * @param probability chance (0.0-1.0) that this effect actually triggers on eat.
 *                    Use 1.0f for a guaranteed effect (e.g. Glow Berries' Glowing),
 *                    lower for a "gamble" food like Suspicious Stew.
 */
public record FoodEffect(Holder<MobEffect> effect, int durationTicks, int amplifier, float probability) {

    public static FoodEffect always(Holder<MobEffect> effect, int durationTicks, int amplifier) {
        return new FoodEffect(effect, durationTicks, amplifier, 1.0f);
    }

    public static FoodEffect chance(Holder<MobEffect> effect, int durationTicks, int amplifier, float probability) {
        return new FoodEffect(effect, durationTicks, amplifier, probability);
    }

    MobEffectInstance toInstance() {
        return new MobEffectInstance(effect, durationTicks, amplifier);
    }
}
