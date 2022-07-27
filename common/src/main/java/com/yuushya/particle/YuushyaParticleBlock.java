package com.yuushya.particle;

import com.yuushya.Yuushya;
import com.yuushya.block.YuushyaBlockFactory;
import com.yuushya.utils.YuushyaLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class YuushyaParticleBlock extends YuushyaBlockFactory.BlockWithClassType {
    private final ParticleSupplier particleSupplier;
    public YuushyaParticleBlock(Properties properties, Integer tipLines, String classType,ParticleSupplier particleSupplier) {
        super(properties.noCollission().noOcclusion(), tipLines, classType);
        this.particleSupplier =particleSupplier;
    }
    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos pos, Random random) {
        double d = pos.getX();
        double e = pos.getY()+0.5D;
        double f = pos.getZ();
        if(Math.floor(random.nextDouble()*1000)%2==1) level.addParticle(this.particleSupplier.get(), d+random.nextDouble(), e, f+random.nextDouble(), 0.0D, 0.0D, 0.0D);

    }
    public interface ParticleSupplier{
        SimpleParticleType get();
    }
    public static class YuushyaParticleType extends SimpleParticleType{
        protected YuushyaParticleType(boolean bl) {super(bl);}
        public static YuushyaParticleType create(){return new YuushyaParticleType(true);}
    }
}
