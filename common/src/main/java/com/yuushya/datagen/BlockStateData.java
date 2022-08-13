package com.yuushya.datagen;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Var;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.yuushya.datagen.BlockStateData.ChildVariant.ChildPropertyVariant;
import com.yuushya.datagen.BlockStateData.ChildVariant.ChildPropertyVariant.ChildProperty;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaLogger;
import com.yuushya.utils.YuushyaModelUtils;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class BlockStateData {
    //Property
    public static final ChildProperty FACING=ChildProperty.of("facing","north","east","south","west","up","down");
    public static final ChildProperty HORIZONTAL_FACING=ChildProperty.of("facing","north","east","south","west");
    public static final ChildProperty FACE=ChildProperty.of("face","floor","wall","ceiling");
    public static final ChildProperty FORM=ChildProperty.of("form","0","1","2","3","4","5","6","7");
    public static final ChildProperty POS_DIRECTION=ChildProperty.of("pos","north","south","west","east","middle","none");
    public static final ChildProperty POS_HORIZON=ChildProperty.of("pos","left","middle","right","none");
    public static final ChildProperty POS_VERTICAL=ChildProperty.of("pos","top","middle","bottom","none");
    public static final ChildProperty XPOS=ChildProperty.of("xpos","north","south","west","east","middle","none");
    public static final ChildProperty ZPOS=ChildProperty.of("zpos","north","south","west","east","middle","none");
    public static final ChildProperty POWERED=ChildProperty.of("powered","true","false");
    public static final ChildProperty HALF=ChildProperty.of("half","top","bottom");
    public static final ChildProperty STAIRS_SHAPE=ChildProperty.of("shape","straight","inner_left","inner_right","outer_left","outer_right");
    public static final ChildProperty DOOR_HINGE=ChildProperty.of("hinge","left","right");
    public static final ChildProperty DOUBLE_BLOCK_HALF=ChildProperty.of("half","upper","lower");
    public static final ChildProperty OPEN=ChildProperty.of("open","true","false");


    public static JsonElement genSimpleBlock(ResourceLocation resourceLocation) {
        return ChildVariant.of(Variant.variant().with(VariantProperties.MODEL,resourceLocation)).get();
    }
    //TODO: support for block which written in both kit and states
    private static final ResourceLocation blankModel=new ResourceLocation("yuushya:extra_building_material/blank");
    public static JsonElement genBlockState(YuushyaRegistryData.Block.BlockState blockState) {
        ChildVariant childVariant;
        if (blockState.kit!=null&&!blockState.kit.isEmpty()){
            int formsNum=blockState.forms.size();
            Variant baseVariant=Variant.variant().with(VariantProperties.MODEL,new ResourceLocation(blockState.forms.get(0).get(0)));
            childVariant= switch (blockState.kit){
                case "normal"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM).generate((variantKeyList)->{
                            int i=FORM.indexOf(variantKeyList.get(0));
                            return List.of(i < formsNum
                                    ? Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(blockState.forms.get(i).get(0)))
                                    : Variant.variant()) ;
                        }));
                case "attachment"-> ChildVariant.of(baseVariant)
                        .add(createFaceAndFacingVariant())
                        .add(ChildPropertyVariant.of(FORM).generate((variantKeyList)->{
                            int i=FORM.indexOf(variantKeyList.get(0));
                            return List.of(i < formsNum
                                    ? Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(blockState.forms.get(i).get(0)))
                                    : Variant.variant()) ;
                        }));
                case "line"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM,POS_HORIZON).generate((variantKeyList)->{
                            int i=FORM.indexOf(variantKeyList.get(0));
                            int j=POS_HORIZON.indexOf(variantKeyList.get(1));
                            return List.of(i < formsNum
                                    ? Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(blockState.forms.get(i).get(j)))
                                    : Variant.variant());
                        }));
                case "face"->ChildVariant.of(baseVariant)
                        .add(ChildPropertyVariant.of(FORM,XPOS,ZPOS).generate((variantKeyList)->{
                            int i=FORM.indexOf(variantKeyList.get(0));
                            if (i < formsNum){
                                ResourceLocation none = new ResourceLocation(blockState.forms.get(i).get(0));
                                ResourceLocation singleLine = new ResourceLocation(blockState.forms.get(i).get(1));
                                ResourceLocation middle = new ResourceLocation(blockState.forms.get(i).get(2));
                                ResourceLocation bothLine = new ResourceLocation(blockState.forms.get(i).get(3));
                                return List.of(createXYPosVariant(variantKeyList.get(1),variantKeyList.get(2),none,singleLine,middle,bothLine)) ;
                            }
                            else return List.of(Variant.variant());
                        }));
                case "pole"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM,POS_VERTICAL).generate((variantKeyList)->{
                            int i=FORM.indexOf(variantKeyList.get(0));
                            int j=POS_VERTICAL.indexOf(variantKeyList.get(1));
                            return List.of(i < formsNum
                                    ? Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(blockState.forms.get(i).get(j)))
                                    : Variant.variant());
                        }));
                case "tri_part"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM,POS_VERTICAL).generate((variantKeyList)->{
                            int i=FORM.indexOf(variantKeyList.get(0));
                            if (i>=formsNum) return List.of(Variant.variant());
                            else if (variantKeyList.get(1).equals("pos=middle")) return List.of(Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(blockState.forms.get(i).get(0))));
                            else return List.of(Variant.variant().with(VariantProperties.MODEL,blankModel));
                        }));
                case "VanillaStairBlock"->{
                    ResourceLocation inner=new ResourceLocation(blockState.forms.get(0).get(0));
                    ResourceLocation straight=new ResourceLocation(blockState.forms.get(0).get(1));
                    ResourceLocation outer=new ResourceLocation(blockState.forms.get(0).get(2));
                    yield  ChildVariant.of(baseVariant)
                        .add(createStairVariant(inner,straight,outer));
                }
                case "VanillaDoorBlock"->{
                    ResourceLocation bottom=new ResourceLocation(blockState.forms.get(0).get(0));
                    ResourceLocation bottom_hinge=new ResourceLocation(blockState.forms.get(0).get(1));
                    ResourceLocation top=new ResourceLocation(blockState.forms.get(0).get(2));
                    ResourceLocation top_hinge=new ResourceLocation(blockState.forms.get(0).get(3));
                    yield  ChildVariant.of(baseVariant)
                            .add(createDoorVariant(bottom,bottom_hinge,top,top_hinge));
                }

                default -> ChildVariant.of(baseVariant);
            };
            return childVariant.get();
        }
        else {
            Variant baseVariant=Variant.variant().with(VariantProperties.MODEL,new ResourceLocation(getModelListFromData(blockState.models).get(0)));
            childVariant=ChildVariant.of(baseVariant);
            if (blockState.states.contains("powered")) {
                childVariant.add(createDefaultVariant(POWERED));
            }

            if (blockState.states.contains("horizontal_facing") && blockState.states.contains("face")) {
                childVariant.add(createFaceAndFacingVariant());
            } else if (blockState.states.contains("facing")) {
                childVariant.add(createFacingVariant());
            } else if (blockState.states.contains("face")) {
                childVariant.add(createFaceVariant());
            }

            if (blockState.states.contains("form")) {
                childVariant.add(createDefaultVariant(FORM));
            }

            if (blockState.states.contains("pos_direction")) {
                childVariant.add(createDefaultVariant(POS_DIRECTION));
            } else if (blockState.states.contains("pos_horizon")) {
                childVariant.add(createDefaultVariant(POS_HORIZON));
            } else if (blockState.states.contains("pos_vertical")) {
                childVariant.add(createDefaultVariant(POS_VERTICAL));
            }
            return addModeltoBlockStateJsonObject(childVariant.get().getAsJsonObject(),blockState.models);
        }
    }
    private static ChildPropertyVariant createFaceVariant() {
        return ChildPropertyVariant.of(FACE)
                .addVariant(List.of("face=wall"), Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .addVariant(List.of("face=floor"), Variant.variant())
                .addVariant(List.of("face=ceiling"), Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180));
    }
    private static ChildPropertyVariant createHorizonFacingVariant() {
        return ChildPropertyVariant.of(HORIZONTAL_FACING)
                .addVariant(List.of("facing=north"), Variant.variant())
                .addVariant(List.of("facing=south"),Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("facing=west"),Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("facing=east"), Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }
    private static ChildPropertyVariant createFacingVariant() {
        return ChildPropertyVariant.of(HORIZONTAL_FACING)
                .addVariant(List.of("facing=down"), Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .addVariant(List.of("facing=up"), Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("facing=north"), Variant.variant())
                .addVariant(List.of("facing=south"),Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("facing=west"),Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("facing=east"), Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }
    private static ChildPropertyVariant createDefaultVariant(ChildProperty childProperty) {
        return ChildPropertyVariant.of(childProperty).generate(e ->List.of(Variant.variant()));
    }

    private static ChildPropertyVariant createFaceAndFacingVariant(){
        return ChildPropertyVariant.of(FACE,FACING)
                .addVariant(List.of("face=floor", "facing=east"),
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .addVariant(List.of("face=floor", "facing=west"),
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("face=floor", "facing=south"),
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("face=floor", "facing=north"),
                        Variant.variant())
                .addVariant(List.of("face=wall", "facing=east"),
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("face=wall", "facing=west"),
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("face=wall", "facing=south"),
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("face=wall", "facing=north"),
                        Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("face=ceiling", "facing=east"),
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("face=ceiling", "facing=west"),
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("face=ceiling", "facing=south"),
                        Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("face=ceiling", "facing=north"),
                        Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180));
    }
    private static ChildPropertyVariant createDoorVariant(ResourceLocation bottom,ResourceLocation bottom_hinge,ResourceLocation top,ResourceLocation top_hinge){
        return ChildPropertyVariant.of(HORIZONTAL_FACING,DOUBLE_BLOCK_HALF,DOOR_HINGE,OPEN)
                .addVariant(List.of("facing=east", "half=lower", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, bottom))
                .addVariant(List.of("facing=south", "half=lower", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .addVariant(List.of("facing=west", "half=lower", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("facing=north", "half=lower", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("facing=east", "half=lower", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, bottom_hinge))
                .addVariant(List.of("facing=south", "half=lower", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, bottom_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .addVariant(List.of("facing=west", "half=lower", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, bottom_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("facing=north", "half=lower", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, bottom_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("facing=east", "half=lower", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, bottom_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .addVariant(List.of("facing=south", "half=lower", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, bottom_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("facing=west", "half=lower", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, bottom_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("facing=north", "half=lower", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, bottom_hinge))
                .addVariant(List.of("facing=east", "half=lower", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("facing=south", "half=lower", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, bottom))
                .addVariant(List.of("facing=west", "half=lower", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .addVariant(List.of("facing=north", "half=lower", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, bottom).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("facing=east", "half=upper", "hinge=left", "open=false"),
                    Variant.variant().with(VariantProperties.MODEL, top))
                .addVariant(List.of("facing=south", "half=upper", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .addVariant(List.of("facing=west", "half=upper", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("facing=north", "half=upper", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("facing=east", "half=upper", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, top_hinge))
                .addVariant(List.of("facing=south", "half=upper", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, top_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .addVariant(List.of("facing=west", "half=upper", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, top_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("facing=north", "half=upper", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperties.MODEL, top_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("facing=east", "half=upper", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, top_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .addVariant(List.of("facing=south", "half=upper", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, top_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .addVariant(List.of("facing=west", "half=upper", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, top_hinge).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("facing=north", "half=upper", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, top_hinge))
                .addVariant(List.of("facing=east", "half=upper", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .addVariant(List.of("facing=south", "half=upper", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, top))
                .addVariant(List.of("facing=west", "half=upper", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .addVariant(List.of("facing=north", "half=upper", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperties.MODEL, top).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180));

    }

    private static ChildPropertyVariant createStairVariant(ResourceLocation inner,ResourceLocation straight,ResourceLocation outer){
        return ChildPropertyVariant.of(HORIZONTAL_FACING,HALF,STAIRS_SHAPE)
                .addVariant(List.of("facing=east", "half=bottom", "shape=straight"),
                        Variant.variant().with(VariantProperties.MODEL, straight))
                .addVariant(List.of("facing=west", "half=bottom", "shape=straight"),
                        Variant.variant().with(VariantProperties.MODEL, straight).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=bottom", "shape=straight"),
                        Variant.variant().with(VariantProperties.MODEL, straight).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=bottom", "shape=straight"),
                        Variant.variant().with(VariantProperties.MODEL, straight).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=bottom", "shape=outer_right"),
                        Variant.variant().with(VariantProperties.MODEL, outer))
                .addVariant(List.of("facing=west", "half=bottom", "shape=outer_right"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=bottom", "shape=outer_right"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=bottom", "shape=outer_right"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=bottom", "shape=outer_left"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=bottom", "shape=outer_left"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=bottom", "shape=outer_left"),
                        Variant.variant().with(VariantProperties.MODEL, outer))
                .addVariant(List.of("facing=north", "half=bottom", "shape=outer_left"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=bottom", "shape=inner_right"),
                        Variant.variant().with(VariantProperties.MODEL, inner))
                .addVariant(List.of("facing=west", "half=bottom", "shape=inner_right"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=bottom", "shape=inner_right"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=bottom", "shape=inner_right"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=bottom", "shape=inner_left"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=bottom", "shape=inner_left"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=bottom", "shape=inner_left"),
                        Variant.variant().with(VariantProperties.MODEL, inner))
                .addVariant(List.of("facing=north", "half=bottom", "shape=inner_left"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=top", "shape=straight"),
                        Variant.variant().with(VariantProperties.MODEL, straight).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=top", "shape=straight"),
                        Variant.variant().with(VariantProperties.MODEL, straight).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=top", "shape=straight"),
                        Variant.variant().with(VariantProperties.MODEL, straight).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=top", "shape=straight"),
                        Variant.variant().with(VariantProperties.MODEL, straight).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=top", "shape=outer_right"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=top", "shape=outer_right"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=top", "shape=outer_right"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=top", "shape=outer_right"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=top", "shape=outer_left"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=top", "shape=outer_left"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=top", "shape=outer_left"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=top", "shape=outer_left"),
                        Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=top", "shape=inner_right"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=top", "shape=inner_right"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=top", "shape=inner_right"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=top", "shape=inner_right"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=top", "shape=inner_left"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=top", "shape=inner_left"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=top", "shape=inner_left"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=top", "shape=inner_left"),
                        Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true));

    }

    private static Variant createXYPosVariant(String xpos,String zpos,ResourceLocation none, ResourceLocation singleLine, ResourceLocation middle, ResourceLocation bothLine){
        YuushyaLogger.info(xpos+" "+zpos);
        if (xpos.equals("xpos=none") && zpos.equals("zpos=none")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, none);
        if (xpos.equals("xpos=none") && zpos.equals("zpos=south")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, singleLine);
        if (xpos.equals("xpos=none") && zpos.equals("zpos=north")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.MODEL, singleLine);
        if (xpos.equals("xpos=west") && zpos.equals("zpos=none")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.MODEL, singleLine);
        if (xpos.equals("xpos=east") && zpos.equals("zpos=none")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.MODEL, singleLine);
        if (xpos.equals("xpos=middle") && zpos.equals("zpos=middle")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
        if (xpos.equals("xpos=middle") && zpos.equals("zpos=none")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
        if (xpos.equals("xpos=none") && zpos.equals("zpos=middle")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
        if (xpos.equals("xpos=middle") && zpos.equals("zpos=north")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
        if (xpos.equals("xpos=middle") && zpos.equals("zpos=south")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
        if (xpos.equals("xpos=middle") && zpos.equals("zpos=west")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
        if (xpos.equals("xpos=middle") && zpos.equals("zpos=east")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, middle);
        if (xpos.equals("xpos=west") && zpos.equals("zpos=south")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.MODEL, bothLine);
        if (xpos.equals("xpos=west") && zpos.equals("zpos=north")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.MODEL, bothLine);
        if (xpos.equals("xpos=east") && zpos.equals("zpos=south")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.MODEL, bothLine);
        if (xpos.equals("xpos=east") && zpos.equals("zpos=north")) return
                Variant.variant().with(VariantProperties.UV_LOCK, true).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.MODEL, bothLine);
        return Variant.variant();
    }

    public static class ChildVariant{//合并多个不同
        public static class ChildPropertyVariant{//同比于多Property对应多Variant
            public static class ChildProperty{//同比于单个Property，作为variant的键
                private String property;
                private final List<String> possibleValues=new ArrayList<>();
                private final List<String> possibleVariantKeys=new ArrayList<>();
                public ChildProperty chooseProperty(String property){this.property=property;return this;}
                public ChildProperty setPossibleValues(String ...values){
                    this.possibleValues.addAll(Arrays.asList(values));
                    possibleVariantKeys.clear();
                    possibleVariantKeys.addAll(possibleValues.stream().map((e)->property+"="+e).toList()) ;
                    return this;
                }
                public List<String> getVariantKeys(){return possibleVariantKeys;};
                public int indexOf(String value){return possibleVariantKeys.indexOf(value);}
                public static ChildProperty of(String property,String ...values){
                    return new ChildProperty().chooseProperty(property).setPossibleValues(values);
                }
            }
            private final Map<String,List<Variant>> values=new TreeMap<>();
            private final List<String> possibleVariantKeys=new ArrayList<>();
            private final List<ChildProperty> childProperties=new ArrayList<>();
            private final List<List<String>> possibleCombination=new ArrayList<>();
            public ChildPropertyVariant setProperty(ChildProperty ...childProperties){
                this.childProperties.addAll(List.of(childProperties));
                Arrays.stream(childProperties).forEach((e)->possibleVariantKeys.addAll(e.getVariantKeys()));
                return this;
            }
            public ChildPropertyVariant generate(Function<List<String>, List<Variant>> fromVariantKeyToVariants){
                List<List<String>> childPropertiesVariantKeys=childProperties.stream().map(ChildProperty::getVariantKeys).toList();
                possibleCombination.addAll(YuushyaUtils.CartesianProduct(childPropertiesVariantKeys));
                for(List<String> combination:possibleCombination){
                    values.put(String.join(",",combination),fromVariantKeyToVariants.apply(combination));
                }
                return this;
            }
            public ChildPropertyVariant addVariant(List<String> variantKeys,Variant ...variants){
                if (possibleVariantKeys.containsAll(variantKeys)){
                    values.put(String.join(",",variantKeys),Arrays.asList(variants));
                }
                return this;
            }
            public Map<String,List<Variant>> get(){
                return values;
            }
            public static ChildPropertyVariant of(ChildProperty ...childProperties){
                return new ChildPropertyVariant().setProperty(childProperties);
            }
        }
        private final List<ChildPropertyVariant> childPropertyVariants=new ArrayList<>();
        private final List<Variant> baseVariants=new ArrayList<>();
        public ChildVariant setBaseVariants(Variant ...variant){this.baseVariants.addAll(List.of(variant));return this;}
        public ChildVariant add(ChildPropertyVariant ...childPropertyVariant){this.childPropertyVariants.addAll(List.of(childPropertyVariant));return this;}
        public JsonElement get() {
            Stream<Map.Entry<String, List<Variant>>> resStream=Stream.of(Map.entry("",baseVariants));
            for (ChildPropertyVariant propertyVariant : childPropertyVariants) {
                resStream = resStream.flatMap((fatherPair)-> propertyVariant.get().entrySet().stream().map((childPair)->
                        Map.entry(
                                fatherPair.getKey().isBlank() ?childPair.getKey() :fatherPair.getKey()+","+childPair.getKey(),
                                mergeVariants(fatherPair.getValue(), childPair.getValue()))
                ));

            }
            //这里可以用TreeMap去重，我就不去重了
            JsonObject variantsObject=new JsonObject();
            resStream.forEach(entry->variantsObject.add(entry.getKey(),Variant.convertList(entry.getValue())));
            JsonObject json = new JsonObject();
            json.add("variants", variantsObject);
            return json;
        }
        private static List<Variant> mergeVariants(List<Variant> list1, List<Variant> list2) {
            ImmutableList.Builder<Variant> list3 = ImmutableList.builder();
            for (Variant variant1:list1) for (Variant variant2:list2){
                list3.add(Variant.merge(variant1,variant2));
            }
            return list3.build();
        }
        public static ChildVariant of(Variant ...variant){
            return new ChildVariant().setBaseVariants(variant);
        }
    }
    public static final Splitter STATE_SPLITTER = Splitter.on('#');
    public static List<String> getModelListFromData(List<String> models) {
        return models.stream().map((name) -> {
            if (name.contains("#")) {
                Iterator<String> iterator = STATE_SPLITTER.split(name).iterator();
                iterator.next();
                return iterator.next();
            } else {
                return name;
            }
        }).toList();
    }
    public static void addModeltoJsonObject(Map.Entry<String, JsonElement> entry, String model) {
        if (entry.getValue() instanceof JsonArray nowArray) {
            JsonObject oneValue = nowArray.get(0).getAsJsonObject().deepCopy();
            oneValue.add("model", new JsonPrimitive(model));
            nowArray.add(oneValue);
        } else {
            JsonObject nowValue = entry.getValue().getAsJsonObject();
            nowValue.add("model", new JsonPrimitive(model));
            JsonArray newArray = new JsonArray();
            newArray.add(nowValue);
            entry.setValue(newArray);
        }
    }
    public static JsonElement addModeltoBlockStateJsonObject(JsonObject rawJson ,List<String> models){
        JsonObject variants = rawJson.get("variants").getAsJsonObject();
        for (String name : models) {
            if (name.contains("#")) {
                Iterator<String> iterator = STATE_SPLITTER.split(name).iterator();
                String state = iterator.next();
                String model = iterator.next();
                variants.entrySet().stream().filter((entry) -> {
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
        return rawJson;
    }


}
