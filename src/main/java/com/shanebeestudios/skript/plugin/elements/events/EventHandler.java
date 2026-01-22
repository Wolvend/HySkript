package com.shanebeestudios.skript.plugin.elements.events;

import io.github.syst3ms.skriptparser.registration.SkriptRegistration;

public class EventHandler {

    public static void register(SkriptRegistration registration) {
        EvtLoad.register(registration);
        EvtPlayerJoin.register(registration);
    }

}
