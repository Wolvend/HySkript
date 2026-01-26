package com.github.skriptdev.skript.api.skript.command;

import io.github.syst3ms.skriptparser.log.SkriptLogger;

public interface ScriptCommandParent {

    void loadChild(ScriptCommandBuilder parent, SkriptLogger logger);
}
