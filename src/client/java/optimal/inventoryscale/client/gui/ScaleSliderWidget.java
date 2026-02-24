package optimal.inventoryscale.client.gui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ScaleSliderWidget extends SliderWidget {

    private final float min;
    private final float max;
    private final Consumer<Float> changeListener;

    public ScaleSliderWidget(int x, int y, int width, int height,
                              float initialValue, float min, float max,
                              Consumer<Float> changeListener) {
        super(x, y, width, height, Text.empty(),
                (double)(initialValue - min) / (max - min));
        this.min = min;
        this.max = max;
        this.changeListener = changeListener;
        updateMessage();
    }

    public float getFloatValue() {
        float raw = min + (float) value * (max - min);
        return Math.round(raw * 100f) / 100f;
    }

    @Override
    protected void updateMessage() {
        setMessage(Text.literal(String.format("%.2fx", getFloatValue())));
    }

    @Override
    protected void applyValue() {
        changeListener.accept(getFloatValue());
    }
}
