package com.yuushya.datagen.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.yuushya.datagen.data.BlockStateData.ChildVariant.ChildPropertyVariant;
import com.yuushya.datagen.data.BlockStateData.ChildVariant.ChildPropertyVariant.ChildProperty;
import com.yuushya.datagen.utils.ResourceLocation;
import com.yuushya.datagen.utils.Utils;
import com.yuushya.datagen.utils.Variant;
import com.yuushya.datagen.utils.VariantProperty;
import com.yuushya.registries.YuushyaRegistryData;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.yuushya.datagen.utils.Utils.COMMA_SPLITTER;
import static com.yuushya.datagen.utils.Utils.CartesianProduct;

public class BlockStateData {
    //Property
    public static final ChildProperty FACING=ChildProperty.of("facing","north","east","south","west","up","down");
    public static final ChildProperty HORIZONTAL_FACING=ChildProperty.of("facing","north","east","south","west");
    public static final ChildProperty FACE=ChildProperty.of("face","floor","wall","ceiling");
    public static final ChildProperty FORM8=ChildProperty.of("form","0","1","2","3","4","5","6","7");
    public static final ChildProperty FORM7=ChildProperty.of("form","0","1","2","3","4","5","6");
    public static final ChildProperty FORM6=ChildProperty.of("form","0","1","2","3","4","5");
    public static final ChildProperty FORM5=ChildProperty.of("form","0","1","2","3","4");
    public static final ChildProperty FORM4=ChildProperty.of("form","0","1","2","3");
    public static final ChildProperty FORM3=ChildProperty.of("form","0","1","2");
    public static final ChildProperty FORM2=ChildProperty.of("form","0","1");

    public static ChildProperty forms(int n){
        return switch (n){
            case 0,1->null;
            case 2->FORM2;
            case 3->FORM3;
            case 4->FORM4;
            case 5->FORM5;
            case 6->FORM6;
            case 7->FORM7;
            default -> FORM8;
        };
    }

    public static final ChildProperty POS_HORIZON=ChildProperty.of("pos","left","middle","right","none");
    public static final ChildProperty FRONT=ChildProperty.of("front","left","middle","right","none");
    public static final ChildProperty BACK=ChildProperty.of("back","left","middle","right","none");
    public static final ChildProperty POS_VERTICAL=ChildProperty.of("pos","top","middle","bottom","none");
    public static final ChildProperty YPOS=ChildProperty.of("ypos","top","middle","bottom","none");
    public static final ChildProperty XPOS=ChildProperty.of("xpos","west","east","middle","none");
    public static final ChildProperty ZPOS=ChildProperty.of("zpos","north","south","middle","none");
    public static final ChildProperty X=ChildProperty.of("x","0","1","2","3","4","5","6","7","8","9","10","11");
    public static final ChildProperty Y=ChildProperty.of("y","0","1","2","3","4","5","6","7","8","9","10","11");
    public static final ChildProperty Z=ChildProperty.of("z","0","1","2","3","4","5","6","7","8","9","10","11");
    public static final ChildProperty POWERED=ChildProperty.of("powered","true","false");
    public static final ChildProperty HALF=ChildProperty.of("half","top","bottom");
    public static final ChildProperty SLAB_TYPE = ChildProperty.of("type","top","bottom","double");
    public static final ChildProperty STAIRS_SHAPE=ChildProperty.of("shape","straight","inner_left","inner_right","outer_left","outer_right");
    public static final ChildProperty DOOR_HINGE=ChildProperty.of("hinge","left","right");
    public static final ChildProperty DOUBLE_BLOCK_HALF=ChildProperty.of("half","upper","lower");
    public static final ChildProperty OPEN=ChildProperty.of("open","true","false");
    public static final ChildProperty SHAPE = ChildProperty.of("shape","straight","inner","outer");



