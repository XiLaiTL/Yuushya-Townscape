package net.cocofish.yuushya.particle;

import net.cocofish.yuushya.block.AbstractYuushyaBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class LeafParticleBlock extends AbstractYuushyaBlock {
    protected final ParticleEffect particle;
    public LeafParticleBlock(Settings settings, String registname, float ambientocclusionlightlevel, int linecount, ParticleEffect particle) {
        super(settings, registname, ambientocclusionlightlevel, linecount);
        this.particle=particle;
    }
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double d = (double)pos.getX();
        double e = (double)pos.getY()+0.5D;
        double f = (double)pos.getZ();
        if(Math.floor(random.nextDouble()*1000)%2==1) world.addParticle(this.particle, d+random.nextDouble(), e, f+random.nextDouble(), 0.0D, 0.0D, 0.0D);
    }
}
