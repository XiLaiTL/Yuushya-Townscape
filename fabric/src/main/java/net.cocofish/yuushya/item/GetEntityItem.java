package net.cocofish.yuushya.item;

import net.cocofish.yuushya.entity.ExhibitionEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class GetEntityItem extends Item {

    public GetEntityItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        for(int i=1;i<=5;i++) {
            tooltip.add(new TranslatableText("item.get_entity_item.tooltip"+ i)); }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if(hand==Hand.OFF_HAND) {
            BlockPos pos=playerEntity.getBlockPos();
            ExhibitionEntity fallingBlockEntity = new ExhibitionEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, world.getBlockState(pos));
            Box box=fallingBlockEntity.getBoundingBox();
            List<ExhibitionEntity> list=world.getEntitiesByClass(ExhibitionEntity.class,box,null);
            if(!list.isEmpty())
            {
                for(Entity entity:list)
                {
                    entity.setRemoved(Entity.RemovalReason.KILLED);
                }
            }
            return TypedActionResult.success(playerEntity.getStackInHand(hand));
        }
        return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity=context.getPlayer();
        Hand hand= context.getHand();
        World world=context.getWorld();

        BlockPos pos=context.getBlockPos();
        ItemStack mainhand= playerEntity.getMainHandStack();
        ItemStack offhand=playerEntity.getOffHandStack();
        Item item = offhand.getItem();
        Block block = item instanceof BlockItem ? ((BlockItem)item).getBlock(): Blocks.AIR;


        if(hand==Hand.OFF_HAND){
            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY()+1.0D, (double)pos.getZ() + 0.5D, world.getBlockState(pos));
            Box box=fallingBlockEntity.getBoundingBox();
            List<FallingBlockEntity> list=world.getEntitiesByClass(FallingBlockEntity.class,box,null);
            if(!list.isEmpty())
            {
                for(Entity entity:list)
                {
                    entity.setRemoved(Entity.RemovalReason.KILLED);
                }
            }
            return ActionResult.PASS;
        }
        else if(block == Blocks.AIR){
            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, world.getBlockState(pos));
            fallingBlockEntity.timeFalling=-2147483648;
            fallingBlockEntity.dropItem=false;
            fallingBlockEntity.setNoGravity(true);
            world.spawnEntity(fallingBlockEntity);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 35);
            return ActionResult.SUCCESS;
        }
        else if(item instanceof BlockItem){

            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY()+1.0D, (double)pos.getZ() + 0.5D, block.getDefaultState());
            fallingBlockEntity.timeFalling=-2147483648;
            fallingBlockEntity.dropItem=false;
            fallingBlockEntity.setNoGravity(true);
            world.spawnEntity(fallingBlockEntity);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
