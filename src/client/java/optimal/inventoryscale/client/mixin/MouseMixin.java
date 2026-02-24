package optimal.inventoryscale.client.mixin;

import net.minecraft.client.Mouse;
import optimal.inventoryscale.client.Scale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @ModifyArg(
        method = "getScaledX(Lnet/minecraft/client/util/Window;D)D",
        at = @At(value = "INVOKE",
                 target = "Lnet/minecraft/client/util/Window;getScaledWidth()I")
    )
    private int fixScaledWidth(int original) {
        float s = Scale.getGuiScale();
        return s == 1.0f ? original : Math.round(original / s);
    }

    @ModifyArg(
        method = "getScaledY(Lnet/minecraft/client/util/Window;D)D",
        at = @At(value = "INVOKE",
                 target = "Lnet/minecraft/client/util/Window;getScaledHeight()I")
    )
    private int fixScaledHeight(int original) {
        float s = Scale.getGuiScale();
        return s == 1.0f ? original : Math.round(original / s);
    }
}
