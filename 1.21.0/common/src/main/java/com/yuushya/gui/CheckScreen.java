package com.yuushya.gui;

import com.yuushya.registries.YuushyaConfig;
import com.yuushya.utils.CheckFileUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.NoticeWithLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yuushya.utils.CheckFileUtils.*;

public class CheckScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui.yuushya.checkscreen.header").withStyle(ChatFormatting.BOLD);
    private static final Component NARRATION = TITLE.copy().append("\n");
    private final Screen previous;
    private static final int MESSAGE_PADDING = 100;
    private final Component check;
    private final Component message = Component.translatable("gui.yuushya.checkscreen.message");
    protected Checkbox stopShowing;
    private final FrameLayout layout;
    private final List<InfoPanel> resourcepackPanel = new ArrayList<>();
    private final List<InfoPanel> recommendPanel = new ArrayList<>();
    private final List<InfoPanel> ctmPanel = new ArrayList<>();

    public CheckScreen(Screen previous) {
        super(TITLE);
        this.check = Component.translatable("multiplayerWarning.check");
        this.layout = new FrameLayout(0, 0, this.width, this.height);
        this.previous = previous;
    }

    @Override
    protected void init() {
        LinearLayout linearLayout = this.layout.addChild(LinearLayout.vertical().spacing(8));
        linearLayout.setY(this.font.lineHeight);
        linearLayout.defaultCellSetting().alignHorizontallyCenter();
        linearLayout.addChild(new FocusableTextWidget(this.width - 100, this.message, this.font, 12), layoutSettings -> layoutSettings.padding(12));

        int yCurrent = linearLayout.getY()+linearLayout.getHeight();

        //Resource Pack
        for (CheckFileUtils.Info info : checkResourcePacks()) {
            InfoPanel infoPanel = new InfoPanel(info, this.font, 0, yCurrent);
            infoPanel.init(this);
            resourcepackPanel.add(infoPanel);
            yCurrent += 2*this.font.lineHeight;
        }
        //Recommend Mod
        yCurrent += 2*this.font.lineHeight;
        for (CheckFileUtils.Info info : checkRecommend()) {
            InfoPanel infoPanel = new InfoPanel(info, this.font, 0, yCurrent);
            infoPanel.init(this);
            recommendPanel.add(infoPanel);
            yCurrent += 2*this.font.lineHeight;
        }
        yCurrent += 2*this.font.lineHeight;
        //Connect Texture Mod
        for (CheckFileUtils.Info info : checkCTM()) {
            InfoPanel infoPanel = new InfoPanel(info, this.font, 0, yCurrent);
            infoPanel.init(this);
            ctmPanel.add(infoPanel);
            yCurrent += 2*this.font.lineHeight;
        }



        LinearLayout linearLayout2 = linearLayout.addChild(LinearLayout.vertical().spacing(8));
        linearLayout2.defaultCellSetting().alignHorizontallyCenter();
        this.stopShowing = linearLayout2.addChild(Checkbox.builder(this.check, this.font).build());

        LinearLayout linearLayout3 = linearLayout2.addChild(LinearLayout.horizontal().spacing(8));
        linearLayout3.addChild(Button.builder(CommonComponents.GUI_PROCEED, button -> {
            if (this.stopShowing.selected()) {
                YuushyaConfig.CLIENT_CONFIG.check = false;
                YuushyaConfig.CLIENT_CONFIG.update();
            }
            this.minecraft.setScreen(this.previous);
        }).build());

        this.layout.visitWidgets(guiEventListener -> {
            AbstractWidget cfr_ignored_0 = (AbstractWidget)this.addRenderableWidget(guiEventListener);
        });
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
//        if (this.messageWidget != null) {
//            this.messageWidget.setMaxWidth(this.width - 100);
//        }
        this.layout.arrangeElements();
        FrameLayout.centerInRectangle(this.layout, this.getRectangle());
    }

    @Override
    public Component getNarrationMessage() {
        return NARRATION;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.previous);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics,mouseX,mouseY,partialTick);
        guiGraphics.drawString(this.font,TITLE,0,0,0xFFFFFF);
        resourcepackPanel.forEach(panel->panel.render(guiGraphics,mouseX,mouseY,partialTick));
        recommendPanel.forEach(panel->panel.render(guiGraphics,mouseX,mouseY,partialTick));
        ctmPanel.forEach(panel->panel.render(guiGraphics,mouseX,mouseY,partialTick));
    }

    public static class InfoPanel{
        private final CheckFileUtils.Info info;
        private final List<Button> links = new ArrayList<>();
        private final Font font;
        public final int x;
        public final int y;
        private final MutableComponent descriptionLine;
        public InfoPanel(CheckFileUtils.Info info, Font font, int x, int y){
            this.info = info;
            this.font = font;
            this.x = x;
            this.y = y;
            this.descriptionLine = Component.translatable("gui.yuushya.checkscreen.description."+info.id())
                    .append(Component.literal(" : "))
                    .append(Component.translatable("gui.yuushya.checkscreen.name."+info.id()));
        }
        public void init(CheckScreen screen){
            int widthCurrent = 0;
            int yCurrent = y+font.lineHeight;
            for (Map.Entry<String, String> entry : info.link().entrySet()) {
                String text = entry.getKey();
                String url = entry.getValue();
                int widthText = font.width(text);
                links.add(
                        new PlainTextButton(x + widthCurrent, yCurrent, widthText, font.lineHeight,
                                Component.literal(text),
                                button -> Util.getPlatform().openUri(URI.create(url)),
                                font
                        )
                );
                widthCurrent += (widthText + 10);
            }
            links.forEach(screen::addRenderableWidget);
        }
        /*
        Recommend Resource:
        Yuushya Release : description...
        official curseforge
         */
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.drawString(this.font,this.descriptionLine,x,y,0xFFFFFF);
        }
    }

}
