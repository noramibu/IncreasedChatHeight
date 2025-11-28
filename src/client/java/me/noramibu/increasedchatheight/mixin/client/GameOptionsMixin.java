package me.noramibu.increasedchatheight.mixin.client;

import me.noramibu.increasedchatheight.ChatHeightSliderCallbacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {

    @Shadow @Final @Mutable
    private SimpleOption<Double> chatHeightFocused;

    @Shadow @Final @Mutable
    private SimpleOption<Double> chatHeightUnfocused;

    @Shadow @Final @Mutable
    private SimpleOption<Double> chatScale;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;load()V"))
    private void modifyChatOptions(CallbackInfo ci) {
        this.chatHeightFocused = new SimpleOption<Double>(
                "options.chat.height.focused",
                SimpleOption.emptyTooltip(),
                (Text optionText, Double value) -> Text.translatable("options.pixel_value", optionText, (int)Math.round(value * 180.0)),
                ChatHeightSliderCallbacks.FOCUSED,
                ChatHeightSliderCallbacks.FOCUSED.codec(),
                1.0,
                (Double value) -> {}
        );

        this.chatHeightUnfocused = new SimpleOption<Double>(
                "options.chat.height.unfocused",
                SimpleOption.emptyTooltip(),
                (Text optionText, Double value) -> {
                    if (value == 0.0) {
                        return Text.translatable("options.generic_value", optionText, Text.translatable("options.off"));
                    }
                    return Text.translatable("options.pixel_value", optionText, (int)Math.round(value * 180.0));
                },
                ChatHeightSliderCallbacks.UNFOCUSED,
                ChatHeightSliderCallbacks.UNFOCUSED.codec(),
                0.5,
                (Double value) -> {}
        );

        this.chatScale = new SimpleOption<Double>(
                "options.chat.scale",
                SimpleOption.emptyTooltip(),
                (optionText, value) -> (Text)(value == 0.0 ? ScreenTexts.composeToggleText(optionText, false) : Text.translatable("options.percent_value", optionText, (int)(value * 100.0))),
                ChatHeightSliderCallbacks.SCALE,
                ChatHeightSliderCallbacks.SCALE.codec(),
                1.0,
                (Double value) -> MinecraftClient.getInstance().inGameHud.getChatHud().reset()
        );
    }
} 