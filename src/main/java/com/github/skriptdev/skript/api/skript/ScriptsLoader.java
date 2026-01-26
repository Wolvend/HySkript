package com.github.skriptdev.skript.api.skript;

import com.github.skriptdev.skript.api.utils.Utils;
import com.github.skriptdev.skript.plugin.Skript;
import io.github.syst3ms.skriptparser.Parser;
import io.github.syst3ms.skriptparser.log.LogEntry;
import io.github.syst3ms.skriptparser.parsing.ScriptLoader;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ScriptsLoader {

    private final Skript skript;
    private int loadedScriptCount = 0;

    public ScriptsLoader(Skript skript) {
        this.skript = skript;
    }

    public void loadScripts(Path directory, boolean reload) {
        ScriptLoader.getTriggerMap().clear();
        this.loadedScriptCount = 0;
        Utils.log((reload ? "Reloading" : "Loading") + " scripts...");
        long start = System.currentTimeMillis();

        File directoryFile = directory.toFile();
        if (!directoryFile.isDirectory()) {
            if (!directoryFile.mkdirs()) {
                Utils.error("Failed to create scripts directory!");
            }
        }

        List<String> scriptNames = loadScriptsInDirectory(directoryFile);

        long end = System.currentTimeMillis() - start;
        Utils.log((reload ? "Reloaded" : "Loaded") + " %s scripts in %sms", this.loadedScriptCount, end);

        if (reload) {
            // Run load events after reloading scripts
            for (String scriptName : scriptNames) {
                Parser.getMainRegistration().getRegisterer().finishedLoading(scriptName);
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public List<String> loadScriptsInDirectory(File directory) {
        if (directory == null || !directory.isDirectory()) return List.of();
        List<String> loadedScripts = new ArrayList<>();

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                loadedScripts.addAll(loadScriptsInDirectory(file));
            } else {
                if (!file.getName().endsWith(".sk")) continue;
                Utils.log("Loading script '" + file.getName() + "'...");
                List<LogEntry> logEntries = ScriptLoader.loadScript(file.toPath(), false);
                this.loadedScriptCount++;
                for (LogEntry logEntry : logEntries) {
                    Utils.log(logEntry);
                }
                loadedScripts.add(file.getName().substring(0, file.getName().length() - 3));
            }
        }
        return loadedScripts;
    }

    public void reloadScript(String name) {
        long start = System.currentTimeMillis();
        Path path = this.skript.getScriptsPath().resolve(name);
        if (path.toFile().isDirectory()) {
            this.loadedScriptCount = 0;
            Utils.log("Reloading scripts in path '%s'...", name);
            List<String> scriptNames = loadScriptsInDirectory(path.toFile());
            long fin = System.currentTimeMillis() - start;
            Utils.log("Reloaded %s scripts in %sm.", this.loadedScriptCount, fin);

            // Run load events after reloading scripts
            for (String scriptName : scriptNames) {
                Parser.getMainRegistration().getRegisterer().finishedLoading(scriptName);
            }

        } else {
            path = this.skript.getScriptsPath().resolve(name + (name.endsWith(".sk") ? "" : ".sk"));
            if (!path.toFile().exists()) {
                Utils.error("Script '%s' does not exist!", name);
                return;
            }

            Utils.log("Reloading script '%s'...", name);
            List<LogEntry> logEntries = ScriptLoader.loadScript(path, false);
            for (LogEntry logEntry : logEntries) {
                Utils.log(logEntry);
            }
            long fin = System.currentTimeMillis() - start;
            Utils.log("Reloaded script '%s' in %sms.", name, fin);

            String scriptFileName = path.toFile().getName();
            String scriptName = scriptFileName.substring(0, scriptFileName.length() - 3);

            // Run load events after reloading a script
            Parser.getMainRegistration().getRegisterer().finishedLoading(scriptName);
        }
    }

}
