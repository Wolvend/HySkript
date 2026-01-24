package com.github.skriptdev.skript.api.skript.eventcontext;

import io.github.syst3ms.skriptparser.lang.TriggerContext;

/**
 * Represents a TriggerContext for an event that can be cancelled.
 */
public interface CancellableContext extends TriggerContext {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

}
