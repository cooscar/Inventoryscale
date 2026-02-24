package optimal.inventoryscale.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import optimal.inventoryscale.client.ModConfig;

/**
 * In-game config screen with three sliders:
 *   Inventory Scale, Container Scale, Tooltip Scale
 * Open with the keybind (default: I while a GUI is open, or via the mod's tick handler).
 */
public class ConfigScreen extends Screen {

    private static final int PANEL_W  = 310;
    private static final int PANEL_H  = 230;
    private static final float MIN    = 0.5f;
    private static final float MAX    = 3.0f;
    private static final int SLIDER_W = 250;
    private static final int SLIDER_H = 20;

    private final Screen parent;

    // Working copies — only written to config on Done
    private float inventoryScale;
    private float containerScale;
    private float tooltipScale;

    private ScaleSliderWidget invSlider;
    private ScaleSliderWidget ctnSlider;
    private ScaleSliderWidget tipSlider;

    public ConfigScreen(Screen parent) {
        super(Text.literal("InventoryScale Config"));
        this.parent        = parent;
        this.inventoryScale = ModConfig.get().inventoryScale;
        this.containerScale = ModConfig.get().containerScale;
        this.tooltipScale   = ModConfig.get().tooltipScale;
    }

    @Override
    protected void init() {
        int px = (width  - PANEL_W) / 2;
        int py = (height - PANEL_H) / 2;
        int sx = px + (PANEL_W - SLIDER_W) / 2;

        // Inventory slider  (row 1)
        invSlider = new ScaleSliderWidget(sx, py + 55, SLIDER_W, SLIDER_H,
                inventoryScale, MIN, MAX, v -> inventoryScale = v);
        addDrawableChild(invSlider);

        // Container slider (row 2)
        ctnSlider = new ScaleSliderWidget(sx, py + 105, SLIDER_W, SLIDER_H,
                containerScale, MIN, MAX, v -> containerScale = v);
        addDrawableChild(ctnSlider);

        // Tooltip slider   (row 3)
        tipSlider = new ScaleSliderWidget(sx, py + 155, SLIDER_W, SLIDER_H,
                tooltipScale, MIN, MAX, v -> tooltipScale = v);
        addDrawableChild(tipSlider);

        // Done / Cancel buttons
        int btnY = py + PANEL_H - 26;
        int mid  = px + PANEL_W / 2;

        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, btn -> {
            commit();
            close();
        }).dimensions(mid - 104, btnY, 100, 20).build());

        addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, btn -> close())
                .dimensions(mid + 4, btnY, 100, 20).build());
    }

    private void commit() {
        ModConfig cfg       = ModConfig.get();
        cfg.inventoryScale  = inventoryScale;
        cfg.containerScale  = containerScale;
        cfg.tooltipScale    = tooltipScale;
        ModConfig.save();
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        renderBackground(ctx, mx, my, delta);

        int px = (width  - PANEL_W) / 2;
        int py = (height - PANEL_H) / 2;
        int sx = px + (PANEL_W - SLIDER_W) / 2;

        // Panel background + border
        ctx.fill(px, py, px + PANEL_W, py + PANEL_H, 0xCC101010);
        ctx.drawBorder(px, py, PANEL_W, PANEL_H, 0xFF444466);

        // Title
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("\u2699 InventoryScale"),
                width / 2, py + 12, 0xAABBFF);

        // Divider line under title
        ctx.fill(px + 10, py + 26, px + PANEL_W - 10, py + 27, 0xFF333355);

        int labelColor = 0xCCCCCC;
        // Slider labels — drawn just above each slider
        ctx.drawTextWithShadow(textRenderer, Text.literal("Inventory Scale"),
                sx, py + 42, labelColor);
        ctx.drawTextWithShadow(textRenderer, Text.literal("Container Scale"),
                sx, py + 92, labelColor);
        ctx.drawTextWithShadow(textRenderer, Text.literal("Tooltip Scale"),
                sx, py + 142, labelColor);

        // Hint
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("Done saves  \u2022  Escape / Cancel discards"),
                width / 2, py + PANEL_H - 44, 0x888888);

        super.render(ctx, mx, my, delta);
    }

    @Override
    public void close() {
        assert client != null;
        client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() { return false; }
}
