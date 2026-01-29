package com.github.skriptdev.skript.api.skript.event;

import com.hypixel.hytale.server.core.entity.entities.Player;
import io.github.syst3ms.skriptparser.lang.TriggerContext;

/**
 * A {@link TriggerContext} for player-related events.
 */
public class PlayerEventContext implements TriggerContext {

    private final Player player;
    private final int pattern;

    public PlayerEventContext(Player player, int pattern) {
        this.player = player;
        this.pattern = pattern;
    }

    public Player[] getPlayer() {
        return new Player[]{player};
    }

    public int getPattern() {
        return pattern;
    }

    @Override
    public String getName() {
        return "player-join";
    }

}
