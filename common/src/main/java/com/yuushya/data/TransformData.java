package com.yuushya.data;

import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import com.yuushya.mappings.Vector3dMapper;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TransformData {
    private static final String POS_KEY = "ShowPos";
    private static final String ROT_KEY = "ShowRotation";
    private static final String SCA_KEY = "ShowScales";
    private static final String STATE_KEY = "BlockState";
    private static final String SHOWN_KEY = "isShown";

    public Vector3dMapper pos;
    public Vector3f rot;
    public Vector3f scales;
    public BlockState blockState;
    public boolean isShown;

    public TransformData() {
        this.pos = new Vector3dMapper(0, 0, 0);
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
        ListTag listTagPos = compoundTag.getList(POS_KEY, 6);
        ListTag listTagRot = compoundTag.getList(ROT_KEY, 5);
        ListTag listTagScales = compoundTag.getList(SCA_KEY, 5);
        this.pos.set(listTagPos.getDouble(0), listTagPos.getDouble(1), listTagPos.getDouble(2));
        this.rot.set(listTagRot.getFloat(0), listTagRot.getFloat(1), listTagRot.getFloat(2));
        this.scales.set(listTagScales.getFloat(0), listTagScales.getFloat(1), listTagScales.getFloat(2));
        this.isShown = compoundTag.getBoolean(SHOWN_KEY);
        this.blockState = NbtUtils.readBlockState(compoundTag.getCompound(STATE_KEY));
    }

    public void saveAdditional(CompoundTag compoundTag) {
        compoundTag.put(POS_KEY, toListTag(pos.x, pos.y, pos.z));
        compoundTag.put(ROT_KEY, toListTag(rot.x(), rot.y(), rot.z()));
        compoundTag.put(SCA_KEY, toListTag(scales.x(), scales.y(), scales.z()));
        compoundTag.put(STATE_KEY, NbtUtils.writeBlockState(blockState));
        compoundTag.put(SHOWN_KEY, ByteTag.valueOf(isShown));
    }

    public static <T> ListTag toListTag(T... values) {
        ListTag listTag = new ListTag();
        Arrays.stream(values).collect(Collectors.toList()).forEach((e) -> {
            if (e instanceof Float) {
                float e1 = (Float) e;
                listTag.add(FloatTag.valueOf(e1));
            } else if (e instanceof Double) {
                double e1 = (Double) e;
                listTag.add(DoubleTag.valueOf(e1));
            }
        });
        return listTag;
    }
}