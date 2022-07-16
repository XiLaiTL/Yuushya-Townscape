package com.yuushya.datagen;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.yuushya.block.blockstate.PositionDirectionState;
import com.yuushya.block.blockstate.PositionHorizonState;
import com.yuushya.block.blockstate.PositionVerticalState;
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

import static com.yuushya.block.blockstate.YuushyaBlockStates.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class BlockStateData {


    private static MultiVariantGenerator createBlockState(Block block) {
        List<Property<?>> properties = (List<Property<?>>) block.getStateDefinition().getProperties();
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block);
        if (properties.contains(BlockStateProperties.POWERED)) {
            generator.with(PropertyDispatch.property(BlockStateProperties.POWERED).generate((e) -> Variant.variant()));
        }

        if (properties.contains(HORIZONTAL_FACING) && properties.contains(BlockStateProperties.ATTACH_FACE)) {
            generator.with(createFacingAndFaceDispatch());
        } else if (properties.contains(BlockStateProperties.FACING)) {
            generator.with(createFacingDispatch());
        } else if (properties.contains(BlockStateProperties.ATTACH_FACE)) {
            generator.with(createFaceDispatch());
        }

        if (properties.contains(FORM)) {
            generator.with(createDefaultDispatch(FORM));
        }

        if (properties.contains(YuushyaBlockStates.POS_DIRECTION)) {
            generator.with(createDefaultDispatch(YuushyaBlockStates.POS_DIRECTION));
        } else if (properties.contains(YuushyaBlockStates.POS_HORIZON)) {
            generator.with(createDefaultDispatch(YuushyaBlockStates.POS_HORIZON));
        } else if (properties.contains(YuushyaBlockStates.POS_VERTICAL)) {
            generator.with(createDefaultDispatch(YuushyaBlockStates.POS_VERTICAL));
        }

        return generator;
    }

    private static void addModeltoJsonObject(Map.Entry<String, JsonElement> entry, String name) {
        if (entry.getValue() instanceof JsonArray nowArray) {
            JsonObject oneValue = nowArray.get(0).getAsJsonObject().deepCopy();
            oneValue.add("model", new JsonPrimitive(name));
            nowArray.add(oneValue);
        } else {
            JsonObject nowValue = entry.getValue().getAsJsonObject();
            nowValue.add("model", new JsonPrimitive(name));
            JsonArray newArray = new JsonArray();
            newArray.add(nowValue);
            entry.setValue(newArray);
        }
    }

    public static JsonElement clearEmptyVariant(JsonElement rawJson) {
        JsonObject variants = rawJson.getAsJsonObject().get("variants").getAsJsonObject();
        JsonObject variantsCopy =new JsonObject();
        variants.entrySet().forEach((entry)->{
            JsonElement value =entry.getValue();
            if(value instanceof  JsonArray valueArray){
                JsonArray valueArrayCopy=new JsonArray();
                valueArray.forEach((jsonElement)->{
                    if (jsonElement.getAsJsonObject().has("model"))
                        valueArray.add(jsonElement );
                });
                if (!valueArray.isEmpty())
                    variantsCopy.add(entry.getKey(),valueArrayCopy);
            }
            else if (value instanceof  JsonObject valueObject){
                if (valueObject.has("model"))
                    variantsCopy.add(entry.getKey(),entry.getValue());
            }
        });
        rawJson.getAsJsonObject().add("variants",variantsCopy);
        return rawJson;
    }

    private static final Splitter STATE_SPLITTER = Splitter.on('#');

    public static JsonElement genBlockState(Block block, List<String> names) {

        JsonObject rawJson = createBlockState(block).get().getAsJsonObject();
        JsonObject variants = rawJson.get("variants").getAsJsonObject();

        for (String name : names) {
            if (name.contains("#")) {
                Iterator<String> iterator = STATE_SPLITTER.split(name).iterator();
                String state = iterator.next();
                String model = iterator.next();

                variants.entrySet().stream()
                        .filter((entry) -> {
                            String s = entry.getKey();
                            boolean res = s != null;
                            for (String con : YuushyaModelUtils.COMMA_SPLITTER.split(state)) {
                                res = res && s.contains(con);
                                if (!res) break;
                            }
                            return res;
                        })
                        .forEach((entry) -> addModeltoJsonObject(entry, model));
            } else {
                variants.entrySet().forEach((entry) -> addModeltoJsonObject(entry, name));
            }
        }
        clearEmptyVariant(rawJson);
        return rawJson;
    }

    public static List<String> getModelListFromData(List<String> names) {
        return names.stream().map((name) -> {
            if (name.contains("#")) {
                Iterator<String> iterator = STATE_SPLITTER.split(name).iterator();
                iterator.next();
                return iterator.next();
            } else {
                return name;
            }
        }).toList();
    }

    public static JsonElement genSimpleBlock(Block block, ResourceLocation resourceLocation) {
        return MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, resourceLocation)).get();
    }

    public static JsonElement genFromSuit(Block block, String suit, List<List<String>> forms) {
        int formsNum = forms.size();
        MultiVariantGenerator multiVariantGenerator =
                switch (suit) {
                    case "normal" -> {
                        yield MultiVariantGenerator.multiVariant(block).with(createFacingAndFaceDispatch())
                                .with(PropertyDispatch.property(FORM).generate(i -> {
                                    return i < formsNum ? Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(forms.get(i).get(0)))
                                            : Variant.variant();
                                }));
                    }
                    case "line" -> {
                        yield MultiVariantGenerator.multiVariant(block).with(createHorizonFacingDispatch())
                                .with(PropertyDispatch.properties(FORM, POS_HORIZON).generate((i, pos) -> {
                                    return i < formsNum ? Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(forms.get(i).get(pos.ordinal())))
                                            : Variant.variant();
                                }));
                    }
                    case "face" -> {
                        yield MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.properties(FORM, XPOS, ZPOS).generate((i, xpos, zpos) -> {
                            ResourceLocation none = new ResourceLocation(forms.get(i).get(0));
                            ResourceLocation singleLine = new ResourceLocation(forms.get(i).get(1));
                            ResourceLocation middle = new ResourceLocation(forms.get(i).get(2));
                            ResourceLocation bothLine = new ResourceLocation(forms.get(i).get(3));
                            if (xpos == PositionDirectionState.NONE && zpos == PositionDirectionState.NONE) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, none);
                            if (xpos == PositionDirectionState.NONE && zpos == PositionDirectionState.SOUTH) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, singleLine);
                            if (xpos == PositionDirectionState.NONE && zpos == PositionDirectionState.NORTH) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.MODEL, singleLine);
                            if (xpos == PositionDirectionState.WEST && zpos == PositionDirectionState.NONE) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.MODEL, singleLine);
                            if (xpos == PositionDirectionState.EAST && zpos == PositionDirectionState.NONE) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.MODEL, singleLine);
                            if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.MIDDLE) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                            if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.NONE) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                            if (xpos == PositionDirectionState.NONE && zpos == PositionDirectionState.MIDDLE) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                            if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.NORTH) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                            if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.SOUTH) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                            if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.WEST) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                            if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.EAST) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                            if (xpos == PositionDirectionState.WEST && zpos == PositionDirectionState.SOUTH) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, bothLine);
                            if (xpos == PositionDirectionState.WEST && zpos == PositionDirectionState.NORTH) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.MODEL, bothLine);
                            if (xpos == PositionDirectionState.EAST && zpos == PositionDirectionState.SOUTH) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.MODEL, bothLine);
                            if (xpos == PositionDirectionState.EAST && zpos == PositionDirectionState.NORTH) return
                                    Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.MODEL, bothLine);
                            return Variant.variant();
                        }));
                    }
                    case "pole" -> {
                        yield MultiVariantGenerator.multiVariant(block).with(createHorizonFacingDispatch())
                                .with(PropertyDispatch.properties(FORM, POS_VERTICAL).generate((i, pos) -> {
                                    return i < formsNum ? Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(forms.get(i).get(pos.ordinal())))
                                            : Variant.variant();
                                }));
                    }
                    default -> {
                        yield MultiVariantGenerator.multiVariant(block);
                    }
                };
        JsonElement rawJson = multiVariantGenerator.get();
        clearEmptyVariant(rawJson);
        return rawJson;
    }
