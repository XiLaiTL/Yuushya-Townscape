package net.cocofish.yuushya.item;

import net.cocofish.yuushya.entity.ExhibitionEntity;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class HatItem extends AbstractYuushyaItem implements Wearable {
    public HatItem(FabricItemSettings settings, String registname,int linecount) {
        super(settings.maxCount(1).equipmentSlot((itemStack) -> EquipmentSlot.HEAD), registname,linecount);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        //if(hand==Hand.MAIN_HAND) {
        if (!world.isClient) {
            if (playerEntity.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
                playerEntity.equipStack(EquipmentSlot.HEAD, playerEntity.getStackInHand(hand).copy());
                playerEntity.getStackInHand(hand).setCount(0);
                return TypedActionResult.success(playerEntity.getStackInHand(hand));
            } else {
                return TypedActionResult.pass(playerEntity.getStackInHand(hand));
            }
        }
        else
        {
            return TypedActionResult.success(playerEntity.getStackInHand(hand));
        }
        //}
        //return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }


}
