package tfar.deathabilities;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.deathabilities.client.DeathAbilitiesClientForge;
import tfar.deathabilities.data.Datagen;
import tfar.deathabilities.ducks.EnderDragonDuck;
import tfar.deathabilities.ducks.MobEntityDuck;
import tfar.deathabilities.entity.*;
import tfar.deathabilities.init.ModEntityTypes;
import tfar.deathabilities.network.PacketHandlerForge;
import tfar.deathabilities.platform.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod(DeathAbilities.MOD_ID)
public class DeathAbilitiesForge {
    
    public DeathAbilitiesForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::register);
        bus.addListener(this::attributes);
        bus.addListener(Datagen::gather);
        bus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, this::onDeath);
        MinecraftForge.EVENT_BUS.addListener(this::onDamage);
        MinecraftForge.EVENT_BUS.addListener(this::commands);
        MinecraftForge.EVENT_BUS.addListener(this::entityJoinWorld);
        MinecraftForge.EVENT_BUS.addListener(this::changeTarget);
        MinecraftForge.EVENT_BUS.addListener(this::onClone);
        if (Services.PLATFORM.isPhysicalClient()) {
            DeathAbilitiesClientForge.events(bus);
        }
        // Use Forge to bootstrap the Common mod.
        DeathAbilities.init();
    }

    private void onClone(PlayerEvent.Clone event) {
        DeathAbilities.onClone((ServerPlayer) event.getOriginal(),(ServerPlayer) event.getEntity(),event.isWasDeath());
    }

    private void entityJoinWorld(EntityJoinLevelEvent event) {
        if (DeathAbilities.enable_dragon) {
            Entity entity = event.getEntity();
            Level level = event.getLevel();
            if (entity instanceof DragonFireball dragonFireball) {
                Entity owner = dragonFireball.getOwner();
                if (owner instanceof EnderDragon enderDragon) {
                    if (!level.isClientSide) {
                        DeathAbility deathAbility = EnderDragonDuck.of(enderDragon).getPhase();
                        switch (deathAbility) {
                            case fire -> {
                                entity.level().addFreshEntity(new FireDragonFireballEntity(entity.level(),enderDragon,
                                        dragonFireball.xPower,dragonFireball.yPower,dragonFireball.zPower,2));
                            }
                            case water -> {
                                entity.level().addFreshEntity(new WaterDragonFireballEntity(entity.level(),enderDragon,
                                        dragonFireball.xPower,dragonFireball.yPower,dragonFireball.zPower,2));
                            }
                            case lightning -> {
                                entity.level().addFreshEntity(new LightningDragonFireballEntity(entity.level(),enderDragon,
                                        dragonFireball.xPower,dragonFireball.yPower,dragonFireball.zPower,2));
                            }
                            case earth -> {
                                entity.level().addFreshEntity(new EarthDragonFireballEntity(entity.level(),enderDragon,
                                        dragonFireball.xPower,dragonFireball.yPower,dragonFireball.zPower,4));
                            }
                        }
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

    private void commands(RegisterCommandsEvent event) {
        DeathAbilitiesCommands.register(event.getDispatcher());
    }
    private void commonSetup(FMLCommonSetupEvent event) {
        PacketHandlerForge.registerMessages();
    }

    private void onDamage(LivingDamageEvent event) {
        DeathAbilities.onDamage(event.getSource(),event.getEntity(),event.getAmount());
    }

    private void changeTarget(LivingChangeTargetEvent event) {
        LivingEntity originalTarget = event.getOriginalTarget();
        if (event.getTargetType() == LivingChangeTargetEvent.LivingTargetType.MOB_TARGET) {
            LivingEntity attacker = event.getEntity();
            if (attacker instanceof Mob mob) {
                MobEntityDuck mobEntityDuck = MobEntityDuck.of(mob);
                if (mobEntityDuck.targetHunters() && originalTarget.getUUID().equals(HunterData.runner)) {
                    mob.setTarget(null);
                }
            }
        }
    }

    private void onDeath(LivingDeathEvent event) {
        if(DeathAbilities.onDeath(event.getSource(),event.getEntity())) {
            event.setCanceled(true);
        }
    }

    public static Map<Registry<?>, List<Pair<ResourceLocation, Supplier<?>>>> registerLater = new HashMap<>();
    private void register(RegisterEvent e) {
        for (Map.Entry<Registry<?>,List<Pair<ResourceLocation, Supplier<?>>>> entry : registerLater.entrySet()) {
            Registry<?> registry = entry.getKey();
            List<Pair<ResourceLocation, Supplier<?>>> toRegister = entry.getValue();
            for (Pair<ResourceLocation,Supplier<?>> pair : toRegister) {
                e.register((ResourceKey<? extends Registry<Object>>)registry.key(),pair.getLeft(),(Supplier<Object>)pair.getValue());
            }
        }
    }

    private void attributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.DOLPHIN_WITH_LEGS, DolphinWithLegsEntity.createAttributes().build());
        event.put(ModEntityTypes.SANDFISH, SandFishEntity.createAttributes().build());
        event.put(ModEntityTypes.LIGHTNING_VEX, LightningVexEntity.createAttributes().build());
    }

}