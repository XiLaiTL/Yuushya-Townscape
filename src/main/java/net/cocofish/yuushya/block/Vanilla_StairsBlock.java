package net.cocofish.yuushya.block;

import net.cocofish.yuushya.Yuushya;
import net.cocofish.yuushya.YuushyaElements;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.cocofish.yuushya.Yuushya.yuushyaElements;

public class Vanilla_StairsBlock extends StairsBlock {
    public String name;
    public float aokey;
    public int lines=1;
    public Vanilla_StairsBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super(yuushyaElements.blocksregister.get("020").getDefaultState(), settings);
        name=registname;
        aokey=ambientocclusionlightlevel;
        lines=linecount;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
//tooltip.add(new TranslatableText("block.yuushya."+name+".line1"));
        for(int i=1;i<=lines;i++) {
            tooltip.add(new TranslatableText(this.getTranslationKey()+".line"+ i)); }
    }
    @Environment(EnvType.CLIENT)
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return aokey;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return ActionResult.PASS;
    }
}
