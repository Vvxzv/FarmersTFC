package net.vvxzv.farmerstfc.common.utils;

import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;

public class BlockPropertyUtils {
    public static final Map<String, Direction> DIRECTION_MAP = new HashMap<>();
    static {
        DIRECTION_MAP.put("north", Direction.NORTH);
        DIRECTION_MAP.put("south", Direction.SOUTH);
        DIRECTION_MAP.put("west", Direction.WEST);
        DIRECTION_MAP.put("east", Direction.EAST);
    }
}
