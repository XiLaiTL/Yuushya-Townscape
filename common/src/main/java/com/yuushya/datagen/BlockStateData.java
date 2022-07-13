package com.yuushya.datagen;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.yuushya.block.blockstate.YuushyaBlockStates;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaLogger;
import com.yuushya.utils.YuushyaModelUtils;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.*;
import java.util.function.Consumer;

public class BlockStateData {


    private static MultiVariantGenerator createBlockState(Block block){
        List<Property<?>> properties= (List<Property<?>>) block.getStateDefinition().getProperties();
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block);
        if(properties.contains(BlockStateProperties.POWERED)){
            generator.with(PropertyDispatch.property(BlockStateProperties.POWERED).generate((e)->Variant.variant()));}

        if(properties.contains(BlockStateProperties.HORIZONTAL_FACING)&&properties.contains(BlockStateProperties.ATTACH_FACE)){
            generator.with(createFacingAndFaceDispatch());}
        else if(properties.contains(BlockStateProperties.FACING)){
            generator.with(createFacingDispatch());}
        else if(properties.contains(BlockStateProperties.ATTACH_FACE)){
            generator.with(createFaceDispatch());}

        if (properties.contains(YuushyaBlockStates.FORM)){
            generator.with(createDefaultDispatch(YuushyaBlockStates.FORM));}

        if(properties.contains(YuushyaBlockStates.POS_DIRECTION)){
            generator.with(createDefaultDispatch(YuushyaBlockStates.POS_DIRECTION));}
        else if(properties.contains(YuushyaBlockStates.POS_HORIZON)){
            generator.with(createDefaultDispatch(YuushyaBlockStates.POS_HORIZON));}
        else if(properties.contains(YuushyaBlockStates.POS_VERTICAL)){
            generator.with(createDefaultDispatch(YuushyaBlockStates.POS_VERTICAL));}

        return generator;
    }
    private static void addModeltoJsonObject(Map.Entry<String,JsonElement> entry,String name) {
        if(entry.getValue() instanceof JsonArray nowArray){
            JsonObject oneValue=nowArray.get(0).getAsJsonObject().deepCopy();
            oneValue.add("model",new JsonPrimitive(name));
            nowArray.add(oneValue);
        }
        else{
            JsonObject nowValue=entry.getValue().getAsJsonObject();
            nowValue.add("model",new JsonPrimitive(name));
            JsonArray newArray = new JsonArray();newArray.add(nowValue);
            entry.setValue(newArray);
        }
    }
    private static final Splitter STATE_SPLITTER = Splitter.on('#');
    public static JsonElement genBlockState(Block block,List<String> names){

        JsonObject rawJson=createBlockState(block).get().getAsJsonObject();
        JsonObject variants= rawJson.get("variants").getAsJsonObject();

        for(String name:names){
            if(name.contains("#")){
                Iterator<String> iterator= STATE_SPLITTER.split(name).iterator();
                String state=iterator.next();String model=iterator.next();

                variants.entrySet().stream()
                        .filter((entry)->{
                            String s=entry.getKey();
                            boolean res=s!=null;
                            for(String con: YuushyaModelUtils.COMMA_SPLITTER.split(state)){
                                res=res&&s.contains(con);
                                if (!res) break;
                            }
                            return res;
                        })
                        .forEach((entry)->addModeltoJsonObject(entry,model));
            }
            else{
                variants.entrySet().forEach((entry)->addModeltoJsonObject(entry,name));
            }
        }
        return rawJson;
    }
    public static List<String> getModelListFromData(List<String> names){
        return names.stream().map((name)->{
            if(name.contains("#")){
                Iterator<String> iterator= STATE_SPLITTER.split(name).iterator();
                iterator.next();
                return iterator.next();}
            else{
                return name;}
        }).toList();
    }

    public static JsonElement genSimpleBlock(Block block, ResourceLocation resourceLocation){
        return MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, resourceLocation)).get();
    }
//    public static JsonElement genButton(Block block, ResourceLocation resourceLocation){
//        return MultiVariantGenerator.multiVariant(block)
//                .with(PropertyDispatch.property(BlockStateProperties.POWERED)
//                        .select((Boolean)false,
//                                Variant.variant())//.with(VariantProperties.MODEL, resourceLocation))
//                        .select((Boolean)true,
//                                Variant.variant()))//.with(VariantProperties.MODEL, resourceLocation)))
//                .with(PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
//                        .select(AttachFace.FLOOR, Direction.EAST,
//                                Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
//                        .select(AttachFace.FLOOR, Direction.WEST,
//                                Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
//                        .select(AttachFace.FLOOR, Direction.SOUTH,
//                                Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
//                        .select(AttachFace.FLOOR, Direction.NORTH,
//                                Variant.variant())
//                        .select(AttachFace.WALL, Direction.EAST,
//                                Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
//                        .select(AttachFace.WALL, Direction.WEST,
//                                Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
//                        .select(AttachFace.WALL, Direction.SOUTH,
//                                Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
//                        .select(AttachFace.WALL, Direction.NORTH,
//                                Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
//                        .select(AttachFace.CEILING, Direction.EAST,
//                                Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
//                        .select(AttachFace.CEILING, Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
//                        .select(AttachFace.CEILING, Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
//                        .select(AttachFace.CEILING, Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)))
//
//                .get();
//
//
//    }
//    public JsonElement genGrindStone(){
//        return MultiVariantGenerator.multiVariant(Blocks.GRINDSTONE,
//                Variant.variant()
//                        .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.GRINDSTONE)))
//                .with(PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
//                        .select(AttachFace.FLOOR, Direction.NORTH, Variant.variant())
//                        .select(AttachFace.FLOOR, Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.FLOOR, Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.FLOOR, Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(AttachFace.WALL, Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).select(AttachFace.WALL, Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.WALL, Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.WALL, Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(AttachFace.CEILING, Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.CEILING, Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))).get();
//    }
    private static PropertyDispatch createDefaultDispatch(Property<?> property){
        return PropertyDispatch.property(property).generate(e->Variant.variant());
    }
    private static PropertyDispatch createFaceDispatch(){
        return PropertyDispatch.property(BlockStateProperties.ATTACH_FACE)
                .select(AttachFace.WALL,Variant.variant().with(VariantProperties.X_ROT,VariantProperties.Rotation.R90))
                .select(AttachFace.FLOOR,Variant.variant())
                .select(AttachFace.CEILING,Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180));
    }
    private static PropertyDispatch createFacingDispatch() {
        return PropertyDispatch.property(BlockStateProperties.FACING)
                .select(Direction.DOWN, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .select(Direction.UP, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
                .select(Direction.NORTH, Variant.variant())
                .select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }
    private static PropertyDispatch createFacingAndFaceDispatch() {
        return PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
                .select(AttachFace.FLOOR, Direction.EAST,
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(AttachFace.FLOOR, Direction.WEST,
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(AttachFace.FLOOR, Direction.SOUTH,
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(AttachFace.FLOOR, Direction.NORTH,
                        Variant.variant())
                .select(AttachFace.WALL, Direction.EAST,
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(AttachFace.WALL, Direction.WEST,
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(AttachFace.WALL, Direction.SOUTH,
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(AttachFace.WALL, Direction.NORTH,
                        Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(AttachFace.CEILING, Direction.EAST,
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .select(AttachFace.CEILING, Direction.WEST,
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .select(AttachFace.CEILING, Direction.SOUTH,
                        Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .select(AttachFace.CEILING, Direction.NORTH,
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180));
    }

}
