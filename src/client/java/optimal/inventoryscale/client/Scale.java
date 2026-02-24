package optimal.inventoryscale.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

/**
 * Returns the correct scale factor based on which screen is currently open.
 */
public class Scale {

    /** GUI scale to apply this frame. Returns 1.0 if nothing is open. */
    public static float getGuiScale() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen == null) return 1.0f;
        ModConfig cfg = ModConfig.get();
        if (client.currentScreen instanceof InventoryScreen) {
            return cfg.inventoryScale;
        }
        return cfg.containerScale;
    }

    public static float getTooltipScale() {
        return ModConfig.get().tooltipScale;
    }

    public static boolean isScaled() {
        return getGuiScale() != 1.0f;
    }
}
