package com.github.skriptdev.skript.plugin.elements.events.player;

import com.github.skriptdev.skript.api.skript.event.PlayerContext;
import com.github.skriptdev.skript.api.skript.event.WorldContext;
import com.github.skriptdev.skript.api.skript.registration.SkriptRegistration;
import com.github.skriptdev.skript.plugin.HySk;
import com.hypixel.hytale.event.EventRegistration;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.DrainPlayerFromWorldEvent;
import com.hypixel.hytale.server.core.universe.world.World;
import io.github.syst3ms.skriptparser.lang.Expression;
import io.github.syst3ms.skriptparser.lang.TriggerContext;
import io.github.syst3ms.skriptparser.lang.TriggerMap;
import io.github.syst3ms.skriptparser.lang.event.SkriptEvent;
import io.github.syst3ms.skriptparser.parsing.ParseContext;
import org.jetbrains.annotations.NotNull;

public class EvtPlayerDrainFromWorld extends SkriptEvent {

    public static void register(SkriptRegistration reg) {
        reg.newEvent(EvtPlayerDrainFromWorld.class,
                "drain player from world", "player drained from world", "player drain from world")
            .name("Player Drain From World")
            .description("Called when a world is unloaded and players are  moved out of it.")
            .since("1.0.0")
            .register();
    }

    private static EventRegistration<String, DrainPlayerFromWorldEvent> LISTENER;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, ParseContext parseContext) {
        if (LISTENER == null) {
            LISTENER = HySk.getInstance().getEventRegistry().registerGlobal(DrainPlayerFromWorldEvent.class, event -> {
                DrainContext context = new DrainContext(event);
                TriggerMap.callTriggersByContext(context);
            });
        }
        return true;
    }

    @Override
    public boolean check(TriggerContext triggerContext) {
        return triggerContext instanceof DrainContext;
    }

    @Override
    public String toString(@NotNull TriggerContext ctx, boolean debug) {
        return "drain player from world";
    }

    private record DrainContext(DrainPlayerFromWorldEvent event) implements PlayerContext, WorldContext {

        public World getWorld() {
            return this.event.getWorld();
        }

        @SuppressWarnings("deprecation")
        @Override
        public Player getPlayer() {
            Entity entity = EntityUtils.getEntity(this.event.getHolder());
            return entity instanceof Player p ? p : null;
        }

        @Override
        public String getName() {
            return "drain context";
        }
    }

}
