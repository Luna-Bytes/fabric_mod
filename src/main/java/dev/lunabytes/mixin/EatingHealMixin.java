package dev.lunabytes.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class EatingHealMixin {

    @Inject(method = "completeUsingItem", at = @At("HEAD"))
    private void healAfterEating(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        ItemStack stack = player.getUseItem();

        FoodProperties food = stack.get(DataComponents.FOOD);

        if (food != null) {
            player.heal(food.saturation() * 0.5F);
        }
    }
}