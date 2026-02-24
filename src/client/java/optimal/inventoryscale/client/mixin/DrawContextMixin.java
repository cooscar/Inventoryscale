package optimal.inventoryscale.client.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.text.Text;
import optimal.inventoryscale.client.Scale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

/**
 * Wraps tooltip rendering with a matrix scale so tooltips obey the tooltip scale setting.
 */
@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Unique private boolean inventoryscale$scalingTooltip = false;

    @Inject(
        method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V",
        at = @At("HEAD")
    )
    private void preTooltip(TextRenderer tr, List<Text> lines,
                             Optional<TooltipData> data, int x, int y,
                             CallbackInfo ci) {
        float s = Scale.getTooltipScale();
        if (s == 1.0f) return;
        inventoryscale$scalingTooltip = true;
        DrawContext ctx = (DrawContext)(Object)this;
        ctx.getMatrices().push();
        ctx.getMatrices().translate(x, y, 0);
        ctx.getMatrices().scale(s, s, 1f);
        ctx.getMatrices().translate(-x, -y, 0);
    }

    @Inject(
        method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V",
        at = @At("TAIL")
    )
    private void postTooltip(TextRenderer tr, List<Text> lines,
                              Optional<TooltipData> data, int x, int y,
                              CallbackInfo ci) {
        if (!inventoryscale$scalingTooltip) return;
        inventoryscale$scalingTooltip = false;
        ((DrawContext)(Object)this).getMatrices().pop();
    }
}
