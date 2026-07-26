package dev.lunabytes.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    @Shadow
    private int foodLevel;

    @Shadow
    private float saturationLevel;

    @Shadow
    private float exhaustionLevel;

    @Shadow
    private int tickTimer;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void disableHunger(ServerPlayer player, CallbackInfo ci) {
        // Keep hunger fixed at half a bar.
        this.foodLevel = 10;

        // No saturation or exhaustion.
        this.saturationLevel = 0.0F;
        this.exhaustionLevel = 0.0F;

        // Reset the regeneration/starvation timer.
        this.tickTimer = 0;

        // Skip all vanilla hunger processing.
        ci.cancel();
    }

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void blockFoodGain(int food, float saturation, CallbackInfo ci) {
        ci.cancel();
    }
}