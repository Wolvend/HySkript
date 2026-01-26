package com.github.skriptdev.skript.plugin.elements.conditions;

import io.github.syst3ms.skriptparser.registration.SkriptRegistration;

public class ConditionHandler {

    public static void register(SkriptRegistration registration) {
        CondHasPermission.register(registration);
    }

}
