package net.cocofish.yuushya.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;

public abstract class AbstractShowFrameBlock extends BlockEntity implements iBlockInventory {
    public AbstractShowFrameBlock(BlockEntityType<?> type,BlockPos blockPos,BlockState blockState) {
        super(type,blockPos,blockState);
    }

    public DefaultedList<BlockState> blocks;
    private Vec3d pos=new Vec3d(0,0,0);
    private Vec3f rot=new Vec3f(0,0,0);

    private NbtCompound saveInitialChunkData(NbtCompound tag) {
        super.writeNbt(tag);
        iBlockInventories.writeNbt(tag,blocks,true);
        return tag;
    }
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        sync();
        return super.toInitialChunkDataNbt();
    }
    @Override
    public void writeNbt(NbtCompound tag) {
        this.saveInitialChunkData(tag);
        tag.put("ShowPos", this.toNbtList(this.getShowX(), this.getShowY(), this.getShowZ()));
        tag.put("ShowRotation", this.toNbtList(this.getShowRotationX(), this.getShowRotationY(), this.getShowRotationZ()));
    }
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        NbtList NbtList = tag.getList("ShowPos", 6);
        NbtList NbtList1=tag.getList("ShowRotation",5);
        this.pos=new Vec3d(NbtList.getDouble(0),NbtList.getDouble(1),NbtList.getDouble(2));
        this.rot=new Vec3f(NbtList1.getFloat(0),NbtList1.getFloat(1),NbtList1.getFloat(2));

        blocks.clear();
        iBlockInventories.readNbt(tag,blocks);
    }

    public void fromClientTag(NbtCompound tag) {
        NbtList NbtList = tag.getList("ShowPos", 6);
        NbtList NbtList1=tag.getList("ShowRotation",5);
        this.pos=new Vec3d(NbtList.getDouble(0),NbtList.getDouble(1),NbtList.getDouble(2));
        this.rot=new Vec3f(NbtList1.getFloat(0),NbtList1.getFloat(1),NbtList1.getFloat(2));

        blocks.clear();
        iBlockInventories.readNbt(tag,blocks);
    }
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        NbtCompound tag=toInitialChunkDataNbt();
        return BlockEntityUpdateS2CPacket.create(this,(blockEntity)->toClientTag(tag));
    }
    public NbtCompound toClientTag(NbtCompound tag)
    {
        this.saveInitialChunkData(tag);
        tag.put("ShowPos", this.toNbtList(this.getShowX(), this.getShowY(), this.getShowZ()));
        tag.put("ShowRotation", this.toNbtList(this.getShowRotationX(), this.getShowRotationY(), this.getShowRotationZ()));
        iBlockInventories.writeNbt(tag, blocks);
        return tag;
    }
    public void sync(){
        BlockState blockState =this.getWorld().getBlockState(this.getPos());
        this.getWorld().updateListeners(this.getPos(),blockState,blockState,2);
    }
    protected NbtList toNbtList(float... values) {
        NbtList NbtList = new NbtList();
        float[] var3 = values;
        int var4 = values.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            float f = var3[var5];
            NbtList.add(NbtFloat.of(f));
        }

        return NbtList;
    }
    protected NbtList toNbtList(double... values) {
        NbtList NbtList = new NbtList();
        double[] var3 = values;
        int var4 = values.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            double d = var3[var5];
            NbtList.add(NbtDouble.of(d));
        }

        return NbtList;
    }

    public double getShowX(){return this.pos.x;}
    public double getShowY(){return this.pos.y;}
    public double getShowZ(){return this.pos.z;}
    public float getShowRotationX(){return this.rot.getX();}
    public float getShowRotationY(){return this.rot.getY();}
    public float getShowRotationZ(){return this.rot.getZ();}
    public void setShowPos(double x,double y,double z,float rx,float ry,float rz){
        this.pos=new Vec3d(x,y,z);
        this.rot=new Vec3f(rx,ry,rz);
    }
    public void setShowPos(double x,int mode){
        switch (mode)
        {
            case 0:this.pos=new Vec3d(x,this.getShowY(),this.getShowZ());break;
            case 1:this.pos=new Vec3d(this.getShowX(),x,this.getShowZ());break;
            case 2:this.pos=new Vec3d(this.getShowX(),this.getShowY(),x);break;
            case 3:this.rot=new Vec3f((float) x,this.getShowRotationY(),this.getShowRotationZ());break;
            case 4:this.rot=new Vec3f(this.getShowRotationX(),(float)x,this.getShowRotationZ());break;
            case 5:this.rot=new Vec3f(this.getShowRotationX(),this.getShowRotationY(),(float)x);break;
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
    public BlockState getBlock(){return getBlock(0);}
}
