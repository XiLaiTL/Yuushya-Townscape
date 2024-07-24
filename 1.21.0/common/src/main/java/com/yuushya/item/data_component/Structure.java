package com.yuushya.item.data_component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record Structure(
        BlockPos pos,
        BlockPos size,
        int rotation,
        int mirror
) {
    public static final Structure EMPTY = new Structure(new BlockPos(0,0,0),new BlockPos(0,0,0),0,0);
    public static final Codec<Structure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(Structure::pos),
            BlockPos.CODEC.fieldOf("size").forGetter(Structure::size),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("rotation").forGetter(Structure::rotation),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("mirror").forGetter(Structure::mirror)
            // 最多可以在这里声明 16 个字段
    ).apply(instance, Structure::new));
    public static final StreamCodec<ByteBuf, Structure> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, Structure::pos,
                    BlockPos.STREAM_CODEC,Structure::size,
                    ByteBufCodecs.VAR_INT, Structure::rotation,
                    ByteBufCodecs.VAR_INT, Structure::mirror,
                    Structure::new
            );
}
