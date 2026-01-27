package com.github.skriptdev.skript.plugin.elements.events;

import com.github.skriptdev.skript.api.skript.event.SimpleEvent;
import com.github.skriptdev.skript.api.skript.registration.SkriptRegistration;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.MouseButtonEvent;
import com.hypixel.hytale.protocol.MouseMotionEvent;
import com.hypixel.hytale.protocol.Vector2f;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.BootEvent;
import com.hypixel.hytale.server.core.event.events.ShutdownEvent;
import com.hypixel.hytale.server.core.event.events.entity.EntityRemoveEvent;
import com.hypixel.hytale.server.core.event.events.entity.LivingEntityInventoryChangeEvent;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.DrainPlayerFromWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseMotionEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupDisconnectEvent;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.UUID;

public class SimpleEvents {

    public static void register(SkriptRegistration reg) {
        // ENTITY
        SimpleEvent.register(reg, "Entity Remove", EntityRemoveEvent.class,
                "entity remove", "entity removed", "entity removal")
            .description("Called when an entity is removed from the world.")
            .since("INSERT VERSION")
            .register();
        reg.addIEventContext(EntityRemoveEvent.class, Entity.class, "entity", EntityRemoveEvent::getEntity);

        SimpleEvent.register(reg, "Living Entity Inventory Change", LivingEntityInventoryChangeEvent.class,
                "living entity inventory change", "living entity inventory changed", "living entity inventory changes")
            .description("Called when an living entity's inventory changes.")
            .since("INSERT VERSION")
            .register();

        // PLAYER
        SimpleEvent.register(reg, "Add Player To World", AddPlayerToWorldEvent.class,
                "add player to world", "player added to world")
            .description("Called when a player joins a world.")
            .since("INSERT VERSION")
            .register();
        reg.addIEventContext(AddPlayerToWorldEvent.class, World.class, "world", AddPlayerToWorldEvent::getWorld);

        SimpleEvent.register(reg, "Player Mouse Button", PlayerMouseButtonEvent.class,
                "player mouse button")
            .description("Called when a player clicks on the mouse.")
            .since("INSERT VERSION")
            .register();
        reg.addIEventContext(PlayerMouseButtonEvent.class, Player.class, "player", PlayerMouseButtonEvent::getPlayer);
        reg.addIEventContext(PlayerMouseButtonEvent.class, Item.class, "item", PlayerMouseButtonEvent::getItemInHand);
        reg.addIEventContext(PlayerMouseButtonEvent.class, Entity.class, "target-entity", PlayerMouseButtonEvent::getTargetEntity);
        reg.addIEventContext(PlayerMouseButtonEvent.class, Vector3i.class, "target-block", PlayerMouseButtonEvent::getTargetBlock);
        reg.addIEventContext(PlayerMouseButtonEvent.class, Vector2f.class, "screen-point", PlayerMouseButtonEvent::getScreenPoint);
        reg.addIEventContext(PlayerMouseButtonEvent.class, MouseButtonEvent.class, "mouse-button", PlayerMouseButtonEvent::getMouseButton);

        SimpleEvent.register(reg, "Player Mouse Motion", PlayerMouseMotionEvent.class,
                "player mouse motion")
            .description("Called when a player moves the mouse.")
            .since("INSERT VERSION")
            .register();
        reg.addIEventContext(PlayerMouseMotionEvent.class, Player.class, "player", PlayerMouseMotionEvent::getPlayer);
        reg.addIEventContext(PlayerMouseMotionEvent.class, Item.class, "item", PlayerMouseMotionEvent::getItemInHand);
        reg.addIEventContext(PlayerMouseMotionEvent.class, Entity.class, "target-entity", PlayerMouseMotionEvent::getTargetEntity);
        reg.addIEventContext(PlayerMouseMotionEvent.class, Vector3i.class, "target-block", PlayerMouseMotionEvent::getTargetBlock);
        reg.addIEventContext(PlayerMouseMotionEvent.class, Vector2f.class, "screen-point", PlayerMouseMotionEvent::getScreenPoint);
        reg.addIEventContext(PlayerMouseMotionEvent.class, MouseMotionEvent.class, "mouse-motion", PlayerMouseMotionEvent::getMouseMotion);


        SimpleEvent.register(reg, "Player Setup Connect", PlayerSetupConnectEvent.class,
                "player setup connect")
            .description("Called when a player is connecting to the server.")
            .since("INSERT VERSION")
            .register();
        reg.addIEventContext(PlayerSetupConnectEvent.class, String.class, "name", PlayerSetupConnectEvent::getUsername);
        reg.addIEventContext(PlayerSetupConnectEvent.class, UUID.class, "uuid", PlayerSetupConnectEvent::getUuid);
        reg.addIEventContext(PlayerSetupConnectEvent.class, String.class, "reason", PlayerSetupConnectEvent::getReason);

        SimpleEvent.register(reg, "Player Setup Disconnect", PlayerSetupDisconnectEvent.class,
                "player setup disconnect")
            .description("Called when a player is disconnecting to the server.")
            .since("INSERT VERSION")
            .register();
        reg.addIEventContext(PlayerSetupDisconnectEvent.class, String.class, "name", PlayerSetupDisconnectEvent::getUsername);
        reg.addIEventContext(PlayerSetupDisconnectEvent.class, UUID.class, "uuid", PlayerSetupDisconnectEvent::getUuid);
        reg.addIEventContext(PlayerSetupDisconnectEvent.class, String.class, "reason", event -> event.getDisconnectReason().getServerDisconnectReason());

        SimpleEvent.register(reg, "Drain Player From World", DrainPlayerFromWorldEvent.class,
                "drain player from world", "player drained from world")
            .description("Really not sure...") // TODO put real docs
            .since("INSERT VERSION")
            .register();
        reg.addIEventContext(DrainPlayerFromWorldEvent.class, World.class, "world", DrainPlayerFromWorldEvent::getWorld);

        // SERVER
        SimpleEvent.register(reg, "Boot", BootEvent.class, "boot")
            .description("Called when the server is starting up.")
            .since("INSERT VERSION")
            .register();
        SimpleEvent.register(reg, "Shutdown", ShutdownEvent.class, "shutdown")
            .description("Called when the server is shutting down.")
            .since("INSERT VERSION")
            .register();
    }

}
