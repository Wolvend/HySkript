package com.shanebeestudios.skript.plugin;

import com.shanebeestudios.skript.api.skript.ScriptsLoader;
import com.shanebeestudios.skript.api.utils.Utils;
import com.shanebeestudios.skript.plugin.elements.effects.EffectHandler;
import com.shanebeestudios.skript.plugin.elements.events.EventHandler;
import com.shanebeestudios.skript.plugin.elements.listeners.ListenerHandler;
import com.shanebeestudios.skript.plugin.elements.types.Types;
import io.github.syst3ms.skriptparser.Parser;
import io.github.syst3ms.skriptparser.lang.Trigger;
import io.github.syst3ms.skriptparser.log.SkriptLogger;
import io.github.syst3ms.skriptparser.registration.SkriptAddon;
import io.github.syst3ms.skriptparser.registration.SkriptRegistration;
import io.github.syst3ms.skriptparser.types.TypeManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class Skript extends SkriptAddon {

    private final HySk hySk;
    private final Path scriptsPath;
    private final SkriptLogger logger;
    private SkriptRegistration registration;
    private ListenerHandler listenerHandler;

    public Skript(HySk hySk) {
        this.hySk = hySk;
        this.scriptsPath = hySk.getDataDirectory().resolve("scripts");
        this.logger = new SkriptLogger();

        Utils.log("Setting up HySk!");
        setup();
    }

    private void setup() {
        this.registration = new SkriptRegistration(this);
        Parser.init(new String[0], new String[0], new String[0], true);

        // TYPES
        Types.register(this.registration);
        TypeManager.register(this.registration);

        // EFFECTS
        EffectHandler.register(this.registration);

        // EVENTS
        this.listenerHandler = new ListenerHandler(this, this.hySk.getEventRegistry());
        EventHandler.register(this.registration);

        // FINALIZE SETUP
        this.registration.register();

        Utils.log("HySk setup complete!");

        // LOAD SCRIPTS
        ScriptsLoader.loadScripts(this.scriptsPath, false);

        // FINALIZE LOADING
        finishedLoading();
    }

    public HySk getPlugin() {
        return this.hySk;
    }

    public Path getScriptsPath() {
        return this.scriptsPath;
    }

    public SkriptLogger getLogger() {
        return this.logger;
    }

    public SkriptRegistration getRegistration() {
        return this.registration;
    }

    public ListenerHandler getListenerHandler() {
        return this.listenerHandler;
    }

    @Override
    public void handleTrigger(@NotNull Trigger trigger) {
        this.listenerHandler.handleTrigger(trigger);
    }

}
