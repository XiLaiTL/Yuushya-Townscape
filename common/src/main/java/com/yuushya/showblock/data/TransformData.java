package com.yuushya.showblock.data;

import com.yuushya.mappings.CustomNbtUtils;
import com.yuushya.showblock.data.math.Vector3d;
import com.yuushya.showblock.data.math.Vector3f;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TransformData {
    private static final String KEY_SHOW_BLOCK_POS = "ShowPos";
    private static final String KEY_SHOW_BLOCK_ROT = "ShowRotation";
    private static final String KEY_SHOW_BLOCK_SCA = "ShowScales";
    private static final String KEY_SHOW_BLOCK_STATE = "BlockState";
    private static final String KEY_SHOW_BLOCK_SHOWN = "isShown";
    public Vector3d pos;
    public Vector3f rot;
    public Vector3f scales;
    public BlockState blockState;
    public boolean isShown;

    public TransformData() {
        this.pos = new Vector3d(0, 0, 0);
        this.rot = new Vector3f(0, 0, 0);
        this.scales = new Vector3f(1, 1, 1);
        this.blockState = Blocks.AIR.defaultBlockState();
        this.isShown = false;
    }

    public TransformData(Vector3d pos, Vector3f rot, Vector3f scales, BlockState blockState, boolean isShown) {
        this();
        this.pos.set(pos);
        this.rot.set(rot.x(), rot.y(), rot.z());
        this.scales.set(scales.x(), scales.y(), scales.z());
        this.blockState = blockState;
        this.isShown = isShown;
    }

    public void set(Vector3d pos, Vector3f rot, Vector3f scales, BlockState blockState, boolean isShown) {
        this.pos.set(pos);
        this.rot.set(rot.x(), rot.y(), rot.z());
        this.scales.set(scales.x(), scales.y(), scales.z());
        this.blockState = blockState;
        this.isShown = isShown;
    }

    public void set(TransformData old) {
        set(old.pos, old.rot, old.scales, old.blockState, old.isShown);
    }

    public void set() {
        this.pos.set(0, 0, 0);
        this.rot.set(0, 0, 0);
        this.scales.set(1, 1, 1);
        this.blockState = Blocks.AIR.defaultBlockState();
        this.isShown = false;
    }

    public void load(CompoundTag compoundTag) {
        ListTag listTagPos = compoundTag.getList(KEY_SHOW_BLOCK_POS, 6);
        ListTag listTagRot = compoundTag.getList(KEY_SHOW_BLOCK_ROT, 5);
        ListTag listTagScales = compoundTag.getList(KEY_SHOW_BLOCK_SCA, 5);
        this.pos.set(listTagPos.getDouble(0), listTagPos.getDouble(1), listTagPos.getDouble(2));
        this.rot.set(listTagRot.getFloat(0), listTagRot.getFloat(1), listTagRot.getFloat(2));
        this.scales.set(listTagScales.getFloat(0), listTagScales.getFloat(1), listTagScales.getFloat(2));
        this.isShown = compoundTag.getBoolean(KEY_SHOW_BLOCK_SHOWN);
        this.blockState = CustomNbtUtils.readBlockState(compoundTag.getCompound(KEY_SHOW_BLOCK_STATE));
    }

    public void saveAdditional(CompoundTag compoundTag) {
        compoundTag.put(KEY_SHOW_BLOCK_POS, toListTag(pos.x, pos.y, pos.z));
        compoundTag.put(KEY_SHOW_BLOCK_ROT, toListTag(rot.x(), rot.y(), rot.z()));
        compoundTag.put(KEY_SHOW_BLOCK_SCA, toListTag(scales.x(), scales.y(), scales.z()));
        compoundTag.put(KEY_SHOW_BLOCK_STATE, NbtUtils.writeBlockState(blockState));
        compoundTag.put(KEY_SHOW_BLOCK_SHOWN, ByteTag.valueOf(isShown));
    }

    public static <T> ListTag toListTag(T... values) {
        ListTag listTag = new ListTag();
        Arrays.stream(values).collect(Collectors.toList()).forEach((e) -> {
            if (e instanceof Float)
                listTag.add(FloatTag.valueOf((float) e));
            else if (e instanceof Double)
                listTag.add(DoubleTag.valueOf((double) e));
        });
        return listTag;
    }
}