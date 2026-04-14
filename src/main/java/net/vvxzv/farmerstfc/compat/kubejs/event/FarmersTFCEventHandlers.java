package net.vvxzv.farmerstfc.compat.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class FarmersTFCEventHandlers {
    public static final EventGroup eventHandler = EventGroup.of("FarmersTFCEvent");
    public static final EventHandler data = eventHandler.server("data", () -> FarmersTFCDataEvent.class);
}
