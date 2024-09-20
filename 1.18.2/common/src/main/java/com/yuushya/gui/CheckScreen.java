package com.yuushya.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuushya.registries.YuushyaConfig;
import com.yuushya.utils.CheckFileUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yuushya.utils.CheckFileUtils.*;

public class CheckScreen extends Screen {
    private static final Component TITLE = new TranslatableComponent("gui.yuushya.checkScreen.header").withStyle(ChatFormatting.BOLD);
    private static final Component RESOURCEPACK = new TranslatableComponent("gui.yuushya.checkScreen.resourcepack").withStyle(ChatFormatting.RED);
    private static final Component RECOMMEND = new TranslatableComponent("gui.yuushya.checkScreen.recommend").withStyle(ChatFormatting.RED);
    private static final Component CTM = new TranslatableComponent("gui.yuushya.checkScreen.ctm").withStyle(ChatFormatting.RED);
    private static final Component CHECK = new TranslatableComponent("multiplayerWarning.check");
    private static final Component MESSAGE = new TranslatableComponent("gui.yuushya.checkScreen.message");
    private static final Component CONGRATULATIONS = new TranslatableComponent("gui.yuushya.checkScreen.congratulations");
    private Component message;
    private static final Component NARRATION = TITLE.copy().append("\n").append(MESSAGE);
    private final Screen previous;
    private static final int MESSAGE_PADDING = 100;

    public static MutableComponent componentEmpty(){
        return new TextComponent("");//(MutableComponent) TextComponent.EMPTY;
    }

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

        this.finishButton = new Button(this.width/4*3 - 60 ,this.height-35,120,20,CommonComponents.GUI_PROCEED, button -> {
            if (this.stopShowing.selected()) {
                YuushyaConfig.CLIENT_CONFIG.check = false;
                YuushyaConfig.CLIENT_CONFIG.update();
            }
            this.minecraft.setScreen(this.previous);
        });

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
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack,mouseX,mouseY,partialTick);
        drawString(poseStack,this.font,TITLE,center(font.width(TITLE)),10,0xFFFFFF);
        drawString(poseStack,this.font,this.message,center(font.width(this.message)),23,0xFFFFFF);
        if(!resourcepackPanel.isEmpty()) drawString(poseStack,this.font,RESOURCEPACK,0,resourcepackY,0xFFFFFF);
        if(!recommendPanel.isEmpty()) drawString(poseStack,this.font,RECOMMEND,0,recommendY,0xFFFFFF);
        if(!ctmPanel.isEmpty()) drawString(poseStack,this.font,CTM,0,ctmY,0xFFFFFF);
        resourcepackPanel.forEach(panel->panel.render(poseStack,mouseX,mouseY,partialTick));
        recommendPanel.forEach(panel->panel.render(poseStack,mouseX,mouseY,partialTick));
        ctmPanel.forEach(panel->panel.render(poseStack,mouseX,mouseY,partialTick));
    }

    public static class InfoPanel{
        private final CheckFileUtils.Info info;
        private final List<Button> links = new ArrayList<>();
        private final Font font;
        public final int x;
        public final int y;
        private final MutableComponent descriptionLine;
        private static final MutableComponent link = new TranslatableComponent("gui.yuushya.checkScreen.link").append(new TextComponent(" : "));
        public InfoPanel(CheckFileUtils.Info info, Font font, int x, int y){
            this.info = info;
            this.font = font;
            this.x = x;
            this.y = y;
            this.descriptionLine = componentEmpty().append(new TranslatableComponent("gui.yuushya.checkScreen.name."+info.id()).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GOLD))
                    .append(new TextComponent(" : "))
                    .append(new TranslatableComponent("gui.yuushya.checkScreen.description."+info.id()));
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
                                new TextComponent(text),
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
        public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            drawString(poseStack,this.font,this.descriptionLine,x,y,0xFFFFFF);
            drawString(poseStack,this.font,link,x,y+this.font.lineHeight,0xFFFFFF);
        }
    }

}
