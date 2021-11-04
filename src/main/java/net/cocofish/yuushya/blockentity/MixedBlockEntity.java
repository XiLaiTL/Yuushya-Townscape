package net.cocofish.yuushya.blockentity;

import net.cocofish.yuushya.Yuushya;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.state.property.Properties.POWERED;

public class MixedBlockEntity extends BlockEntity implements iBlockInventory, BlockEntityClientSerializable {
    private final DefaultedList<BlockState> blocks;
    private Vec3d pos=new Vec3d(0,0,0);
    private float yaw=0;
    private float pitch=0;
    public MixedBlockEntity() {
        super(Yuushya.mixedblockentity);
        blocks = DefaultedList.ofSize(2, Blocks.AIR.getDefaultState());
    }



    private CompoundTag saveInitialChunkData(CompoundTag tag) {
        super.toTag(tag);
        iBlockInventories.toTag(tag,blocks,true);
        return tag;
    }
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        this.saveInitialChunkData(tag);
        tag.put("ShowPos", this.toListTag(this.getShowX(), this.getShowY(), this.getShowZ()));
        tag.put("ShowRotation", this.toListTag(this.getShowYaw(), this.getShowPitch()));
        return tag;
    }
    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        ListTag listTag = tag.getList("ShowPos", 6);
        ListTag listTag1=tag.getList("ShowRotation",5);
        this.pos=new Vec3d(listTag.getDouble(0),listTag.getDouble(1),listTag.getDouble(2));
        this.yaw = listTag1.getFloat(0);
        this.pitch = listTag1.getFloat(1);
        blocks.clear();
        iBlockInventories.fromTag(tag,blocks);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        ListTag listTag = tag.getList("ShowPos", 6);
        ListTag listTag1=tag.getList("ShowRotation",5);
        this.pos=new Vec3d(listTag.getDouble(0),listTag.getDouble(1),listTag.getDouble(2));
        this.yaw = listTag1.getFloat(0);
        this.pitch = listTag1.getFloat(1);
        blocks.clear();
        iBlockInventories.fromTag(tag,blocks);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag)
    {
        this.saveInitialChunkData(tag);
        tag.put("ShowPos", this.toListTag(this.getShowX(), this.getShowY(), this.getShowZ()));
        tag.put("ShowRotation", this.toListTag(this.getShowYaw(), this.getShowPitch()));
        iBlockInventories.toTag(tag, blocks);
        return tag;
    }

    protected ListTag toListTag(float... values) {
        ListTag listTag = new ListTag();
        float[] var3 = values;
        int var4 = values.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            float f = var3[var5];
            listTag.add(FloatTag.of(f));
        }

        return listTag;
    }
    protected ListTag toListTag(double... values) {
        ListTag listTag = new ListTag();
        double[] var3 = values;
        int var4 = values.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            double d = var3[var5];
            listTag.add(DoubleTag.of(d));
        }

        return listTag;
    }

    public double getShowX(){return this.pos.x;}
    public double getShowY(){return this.pos.y;}
    public double getShowZ(){return this.pos.z;}
    public float getShowYaw(){return this.yaw;}
    public float getShowPitch(){return this.pitch;}
    public void setShowPos(double x,double y,double z,float yaw1,float pitch1){
        this.pos=new Vec3d(x,y,z);
        this.yaw=yaw1;
        this.pitch=pitch1;
    }
    public void setShowPos(double x,int mode){
        switch (mode)
        {
            case 0:this.pos=new Vec3d(x,this.getShowY(),this.getShowZ());break;
            case 1:this.pos=new Vec3d(this.getShowX(),x,this.getShowZ());break;
            case 2:this.pos=new Vec3d(this.getShowX(),this.getShowY(),x);break;
            case 3:this.yaw=(float) x;break;
            case 4:this.pitch=(float) x;break;
        }
        markDirty();
        sync();
    }
    @Override
    public DefaultedList<BlockState> getBlocks() {
        return blocks;
    }
    public void setBlock(BlockState blockState){
        setBlock(0,blockState);
        markDirty();
    }
    public void setBasicBlock(BlockState blockState){
        if(getBlock(0)==null){
            setBlock(0,Blocks.AIR.getDefaultState());
        }
        setBlock(1,blockState);
        world.setBlockState(getPos(),world.getBlockState(getPos()).with(POWERED, !world.getBlockState(getPos()).get(POWERED)));
        markDirty();
    }
    public BlockState getBlock(){return getBlock(0);}
    public BlockState getBasicBlock(){return getBlock(1);}

}