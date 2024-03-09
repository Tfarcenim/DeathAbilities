package tfar.deathabilities.client;

import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.entity.SandFishEntity;

public class SandfishRenderer extends MobRenderer<SandFishEntity, SilverfishModel<SandFishEntity>> {

    private static final ResourceLocation SILVERFISH_LOCATION = new ResourceLocation(DeathAbilities.MOD_ID,"textures/entity/sandfish.png");


    public SandfishRenderer(EntityRendererProvider.Context p174378) {
        super(p174378, new SilverfishModel<>(p174378.bakeLayer(ModelLayers.SILVERFISH)), 0.3F);
    }

    @Override
    protected float getFlipDegrees(SandFishEntity pLivingEntity) {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(SandFishEntity pEntity) {
        return SILVERFISH_LOCATION;
    }
}
