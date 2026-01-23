package com.shanebeestudios.skript.plugin.elements.expressions;

import io.github.syst3ms.skriptparser.registration.SkriptRegistration;

public class ExpressionHandler {

    public static void register(SkriptRegistration registration) {
        ExprClassInfoOf.register(registration);
        ExprInventory.register(registration);
        ExprItemStack.register(registration);
        ExprItemType.register(registration);
        ExprName.register(registration);
        ExprUUID.register(registration);
        ExprUUIDRandom.register(registration);
        ExprWorld.register(registration);
        ExprWorldOfEntity.register(registration);
    }

}
