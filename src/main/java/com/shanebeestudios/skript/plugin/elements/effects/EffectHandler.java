package com.shanebeestudios.skript.plugin.elements.effects;

import io.github.syst3ms.skriptparser.registration.SkriptRegistration;

public class EffectHandler {

    public static void register(SkriptRegistration registration) {
        EffSendMessage.register(registration);
    }

}
