package com.yuushya.collision;

import com.yuushya.Yuushya;
import com.yuushya.utils.YuushyaLogger;
import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.yuushya.block.YuushyaBlockFactory.getYuushyaVoxelShapes;

public class CollisionNetWorkChannel {
    public static final NetworkChannel INSTANCE = NetworkChannel.create(new ResourceLocation(Yuushya.MOD_ID,"collision"));
    public static class AllCollisionPack{
        public Map<Integer,VoxelShape> map = new HashMap<>();
        public AllCollisionPack(Map<Integer,VoxelShape> map){
            this.map.putAll(map);
        }
    }
    //all pack -> buf
    public static void encoder(AllCollisionPack allPack, FriendlyByteBuf byteBuf){
        byteBuf.writeMap(allPack.map,
                (buf,blockstate)->{
                    buf.writeInt(blockstate);
                },
                (buf,voxelShape)->{
                    List<AABB> list = voxelShape.toAabbs();
                    buf.writeVarInt(list.size());
                    for(AABB aabb:list){
                        buf.writeDouble(aabb.minX);
                        buf.writeDouble(aabb.minY);
                        buf.writeDouble(aabb.minZ);
                        buf.writeDouble(aabb.maxX);
                        buf.writeDouble(aabb.maxY);
                        buf.writeDouble(aabb.maxZ);
                    }
                }
        );
    }

    //buf -> all pack
    public static AllCollisionPack decoder(FriendlyByteBuf byteBuf){
        return new AllCollisionPack(byteBuf.readMap(
                (buf)-> buf.readInt(),
                (buf)->{
                    int len = buf.readVarInt();
                    VoxelShape voxelShape = Shapes.empty();
                    for(int i=0;i<len;i++){
                        voxelShape = Shapes.or(voxelShape,Shapes.create(buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble()));
                    }
                    return voxelShape;
                }
        ));
    }

    //after receive
    public static void handler(AllCollisionPack allPack, Supplier<NetworkManager.PacketContext> ctx){
        getYuushyaVoxelShapes().putAll(allPack.map);

    }

    public static void sendAllToClient(ServerPlayer player){
        YuushyaLogger.info("i1iio"+getYuushyaVoxelShapes().size());
        List<Map.Entry<Integer,VoxelShape>> list = getYuushyaVoxelShapes().entrySet().stream().toList();
        int len = list.size();
        for(int i=0;i<len;i+=200){
            List<Map.Entry<Integer,VoxelShape>> subList = list.subList(i,Math.min(len,i+200) );
            AllCollisionPack allPack = new AllCollisionPack(subList.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
            INSTANCE.sendToPlayer(player,allPack);
        }
        YuushyaLogger.info("i2iio"+getYuushyaVoxelShapes().size());
    }
    public static void register(){
        INSTANCE.register(AllCollisionPack.class,CollisionNetWorkChannel::encoder,CollisionNetWorkChannel::decoder,CollisionNetWorkChannel::handler);
    }


}
