package com.shanebeestudios.skript.api.skript;

import com.shanebeestudios.skript.api.utils.Utils;
import com.shanebeestudios.skript.plugin.HySk;
import io.github.syst3ms.skriptparser.log.LogEntry;
import io.github.syst3ms.skriptparser.parsing.ScriptLoader;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class ScriptsLoader {

    private static int LOADED_SCRIPT_COUNT = 0;

    public static void loadScripts(Path directory, boolean reload) {
        ScriptLoader.getTriggerMap().clear();
        LOADED_SCRIPT_COUNT = 0;
        Utils.log((reload ? "Reloading" : "Loading") + " scripts...");
        long start = System.currentTimeMillis();

        File directoryFile = directory.toFile();
        if (!directoryFile.isDirectory()) {
            if (!directoryFile.mkdirs()) {
                Utils.error("Failed to create scripts directory!");
            }
        }

        loadScriptsInDirectory(directoryFile);

        long end = System.currentTimeMillis() - start;
        Utils.log((reload ? "Reloaded" : "Loaded") + " %s scripts in %sms", LOADED_SCRIPT_COUNT, end);

        // Call load event and start periodical events
        HySk.getInstance().getSkript().getListenerHandler().finishedLoading();
    }

    public static void loadScriptsInDirectory(File directory) {
        if (directory == null) return;

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                loadScriptsInDirectory(file);
            } else {
                Utils.log("Loading script " + file.getName() + "...");
                List<LogEntry> logEntries = ScriptLoader.loadScript(file.toPath(), false);
                LOADED_SCRIPT_COUNT++;
                for (LogEntry logEntry : logEntries) {
                    Utils.log(logEntry.getMessage());
                }
            }
        }
    }

}
