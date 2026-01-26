package com.github.skriptdev.skript.plugin.elements.events;

import com.github.skriptdev.skript.plugin.HySk;
import com.hypixel.hytale.event.EventRegistration;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.github.skriptdev.skript.api.skript.eventcontext.PlayerEventContext;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import io.github.syst3ms.skriptparser.lang.Expression;
import io.github.syst3ms.skriptparser.lang.Statement;
import io.github.syst3ms.skriptparser.lang.Trigger;
import io.github.syst3ms.skriptparser.lang.TriggerContext;
import io.github.syst3ms.skriptparser.lang.event.SkriptEvent;
import io.github.syst3ms.skriptparser.parsing.ParseContext;
import io.github.syst3ms.skriptparser.registration.SkriptRegistration;
import io.github.syst3ms.skriptparser.registration.context.ContextValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EvtPlayerJoin extends SkriptEvent {

    public static void register(SkriptRegistration registration) {
        registration.newEvent(EvtPlayerJoin.class, "player connect", "player ready", "player quit")
            .name("Player Join/Quit")
            .description("Events triggered when a player joins or quits the server.")
            .since("INSERT VERSION")
            .setHandledContexts(PlayerEventContext.class)
            .register();

        registration.newContextValue(PlayerEventContext.class, Player.class, true, "player", PlayerEventContext::getPlayer)
            .setUsage(ContextValue.Usage.EXPRESSION_OR_ALONE)
            .register();
    }

    private static EventRegistration<Void, PlayerConnectEvent> connectListeners;
    private static EventRegistration<String, PlayerReadyEvent> readyListener;
    private static EventRegistration<Void, PlayerDisconnectEvent> disconnectListener;
    private int pattern;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull ParseContext parseContext) {
        this.pattern = matchedPattern;

        EventRegistry registry = HySk.getInstance().getEventRegistry();
        if (connectListeners == null) {
            connectListeners = registry.register(PlayerConnectEvent.class, playerConnectEvent -> {
                Player player = playerConnectEvent.getHolder().getComponent(Player.getComponentType());
                for (Trigger trigger : this.getTriggers()) {
                    Statement.runAll(trigger, new PlayerEventContext(player, 0));
                }
            });
        }
        if (readyListener == null) {
            readyListener = registry.registerGlobal(PlayerReadyEvent.class, playerReadyEvent -> {
                Player player = playerReadyEvent.getPlayer();
                for (Trigger trigger : this.getTriggers()) {
                    Statement.runAll(trigger, new PlayerEventContext(player, 1));
                }
            });
        }
        if (disconnectListener == null) {
            disconnectListener = registry.register(PlayerDisconnectEvent.class, playerDisconnectEvent -> {
            });
        }
        return true;
    }

    @Override
    public boolean check(@NotNull TriggerContext ctx) {
        if (!(ctx instanceof PlayerEventContext playerEventContext)) return false;
        if (this.pattern != playerEventContext.getPattern()) return false;
        return true;
    }

    public int getPattern() {
        return this.pattern;
    }

    @Override
    public String toString(@Nullable TriggerContext ctx, boolean debug) {
        String t = switch (this.pattern) {
            case 0 -> "connect";
            case 1 -> "ready";
            case 2 -> "quit";
            default -> "unknown";
        };
        return "player " + t;
    }

}
