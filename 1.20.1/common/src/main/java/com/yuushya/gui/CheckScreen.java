package com.yuushya.gui;

import com.yuushya.registries.YuushyaConfig;
import com.yuushya.utils.CheckFileUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.PlainTextButton;
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
    private static final Component TITLE = Component.translatable("gui.yuushya.checkScreen.header").withStyle(ChatFormatting.BOLD);
    private static final Component RESOURCEPACK = Component.translatable("gui.yuushya.checkScreen.resourcepack").withStyle(ChatFormatting.RED);
    private static final Component RECOMMEND = Component.translatable("gui.yuushya.checkScreen.recommend").withStyle(ChatFormatting.RED);
    private static final Component CTM = Component.translatable("gui.yuushya.checkScreen.ctm").withStyle(ChatFormatting.RED);
    private static final Component CHECK = Component.translatable("multiplayerWarning.check");
    private static final Component MESSAGE = Component.translatable("gui.yuushya.checkScreen.message");
    private static final Component CONGRATULATIONS = Component.translatable("gui.yuushya.checkScreen.congratulations");
    private Component message;
    private static final Component NARRATION = TITLE.copy().append("\n").append(MESSAGE);
    private final Screen previous;
    private static final int MESSAGE_PADDING = 100;


    protected Checkbox stopShowing;
    protected Button finishButton;

    private final List<InfoPanel> resourcepackPanel = new ArrayList<>();
    private final List<InfoPanel> recommendPanel = new ArrayList<>();
    private final List<InfoPanel> ctmPanel = new ArrayList<>();

    public CheckScreen(Screen previous) {
        super(TITLE);
        this.previous = previous;
    }

    public int center(int width){
        return (this.width - width)/2;
    }

    private int resourcepackY;
    private int recommendY;
    private int ctmY;

    @Override
    protected void init() {
        int yCurrent = 45;
        resourcepackY = yCurrent-this.font.lineHeight;
        //Resource Pack
        for (CheckFileUtils.Info info : checkResourcePacks()) {
            InfoPanel infoPanel = new InfoPanel(info, this.font, 20, yCurrent);
            infoPanel.init(this);
            resourcepackPanel.add(infoPanel);
            yCurrent += 2*this.font.lineHeight;
        }
        //Recommend Mod
        recommendY = yCurrent+this.font.lineHeight;
        yCurrent += 2*this.font.lineHeight;
        for (CheckFileUtils.Info info : checkRecommend()) {
            InfoPanel infoPanel = new InfoPanel(info, this.font, 20, yCurrent);
            infoPanel.init(this);
            recommendPanel.add(infoPanel);
            yCurrent += 2*this.font.lineHeight;
        }

        //Connect Texture Mod
        ctmY = yCurrent+this.font.lineHeight;
        yCurrent += 2*this.font.lineHeight;
        for (CheckFileUtils.Info info : checkCTM()) {
            InfoPanel infoPanel = new InfoPanel(info, this.font, 20, yCurrent);
            infoPanel.init(this);
            ctmPanel.add(infoPanel);
            yCurrent += 2*this.font.lineHeight;
        }

        this.message = resourcepackPanel.isEmpty() && recommendPanel.isEmpty() && ctmPanel.isEmpty()
                ? CONGRATULATIONS : MESSAGE;

        this.stopShowing = new Checkbox(this.width/4-30,this.height-3,this.font.width(CHECK),20,CHECK,false);

        this.finishButton = Button.builder(CommonComponents.GUI_PROCEED, button -> {
            if (this.stopShowing.selected()) {
                YuushyaConfig.CLIENT_CONFIG.check = false;
                YuushyaConfig.CLIENT_CONFIG.update();
            }
            this.minecraft.setScreen(this.previous);
        }).bounds(this.width/4*3 - 60 ,this.height-35,120,20).build();

        this.addRenderableWidget(this.stopShowing);
        this.addRenderableWidget(this.finishButton);
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
        guiGraphics.drawString(this.font,TITLE,center(font.width(TITLE)),10,0xFFFFFF);
        guiGraphics.drawString(this.font,this.message,center(font.width(this.message)),23,0xFFFFFF);
        if(!resourcepackPanel.isEmpty()) guiGraphics.drawString(this.font,RESOURCEPACK,0,resourcepackY,0xFFFFFF);
        if(!recommendPanel.isEmpty()) guiGraphics.drawString(this.font,RECOMMEND,0,recommendY,0xFFFFFF);
        if(!ctmPanel.isEmpty()) guiGraphics.drawString(this.font,CTM,0,ctmY,0xFFFFFF);
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
        private static final MutableComponent link = Component.translatable("gui.yuushya.checkScreen.link").append(Component.literal(" : "));
        public InfoPanel(CheckFileUtils.Info info, Font font, int x, int y){
            this.info = info;
            this.font = font;
            this.x = x;
            this.y = y;
            this.descriptionLine = Component.empty().append(Component.translatable("gui.yuushya.checkScreen.name."+info.id()).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GOLD))
                    .append(Component.literal(" : "))
                    .append(Component.translatable("gui.yuushya.checkScreen.description."+info.id()));
        }
        public void init(CheckScreen screen){
            int widthCurrent = this.font.width(link);
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
            guiGraphics.drawString(this.font,link,x,y+this.font.lineHeight,0xFFFFFF);
        }
    }

}
