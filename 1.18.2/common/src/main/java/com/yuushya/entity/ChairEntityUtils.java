package com.yuushya.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class ChairEntityUtils {
    // 检查坐标是否有生物坐着的函数，传入一个世界和坐标
    public static boolean isLivingEntitySittingOnPos( LevelAccessor world, BlockPos pos ) {
        return isLivingEntitySittingOnPos( world, pos.getX( ), pos.getY( ), pos.getZ( ) ) ;
    }
    public static boolean isLivingEntitySittingOnPos( LevelAccessor world, double posX, double posY, double posZ ) {
        List<ChairEntity> list = world.getEntitiesOfClass( ChairEntity.class, new AABB( posX +0.0625, posY, posZ +0.0625, posX +0.9375, posY +1.5, posZ +0.9375 ) ) ;
        return list.size( ) != 0 ;
    }
    /** 让生物坐在指定坐标
     *  预留ignoreCrowded是因为椅子方块不会检查，因为椅子方块自己会检查，并且不会提示
     *
     * @param entity   生物
     * @param pos      坐标
     * @param sendMessage   不检查
     * @return   椅子实体
     */
    @Nullable
    public static ChairEntity sitToChair ( LivingEntity entity, Vec3 pos, boolean sendMessage ) {
        return sitToChair( entity, pos.x( ), pos.y( ), pos.z( ), sendMessage ) ;
    } ;
    /** 让生物坐在指定坐标
     *
     * @param entity   生物
     * @param x   坐标X
     * @param y   坐标Y
     * @param z   坐标Z
     * @param sendMessage   不检查
     * @return   椅子实体
     */
    public static ChairEntity sitToChair ( LivingEntity entity, double x, double y, double z, boolean sendMessage ) {
        // 不处理尸体
        if ( !entity.isAlive( ) ) { return null ; } ;
        if ( isLivingEntitySittingOnPos( entity.level, x, y, z ) ) { // 有生物坐着
            // 通知
            if ( sendMessage && entity instanceof Player ) {  } ;
            return null ;
        } ;
        // 生成座椅
        ChairEntity chair = new ChairEntity( entity.level, x, y, z ) ;
        // 添加座椅
        entity.level.addFreshEntity( chair ) ;
        // 解除乘坐状态
        if ( entity.isPassenger( ) ) { entity.stopRiding( ) ; } ;
        // 让生物坐上去
        entity.startRiding( chair ) ;
        return chair ;
    } ;
    // 同上，只不过已经写好了一些参数，能直接坐在原地
    public static ChairEntity sitToChair ( LivingEntity entity ) {
        return sitToChair( entity, entity.position( ), true ) ;
    } ;


    /** 坐标合理性判断
     *
     * @param world    世界
     * @param pos      检测的位置，通常是乘客或者自身的位置
     * @param passengersPose   乘客的姿势
     * @return   是否通过校验
     */
    public static boolean isValidityLocation(LevelAccessor world, Vec3 pos, Pose passengersPose ) {
        return isValidityLocation( world, new BlockPos( Math.floor( pos.x( ) ), Math.floor( pos.y( ) - 0.03 ), Math.floor( pos.z( ) ) ), passengersPose ) ;
    } ;
    /** 坐标合理性判断
     *
     * 同上，只不过pos换成了检测点
     */
    public static boolean isValidityLocation(LevelAccessor world, BlockPos pos, Pose passengersPose ) {
        return !world.getBlockState( pos ).getMaterial( ).equals( Material.AIR ) && ( passengersPose.equals( Pose.STANDING ) || passengersPose.equals( Pose.CROUCHING ) ) ;
    } ;
    /** 坐标合理性判断
     *
     * 同上，只不过这个需要传入一个需要坐下的生物
     */
    public static boolean isValidityLocation( LivingEntity entity ) {
        return entity.isOnGround( ) && isValidityLocation( entity.level, entity.position( ), entity.getPose( ) ) ;
    } ;

    public static InteractionResult use(Vec3 sitPos,BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide){
            Direction facing
                    =(state.hasProperty(FACING))? state.getValue(FACING)
                    :(state.hasProperty(HORIZONTAL_FACING))?state.getValue(HORIZONTAL_FACING)
                    :Direction.NORTH
                    ;
            RotatePos.RotateAngle ra = RotatePos.RotateAngle.get( ( facing.get2DDataValue( ) -Direction.NORTH.get2DDataValue( ) ) & 3 ) ;
            double[] rotate = RotatePos.rotate( sitPos.z(), sitPos.x(), 0.5f, ra ) ;
            if ( !isLivingEntitySittingOnPos( level, pos ) ) {
                sitToChair( player, pos.getX( ) +rotate[0], pos.getY( ) +sitPos.y(), pos.getZ( ) +rotate[1], false ) ;
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL ;
            }
        }
        return InteractionResult.PASS;
    }
    public static class RotatePos {

        public enum RotateAngle {
            R0(0), R90(1), R180(2), R270(3);
            public final int id;

            RotateAngle(int id) {
                this.id = id;
            }

            public static RotateAngle get(int id) {
                return switch (id) {
                    case 1 -> R90;
                    case 2 -> R180;
                    case 3 -> R270;
                    default -> R0;
                };
            };
        };

        public static double[] rotate(double x, double z, double center, RotateAngle rotate) {
            double[] ret = new double[2];
            double lx = x - center;
            double lz = z - center;
            switch (rotate) {
                case R90 -> {
                    ret[0] = -lz;
                    ret[1] = lx;
                }
                case R180 -> {
                    ret[0] = -lx;
                    ret[1] = -lz;
                }
                case R270 -> {
                    ret[0] = lz;
                    ret[1] = -lx;
                }
                case R0 -> {
                    ret[0] = lx;
                    ret[1] = lz;
                }
            };
            ret[0] += center;
            ret[1] += center;
            return ret;
        };

        public static float[] rotate(float x, float z, float center, RotateAngle rotate) {
            float[] ret = new float[2];
            float lx = x - center;
            float lz = z - center;
            switch (rotate) {
                case R90 -> {
                    ret[0] = -lz;
                    ret[1] = lx;
                }
                case R180 -> {
                    ret[0] = -lx;
                    ret[1] = -lz;
                }
                case R270 -> {
                    ret[0] = lz;
                    ret[1] = -lx;
                }
                case R0 -> {
                    ret[0] = lx;
                    ret[1] = lz;
                }
            };
            ret[0] += center;
            ret[1] += center;
            return ret;
        };

        public static int[] rotate(int x, int z, int center, RotateAngle rotate) {
            int[] ret = new int[2];
            int lx = x - center;
            int lz = z - center;
            switch (rotate) {
                case R90 -> {
                    ret[0] = -lz;
                    ret[1] = lx;
                }
                case R180 -> {
                    ret[0] = -lx;
                    ret[1] = -lz;
                }
                case R270 -> {
                    ret[0] = lz;
                    ret[1] = -lx;
                }
                case R0 -> {
                    ret[0] = lx;
                    ret[1] = lz;
                }
            };
            ret[0] += center;
            ret[1] += center;
            return ret;
        };
    }
}
