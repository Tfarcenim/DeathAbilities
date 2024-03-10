package tfar.deathabilities.client;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tfar.deathabilities.entity.DolphinWithLegsEntityGeo;
import tfar.deathabilities.init.ModEntityTypes;

public class DeathAbilitiesClientForge {

    public static void events(IEventBus bus) {
        bus.addListener(DeathAbilitiesClientForge::registerRenderers);
        bus.addListener(DeathAbilitiesClientForge::keybinds);

        MinecraftForge.EVENT_BUS.addListener(DeathAbilitiesClientForge::clientTick);
    }

    private static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            DeathAbilitiesClient.keyPressed();
        }
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        EntityRenderers.register((EntityType<DolphinWithLegsEntityGeo>) ModEntityTypes.DOLPHIN_WITH_LEGS, DolphinWithLegsRenderer::new);
        EntityRenderers.register(ModEntityTypes.QUICKSAND_BOMB, ThrownItemRenderer::new);
        EntityRenderers.register(ModEntityTypes.SANDFISH, SandfishRenderer::new);
    }

    private static void keybinds(RegisterKeyMappingsEvent event) {
        DeathAbilitiesClient.registerKeybinds();
    }
}
