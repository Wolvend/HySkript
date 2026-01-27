package com.github.skriptdev.skript.api.skript.event;

import com.hypixel.hytale.event.IBaseEvent;
import io.github.syst3ms.skriptparser.lang.TriggerContext;

public record IEventContext<E extends IBaseEvent<?>>(E event) implements TriggerContext {

    @Override
    public String getName() {
        return "i-event-context";
    }

}
