package net.vvxzv.farmerstfc.common.util;

import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;

public class BlockPropertyUtils {
    public static final Map<String, Direction> DIRECTION_MAP = new HashMap<>();
    public static final Map<String, Boolean> BOOLEAN_MAP = new HashMap<>();
    public static final Map<String, Integer> INTEGER_MAP = new HashMap<>();
    static {
        DIRECTION_MAP.put("north", Direction.NORTH);
        DIRECTION_MAP.put("south", Direction.SOUTH);
        DIRECTION_MAP.put("west", Direction.WEST);
        DIRECTION_MAP.put("east", Direction.EAST);

        BOOLEAN_MAP.put("true", true);
        BOOLEAN_MAP.put("false", false);

        INTEGER_MAP.put("0", 0);
        INTEGER_MAP.put("1", 1);
        INTEGER_MAP.put("2", 2);
        INTEGER_MAP.put("3", 3);
        INTEGER_MAP.put("4", 4);
        INTEGER_MAP.put("5", 5);
        INTEGER_MAP.put("6", 6);
        INTEGER_MAP.put("7", 7);
        INTEGER_MAP.put("8", 8);
        INTEGER_MAP.put("9", 9);
        INTEGER_MAP.put("10", 10);
        INTEGER_MAP.put("11", 11);
        INTEGER_MAP.put("12", 12);
        INTEGER_MAP.put("13", 13);
        INTEGER_MAP.put("14", 14);
        INTEGER_MAP.put("15", 15);
    }
}
