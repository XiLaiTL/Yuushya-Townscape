package com.yuushya.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class YuushyaModelUtils {
    //from ModelBakery
    public static final Splitter COMMA_SPLITTER = Splitter.on(',');
    public static final Splitter EQUAL_SPLITTER = Splitter.on('=').limit(2);

    public static List<BlockState> getBlockStateFromVariantString(Block block,String variantKey){
        return block.getStateDefinition().getPossibleStates().stream().filter(predicate(block,variantKey)).collect(Collectors.toList());
    }

    public static boolean isBlockStateInVariantString(BlockState blockState,String variantKey){
        return predicate(blockState.getBlock(),variantKey).test(blockState);
    }

    public static final Map<String,Predicate<BlockState>> PREDICATE_MAP = new HashMap<>();
    public static Predicate<BlockState> predicate(Block block,String variantKey){
        String s = Registry.BLOCK.getKey(block)+"#"+variantKey;
        return PREDICATE_MAP.computeIfAbsent(s,(s1)->predicateBuilder(block.getStateDefinition(),variantKey));
    }
    public static Predicate<BlockState> predicateBuilder(StateDefinition<Block, BlockState> stateDefinition, String string) {
        if(string.equals("empty")){
            return blockState -> (blockState != null && blockState.is(stateDefinition.getOwner())) ;
        }
        HashMap<Property<?>, Object> map = Maps.newHashMap();
        for (String s : COMMA_SPLITTER.split(string)) {
            Iterator<String> iterator = EQUAL_SPLITTER.split(s).iterator();
            if (!iterator.hasNext()) continue;
            String key = iterator.next();
            Property<?> property = stateDefinition.getProperty(key);
            if (property != null && iterator.hasNext()) {
                String value = iterator.next();
                Object comparable = getValueHelper(property, value);
                if (comparable == null) {
                    throw new RuntimeException("Unknown value: '" + value + "' for blockstate property: '" + key + "' " + property.getPossibleValues());
                }
                map.put(property, comparable);
                continue;
            }
            if (key.isEmpty()) continue;
            throw new RuntimeException("Unknown blockstate property: '" + key + "'");
        }
        Block block = stateDefinition.getOwner();
        return blockState -> {
            if (blockState != null && blockState.is(block)) {
                for (Map.Entry<Property<?>,Object> entry : map.entrySet()) {
                    if (Objects.equals(blockState.getValue((Property<?>)entry.getKey()), entry.getValue())) continue;
                    return false;
                }
                return true;
            }
            return false;
        };
    }

    //获得方块状态具体值
    @Nullable
    private static <T extends Comparable<T>> T getValueHelper(Property<T> property, String name) {
        return (T)property.getValue(name).orElse(null);
    }
}
