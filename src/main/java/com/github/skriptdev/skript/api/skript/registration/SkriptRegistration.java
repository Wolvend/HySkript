package com.github.skriptdev.skript.api.skript.registration;

import com.github.skriptdev.skript.api.skript.event.IEventContext;
import com.hypixel.hytale.event.IBaseEvent;
import io.github.syst3ms.skriptparser.registration.SkriptAddon;

import java.lang.reflect.Array;
import java.util.function.Function;

public class SkriptRegistration extends io.github.syst3ms.skriptparser.registration.SkriptRegistration {

    public SkriptRegistration(SkriptAddon registerer) {
        super(registerer);
    }

    @SuppressWarnings("rawtypes")
    public class IEventBuilder<E extends IBaseEvent<?>, RETURN> {
        ContextValueRegistrar<IEventContext, RETURN> cv;

        @SuppressWarnings("unchecked")
        public IEventBuilder(Class<E> event, Class<RETURN> returnType,
                             Function<E, RETURN> function, String pattern) {
            this.cv = SkriptRegistration.this.newContextValue(IEventContext.class,
                returnType, true, pattern,
                iEventContext -> {
                    RETURN[] o = (RETURN[]) Array.newInstance(returnType, 1);
                    o[0] = function.apply((E) iEventContext.event());
                    return o;
                });

        }

        public void register() {
            this.cv.register();
        }
    }

    public <E extends IBaseEvent<?>, RETURN> void addIEventContext(Class<E> event, Class<RETURN> returnType, String name, Function<E, RETURN> function) {
        new IEventBuilder<>(event, returnType, function, name).register();
    }

    public <E extends IBaseEvent<?>, RETURN> IEventBuilder<E,RETURN> newIEventContext(Class<E> event, Class<RETURN> returnType, String name, Function<E, RETURN> function) {
        return new IEventBuilder<>(event, returnType, function, name);
    }

}
