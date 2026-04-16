package me.noramibu.increasedchatheight.mixin;

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
public abstract class GameOptionsCommonMixin {

    @Shadow
    @Final
    @Mutable
    private OptionInstance<Double> chatHeightFocused;

    @Shadow
    @Final
    @Mutable
    private OptionInstance<Double> chatHeightUnfocused;

    @Shadow
    @Final
    @Mutable
    private OptionInstance<Double> chatScale;

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;load()V"))
    private void increasedChatHeight$modifyChatOptions(CallbackInfo ci) {
        this.chatHeightFocused = new OptionInstance<Double>(
                "options.chat.height.focused",
                OptionInstance.noTooltip(),
                (optionText, value) -> Component.translatable("options.pixel_value", optionText, (int) Math.round(value * 180.0)),
                OptionInstance.UnitDouble.INSTANCE.xmap(value -> value * 4.0, value -> value / 4.0),
                1.0,
                value -> {
                }
        );

        this.chatHeightUnfocused = new OptionInstance<Double>(
                "options.chat.height.unfocused",
                OptionInstance.noTooltip(),
                (optionText, value) -> value == 0.0
                        ? Component.translatable("options.generic_value", optionText, CommonComponents.OPTION_OFF)
                        : Component.translatable("options.pixel_value", optionText, (int) Math.round(value * 180.0)),
                OptionInstance.UnitDouble.INSTANCE.xmap(value -> value * 4.0, value -> value / 4.0),
                0.5,
                value -> {
                }
        );

        this.chatScale = new OptionInstance<Double>(
                "options.chat.scale",
                OptionInstance.noTooltip(),
                (optionText, value) -> value == 0.0
                        ? CommonComponents.optionStatus(optionText, false)
                        : Component.translatable("options.percent_value", optionText, (int) (value * 100.0)),
                OptionInstance.UnitDouble.INSTANCE.xmap(value -> value * 5.0, value -> value / 5.0),
                1.0,
                value -> Minecraft.getInstance().gui.getChat().rescaleChat()
        );
    }
}
