package optimal.inventoryscale.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import optimal.inventoryscale.client.gui.ConfigScreen;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryscaleClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("inventoryscale");
    public static KeyBinding configKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("InventoryScale loading...");

        ModConfig.load();

        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.inventoryscale.openConfig",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                KeyBinding.Category.create(Identifier.of("inventoryscale", "misc"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKey.wasPressed() && client.player != null) {
                client.setScreen(new ConfigScreen(client.currentScreen));
            }
        });

        LOGGER.info("InventoryScale ready! Press [I] to configure.");
    }
}
