package net.cocofish.yuushya.blockentity;

import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public class TransformData{
    public Vector3d pos;
    public Vector3f rot;
    public Vector3f scales;
    public BlockState blockState;
    public boolean isShown;
    public TransformData(){
        this.pos=new Vector3d(0,0,0);
        this.rot=new Vector3f(0,0,0);
        this.scales=new Vector3f(1,1,1);
        this.blockState= Blocks.AIR.defaultBlockState();
        this.isShown=false;
    }
    public TransformData(Vector3d pos, Vector3f rot, Vector3f scales, BlockState blockState,boolean isShown){
        this.pos=pos;
        this.rot=rot;
        this.scales=scales;
        this.blockState= blockState;
        this.isShown=isShown;
    }
    //readNbt from compoundTag
    public void load(CompoundTag compoundTag) {
        ListTag listTagPos = compoundTag.getList("ShowPos",6);//6 means Double
        ListTag listTagRot = compoundTag.getList("ShowRotation",5);//5 means Float
        ListTag listTagScales = compoundTag.getList("ShowScales",5);//5 means Float
        this.pos = new Vector3d(listTagPos.getDouble(0), listTagPos.getDouble(1), listTagPos.getDouble(2));
        this.rot = new Vector3f(listTagRot.getFloat(0), listTagRot.getFloat(1), listTagRot.getFloat(2));
        this.scales = new Vector3f(listTagScales.getFloat(0), listTagScales.getFloat(1), listTagScales.getFloat(2));
        this.isShown = compoundTag.getBoolean("isShown");
        this.blockState = NbtUtils.readBlockState(compoundTag.getCompound("BlockState"));
    }

    //writeNbt to compoundTag
    public void saveAdditional(CompoundTag compoundTag) {
        compoundTag.put("ShowPos",toListTag(pos.x,pos.y,pos.z));
        compoundTag.put("ShowRotation",toListTag(rot.x(),rot.y(),rot.z()));
        compoundTag.put("ShowScales",toListTag(scales.x(),scales.y(),scales.z()));
        compoundTag.put("BlockState", NbtUtils.writeBlockState(blockState));
        compoundTag.put("isShown",ByteTag.valueOf(isShown));
    }

    private static <T> ListTag toListTag(T... values){
        ListTag listTag=new ListTag();
        Arrays.stream(values).toList().forEach((e)->{
            if(e instanceof Float e1)
                listTag.add(FloatTag.valueOf(e1));
            else if( e instanceof Double e1)
                listTag.add(DoubleTag.valueOf(e1));
        });
        return listTag;
    }
}
