package com.github.skriptdev.skript.plugin.elements.events;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.github.skriptdev.skript.api.skript.eventcontext.PlayerEventContext;
import io.github.syst3ms.skriptparser.lang.Expression;
import io.github.syst3ms.skriptparser.lang.SkriptEvent;
import io.github.syst3ms.skriptparser.lang.TriggerContext;
import io.github.syst3ms.skriptparser.parsing.ParseContext;
import io.github.syst3ms.skriptparser.registration.SkriptRegistration;
import io.github.syst3ms.skriptparser.registration.context.ContextValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    private int pattern;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull ParseContext parseContext) {
        this.pattern = matchedPattern;
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
