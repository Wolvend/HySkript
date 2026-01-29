package com.github.skriptdev.skript.api.skript.registration;

import io.github.syst3ms.skriptparser.registration.SkriptAddon;

/**
 * An extension of {@link io.github.syst3ms.skriptparser.registration.SkriptRegistration} with additional features.
 */
public class SkriptRegistration extends io.github.syst3ms.skriptparser.registration.SkriptRegistration {

    public SkriptRegistration(SkriptAddon registerer) {
        super(registerer);
    }

}
