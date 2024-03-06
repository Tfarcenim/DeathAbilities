package tfar.deathabilities.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.entity.DolphinWithLegsEntityGeo;

public class DolphinWithLegsRenderer extends GeoEntityRenderer<DolphinWithLegsEntityGeo> {
    public DolphinWithLegsRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(DeathAbilities.MOD_ID,"dolphin_with_legs"),true));
    }
}
