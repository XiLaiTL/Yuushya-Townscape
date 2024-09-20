package com.yuushya.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;

public class PlainTextButton extends Button {
    private final Font font;
    private final Component message;
    private final Component underlinedMessage;

    public PlainTextButton(int x, int y, int width, int height, Component component, Button.OnPress onPress, Font font) {
        super(x, y, width, height, component, onPress);
        this.font = font;
        this.message = component;
        this.underlinedMessage = ComponentUtils.mergeStyles(component.copy(), Style.EMPTY.withUnderlined(true));
    }

    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        Component component = (this.isHovered()||this.isFocused()) ? this.underlinedMessage : this.message;
        drawString(poseStack, this.font, component, this.x, this.y, 16777215 | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}

