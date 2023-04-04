package com.yuushya.datagen.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yuushya.block.blockstate.PositionDirectionState;
import com.yuushya.block.blockstate.PositionHorizonState;
import com.yuushya.block.blockstate.PositionVerticalState;
import com.yuushya.block.blockstate.YuushyaBlockStates;
import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.*;

import java.util.*;

import static com.yuushya.block.blockstate.YuushyaBlockStates.*;
import static com.yuushya.datagen.data.BlockStateData.addModeltoBlockStateJsonObject;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class BlockStateData_legacy {


    private static MultiVariantGenerator createBlockState(Block block) {
        List<Property<?>> properties = (List<Property<?>>) block.getStateDefinition().getProperties();
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block);
        if (properties.contains(BlockStateProperties.POWERED)) {
            generator.with(PropertyDispatch.property(BlockStateProperties.POWERED).generate((e) -> net.minecraft.data.models.blockstates.Variant.variant()));
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



    public static JsonElement genBlockState(Block block, List<String> names) {
        JsonObject rawJson = addModeltoBlockStateJsonObject(createBlockState(block).get().getAsJsonObject(),names).getAsJsonObject();
        return clearEmptyVariant(rawJson);
    }


    public static JsonElement genSimpleBlock(Block block, ResourceLocation resourceLocation) {
        return MultiVariantGenerator.multiVariant(block, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation)).get();
    }

    public static JsonElement genFromKit(Block block, String kit, List<List<String>> forms) {
        int formsNum = forms.size();
        MultiVariantGenerator multiVariantGenerator =
                switch (kit) {
                    case "normal" -> MultiVariantGenerator.multiVariant(block,
                                    net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL,new ResourceLocation(forms.get(0).get(0))))
                            .with(createFacingAndFaceDispatch())
                            .with(PropertyDispatch.property(FORM).generate(i ->
                                    i < formsNum ? net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(forms.get(i).get(0)))
                                            : net.minecraft.data.models.blockstates.Variant.variant()));
                    case "line" -> MultiVariantGenerator.multiVariant(block,
                                    net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL,new ResourceLocation(forms.get(0).get(0))))
                            .with(createHorizonFacingDispatch())
                            .with(PropertyDispatch.properties(FORM, POS_HORIZON).generate((i, pos) ->
                                    i < formsNum ? net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(forms.get(i).get(pos.ordinal())))
                                            : net.minecraft.data.models.blockstates.Variant.variant()));
                    case "face" -> MultiVariantGenerator.multiVariant(block, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL,new ResourceLocation(forms.get(0).get(0)))).with(PropertyDispatch.properties(FORM, XPOS, ZPOS).generate((i, xpos, zpos) -> {
                        ResourceLocation none = new ResourceLocation(forms.get(i).get(0));
                        ResourceLocation singleLine = new ResourceLocation(forms.get(i).get(1));
                        ResourceLocation middle = new ResourceLocation(forms.get(i).get(2));
                        ResourceLocation bothLine = new ResourceLocation(forms.get(i).get(3));
                        if (xpos == PositionDirectionState.NONE && zpos == PositionDirectionState.NONE) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, none);
                        if (xpos == PositionDirectionState.NONE && zpos == PositionDirectionState.SOUTH) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, singleLine);
                        if (xpos == PositionDirectionState.NONE && zpos == PositionDirectionState.NORTH) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.MODEL, singleLine);
                        if (xpos == PositionDirectionState.WEST && zpos == PositionDirectionState.NONE) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.MODEL, singleLine);
                        if (xpos == PositionDirectionState.EAST && zpos == PositionDirectionState.NONE) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.MODEL, singleLine);
                        if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.MIDDLE) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                        if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.NONE) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                        if (xpos == PositionDirectionState.NONE && zpos == PositionDirectionState.MIDDLE) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                        if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.NORTH) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                        if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.SOUTH) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                        if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.WEST) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                        if (xpos == PositionDirectionState.MIDDLE && zpos == PositionDirectionState.EAST) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
                        if (xpos == PositionDirectionState.WEST && zpos == PositionDirectionState.SOUTH) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, bothLine);
                        if (xpos == PositionDirectionState.WEST && zpos == PositionDirectionState.NORTH) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.MODEL, bothLine);
                        if (xpos == PositionDirectionState.EAST && zpos == PositionDirectionState.SOUTH) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.MODEL, bothLine);
                        if (xpos == PositionDirectionState.EAST && zpos == PositionDirectionState.NORTH) return
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.MODEL, bothLine);
                        return net.minecraft.data.models.blockstates.Variant.variant();
                    }));
                    case "pole" -> MultiVariantGenerator.multiVariant(block,
                                    net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL,new ResourceLocation(forms.get(0).get(0))))
                            .with(createHorizonFacingDispatch())
                            .with(PropertyDispatch.properties(FORM, POS_VERTICAL).generate((i, pos) ->
                                    i < formsNum ? net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(forms.get(i).get(pos.ordinal())))
                                            : net.minecraft.data.models.blockstates.Variant.variant()));
                    default -> MultiVariantGenerator.multiVariant(block);
                };
        JsonElement rawJson = multiVariantGenerator.get();
        return clearEmptyVariant(rawJson);
    }

    private static PropertyDispatch createDefaultDispatch(Property<?> property) {
        return PropertyDispatch.property(property).generate(e -> net.minecraft.data.models.blockstates.Variant.variant());
    }

    private static PropertyDispatch createFaceDispatch() {
        return PropertyDispatch.property(BlockStateProperties.ATTACH_FACE)
                .select(AttachFace.WALL, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .select(AttachFace.FLOOR, net.minecraft.data.models.blockstates.Variant.variant())
                .select(AttachFace.CEILING, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180));
    }

    private static PropertyDispatch createHorizonFacingDispatch() {
        return PropertyDispatch.property(HORIZONTAL_FACING)
                .select(Direction.NORTH, net.minecraft.data.models.blockstates.Variant.variant())
                .select(Direction.SOUTH, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.EAST, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }

    private static PropertyDispatch createFacingDispatch() {
        return PropertyDispatch.property(BlockStateProperties.FACING)
                .select(Direction.DOWN, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .select(Direction.UP, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
                .select(Direction.NORTH, net.minecraft.data.models.blockstates.Variant.variant())
                .select(Direction.SOUTH, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.EAST, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }

    private static PropertyDispatch createFacingAndFaceDispatch() {
        return PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, HORIZONTAL_FACING)
                .select(AttachFace.FLOOR, Direction.EAST,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(AttachFace.FLOOR, Direction.WEST,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(AttachFace.FLOOR, Direction.SOUTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(AttachFace.FLOOR, Direction.NORTH,
                        net.minecraft.data.models.blockstates.Variant.variant())
                .select(AttachFace.WALL, Direction.EAST,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(AttachFace.WALL, Direction.WEST,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(AttachFace.WALL, Direction.SOUTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(AttachFace.WALL, Direction.NORTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(AttachFace.CEILING, Direction.EAST,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .select(AttachFace.CEILING, Direction.WEST,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .select(AttachFace.CEILING, Direction.SOUTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .select(AttachFace.CEILING, Direction.NORTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180));
    }

    private static PropertyDispatch createPosVerticalDispatch(ResourceLocation none, ResourceLocation bottom, ResourceLocation middle, ResourceLocation top) {
        return PropertyDispatch.property(YuushyaBlockStates.POS_VERTICAL)
                .select(PositionVerticalState.NONE, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, none))
                .select(PositionVerticalState.BOTTOM, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, bottom))
                .select(PositionVerticalState.MIDDLE, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, middle))
                .select(PositionVerticalState.TOP, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, top));
    }

    private static PropertyDispatch createPosHorizonDispatch(ResourceLocation none, ResourceLocation left, ResourceLocation middle, ResourceLocation right) {
        return PropertyDispatch.property(YuushyaBlockStates.POS_HORIZON)
                .select(PositionHorizonState.NONE, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, none))
                .select(PositionHorizonState.LEFT, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, left))
                .select(PositionHorizonState.MIDDLE, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, middle))
                .select(PositionHorizonState.RIGHT, net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, right));
    }

    private static PropertyDispatch createXZposDispatch(ResourceLocation none, ResourceLocation singleLine, ResourceLocation middle, ResourceLocation bothLine) {
        return PropertyDispatch.properties(YuushyaBlockStates.XPOS, YuushyaBlockStates.ZPOS)
                .select(PositionDirectionState.NONE, PositionDirectionState.NONE,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, none))
                .select(PositionDirectionState.NONE, PositionDirectionState.SOUTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, singleLine))
                .select(PositionDirectionState.NONE, PositionDirectionState.NORTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.MODEL, singleLine))
                .select(PositionDirectionState.WEST, PositionDirectionState.NONE,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.MODEL, singleLine))
                .select(PositionDirectionState.EAST, PositionDirectionState.NONE,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.MODEL, singleLine))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.MIDDLE,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.NONE,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.NONE, PositionDirectionState.MIDDLE,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.NORTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.SOUTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.WEST,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.MIDDLE, PositionDirectionState.EAST,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle))
                .select(PositionDirectionState.WEST, PositionDirectionState.SOUTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, bothLine))
                .select(PositionDirectionState.WEST, PositionDirectionState.NORTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.MODEL, bothLine))
                .select(PositionDirectionState.EAST, PositionDirectionState.SOUTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.MODEL, bothLine))
                .select(PositionDirectionState.EAST, PositionDirectionState.NORTH,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.MODEL, bothLine))
                ;

    }
    static BlockStateGenerator createStairs(Block block, ResourceLocation resourceLocation, ResourceLocation resourceLocation2, ResourceLocation resourceLocation3) {
        return MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.STAIRS_SHAPE)
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.STRAIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2))
                        .select(Direction.WEST, Half.BOTTOM, StairsShape.STRAIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.STRAIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.BOTTOM, StairsShape.STRAIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3))
                        .select(Direction.WEST, Half.BOTTOM, StairsShape.OUTER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.BOTTOM, StairsShape.OUTER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3))
                        .select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation))
                        .select(Direction.WEST, Half.BOTTOM, StairsShape.INNER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.BOTTOM, StairsShape.INNER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation))
                        .select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.TOP, StairsShape.STRAIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.TOP, StairsShape.STRAIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.TOP, StairsShape.STRAIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.TOP, StairsShape.STRAIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.TOP, StairsShape.OUTER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.TOP, StairsShape.OUTER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.TOP, StairsShape.OUTER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.TOP, StairsShape.OUTER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.TOP, StairsShape.OUTER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.TOP, StairsShape.OUTER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.TOP, StairsShape.INNER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.TOP, StairsShape.INNER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.TOP, StairsShape.INNER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.TOP, StairsShape.INNER_RIGHT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.TOP, StairsShape.INNER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.TOP, StairsShape.INNER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.TOP, StairsShape.INNER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.TOP, StairsShape.INNER_LEFT,
                                net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)));

    }
    private static PropertyDispatch.C4<Direction, DoubleBlockHalf, DoorHingeSide, Boolean> configureDoorHalf(PropertyDispatch.C4<Direction, DoubleBlockHalf, DoorHingeSide, Boolean> c4, DoubleBlockHalf doubleBlockHalf, ResourceLocation resourceLocation, ResourceLocation resourceLocation2) {
        return c4
                .select(Direction.EAST, doubleBlockHalf, DoorHingeSide.LEFT, (Boolean)false,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation))
                .select(Direction.SOUTH, doubleBlockHalf, DoorHingeSide.LEFT, (Boolean)false,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(Direction.WEST, doubleBlockHalf, DoorHingeSide.LEFT, (Boolean)false,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.NORTH, doubleBlockHalf, DoorHingeSide.LEFT, (Boolean)false,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.EAST, doubleBlockHalf, DoorHingeSide.RIGHT, (Boolean)false,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2))
                .select(Direction.SOUTH, doubleBlockHalf, DoorHingeSide.RIGHT, (Boolean)false,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(Direction.WEST, doubleBlockHalf, DoorHingeSide.RIGHT, (Boolean)false,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.NORTH, doubleBlockHalf, DoorHingeSide.RIGHT, (Boolean)false,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.EAST, doubleBlockHalf, DoorHingeSide.LEFT, (Boolean)true,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(Direction.SOUTH, doubleBlockHalf, DoorHingeSide.LEFT, (Boolean)true,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, doubleBlockHalf, DoorHingeSide.LEFT, (Boolean)true,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.NORTH, doubleBlockHalf, DoorHingeSide.LEFT, (Boolean)true,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation2))
                .select(Direction.EAST, doubleBlockHalf, DoorHingeSide.RIGHT, (Boolean)true,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.SOUTH, doubleBlockHalf, DoorHingeSide.RIGHT, (Boolean)true,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation))
                .select(Direction.WEST, doubleBlockHalf, DoorHingeSide.RIGHT, (Boolean)true,
                        net.minecraft.data.models.blockstates.Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(Direction.NORTH, doubleBlockHalf, DoorHingeSide.RIGHT, (Boolean)true,
                        Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180));
    }
    private static BlockStateGenerator createDoor(Block block, ResourceLocation resourceLocation, ResourceLocation resourceLocation2, ResourceLocation resourceLocation3, ResourceLocation resourceLocation4) {
        return MultiVariantGenerator.multiVariant(block).with(
                configureDoorHalf(
                        configureDoorHalf(
                                PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.DOUBLE_BLOCK_HALF, BlockStateProperties.DOOR_HINGE, BlockStateProperties.OPEN),
                                DoubleBlockHalf.LOWER,
                                resourceLocation,
                                resourceLocation2
                        ),
                        DoubleBlockHalf.UPPER,
                        resourceLocation3,
                        resourceLocation4
                ));
    }
}