package net.cocofish.yuushya;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.cocofish.yuushya.blockentity.MixedBlock;
import net.cocofish.yuushya.blockentity.MixedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class YuushyaMethod {
    public String FormatJsonString(String uglyJSONString)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    public int String16toInt(String color)
    {
        int b=0;
        for(int i=6,j=1;i>0;i--,j*=16)
            b+=(Char16toInt(color.charAt(i))*j);
        return b;
    }

    public int Char16toInt(char a)
    {
        if('A'<=a&&a<='F'){ return a-'A'+10; }
        else if('0'<=a&&a<='9'){return a-'0';}
        else {return 0;}
    }

    public BlockState getBlockState(BlockState blockState, WorldAccess world, BlockPos blockPos){
        Block block =blockState.getBlock();
        if(block instanceof MixedBlock){
            return ((MixedBlockEntity)world.getBlockEntity(blockPos)).getBasicBlock();
        }
        else return blockState;
    }

//    public double fixXFromQuaternionVector3f(float angle){
//        double rad=Math.toRadians(angle/2);
//        double sina=Math.sin(rad);
//        double sinb=Math.sin(Math.PI/4-rad);
//        return Math.sqrt(2)*sina*sinb;
//    }
//    public double fixZFromQuaternionVector3f(float angle){
//        double rad=Math.toRadians(angle/2);
//        double sina=Math.sin(rad);
//        double cosb=Math.cos(Math.PI/4-rad);
//        return Math.sqrt(2)*sina*cosb;
//    }
//    public double fixXFromQuaternionVector3f(float a,float b){
//        
//        //return -Math.sqrt(2)*Math.sin(a+Math.PI/4)/2+1/2.0;
//    }
//    public double fixYFromQuaternionVector3f(float a,float b){
//        //return -Math.sqrt(2)*Math.sin(b+Math.PI/4)/2+1/2.0;
//    }
//    public double fixZFromQuaternionVector3f(float a,float b){
//        //return -Math.sin(a+Math.PI/4)*Math.sin(b+Math.PI/4)+1/2.0;
//    }
    public Vector3d fixFromQuaternion(float a1,float b1){
        double a= (Math.toRadians(a1));
        double b= (Math.toRadians(b1));
        double sina=Math.sin(a);
        double sinb=Math.sin(b);
        double cosa=Math.cos(a);
        double cosb=Math.cos(b);
        double x=-0.5+(cosa-sina)/2;
        double y=-0.5+(cosb+sinb*(cosa+sina))/2;//反了
        double z=-0.5+(sinb+cosb*(cosa+sina))/2;

        return  new Vector3d(x,y,z);
    }

}