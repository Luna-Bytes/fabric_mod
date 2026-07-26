package dev.lunabytes.client.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud; // or whatever class contains extractFood
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class HideHungerMixin {

    @Inject(method = "extractFood", at = @At("HEAD"), cancellable = true)
    private void hideFood(
            GuiGraphicsExtractor graphics,
            Player player,
            int yLineBase,
            int xRight,
            CallbackInfo ci
    ) {
        ci.cancel();
    }
}