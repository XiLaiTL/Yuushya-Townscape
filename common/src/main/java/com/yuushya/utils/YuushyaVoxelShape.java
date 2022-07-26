package com.yuushya.utils;

import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;

public class YuushyaVoxelShape {
    record Line(Vector3f begin, Vector3f end) {
        boolean equal(Line line) {
            return begin.equals(line.end) && end.equals(line.begin);
        }
    }
    static class QuadWithDirection {
        final List<Vector3f> quad;
        final Direction facing;
        QuadWithDirection(List<Vector3f> quad){
            this.quad=quad;
            facing=getQuadDirection(quad);
        }

        List<Line> getLines(){
            List<Line> list=new ArrayList<>();
            list.add(new Line(quad.get(0), quad.get(1)));
            list.add(new Line(quad.get(1), quad.get(2)));
            list.add(new Line(quad.get(2), quad.get(3)));
            list.add(new Line(quad.get(3), quad.get(0)));
            return list;
        }
        boolean isInOneLine(QuadWithDirection quad){
            for (Line lineThis:this.getLines())
                for (Line lineOther: quad.getLines())
                    if (lineThis.equal(lineOther)) return true;
            return false;
        }
        boolean isInOneCube(QuadWithDirection quad1,QuadWithDirection quad2){//首先初略的判断一下而已
            return !(this.facing==null||quad1.facing==null||quad2.facing==null)
                    &&!(this.facing ==quad1.facing) &&!(quad1.facing==quad2.facing)&&!(facing==quad2.facing)
                    &&isInOneLine(quad1)&&isInOneLine(quad2)&&quad1.isInOneLine(quad2);
        }
        VoxelShape getOutlineShape(QuadWithDirection quad1,QuadWithDirection quad2){
            //只需要验证最大点是否在朝上的面内即可
            List<Vector3f> allDot = new ArrayList<>(quad);
            allDot.addAll(quad1.quad);allDot.addAll(quad2.quad);
            Vector3f maxDot=new Vector3f(-3,-3,-3),minDot=new Vector3f(3,3,3);
            for (Vector3f dot:allDot){
                maxDot.set(Math.max(maxDot.x(),dot.x()),Math.max(maxDot.y(),dot.y()),Math.max(maxDot.z(),dot.z()));
                minDot.set(Math.min(minDot.x(),dot.x()),Math.min(minDot.y(),dot.y()),Math.min(minDot.z(),dot.z()));
            }
            boolean isOutlineCube=false;
            for (Vector3f dot:quad){
                isOutlineCube= switch (this.facing){
                    case DOWN -> dot.y()==minDot.y();
                    case UP -> dot.y()==maxDot.y();
                    case NORTH -> dot.z()==minDot.z();
                    case SOUTH -> dot.z()==maxDot.z();
                    case WEST -> dot.x()==minDot.x();
                    case EAST -> dot.x()==maxDot.x();
                };
            }
            if (isOutlineCube) return Shapes.box(minDot.x(),minDot.y(),minDot.z(),maxDot.x(),maxDot.y(),maxDot.z());
            else return Shapes.empty();
        }
    }

    public static Direction getQuadDirection(List<Vector3f> quad){
        List<Direction> possibleDirection =new ArrayList<>();
        for (int i=0;i<4;i++){
            Vector3f topDot=quad.get(0);
            quad.remove(0);quad.add(topDot);
            Vector3f dot1= quad.get(0),dot2=quad.get(1),dot3=quad.get(2),dot4=quad.get(3);
            if (dot1.z()>dot2.z()&&dot2.x()<dot3.x()&&dot3.z()< dot4.z()&&dot4.x()>dot1.x())
                possibleDirection.add(Direction.DOWN) ;
            if (dot1.z()<dot2.z()&&dot2.x()<dot3.x()&&dot3.z()>dot4.z()&&dot4.x()>dot1.x())
                possibleDirection.add(Direction.UP);
            if (dot1.y()>dot2.y()&&dot2.x()>dot3.x()&&dot3.y()<dot4.y()&&dot4.x()<dot1.x())
                possibleDirection.add(Direction.NORTH);
            if (dot1.y()>dot2.y()&&dot2.x()<dot3.x()&&dot3.y()<dot4.y()&&dot4.x()>dot1.x())
                possibleDirection.add(Direction.SOUTH);
            if (dot1.y()>dot2.y()&&dot2.z()<dot3.z()&&dot3.y()<dot4.y()&&dot4.z()>dot1.z())
                possibleDirection.add(Direction.WEST);
            if (dot1.y()>dot2.y()&&dot2.z()>dot3.z()&&dot3.y()<dot4.y()&&dot4.z()<dot1.z())
                possibleDirection.add(Direction.EAST);
        }
        return possibleDirection.isEmpty()?null:possibleDirection.get(0);
    }

    public static List<QuadWithDirection> getQuadFromBlockState(BlockState blockState){
        BlockRenderDispatcher blockRenderDispatcher= Minecraft.getInstance().getBlockRenderer();
        BakedModel blockModel = blockRenderDispatcher.getBlockModel(blockState);
        Random rand = new Random();

        ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));directions.add(null); // 加个null
        List<QuadWithDirection> quadList=new ArrayList<>();
        for (Direction cullfacedirecion : directions) {
            List<BakedQuad> blockModelQuads = blockModel.getQuads(blockState, cullfacedirecion, rand);
            if (blockModelQuads.size()>200) return null;//TODO:假装有优化
            for (BakedQuad bakedQuad : blockModelQuads) {
                int[] vertex = bakedQuad.getVertices();
                List<Vector3f> quadDotList = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    Vector3f vector3f = new Vector3f(// 顶点的原坐标
                            Float.intBitsToFloat(vertex[YuushyaUtils.vertexSize * i]),
                            Float.intBitsToFloat(vertex[YuushyaUtils.vertexSize * i + 1]),
                            Float.intBitsToFloat(vertex[YuushyaUtils.vertexSize * i + 2]));
                    quadDotList.add(vector3f);
                }
                quadList.add(new QuadWithDirection(quadDotList));
            }
        }
        return quadList;
    }

    public static VoxelShape getVoxelShape(BlockState blockState){
        if(blockState.getBlock() instanceof AirBlock) return Shapes.block();
        List<QuadWithDirection> quadList =getQuadFromBlockState(blockState);
        if (quadList==null) return Shapes.block();
        /*已知：面元的顶点集合,求碰撞箱的对顶点。
        点是逆时针顺序构成面

        首先选取一个面里的一个顶点，找到和这个顶点共边的三个面，三个面中的最大碰撞箱就是改碰撞箱
        这三个面必须是凸面，
         * */
        VoxelShape voxelShape=Shapes.empty();
        for(QuadWithDirection quad1:quadList){
            List<QuadWithDirection> quadNext=new ArrayList<>();
            for (QuadWithDirection quad2:quadList){if (quad1==quad2) continue;
                if (quad1.isInOneLine(quad2)) quadNext.add(quad2);
            }
            for (QuadWithDirection quad2:quadNext){
                for (QuadWithDirection quad3:quadNext){if (quad2==quad3) continue;
                    if (quad1.isInOneCube(quad2,quad3)) {
                        VoxelShape voxelShape1=quad1.getOutlineShape(quad2,quad3);
                        voxelShape=Shapes.or(voxelShape,voxelShape1) ;
                    }
                }
            }
        }
        return voxelShape;
    }
}
