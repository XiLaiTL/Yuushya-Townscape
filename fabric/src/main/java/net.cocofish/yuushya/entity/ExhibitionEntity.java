package net.cocofish.yuushya.entity;

import com.google.common.collect.Lists;
import net.cocofish.yuushya.Yuushya;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class ExhibitionEntity extends FallingBlockEntity {
    private BlockState block;
    public int timeFalling;
    public boolean dropItem;
    private boolean destroyedOnLanding;
    private boolean hurtEntities;
    private int fallHurtMax;
    private float fallHurtAmount;
    public NbtCompound blockEntityData;
    protected static final TrackedData<BlockPos> BLOCK_POS;

    public ExhibitionEntity(EntityType<ExhibitionEntity> entityType, World world) {
        super(entityType, world);
        this.block = Blocks.SAND.getDefaultState();
        this.dropItem = false;
    }
    public ExhibitionEntity(World world, double x, double y, double z, BlockState block) {
        this((EntityType<ExhibitionEntity>)(Object) Yuushya.exhibitionEntity, world);
        this.block = block;
        this.inanimate = true;
        this.updatePosition(x, y + (double)((1.0F - this.getHeight()) / 2.0F), z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.setFallingBlockPos(this.getBlockPos());
    }

    public boolean isAttackable() {
        return false;
    }

    public void setFallingBlockPos(BlockPos pos) {
        this.dataTracker.set(BLOCK_POS, pos);
    }

    @Environment(EnvType.CLIENT)
    public BlockPos getFallingBlockPos() {
        return (BlockPos)this.dataTracker.get(BLOCK_POS);
    }

    protected boolean canClimb() {
        return false;
    }

    protected void initDataTracker() {
        this.dataTracker.startTracking(BLOCK_POS, BlockPos.ORIGIN);
    }

    public boolean collides() {
        return !this.isRemoved();
    }

    public void tick() {
        if (this.block.isAir()) {
            this.remove(RemovalReason.KILLED);
        } else {
            Block block = this.block.getBlock();
            BlockPos blockPos2;
            if (this.timeFalling++ == 0) {
                blockPos2 = this.getBlockPos();
                if (this.world.getBlockState(blockPos2).isOf(block)) {
                    this.world.removeBlock(blockPos2, false);
                } else if (!this.world.isClient) {
                    this.remove(RemovalReason.KILLED);
                    return;
                }
            }

            if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add(0.0D, -0.04D, 0.0D));
            }

            this.move(MovementType.SELF, this.getVelocity());
            if (!this.world.isClient) {
                blockPos2 = this.getBlockPos();
                boolean bl = this.block.getBlock() instanceof ConcretePowderBlock;
                boolean bl2 = bl && this.world.getFluidState(blockPos2).isIn(FluidTags.WATER);
                double d = this.getVelocity().lengthSquared();
                if (bl && d > 1.0D) {
                    BlockHitResult blockHitResult = this.world.raycast(new RaycastContext(new Vec3d(this.prevX, this.prevY, this.prevZ), this.getPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, this));
                    if (blockHitResult.getType() != HitResult.Type.MISS && this.world.getFluidState(blockHitResult.getBlockPos()).isIn(FluidTags.WATER)) {
                        blockPos2 = blockHitResult.getBlockPos();
                        bl2 = true;
                    }
                }

                if (!this.onGround && !bl2) {
                    if (!this.world.isClient && (this.timeFalling > 100 && (blockPos2.getY() < 1 || blockPos2.getY() > 256) || this.timeFalling > 600)) {
                        if (this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            this.dropItem(block);
                        }

                        this.remove(RemovalReason.KILLED);
                    }
                } else {
                    BlockState blockState = this.world.getBlockState(blockPos2);
                    this.setVelocity(this.getVelocity().multiply(0.7D, -0.5D, 0.7D));
                    if (!blockState.isOf(Blocks.MOVING_PISTON)) {
                        this.remove(RemovalReason.KILLED);
                        if (!this.destroyedOnLanding) {
                            boolean bl3 = blockState.canReplace(new AutomaticItemPlacementContext(this.world, blockPos2, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                            boolean bl4 = FallingBlock.canFallThrough(this.world.getBlockState(blockPos2.down())) && (!bl || !bl2);
                            boolean bl5 = this.block.canPlaceAt(this.world, blockPos2) && !bl4;
                            if (bl3 && bl5) {
                                if (this.block.contains(Properties.WATERLOGGED) && this.world.getFluidState(blockPos2).getFluid() == Fluids.WATER) {
                                    this.block = (BlockState)this.block.with(Properties.WATERLOGGED, true);
                                }

                                if (this.world.setBlockState(blockPos2, this.block, 3)) {
                                    if (block instanceof FallingBlock) {
                                        ((FallingBlock)block).onLanding(this.world, blockPos2, this.block, blockState, this);
                                    }

                                    if (this.blockEntityData != null && block instanceof BlockEntityProvider) {
                                        BlockEntity blockEntity = this.world.getBlockEntity(blockPos2);
                                        if (blockEntity != null) {
                                            NbtCompound compoundTag = blockEntity.createNbt();
                                            Iterator var13 = this.blockEntityData.getKeys().iterator();

                                            while(var13.hasNext()) {
                                                String string = (String)var13.next();
                                                NbtCompound tag = (NbtCompound) this.blockEntityData.get(string);
                                                if (!"x".equals(string) && !"y".equals(string) && !"z".equals(string)) {
                                                    compoundTag.put(string, tag.copy());
                                                }
                                            }

                                            blockEntity.readNbt(compoundTag);
                                            blockEntity.markDirty();
                                        }
                                    }
                                } else if (this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                    this.dropItem(block);
                                }
                            } else if (this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                this.dropItem(block);
                            }
                        } else if (block instanceof FallingBlock) {
                            ((FallingBlock)block).onDestroyedOnLanding(this.world, blockPos2, this);
                        }
                    }
                }
            }

            this.setVelocity(this.getVelocity().multiply(0.98D));
        }
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        if (this.hurtEntities) {
            int i = MathHelper.ceil(fallDistance - 1.0F);
            if (i > 0) {
                List<Entity> list = Lists.newArrayList((Iterable)this.world.getOtherEntities(this, this.getBoundingBox()));
                boolean bl = this.block.isIn(BlockTags.ANVIL);
                DamageSource damageSource = bl ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                Iterator var7 = list.iterator();

                while(var7.hasNext()) {
                    Entity entity = (Entity)var7.next();
                    entity.damage(damageSource, (float)Math.min(MathHelper.floor((float)i * this.fallHurtAmount), this.fallHurtMax));
                }

                if (bl && (double)this.random.nextFloat() < 0.05000000074505806D + (double)i * 0.05D) {
                    BlockState blockState = AnvilBlock.getLandingState(this.block);
                    if (blockState == null) {
                        this.destroyedOnLanding = true;
                    } else {
                        this.block = blockState;
                    }
                }
            }
        }

        return false;
    }

    protected void writeCustomDataToTag(NbtCompound tag) {
        tag.put("BlockState", NbtHelper.fromBlockState(this.block));
        tag.putInt("Time", this.timeFalling);
        tag.putBoolean("DropItem", this.dropItem);
        tag.putBoolean("HurtEntities", this.hurtEntities);
        tag.putFloat("FallHurtAmount", this.fallHurtAmount);
        tag.putInt("FallHurtMax", this.fallHurtMax);
        if (this.blockEntityData != null) {
            tag.put("TileEntityData", this.blockEntityData);
        }

    }

    protected void readCustomDataFromTag(NbtCompound tag) {
        this.block = NbtHelper.toBlockState(tag.getCompound("BlockState"));
        this.timeFalling = tag.getInt("Time");
        if (tag.contains("HurtEntities", 99)) {
            this.hurtEntities = tag.getBoolean("HurtEntities");
            this.fallHurtAmount = tag.getFloat("FallHurtAmount");
            this.fallHurtMax = tag.getInt("FallHurtMax");
        } else if (this.block.isIn(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }

        if (tag.contains("DropItem", 99)) {
            this.dropItem = tag.getBoolean("DropItem");
        }

        if (tag.contains("TileEntityData", 10)) {
            this.blockEntityData = tag.getCompound("TileEntityData");
        }

        if (this.block.isAir()) {
            this.block = Blocks.SAND.getDefaultState();
        }

    }

    @Environment(EnvType.CLIENT)
    public World getWorldClient() {
        return this.world;
    }

    public void setHurtEntities(boolean hurtEntities) {
        this.hurtEntities = hurtEntities;
    }

    @Environment(EnvType.CLIENT)
    public boolean doesRenderOnFire() {
        return false;
    }

    public void populateCrashReport(CrashReportSection section) {
        super.populateCrashReport(section);
        section.add("Immitating BlockState", (Object)this.block.toString());
    }

    public BlockState getBlockState() {
        return this.block;
    }

    public boolean entityDataRequiresOperator() {
        return true;
    }

    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.getBlockState()));
    }

    static {
        BLOCK_POS = DataTracker.registerData(FallingBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    }
}
