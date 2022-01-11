package net.cocofish.yuushya;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.cocofish.yuushya.blockentity.MixedBlock;
import net.cocofish.yuushya.blockentity.MixedBlockEntity;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.WorldAccess;

import java.util.function.ToIntFunction;

public class YuushyaUtils {
    public String FormatJsonString(String uglyJSONString)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    public BlockState getBlockState(BlockState blockState, WorldAccess world, BlockPos blockPos){
        Block block =blockState.getBlock();
        if(block instanceof MixedBlock){
            return ((MixedBlockEntity)world.getBlockEntity(blockPos)).getBasicBlock();
        }
        else return blockState;
    }

    public static String getModVersion()
    {
        String version= FabricLoader.getInstance().getModContainer("yuushya").get().getMetadata().getVersion().toString();
        return version;
    }

    public static void rotate(MatrixStack arg, float roll, float yaw, float pitch) {
        if (roll != 0.0F) {
            arg.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(roll));
        }
        if (yaw != 0.0F) {
            arg.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(yaw));
        }
        if (pitch != 0.0F) {
            arg.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(pitch));
        }
    }

    public static ToIntFunction<BlockState> createLightLevelFromBlockState(int litLevel) {
        return (blockState) -> {
            return (Boolean)blockState.get(Properties.LIT) ? litLevel : 0;
        };
    }
}