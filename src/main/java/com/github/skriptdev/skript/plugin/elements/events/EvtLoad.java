package com.github.skriptdev.skript.plugin.elements.events;

import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.github.skriptdev.skript.api.skript.event.ScriptLoadContext;
import io.github.syst3ms.skriptparser.lang.Expression;
import io.github.syst3ms.skriptparser.lang.Statement;
import io.github.syst3ms.skriptparser.lang.Trigger;
import io.github.syst3ms.skriptparser.lang.event.SkriptEvent;
import io.github.syst3ms.skriptparser.lang.TriggerContext;
import io.github.syst3ms.skriptparser.lang.event.StartOnLoadEvent;
import io.github.syst3ms.skriptparser.parsing.ParseContext;
import io.github.syst3ms.skriptparser.registration.SkriptRegistration;
import org.jetbrains.annotations.Nullable;

public class EvtLoad extends SkriptEvent implements StartOnLoadEvent {

    public static void register(SkriptRegistration registration) {
        registration.newEvent(EvtLoad.class, "[script] load[ing]")
            .name("Script Loading")
            .description("Triggered when a script is loaded.")
            .since("INSERT VERSION")
            .setHandledContexts(ScriptLoadContext.class)
            .register();

        registration.addContextValue(ScriptLoadContext.class, CommandSender.class, true,"sender", ScriptLoadContext::getSender);
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, ParseContext parseContext) {
        return true;
    }

    @Override
    public boolean check(TriggerContext ctx) {
        return ctx instanceof ScriptLoadContext;
    }

    @Override
    public String toString(@Nullable TriggerContext ctx, boolean debug) {
        return "script loading";
    }

    @Override
    public void onInitialLoad(Trigger trigger) {
        Statement.runAll(trigger, new ScriptLoadContext());
    }

}