//    public static JsonElement genFromSuit(Block block,String suit,List<String> names){
//        names.add(names.get(0));names.add(names.get(0));names.add(names.get(0));names.add(names.get(0));
//        MultiVariantGenerator multiVariantGenerator=
//                switch (suit){
//                    case "normal"->{//
//                        yield  MultiVariantGenerator.multiVariant(block).with(createFacingAndFaceDispatch());
//                    }
//                    case "line"->{
//                        yield MultiVariantGenerator.multiVariant(block).with(createHorizonFacingDispatch())
//                                .with(createPosHorizonDispatch(new ResourceLocation(names.get(0)),new ResourceLocation(names.get(1)),new ResourceLocation(names.get(2)),new ResourceLocation(names.get(3))));
//                    }
//                    case "face"->{
//                        yield MultiVariantGenerator.multiVariant(block)
//                                .with(createXZposDispatch(new ResourceLocation(names.get(0)),new ResourceLocation(names.get(1)),new ResourceLocation(names.get(2)),new ResourceLocation(names.get(3))));
//                    }
//                    case "pole"->{
//                        yield MultiVariantGenerator.multiVariant(block)
//                                .with(createPosVerticalDispatch(new ResourceLocation(names.get(0)),new ResourceLocation(names.get(1)),new ResourceLocation(names.get(2)),new ResourceLocation(names.get(3))));
//                    }
//                    default -> {yield MultiVariantGenerator.multiVariant(block);}
//                };
//        return multiVariantGenerator.get();
//    }

    private static PropertyDispatch createDefaultDispatch(Property<?> property) {
        return PropertyDispatch.property(property).generate(e -> Variant.variant());
    }

    private static PropertyDispatch createFaceDispatch() {
        return PropertyDispatch.property(BlockStateProperties.ATTACH_FACE)
                .select(AttachFace.WALL, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .select(AttachFace.FLOOR, Variant.variant())
                .select(AttachFace.CEILING, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180));
    }

    private static PropertyDispatch createHorizonFacingDispatch() {
        return PropertyDispatch.property(HORIZONTAL_FACING)
                .select(Direction.NORTH, Variant.variant())
                .select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
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
        return PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, HORIZONTAL_FACING)
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

    private static PropertyDispatch createPosVerticalDispatch(ResourceLocation none, ResourceLocation bottom, ResourceLocation middle, ResourceLocation top) {
        return PropertyDispatch.property(YuushyaBlockStates.POS_VERTICAL)
                .select(PositionVerticalState.NONE, Variant.variant().with(VariantProperties.MODEL, none))
                .select(PositionVerticalState.BOTTOM, Variant.variant().with(VariantProperties.MODEL, bottom))
                .select(PositionVerticalState.MIDDLE, Variant.variant().with(VariantProperties.MODEL, middle))
                .select(PositionVerticalState.TOP, Variant.variant().with(VariantProperties.MODEL, top));
    }

    private static PropertyDispatch createPosHorizonDispatch(ResourceLocation none, ResourceLocation left, ResourceLocation middle, ResourceLocation right) {
        return PropertyDispatch.property(YuushyaBlockStates.POS_HORIZON)
                .select(PositionHorizonState.NONE, Variant.variant().with(VariantProperties.MODEL, none))
                .select(PositionHorizonState.LEFT, Variant.variant().with(VariantProperties.MODEL, left))
                .select(PositionHorizonState.MIDDLE, Variant.variant().with(VariantProperties.MODEL, middle))
                .select(PositionHorizonState.RIGHT, Variant.variant().with(VariantProperties.MODEL, right));
    }

    private static PropertyDispatch createXZposDispatch(ResourceLocation none, ResourceLocation singleLine, ResourceLocation middle, ResourceLocation bothLine) {
        return PropertyDispatch.properties(YuushyaBlockStates.XPOS, YuushyaBlockStates.ZPOS)
                .select(PositionDirectionState.NONE, PositionDirectionState.NONE,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, none))
                .select(PositionDirectionState.NONE, PositionDirectionState.SOUTH,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, singleLine))
                .select(PositionDirectionState.NONE, PositionDirectionState.NORTH,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.MODEL, singleLine))
                .select(PositionDirectionState.WEST, PositionDirectionState.NONE,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.MODEL, singleLine))
                .select(PositionDirectionState.EAST, PositionDirectionState.NONE,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.MODEL, singleLine))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.MIDDLE,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.NONE,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.NONE, PositionDirectionState.MIDDLE,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.NORTH,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.SOUTH,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.WEST,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.EAST,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.WEST, PositionDirectionState.SOUTH,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, bothLine))
                .select(PositionDirectionState.WEST, PositionDirectionState.NORTH,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.MODEL, bothLine))
                .select(PositionDirectionState.EAST, PositionDirectionState.SOUTH,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.MODEL, bothLine))
                .select(PositionDirectionState.EAST, PositionDirectionState.NORTH,
                        Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.MODEL, bothLine))
                ;

    }

    public static JsonElement genButton(Block block, ResourceLocation resourceLocation) {
        return MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(BlockStateProperties.POWERED)
                        .select((Boolean) false,
                                Variant.variant())//.with(VariantProperties.MODEL, resourceLocation))
                        .select((Boolean) true,
                                Variant.variant()))//.with(VariantProperties.MODEL, resourceLocation)))
                .with(PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
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
                        .select(AttachFace.CEILING, Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                        .select(AttachFace.CEILING, Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                        .select(AttachFace.CEILING, Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)))

                .get();


    }
//    public JsonElement genGrindStone(){
//        return MultiVariantGenerator.multiVariant(Blocks.GRINDSTONE,
//                Variant.variant()
//                        .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.GRINDSTONE)))
//                .with(PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
//                        .select(AttachFace.FLOOR, Direction.NORTH, Variant.variant())
//                        .select(AttachFace.FLOOR, Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.FLOOR, Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.FLOOR, Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(AttachFace.WALL, Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).select(AttachFace.WALL, Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.WALL, Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.WALL, Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(AttachFace.CEILING, Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.CEILING, Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))).get();
//    }
}