    public static JsonElement genSimpleBlock(ResourceLocation resourceLocation) {
        return ChildVariant.of(Variant.variant().with(VariantProperty.MODEL,resourceLocation)).get();
    }
    //TODO: support for block which written in both kit and states
    private static final ResourceLocation blankModel=ResourceLocation.parse("yuushya:extra_building_material/blank");
    public static JsonElement genBlockState(YuushyaRegistryData.Block.BlockState blockState) {
        ChildVariant childVariant;
        if (blockState.kit!=null&&!blockState.kit.isEmpty()){
            int formsNum=blockState.forms.size();
            ChildProperty FORM = forms(formsNum);
            Variant baseVariant=Variant.variant().with(VariantProperty.MODEL,ResourceLocation.parse(blockState.forms.get(0).get(0)));
            childVariant= switch (blockState.kit){
                case "normal"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM).generate((variantKeyList)->{
                            //如果FROM为空根本进不来
                                int i=FORM.indexOf(variantKeyList.get(0));
                                return List.of(i < formsNum
                                        ? Variant.variant().with(VariantProperty.MODEL, ResourceLocation.parse(blockState.forms.get(i).get(0)))
                                        : Variant.variant()) ;

                        }));
                case "attachment"-> ChildVariant.of(baseVariant)
                        .add(createFaceAndFacingVariant())
                        .add(ChildPropertyVariant.of(FORM).generate((variantKeyList)->{

                                int i=FORM.indexOf(variantKeyList.get(0));
                                return List.of(i < formsNum
                                        ? Variant.variant().with(VariantProperty.MODEL, ResourceLocation.parse(blockState.forms.get(i).get(0)))
                                        : Variant.variant()) ;

                        }));
                case "line"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM,POS_HORIZON).generate((variantKeyList)->{
                            int offset = (FORM!=null) ? 1 : 0;
                            int i = (FORM!=null) ? FORM.indexOf(variantKeyList.get(0)) : 0;
                            int j=POS_HORIZON.indexOf(variantKeyList.get(offset));
                            return List.of(i < formsNum
                                    ? Variant.variant().with(VariantProperty.MODEL, ResourceLocation.parse(blockState.forms.get(i).get(j)))
                                    : Variant.variant());
                        }));
                case "line_corner"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM,POS_HORIZON,SHAPE).generate((variantKeyList)->{
                            int offset = (FORM!=null) ? 1 : 0;
                            int i = (FORM!=null) ? FORM.indexOf(variantKeyList.get(0)) : 0;
                            int j=POS_HORIZON.indexOf(variantKeyList.get(offset));
                            int k=SHAPE.indexOf(variantKeyList.get(offset+1));
                            int l = "pos=left".equals(variantKeyList.get(offset)) && !"shape=straight".equals(variantKeyList.get(offset+1)) ? 3+k
                                    :"pos=right".equals(variantKeyList.get(offset)) && !"shape=straight".equals(variantKeyList.get(offset+1)) ? 5+k
                                    :j;
                            // ResourceLocation left, ResourceLocation midlde, ResourceLocation right, ResourceLocation none, ResourceLocation inner_left,ResourceLocation outer_left, ResourceLocation inner_right,ResourceLocation outer_right
                            return List.of(i < formsNum
                                    ? Variant.variant().with(VariantProperty.MODEL, ResourceLocation.parse(blockState.forms.get(i).get(l)))
                                    : Variant.variant());

                        }));
                case "line_cross_simple"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM,POS_HORIZON,FRONT,BACK).generate((variantKeyList)->{
                            int offset = (FORM!=null) ? 1 : 0;
                            int i = (FORM!=null) ? FORM.indexOf(variantKeyList.get(0)) : 0;
                            if (i < formsNum){
                                ResourceLocation none = ResourceLocation.parse(blockState.forms.get(i).get(0));
                                ResourceLocation none_left = ResourceLocation.parse(blockState.forms.get(i).get(1));
                                ResourceLocation none_right = ResourceLocation.parse(blockState.forms.get(i).get(2));
                                ResourceLocation left = ResourceLocation.parse(blockState.forms.get(i).get(3));
                                ResourceLocation middle = ResourceLocation.parse(blockState.forms.get(i).get(4));
                                ResourceLocation middle_left = ResourceLocation.parse(blockState.forms.get(i).get(5));
                                ResourceLocation middle_right = ResourceLocation.parse(blockState.forms.get(i).get(6));
                                ResourceLocation right = ResourceLocation.parse(blockState.forms.get(i).get(7));
                                return List.of(createHorizonFrontBackVariant(variantKeyList.get(offset),variantKeyList.get(offset+1),variantKeyList.get(offset+2),
                                        none,none_left,none_right,right,none_left,middle_right,left,middle_left,none_right,
                                        left,middle_left,none_right,middle,middle_left,middle_right,left,middle_left,none_right,
                                        middle,middle_left,middle_right,middle,middle_left,middle_right,middle,middle_left,middle_right,
                                        right,none_left,middle_right,right,none_left,middle_right,middle,middle_left,middle_right
                                ));
                            }
                            else return List.of(Variant.variant());
                        }));
                case "line_cross"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM,POS_HORIZON,FRONT,BACK).generate((variantKeyList)->{
                            int offset = (FORM!=null) ? 1 : 0;
                            int i = (FORM!=null) ? FORM.indexOf(variantKeyList.get(0)) : 0;
                            if (i < formsNum){
                                ResourceLocation none_none_none = ResourceLocation.parse(blockState.forms.get(i).get(0));
                                ResourceLocation none_none_left = ResourceLocation.parse(blockState.forms.get(i).get(1));
                                ResourceLocation none_none_right = ResourceLocation.parse(blockState.forms.get(i).get(2));
                                ResourceLocation none_left_none = ResourceLocation.parse(blockState.forms.get(i).get(3));
                                ResourceLocation none_left_left = ResourceLocation.parse(blockState.forms.get(i).get(4));
                                ResourceLocation none_left_right = ResourceLocation.parse(blockState.forms.get(i).get(5));
                                ResourceLocation none_right_none = ResourceLocation.parse(blockState.forms.get(i).get(6));
                                ResourceLocation none_right_left = ResourceLocation.parse(blockState.forms.get(i).get(7));
                                ResourceLocation none_right_right = ResourceLocation.parse(blockState.forms.get(i).get(8));
                                ResourceLocation left_none_none = ResourceLocation.parse(blockState.forms.get(i).get(9));
                                ResourceLocation left_none_left = ResourceLocation.parse(blockState.forms.get(i).get(10));
                                ResourceLocation left_none_right = ResourceLocation.parse(blockState.forms.get(i).get(11));
                                ResourceLocation left_left_none = ResourceLocation.parse(blockState.forms.get(i).get(12));
                                ResourceLocation left_left_left = ResourceLocation.parse(blockState.forms.get(i).get(13));
                                ResourceLocation left_left_right = ResourceLocation.parse(blockState.forms.get(i).get(14));
                                ResourceLocation left_right_none = ResourceLocation.parse(blockState.forms.get(i).get(15));
                                ResourceLocation left_right_left = ResourceLocation.parse(blockState.forms.get(i).get(16));
                                ResourceLocation left_right_right = ResourceLocation.parse(blockState.forms.get(i).get(17));
                                ResourceLocation middle_none_none = ResourceLocation.parse(blockState.forms.get(i).get(18));
                                ResourceLocation middle_none_left = ResourceLocation.parse(blockState.forms.get(i).get(19));
                                ResourceLocation middle_none_right = ResourceLocation.parse(blockState.forms.get(i).get(20));
                                ResourceLocation middle_left_none = ResourceLocation.parse(blockState.forms.get(i).get(21));
                                ResourceLocation middle_left_left = ResourceLocation.parse(blockState.forms.get(i).get(22));
                                ResourceLocation middle_left_right = ResourceLocation.parse(blockState.forms.get(i).get(23));
                                ResourceLocation middle_right_none = ResourceLocation.parse(blockState.forms.get(i).get(24));
                                ResourceLocation middle_right_left = ResourceLocation.parse(blockState.forms.get(i).get(25));
                                ResourceLocation middle_right_right = ResourceLocation.parse(blockState.forms.get(i).get(26));
                                ResourceLocation right_none_none = ResourceLocation.parse(blockState.forms.get(i).get(27));
                                ResourceLocation right_none_left = ResourceLocation.parse(blockState.forms.get(i).get(28));
                                ResourceLocation right_none_right = ResourceLocation.parse(blockState.forms.get(i).get(29));
                                ResourceLocation right_left_none = ResourceLocation.parse(blockState.forms.get(i).get(30));
                                ResourceLocation right_left_left = ResourceLocation.parse(blockState.forms.get(i).get(31));
                                ResourceLocation right_left_right = ResourceLocation.parse(blockState.forms.get(i).get(32));
                                ResourceLocation right_right_none = ResourceLocation.parse(blockState.forms.get(i).get(33));
                                ResourceLocation right_right_left = ResourceLocation.parse(blockState.forms.get(i).get(34));
                                ResourceLocation right_right_right = ResourceLocation.parse(blockState.forms.get(i).get(35));
                                return List.of(createHorizonFrontBackVariant(variantKeyList.get(offset),variantKeyList.get(offset+1),variantKeyList.get(offset+2),
                                        none_none_none, none_none_left, none_none_right, none_left_none, none_left_left, none_left_right, none_right_none, none_right_left, none_right_right,
                                        left_none_none, left_none_left, left_none_right, left_left_none, left_left_left, left_left_right, left_right_none, left_right_left, left_right_right,
                                        middle_none_none, middle_none_left, middle_none_right, middle_left_none, middle_left_left, middle_left_right, middle_right_none, middle_right_left, middle_right_right,
                                        right_none_none, right_none_left, right_none_right, right_left_none, right_left_left, right_left_right, right_right_none, right_right_left, right_right_right
                                ));
                            }
                            else return List.of(Variant.variant());
                        }));
                case "pole_line"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM,POS_HORIZON,YPOS).generate((variantKeyList)->{
                            int offset = (FORM!=null) ? 1 : 0;
                            int i = (FORM!=null) ? FORM.indexOf(variantKeyList.get(0)) : 0;
                            if (i < formsNum){
                                ResourceLocation left_none = ResourceLocation.parse(blockState.forms.get(i).get(0));
                                ResourceLocation middle_none = ResourceLocation.parse(blockState.forms.get(i).get(1));
                                ResourceLocation right_none = ResourceLocation.parse(blockState.forms.get(i).get(2));
                                ResourceLocation none_none = ResourceLocation.parse(blockState.forms.get(i).get(3));
                                ResourceLocation left_top = ResourceLocation.parse(blockState.forms.get(i).get(4));
                                ResourceLocation middle_top = ResourceLocation.parse(blockState.forms.get(i).get(5));
                                ResourceLocation right_top = ResourceLocation.parse(blockState.forms.get(i).get(6));
                                ResourceLocation none_top = ResourceLocation.parse(blockState.forms.get(i).get(7));
                                ResourceLocation left_middle = ResourceLocation.parse(blockState.forms.get(i).get(8));
                                ResourceLocation middle_middle = ResourceLocation.parse(blockState.forms.get(i).get(9));
                                ResourceLocation right_middle = ResourceLocation.parse(blockState.forms.get(i).get(10));
                                ResourceLocation none_middle = ResourceLocation.parse(blockState.forms.get(i).get(11));
                                ResourceLocation left_bottom = ResourceLocation.parse(blockState.forms.get(i).get(12));
                                ResourceLocation middle_bottom = ResourceLocation.parse(blockState.forms.get(i).get(13));
                                ResourceLocation right_bottom = ResourceLocation.parse(blockState.forms.get(i).get(14));
                                ResourceLocation none_bottom = ResourceLocation.parse(blockState.forms.get(i).get(15));
                                return List.of(createHorizonYPosVariant(variantKeyList.get(offset),variantKeyList.get(offset+1),
                                        left_none, middle_none, right_none, none_none,
                                        left_top, middle_top, right_top, none_top,
                                        left_middle, middle_middle, right_middle, none_middle,
                                        left_bottom, middle_bottom, right_bottom, none_bottom));
                            }
                            else return List.of(Variant.variant());
                        }));
                case "face"->ChildVariant.of(baseVariant)
                        .add(ChildPropertyVariant.of(FORM,XPOS,ZPOS).generate((variantKeyList)->{
                                int offset = (FORM!=null) ? 1 : 0;
                                int i = (FORM!=null) ? FORM.indexOf(variantKeyList.get(0)) : 0;
                                if (i < formsNum){
                                    ResourceLocation none = ResourceLocation.parse(blockState.forms.get(i).get(0));
                                    ResourceLocation singleLine = ResourceLocation.parse(blockState.forms.get(i).get(1));
                                    ResourceLocation middle = ResourceLocation.parse(blockState.forms.get(i).get(2));
                                    ResourceLocation bothLine = ResourceLocation.parse(blockState.forms.get(i).get(3));
                                    return List.of(createXYPosVariant(variantKeyList.get(offset),variantKeyList.get(offset+1),none,singleLine,middle,bothLine)) ;
                                }
                                else return List.of(Variant.variant());
                        }));
                case "pole"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM,POS_VERTICAL).generate((variantKeyList)->{
                            int offset = (FORM!=null) ? 1 : 0;
                            int i = (FORM!=null) ? FORM.indexOf(variantKeyList.get(0)) : 0;
                            int j=POS_VERTICAL.indexOf(variantKeyList.get(offset));
                            return List.of(i < formsNum
                                    ? Variant.variant().with(VariantProperty.MODEL, ResourceLocation.parse(blockState.forms.get(i).get(j)))
                                    : Variant.variant());
                        }));
                case "tri_part"->ChildVariant.of(baseVariant)
                        .add(createHorizonFacingVariant())
                        .add(ChildPropertyVariant.of(FORM,POS_VERTICAL).generate((variantKeyList)->{
                            int offset = (FORM!=null) ? 1 : 0;
                            int i = (FORM!=null) ? FORM.indexOf(variantKeyList.get(0)) : 0;
                            if (i>=formsNum) return List.of(Variant.variant());
                            else if (variantKeyList.get(offset).equals("pos=middle")) return List.of(Variant.variant().with(VariantProperty.MODEL, ResourceLocation.parse(blockState.forms.get(i).get(0))));
                            else return List.of(Variant.variant().with(VariantProperty.MODEL,blankModel));
                        }));
                case "VanillaSlabBlock"->{
                    ResourceLocation bottom=ResourceLocation.parse(blockState.forms.get(0).get(0));
                    ResourceLocation _double=ResourceLocation.parse(blockState.forms.get(0).get(1));
                    ResourceLocation top=ResourceLocation.parse(blockState.forms.get(0).get(2));
                    yield  ChildVariant.of(baseVariant)
                            .add(createSlabVariant(bottom,_double,top));
                }
                case "VanillaStairBlock"->{
                    ResourceLocation inner=ResourceLocation.parse(blockState.forms.get(0).get(0));
                    ResourceLocation straight=ResourceLocation.parse(blockState.forms.get(0).get(1));
                    ResourceLocation outer=ResourceLocation.parse(blockState.forms.get(0).get(2));
                    yield  ChildVariant.of(baseVariant)
                        .add(createStairVariant(inner,straight,outer));
                }
                case "VanillaDoorBlock"->{
                    ResourceLocation bottom=ResourceLocation.parse(blockState.forms.get(0).get(0));
                    ResourceLocation bottom_hinge=ResourceLocation.parse(blockState.forms.get(0).get(1));
                    ResourceLocation top=ResourceLocation.parse(blockState.forms.get(0).get(2));
                    ResourceLocation top_hinge=ResourceLocation.parse(blockState.forms.get(0).get(3));
                    yield  ChildVariant.of(baseVariant)
                            .add(createDoorVariant(bottom,bottom_hinge,top,top_hinge));
                }

                default -> ChildVariant.of(baseVariant);
            };
            return childVariant.get();
        }
        else {
            Variant baseVariant=Variant.variant().with(VariantProperty.MODEL,ResourceLocation.parse(getModelListFromData(blockState.models).get(0)));
            childVariant=ChildVariant.of(baseVariant);
            if(blockState.states==null)
                return addModeltoBlockStateJsonObject(childVariant.get().getAsJsonObject(),blockState.models);
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
                childVariant.add(createDefaultVariant(FORM8));
            }

            if (blockState.states.contains("pos_horizon")) {
                childVariant.add(createDefaultVariant(POS_HORIZON));
            } else if (blockState.states.contains("pos_vertical")) {
                childVariant.add(createDefaultVariant(POS_VERTICAL));
            }
            return addModeltoBlockStateJsonObject(childVariant.get().getAsJsonObject(),blockState.models);
        }
    }
    private static ChildPropertyVariant createFaceVariant() {
        return ChildPropertyVariant.of(FACE)
                .addVariant(List.of("face=wall"), Variant.variant().with(VariantProperty.X_ROT, VariantProperty.Rotation.R90))
                .addVariant(List.of("face=floor"), Variant.variant())
                .addVariant(List.of("face=ceiling"), Variant.variant().with(VariantProperty.X_ROT, VariantProperty.Rotation.R180));
    }
    private static ChildPropertyVariant createHorizonFacingVariant() {
        return ChildPropertyVariant.of(HORIZONTAL_FACING)
                .addVariant(List.of("facing=north"), Variant.variant())
                .addVariant(List.of("facing=south"),Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("facing=west"),Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("facing=east"), Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90));
    }

    private static ChildPropertyVariant createFacingVariant() {
        return ChildPropertyVariant.of(HORIZONTAL_FACING)
                .addVariant(List.of("facing=down"), Variant.variant().with(VariantProperty.X_ROT, VariantProperty.Rotation.R90))
                .addVariant(List.of("facing=up"), Variant.variant().with(VariantProperty.X_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("facing=north"), Variant.variant())
                .addVariant(List.of("facing=south"),Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("facing=west"),Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("facing=east"), Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90));
    }
    private static ChildPropertyVariant createDefaultVariant(ChildProperty childProperty) {
        return ChildPropertyVariant.of(childProperty).generate(e ->List.of(Variant.variant()));
    }

    private static ChildPropertyVariant createFaceAndFacingVariant(){
        return ChildPropertyVariant.of(FACE,FACING)
                .addVariant(List.of("face=floor", "facing=east"),
                        Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90))
                .addVariant(List.of("face=floor", "facing=west"),
                        Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("face=floor", "facing=south"),
                        Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("face=floor", "facing=north"),
                        Variant.variant())
                .addVariant(List.of("face=wall", "facing=east"),
                        Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.X_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("face=wall", "facing=west"),
                        Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.X_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("face=wall", "facing=south"),
                        Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.X_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("face=wall", "facing=north"),
                        Variant.variant().with(VariantProperty.X_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("face=ceiling", "facing=east"),
                        Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("face=ceiling", "facing=west"),
                        Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("face=ceiling", "facing=south"),
                        Variant.variant().with(VariantProperty.X_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("face=ceiling", "facing=north"),
                        Variant.variant().with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180));
    }
    private static ChildPropertyVariant createDoorVariant(ResourceLocation bottom,ResourceLocation bottom_hinge,ResourceLocation top,ResourceLocation top_hinge){
        return ChildPropertyVariant.of(HORIZONTAL_FACING,DOUBLE_BLOCK_HALF,DOOR_HINGE,OPEN)
                .addVariant(List.of("facing=east", "half=lower", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, bottom))
                .addVariant(List.of("facing=south", "half=lower", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, bottom).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90))
                .addVariant(List.of("facing=west", "half=lower", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, bottom).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("facing=north", "half=lower", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, bottom).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("facing=east", "half=lower", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, bottom_hinge))
                .addVariant(List.of("facing=south", "half=lower", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, bottom_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90))
                .addVariant(List.of("facing=west", "half=lower", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, bottom_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("facing=north", "half=lower", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, bottom_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("facing=east", "half=lower", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, bottom_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90))
                .addVariant(List.of("facing=south", "half=lower", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, bottom_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("facing=west", "half=lower", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, bottom_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("facing=north", "half=lower", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, bottom_hinge))
                .addVariant(List.of("facing=east", "half=lower", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, bottom).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("facing=south", "half=lower", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, bottom))
                .addVariant(List.of("facing=west", "half=lower", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, bottom).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90))
                .addVariant(List.of("facing=north", "half=lower", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, bottom).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("facing=east", "half=upper", "hinge=left", "open=false"),
                    Variant.variant().with(VariantProperty.MODEL, top))
                .addVariant(List.of("facing=south", "half=upper", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, top).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90))
                .addVariant(List.of("facing=west", "half=upper", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, top).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("facing=north", "half=upper", "hinge=left", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, top).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("facing=east", "half=upper", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, top_hinge))
                .addVariant(List.of("facing=south", "half=upper", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, top_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90))
                .addVariant(List.of("facing=west", "half=upper", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, top_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("facing=north", "half=upper", "hinge=right", "open=false"),
                        Variant.variant().with(VariantProperty.MODEL, top_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("facing=east", "half=upper", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, top_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90))
                .addVariant(List.of("facing=south", "half=upper", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, top_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180))
                .addVariant(List.of("facing=west", "half=upper", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, top_hinge).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("facing=north", "half=upper", "hinge=left", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, top_hinge))
                .addVariant(List.of("facing=east", "half=upper", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, top).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270))
                .addVariant(List.of("facing=south", "half=upper", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, top))
                .addVariant(List.of("facing=west", "half=upper", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, top).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90))
                .addVariant(List.of("facing=north", "half=upper", "hinge=right", "open=true"),
                        Variant.variant().with(VariantProperty.MODEL, top).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180));

    }

    private static ChildPropertyVariant createSlabVariant(ResourceLocation bottom,ResourceLocation _double,ResourceLocation top){
        return ChildPropertyVariant.of(SLAB_TYPE)
                .addVariant(List.of("type=bottom"),Variant.variant().with(VariantProperty.MODEL, bottom))
                .addVariant(List.of("type=double"),Variant.variant().with(VariantProperty.MODEL, _double))
                .addVariant(List.of("type=top"),Variant.variant().with(VariantProperty.MODEL, top));
    }

    private static ChildPropertyVariant createStairVariant(ResourceLocation inner,ResourceLocation straight,ResourceLocation outer){
        return ChildPropertyVariant.of(HORIZONTAL_FACING,HALF,STAIRS_SHAPE)
                .addVariant(List.of("facing=east", "half=bottom", "shape=straight"),
                        Variant.variant().with(VariantProperty.MODEL, straight))
                .addVariant(List.of("facing=west", "half=bottom", "shape=straight"),
                        Variant.variant().with(VariantProperty.MODEL, straight).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=bottom", "shape=straight"),
                        Variant.variant().with(VariantProperty.MODEL, straight).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=bottom", "shape=straight"),
                        Variant.variant().with(VariantProperty.MODEL, straight).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=bottom", "shape=outer_right"),
                        Variant.variant().with(VariantProperty.MODEL, outer))
                .addVariant(List.of("facing=west", "half=bottom", "shape=outer_right"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=bottom", "shape=outer_right"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=bottom", "shape=outer_right"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=bottom", "shape=outer_left"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=bottom", "shape=outer_left"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=bottom", "shape=outer_left"),
                        Variant.variant().with(VariantProperty.MODEL, outer))
                .addVariant(List.of("facing=north", "half=bottom", "shape=outer_left"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=bottom", "shape=inner_right"),
                        Variant.variant().with(VariantProperty.MODEL, inner))
                .addVariant(List.of("facing=west", "half=bottom", "shape=inner_right"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=bottom", "shape=inner_right"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=bottom", "shape=inner_right"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=bottom", "shape=inner_left"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=bottom", "shape=inner_left"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=bottom", "shape=inner_left"),
                        Variant.variant().with(VariantProperty.MODEL, inner))
                .addVariant(List.of("facing=north", "half=bottom", "shape=inner_left"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=top", "shape=straight"),
                        Variant.variant().with(VariantProperty.MODEL, straight).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=top", "shape=straight"),
                        Variant.variant().with(VariantProperty.MODEL, straight).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=top", "shape=straight"),
                        Variant.variant().with(VariantProperty.MODEL, straight).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=top", "shape=straight"),
                        Variant.variant().with(VariantProperty.MODEL, straight).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=top", "shape=outer_right"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=top", "shape=outer_right"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=top", "shape=outer_right"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=top", "shape=outer_right"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=top", "shape=outer_left"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=top", "shape=outer_left"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=top", "shape=outer_left"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=top", "shape=outer_left"),
                        Variant.variant().with(VariantProperty.MODEL, outer).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=top", "shape=inner_right"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=top", "shape=inner_right"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=top", "shape=inner_right"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=top", "shape=inner_right"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=east", "half=top", "shape=inner_left"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=west", "half=top", "shape=inner_left"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=south", "half=top", "shape=inner_left"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.UV_LOCK, true))
                .addVariant(List.of("facing=north", "half=top", "shape=inner_left"),
                        Variant.variant().with(VariantProperty.MODEL, inner).with(VariantProperty.X_ROT, VariantProperty.Rotation.R180).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.UV_LOCK, true));

    }

    private static Variant createHorizonFrontBackVariant( String pos, String front, String back,
                                                        ResourceLocation none_none_none,ResourceLocation none_none_left,ResourceLocation none_none_right,ResourceLocation none_left_none,ResourceLocation none_left_left,ResourceLocation none_left_right,ResourceLocation none_right_none,ResourceLocation none_right_left,ResourceLocation none_right_right,
                                                        ResourceLocation left_none_none,ResourceLocation left_none_left,ResourceLocation left_none_right,ResourceLocation left_left_none,ResourceLocation left_left_left,ResourceLocation left_left_right,ResourceLocation left_right_none,ResourceLocation left_right_left,ResourceLocation left_right_right,
                                                        ResourceLocation middle_none_none,ResourceLocation middle_none_left,ResourceLocation middle_none_right,ResourceLocation middle_left_none,ResourceLocation middle_left_left,ResourceLocation middle_left_right,ResourceLocation middle_right_none,ResourceLocation middle_right_left,ResourceLocation middle_right_right,
                                                        ResourceLocation right_none_none,ResourceLocation right_none_left,ResourceLocation right_none_right,ResourceLocation right_left_none,ResourceLocation right_left_left,ResourceLocation right_left_right,ResourceLocation right_right_none,ResourceLocation right_right_left,ResourceLocation right_right_right
                                                        ){
        if (pos.equals("pos=none") && front.equals("front=none") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_none_none);
        if (pos.equals("pos=none") && front.equals("front=none") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_none_left);
        if (pos.equals("pos=none") && front.equals("front=none") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_none_right);
        if (pos.equals("pos=none") && front.equals("front=left") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_left_none);
        if (pos.equals("pos=none") && front.equals("front=left") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_left_left);
        if (pos.equals("pos=none") && front.equals("front=left") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_left_right);
        if (pos.equals("pos=none") && front.equals("front=right") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_right_none);
        if (pos.equals("pos=none") && front.equals("front=right") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_right_left);
        if (pos.equals("pos=none") && front.equals("front=right") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_right_right);
        if (pos.equals("pos=left") && front.equals("front=none") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_none_none);
        if (pos.equals("pos=left") && front.equals("front=none") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_none_left);
        if (pos.equals("pos=left") && front.equals("front=none") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_none_right);
        if (pos.equals("pos=left") && front.equals("front=left") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_left_none);
        if (pos.equals("pos=left") && front.equals("front=left") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_left_left);
        if (pos.equals("pos=left") && front.equals("front=left") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_left_right);
        if (pos.equals("pos=left") && front.equals("front=right") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_right_none);
        if (pos.equals("pos=left") && front.equals("front=right") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_right_left);
        if (pos.equals("pos=left") && front.equals("front=right") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_right_right);
        if (pos.equals("pos=middle") && front.equals("front=none") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_none_none);
        if (pos.equals("pos=middle") && front.equals("front=none") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_none_left);
        if (pos.equals("pos=middle") && front.equals("front=none") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_none_right);
        if (pos.equals("pos=middle") && front.equals("front=left") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_left_none);
        if (pos.equals("pos=middle") && front.equals("front=left") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_left_left);
        if (pos.equals("pos=middle") && front.equals("front=left") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_left_right);
        if (pos.equals("pos=middle") && front.equals("front=right") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_right_none);
        if (pos.equals("pos=middle") && front.equals("front=right") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_right_left);
        if (pos.equals("pos=middle") && front.equals("front=right") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_right_right);
        if (pos.equals("pos=right") && front.equals("front=none") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_none_none);
        if (pos.equals("pos=right") && front.equals("front=none") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_none_left);
        if (pos.equals("pos=right") && front.equals("front=none") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_none_right);
        if (pos.equals("pos=right") && front.equals("front=left") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_left_none);
        if (pos.equals("pos=right") && front.equals("front=left") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_left_left);
        if (pos.equals("pos=right") && front.equals("front=left") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_left_right);
        if (pos.equals("pos=right") && front.equals("front=right") && back.equals("back=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_right_none);
        if (pos.equals("pos=right") && front.equals("front=right") && back.equals("back=left")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_right_left);
        if (pos.equals("pos=right") && front.equals("front=right") && back.equals("back=right")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_right_right);
        return Variant.variant();
    }

    private static Variant createHorizonYPosVariant(String pos,String ypos,
                                                    ResourceLocation left_none,ResourceLocation middle_none,ResourceLocation right_none,ResourceLocation none_none,
                                                    ResourceLocation left_top,ResourceLocation middle_top,ResourceLocation right_top,ResourceLocation none_top,
                                                    ResourceLocation left_middle,ResourceLocation middle_middle,ResourceLocation right_middle,ResourceLocation none_middle,
                                                    ResourceLocation left_bottom,ResourceLocation middle_bottom,ResourceLocation right_bottom,ResourceLocation none_bottom
                                                    ){
        if (pos.equals("pos=left") && ypos.equals("ypos=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_none);
        if (pos.equals("pos=middle") && ypos.equals("ypos=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_none);
        if (pos.equals("pos=right") && ypos.equals("ypos=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_none);
        if (pos.equals("pos=none") && ypos.equals("ypos=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_none);
        if (pos.equals("pos=left") && ypos.equals("ypos=top")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_top);
        if (pos.equals("pos=middle") && ypos.equals("ypos=top")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_top);
        if (pos.equals("pos=right") && ypos.equals("ypos=top")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_top);
        if (pos.equals("pos=none") && ypos.equals("ypos=top")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_top);
        if (pos.equals("pos=left") && ypos.equals("ypos=middle")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_middle);
        if (pos.equals("pos=middle") && ypos.equals("ypos=middle")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_middle);
        if (pos.equals("pos=right") && ypos.equals("ypos=middle")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_middle);
        if (pos.equals("pos=none") && ypos.equals("ypos=middle")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_middle);
        if (pos.equals("pos=left") && ypos.equals("ypos=bottom")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, left_bottom);
        if (pos.equals("pos=middle") && ypos.equals("ypos=bottom")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, middle_bottom);
        if (pos.equals("pos=right") && ypos.equals("ypos=bottom")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, right_bottom);
        if (pos.equals("pos=none") && ypos.equals("ypos=bottom")) return
                Variant.variant().with(VariantProperty.UV_LOCK, false).with(VariantProperty.MODEL, none_bottom);

        return Variant.variant();
    }

    private static Variant createXYPosVariant(String xpos,String zpos,ResourceLocation none, ResourceLocation singleLine, ResourceLocation middle, ResourceLocation bothLine){

        if (xpos.equals("xpos=none") && zpos.equals("zpos=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.MODEL, none);
        if (xpos.equals("xpos=none") && zpos.equals("zpos=south")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.MODEL, singleLine);
        if (xpos.equals("xpos=none") && zpos.equals("zpos=north")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.MODEL, singleLine);
        if (xpos.equals("xpos=west") && zpos.equals("zpos=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.MODEL, singleLine);
        if (xpos.equals("xpos=east") && zpos.equals("zpos=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.MODEL, singleLine);
        if (xpos.equals("xpos=middle") && zpos.equals("zpos=middle")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.MODEL, middle);
        if (xpos.equals("xpos=middle") && zpos.equals("zpos=none")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.MODEL, middle);
        if (xpos.equals("xpos=none") && zpos.equals("zpos=middle")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.MODEL, middle);
        if (xpos.equals("xpos=middle") && zpos.equals("zpos=north")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.MODEL, middle);
        if (xpos.equals("xpos=middle") && zpos.equals("zpos=south")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.MODEL, middle);
        if (xpos.equals("xpos=west") && zpos.equals("zpos=middle")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.MODEL, middle);
        if (xpos.equals("xpos=east") && zpos.equals("zpos=middle")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.MODEL, middle);
        if (xpos.equals("xpos=west") && zpos.equals("zpos=south")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.MODEL, bothLine);
        if (xpos.equals("xpos=west") && zpos.equals("zpos=north")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R90).with(VariantProperty.MODEL, bothLine);
        if (xpos.equals("xpos=east") && zpos.equals("zpos=north")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R180).with(VariantProperty.MODEL, bothLine);
        if (xpos.equals("xpos=east") && zpos.equals("zpos=south")) return
                Variant.variant().with(VariantProperty.UV_LOCK, true).with(VariantProperty.Y_ROT, VariantProperty.Rotation.R270).with(VariantProperty.MODEL, bothLine);
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
                Arrays.stream(childProperties).forEach((e)->{
                    if(e!=null){
                        this.childProperties.add(e);
                        possibleVariantKeys.addAll(e.getVariantKeys());

                    }
                });
                return this;
            }
            public ChildPropertyVariant generate(Function<List<String>, List<Variant>> fromVariantKeyToVariants){
                List<List<String>> childPropertiesVariantKeys=childProperties.stream().map(ChildProperty::getVariantKeys).toList();
                possibleCombination.addAll(CartesianProduct(childPropertiesVariantKeys));
                for(List<String> combination:possibleCombination){
                    if(combination!=null&&!combination.isEmpty())
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
        public ChildVariant add(ChildPropertyVariant ...childPropertyVariant){
            this.childPropertyVariants.addAll(List.of(childPropertyVariant));
            return this;
        }
        public JsonElement get() {
            Stream<Map.Entry<String, List<Variant>>> resStream=Stream.of(Map.entry("",baseVariants));
            for (ChildPropertyVariant propertyVariant : childPropertyVariants) {
                if(propertyVariant.get().isEmpty()) continue;
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
            List<Variant> list3 = new ArrayList<>();
            for (Variant variant1:list1) for (Variant variant2:list2){
                list3.add(Variant.merge(variant1,variant2));
            }
            return list3.stream().toList();
        }
        public static ChildVariant of(Variant ...variant){
            return new ChildVariant().setBaseVariants(variant);
        }
    }
    public static final Utils.Splitter STATE_SPLITTER = Utils.Splitter.on("#");
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
                            for (String con : COMMA_SPLITTER.split(state)) {
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
