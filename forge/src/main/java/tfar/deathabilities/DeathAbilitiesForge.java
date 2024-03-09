package tfar.deathabilities;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.deathabilities.client.DeathAbilitiesClientForge;
import tfar.deathabilities.data.Datagen;
import tfar.deathabilities.entity.DolphinWithLegsEntity;
import tfar.deathabilities.entity.SandFishEntity;
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
        MinecraftForge.EVENT_BUS.addListener(this::commands);
        if (Services.PLATFORM.isPhysicalClient()) {
            bus.addListener(DeathAbilitiesClientForge::registerRenderers);
        }
        // Use Forge to bootstrap the Common mod.
        DeathAbilities.init();
    }

    private void commands(RegisterCommandsEvent event) {
        DeathAbilitiesCommands.register(event.getDispatcher());
    }
    private void commonSetup(FMLCommonSetupEvent event) {
        PacketHandlerForge.registerMessages();
    }

    private void onDeath(LivingDamageEvent event) {
        DeathAbilities.onDeath(event.getSource(),event.getEntity());
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
    }

}