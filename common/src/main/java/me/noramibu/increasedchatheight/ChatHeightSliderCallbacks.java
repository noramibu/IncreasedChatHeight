package me.noramibu.increasedchatheight;

import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.util.Mth;

import java.util.Optional;

public final class ChatHeightSliderCallbacks implements OptionInstance.SliderableValueSet<Double> {
    public static final ChatHeightSliderCallbacks FOCUSED = new ChatHeightSliderCallbacks(4.0);
    public static final ChatHeightSliderCallbacks UNFOCUSED = new ChatHeightSliderCallbacks(4.0);
    public static final ChatHeightSliderCallbacks SCALE = new ChatHeightSliderCallbacks(5.0);

    private final double max;

    private ChatHeightSliderCallbacks(double max) {
        this.max = max;
    }

    @Override
    public Optional<Double> validateValue(Double value) {
        return value >= 0.0 && value <= this.max ? Optional.of(value) : Optional.empty();
    }

    @Override
    public double toSliderValue(Double value) {
        return Mth.clamp(value / this.max, 0.0, 1.0);
    }

    @Override
    public Double fromSliderValue(double progress) {
        return progress * this.max;
    }

    @Override
    public Codec<Double> codec() {
        return Codec.doubleRange(0.0, this.max);
    }
}
