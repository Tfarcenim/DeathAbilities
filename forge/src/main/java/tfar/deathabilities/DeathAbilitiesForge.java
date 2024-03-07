package tfar.deathabilities;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.deathabilities.client.DeathAbilitiesClient;
import tfar.deathabilities.client.DeathAbilitiesClientForge;
import tfar.deathabilities.entity.DolphinWithLegsEntity;
import tfar.deathabilities.init.ModEntityTypes;
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
        if (Services.PLATFORM.isPhysicalClient()) {
            bus.addListener(DeathAbilitiesClientForge::registerRenderers);
        }
        // Use Forge to bootstrap the Common mod.
        DeathAbilities.init();
        
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
    }

}