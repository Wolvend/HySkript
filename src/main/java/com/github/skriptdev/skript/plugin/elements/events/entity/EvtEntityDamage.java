package com.github.skriptdev.skript.plugin.elements.events.entity;

import com.github.skriptdev.skript.api.skript.event.CancellableContext;
import com.github.skriptdev.skript.api.skript.registration.SkriptRegistration;
import com.github.skriptdev.skript.plugin.HySk;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import io.github.syst3ms.skriptparser.lang.Expression;
import io.github.syst3ms.skriptparser.lang.TriggerContext;
import io.github.syst3ms.skriptparser.lang.TriggerMap;
import io.github.syst3ms.skriptparser.lang.event.SkriptEvent;
import io.github.syst3ms.skriptparser.parsing.ParseContext;
import org.jetbrains.annotations.NotNull;

public class EvtEntityDamage extends SkriptEvent {

    public static void register(SkriptRegistration reg) {
        reg.newEvent(EvtEntityDamage.class, "entity damage", "entity damaged")
            .setHandledContexts(EntityDamageContext.class)
            .name("Entity Damage")
            .description("Called when an entity takes damage.",
                "**NOTE**: This event is cancellable but doesn't appear to work when trying to cancel.")
            .examples("on entity damage:",
                "\tbroadcast \"Poor %context-victim%\" was damaged by %context-damage-amount%")
            .since("1.0.0")
            .register();

        reg.addContextValue(EntityDamageContext.class, Entity.class, true, "victim", EntityDamageContext::getVictim);
        reg.addContextValue(EntityDamageContext.class, Entity.class, true, "attacker", EntityDamageContext::getAttacker);
        reg.addContextValue(EntityDamageContext.class, Float.class, true, "damage-amount", EntityDamageContext::getDamage);
        reg.addContextValue(EntityDamageContext.class, Damage.Source.class, true, "damage-source", EntityDamageContext::getDamageSource);
        reg.addContextValue(EntityDamageContext.class, DamageCause.class, true, "damage-cause", EntityDamageContext::getDamageCause);
    }

    private static EntityDamageSystem SYSTEM;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, ParseContext parseContext) {
        if (SYSTEM == null) {
            SYSTEM = new EntityDamageSystem();
            HySk.getInstance().getEntityStoreRegistry().registerSystem(SYSTEM);
        }
        return true;
    }

    @Override
    public boolean check(TriggerContext ctx) {
        return ctx instanceof EntityDamageContext;
    }

    @Override
    public String toString(@NotNull TriggerContext ctx, boolean debug) {
        return "entity damage event";
    }

    private record EntityDamageContext(Entity entity, Store<EntityStore> store, Damage damage)
        implements TriggerContext, CancellableContext {

        public Entity[] getVictim() {
            return new Entity[]{this.entity};
        }

        @SuppressWarnings("DataFlowIssue")
        public Entity[] getAttacker() {
            Damage.Source source = this.damage.getSource();
            if (source instanceof Damage.EntitySource entitySource) {
                Player player = this.store.getComponent(entitySource.getRef(), Player.getComponentType());
                if (player != null) return new Entity[]{player};
                NPCEntity npc = this.store.getComponent(entitySource.getRef(), NPCEntity.getComponentType());
                if (npc != null) return new Entity[]{npc};
            }
            return null;
        }

        public Float[] getDamage() {
            return new Float[]{this.damage.getAmount()};
        }

        public Damage.Source[] getDamageSource() {
            return new Damage.Source[]{this.damage.getSource()};
        }

        @SuppressWarnings("deprecation")
        public DamageCause[] getDamageCause() {
            return new DamageCause[]{this.damage.getCause()};
        }

        @Override
        public String getName() {
            return "entity damage context";
        }

        @Override
        public boolean isCancelled() {
            return this.damage.isCancelled();
        }

        @Override
        public void setCancelled(boolean cancelled) {
            this.damage.setCancelled(cancelled);
        }
    }

    private static class EntityDamageSystem extends DamageSystems.ApplyDamage {

        @SuppressWarnings("DataFlowIssue")
        @Override
        public void handle(int index, @NotNull ArchetypeChunk<EntityStore> archetypeChunk, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer, @NotNull Damage damage) {
            Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
            NPCEntity npc = store.getComponent(ref, NPCEntity.getComponentType());
            Player player = store.getComponent(ref, Player.getComponentType());

            Entity entity = npc != null ? npc : player;
            EntityDamageContext context = new EntityDamageContext(entity, store, damage);
            TriggerMap.callTriggersByContext(context);
        }

    }

}
