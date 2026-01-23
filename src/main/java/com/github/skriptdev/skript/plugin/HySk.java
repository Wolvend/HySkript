package com.github.skriptdev.skript.plugin;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.github.skriptdev.skript.plugin.command.SkriptCommand;
import org.jetbrains.annotations.NotNull;

public class HySk extends JavaPlugin {

    private static HySk INSTANCE;
    private Skript skript;

    public HySk(@NotNull JavaPluginInit init) {
        super(init);
        INSTANCE = this;
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void start() {
        this.skript = new Skript(this);
        new SkriptCommand(getCommandRegistry());
    }

    public Skript getSkript() {
        return this.skript;
    }

    public static HySk getInstance() {
        return INSTANCE;
    }

}
