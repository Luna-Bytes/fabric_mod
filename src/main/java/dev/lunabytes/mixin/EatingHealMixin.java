package dev.lunabytes.mixin;

import dev.lunabytes.fish.FishDefinition;
import dev.lunabytes.fish.FishItems;
import dev.lunabytes.food.FoodDefinition;
import dev.lunabytes.food.FoodItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
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

        FoodDefinition def = FoodItems.getDefinition(stack.getItem());
        FoodProperties food = stack.get(DataComponents.FOOD);
        FishDefinition fish_def = FishItems.getDefinition(stack.getItem());

        if (def != null) {
            player.heal(def.healHearts() * 2.0F);
        }else if (food != null && fish_def == null) {
            player.heal((float) (food.nutrition() / 2));
        }
    }
}