package tfar.deathabilities.client;

import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tfar.deathabilities.client.renderer.LightningBoltRenderer;
import tfar.deathabilities.client.renderer.LightningVexRenderer;
import tfar.deathabilities.client.renderer.SandfishRenderer;
import tfar.deathabilities.ducks.PlayerDuck;
import tfar.deathabilities.entity.DolphinWithLegsEntityGeo;
import tfar.deathabilities.init.ModEntityTypes;

public class DeathAbilitiesClientForge {

    public static void events(IEventBus bus) {
        bus.addListener(DeathAbilitiesClientForge::registerRenderers);
        bus.addListener(DeathAbilitiesClientForge::keybinds);

        MinecraftForge.EVENT_BUS.addListener(DeathAbilitiesClientForge::clientTick);
        MinecraftForge.EVENT_BUS.addListener(DeathAbilitiesClientForge::playerRender);
    }

    private static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            DeathAbilitiesClient.keyPressed();
        }
    }

    private static void playerRender(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        PlayerDuck playerDuck = PlayerDuck.of(player);

        if (playerDuck.isFireMist()) {
            event.setCanceled(true);
            for (int i = 0; i < 4;i++) {
                RandomSource randomSource = player.getRandom();
                AABB aabb = player.getBoundingBox();
                double offsetX = aabb.getXsize() * (randomSource.nextDouble() - .5);
                double offsetY = aabb.getYsize() * randomSource.nextDouble();
                double offsetZ = aabb.getZsize() * (randomSource.nextDouble() - .5);
                Vec3 pos = player.position();
                player.level().addParticle(ParticleTypes.FLAME,pos.x + offsetX,pos.y+offsetY,pos.z+offsetZ,0,0,0);
            }
        }
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        EntityRenderers.register((EntityType<DolphinWithLegsEntityGeo>) ModEntityTypes.DOLPHIN_WITH_LEGS, DolphinWithLegsRenderer::new);
        EntityRenderers.register(ModEntityTypes.QUICKSAND_BOMB, ThrownItemRenderer::new);
        EntityRenderers.register(ModEntityTypes.SANDFISH, SandfishRenderer::new);
        EntityRenderers.register(ModEntityTypes.FIRE_DRAGON_FIREBALL, (ctx) -> new ThrownItemRenderer<>(ctx, 3.0F, true));
        EntityRenderers.register(ModEntityTypes.WATER_DRAGON_FIREBALL, (ctx) -> new ThrownItemRenderer<>(ctx, 3.0F, true));
        EntityRenderers.register(ModEntityTypes.LIGHTNING_DRAGON_FIREBALL, (ctx) -> new ThrownItemRenderer<>(ctx, 3.0F, true));
        EntityRenderers.register(ModEntityTypes.EARTH_DRAGON_FIREBALL, (ctx) -> new ThrownItemRenderer<>(ctx, 3.0F, true));


        EntityRenderers.register(ModEntityTypes.LIGHTNING_VEX, LightningVexRenderer::new);
        EntityRenderers.register(ModEntityTypes.LIGHTNING_BOLT, LightningBoltRenderer::new);

        EntityRenderers.register(ModEntityTypes.ATTACK_SQUID, (context) -> new SquidRenderer<>(context, new SquidModel<>(context.bakeLayer(ModelLayers.SQUID))));
    }

    private static void keybinds(RegisterKeyMappingsEvent event) {
        DeathAbilitiesClient.registerKeybinds();
    }
}
