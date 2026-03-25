package me.noramibu.increasedchatheight.mixin.client;

import me.noramibu.increasedchatheight.ChatHeightSliderCallbacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public abstract class GameOptionsMixin {

    @Shadow @Final @Mutable
    private OptionInstance<Double> chatHeightFocused;

    @Shadow @Final @Mutable
    private OptionInstance<Double> chatHeightUnfocused;

    @Shadow @Final @Mutable
    private OptionInstance<Double> chatScale;

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;load()V"))
    private void modifyChatOptions(CallbackInfo ci) {
        this.chatHeightFocused = new OptionInstance<Double>(
                "options.chat.height.focused",
                OptionInstance.noTooltip(),
                (Component optionText, Double value) -> Component.translatable("options.pixel_value", optionText, (int)Math.round(value * 180.0)),
                ChatHeightSliderCallbacks.FOCUSED,
                ChatHeightSliderCallbacks.FOCUSED.codec(),
                1.0,
                (Double value) -> {}
        );

        this.chatHeightUnfocused = new OptionInstance<Double>(
                "options.chat.height.unfocused",
                OptionInstance.noTooltip(),
                (Component optionText, Double value) -> {
                    if (value == 0.0) {
                        return Component.translatable("options.generic_value", optionText, CommonComponents.OPTION_OFF);
                    }
                    return Component.translatable("options.pixel_value", optionText, (int)Math.round(value * 180.0));
                },
                ChatHeightSliderCallbacks.UNFOCUSED,
                ChatHeightSliderCallbacks.UNFOCUSED.codec(),
                0.5,
                (Double value) -> {}
        );

        this.chatScale = new OptionInstance<Double>(
                "options.chat.scale",
                OptionInstance.noTooltip(),
                (optionText, value) -> (Component)(value == 0.0 ? CommonComponents.optionStatus(optionText, false) : Component.translatable("options.percent_value", optionText, (int)(value * 100.0))),
                ChatHeightSliderCallbacks.SCALE,
                ChatHeightSliderCallbacks.SCALE.codec(),
                1.0,
                (Double value) -> Minecraft.getInstance().gui.getChat().rescaleChat()
        );
    }
}
