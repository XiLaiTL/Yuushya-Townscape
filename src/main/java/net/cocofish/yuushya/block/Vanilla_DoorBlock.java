package net.cocofish.yuushya.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
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

public class Vanilla_DoorBlock extends DoorBlock {
    public String name;
    public float aokey;
    public int lines=1;
    public Vanilla_DoorBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super( settings);
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

            state = (BlockState)state.cycle(OPEN);
            world.setBlockState(pos, state, 10);
            world.syncWorldEvent(player, (Boolean)state.get(OPEN) ? this.getCloseSoundEventId() : this.getOpenSoundEventId(), pos, 0);
            return ActionResult.success(world.isClient);
    }

    private int getOpenSoundEventId() {
        return this.material == Material.METAL ? 1011 : 1012;
    }
    private int getCloseSoundEventId() { return this.material == Material.METAL ? 1005 : 1006; }
}
