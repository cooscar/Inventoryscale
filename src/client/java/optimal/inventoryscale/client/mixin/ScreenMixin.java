package optimal.inventoryscale.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import optimal.inventoryscale.client.Scale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow protected int width;
    @Shadow protected int height;

    @Inject(
        method = "init(Lnet/minecraft/client/MinecraftClient;II)V",
        at = @At(value = "FIELD",
                 target = "Lnet/minecraft/client/gui/screen/Screen;height:I",
                 shift = At.Shift.AFTER)
    )
    private void onInitAfterSize(MinecraftClient mc, int w, int h, CallbackInfo ci) {
        float s = Scale.getGuiScale();
        if (s == 1.0f) return;
        this.width  = Math.round(w / s);
        this.height = Math.round(h / s);
    }

    @Inject(
        method = "resize",
        at = @At(value = "FIELD",
                 target = "Lnet/minecraft/client/gui/screen/Screen;height:I",
                 shift = At.Shift.AFTER)
    )
    private void onResizeAfterSize(MinecraftClient mc, int w, int h, CallbackInfo ci) {
        float s = Scale.getGuiScale();
        if (s == 1.0f) return;
        this.width  = Math.round(w / s);
        this.height = Math.round(h / s);
    }
}
