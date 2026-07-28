package dev.lunabytes.food;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public record FoodEffect(
        Holder<MobEffect> effect,
        int durationTicks,
        int amplifier,
        float probability,
        boolean cleanse
) {

    public static FoodEffect always(Holder<MobEffect> effect, int durationTicks, int amplifier) {
        return new FoodEffect(effect, durationTicks, amplifier, 1.0f, false);
    }

    public static FoodEffect chance(Holder<MobEffect> effect, int durationTicks, int amplifier, float probability) {
        return new FoodEffect(effect, durationTicks, amplifier, probability, false);
    }

    public static FoodEffect clearAll() {
        return new FoodEffect(MobEffects.LUCK, 0, 0, 1.0f, true);
    }

    public static FoodEffect clearAll(float probability) {
        return new FoodEffect(MobEffects.LUCK, 0, 0, probability, true);
    }

    public MobEffectInstance toInstance() {
        if (cleanse) {
            throw new IllegalStateException("Cannot convert cleanse effect to MobEffectInstance");
        }
        return new MobEffectInstance(effect, durationTicks, amplifier);
    }

    public boolean isCleanse() {
        return cleanse;
    }
}