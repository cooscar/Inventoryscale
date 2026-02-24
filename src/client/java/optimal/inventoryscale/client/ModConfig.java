package optimal.inventoryscale.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger("inventoryscale");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("inventoryscale.json");

    public float inventoryScale = 1.0f;
    public float containerScale = 1.0f;
    public float tooltipScale   = 1.0f;

    private static ModConfig INSTANCE = new ModConfig();

    public static ModConfig get() { return INSTANCE; }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                ModConfig loaded = GSON.fromJson(reader, ModConfig.class);
                if (loaded != null) {
                    INSTANCE = loaded;
                    clamp(INSTANCE);
                }
            } catch (IOException e) {
                LOGGER.warn("Failed to load InventoryScale config, using defaults", e);
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            LOGGER.warn("Failed to save InventoryScale config", e);
        }
    }

    private static void clamp(ModConfig cfg) {
        cfg.inventoryScale = clamp(cfg.inventoryScale, 0.5f, 3.0f);
        cfg.containerScale = clamp(cfg.containerScale, 0.5f, 3.0f);
        cfg.tooltipScale   = clamp(cfg.tooltipScale,   0.5f, 3.0f);
    }

    private static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }
}
