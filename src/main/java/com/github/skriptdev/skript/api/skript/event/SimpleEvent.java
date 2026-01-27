package com.github.skriptdev.skript.api.skript.event;

import com.github.skriptdev.skript.api.skript.registration.SkriptRegistration;
import com.github.skriptdev.skript.plugin.HySk;
import com.hypixel.hytale.event.EventRegistration;
import com.hypixel.hytale.event.IBaseEvent;
import io.github.syst3ms.skriptparser.lang.Expression;
import io.github.syst3ms.skriptparser.lang.Statement;
import io.github.syst3ms.skriptparser.lang.Trigger;
import io.github.syst3ms.skriptparser.lang.TriggerContext;
import io.github.syst3ms.skriptparser.lang.event.SkriptEvent;
import io.github.syst3ms.skriptparser.parsing.MatchContext;
import io.github.syst3ms.skriptparser.parsing.ParseContext;
import io.github.syst3ms.skriptparser.pattern.PatternElement;
import io.github.syst3ms.skriptparser.registration.SkriptRegistration.EventRegistrar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

// This is a bit janky how I had to make this work
// Will hopefully make it nicer in the future
public class SimpleEvent<K, E extends IBaseEvent<K>> extends SkriptEvent {

    private static final Map<String, Class<? extends IBaseEvent<?>>> EVENT_CLASS_MAP = new HashMap<>();

    private Class<E> eventClass;
    private EventRegistration<K, E> eventListener;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, ParseContext parseContext) {
        PatternElement element = parseContext.getElement();
        MatchContext matchContext = new MatchContext(element, parseContext.getParserState(), parseContext.getLogger());

        AtomicReference<String> pattern = new AtomicReference<>();
        EVENT_CLASS_MAP.forEach((p, clazz) -> {
            int i = element.match(p, 0, matchContext);
            if (i < 0) return;
            pattern.set(p);
        });

        this.eventClass = (Class<E>) EVENT_CLASS_MAP.get(pattern.get());
        if (this.eventClass == null) {
            return false;
        }

        // Register Listener
        this.eventListener = HySk.getInstance().getEventRegistry().registerGlobal(this.eventClass, ibaseEvent -> {
            for (Trigger trigger : SimpleEvent.this.getTriggers()) {
                Statement.runAll(trigger, new IEventContext<>(ibaseEvent));
            }
        });

        return true;
    }

    @Override
    public boolean check(TriggerContext ctx) {
        if (ctx instanceof IEventContext<?> eventContext) {
            return this.eventClass.isAssignableFrom(eventContext.event().getClass());
        }
        return false;
    }

    @Override
    public String toString(@NotNull TriggerContext ctx, boolean debug) {
        if (this.eventClass == null) return "simple event";
        return "simple event: " + this.eventClass.getSimpleName();
    }

    @Override
    public void clearTrigger(String scriptName) {
        // Unregister Listener
        this.eventListener.unregister();
        super.clearTrigger(scriptName);
    }

    @SuppressWarnings("rawtypes")
    public static EventRegistrar<SimpleEvent> register(SkriptRegistration registration,
                                                       String name,
                                                       Class<? extends IBaseEvent<?>> eventClass,
                                                       String... patterns) {
        for (String pattern : patterns) {
            EVENT_CLASS_MAP.put(pattern, eventClass);
        }

        return registration.newEvent(SimpleEvent.class, patterns)
            .name(name)
            .setHandledContexts(IEventContext.class);
    }

}
