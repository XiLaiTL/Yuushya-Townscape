package com.yuushya.entity;

import com.yuushya.registries.YuushyaRegistries;
import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class ChairEntity extends Entity {
    public static final float HEIGHT = 0.02f, WIDTH = 0.4f;
    public ChairEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setInvulnerable(true);
    }

    public ChairEntity(Level level,double x, double y, double z){
        this(YuushyaRegistries.CHAIR_ENTITY.get(),level);
        this.setPos(x,y,z);
        this.xo = x;this.yo =y; this.zo =z;
    }

    private int checkTick = 0;

    @Override
    public void tick() {
        if(!this.level().isClientSide){
            if(this.getPassengers().isEmpty()){
                this.remove(RemovalReason.KILLED);
            }
            else if(this.checkTick==0){
                if(!ChairEntityUtils.isValidityLocation(this.level(),this.position(),this.getPassengers().get(0).getPose()))
                    this.remove(RemovalReason.KILLED);
            }
            this.checkTick ++ ;
            this.checkTick &= 15 ;
        }
        super.tick();
    }


    @Override
    protected Vector3f getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float scale) {
        float legacy = getEyeHeight()- 0.25f;
        boolean bl = entity instanceof Villager || entity instanceof WanderingTrader;
        return new Vector3f(0.0f, bl ? legacy : legacy+ 0.1875f, 0.0f);
    }


    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }
}
