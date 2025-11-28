package me.noramibu.increasedchatheight;

import com.mojang.serialization.Codec;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

public class ChatHeightSliderCallbacks implements SimpleOption.SliderCallbacks<Double> {
    public static final ChatHeightSliderCallbacks FOCUSED = new ChatHeightSliderCallbacks(4.0);
    public static final ChatHeightSliderCallbacks UNFOCUSED = new ChatHeightSliderCallbacks(4.0);
    public static final ChatHeightSliderCallbacks SCALE = new ChatHeightSliderCallbacks(5.0);

    private final double max;

    private ChatHeightSliderCallbacks(double max) {
        this.max = max;
    }

    @Override
    public Optional<Double> validate(Double value) {
        return value >= 0.0 && value <= this.max ? Optional.of(value) : Optional.empty();
    }

    @Override
    public double toSliderProgress(Double value) {
        return MathHelper.clamp(value / this.max, 0.0, 1.0);
    }

    @Override
    public Double toValue(double progress) {
        return progress * this.max;
    }

    @Override
    public Codec<Double> codec() {
        return Codec.doubleRange(0.0, this.max);
    }
} 