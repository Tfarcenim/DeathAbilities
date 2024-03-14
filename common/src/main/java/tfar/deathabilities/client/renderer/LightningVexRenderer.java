package tfar.deathabilities.client.renderer;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.client.LightningVexModel;
import tfar.deathabilities.entity.LightningVexEntity;

public class LightningVexRenderer extends MobRenderer<LightningVexEntity, LightningVexModel> {
    private static final ResourceLocation VEX_LOCATION = new ResourceLocation(DeathAbilities.MOD_ID,"textures/entity/lightning_vex.png");
    private static final ResourceLocation VEX_CHARGING_LOCATION = new ResourceLocation("textures/entity/illager/vex_charging.png");

    public LightningVexRenderer(EntityRendererProvider.Context context) {
        super(context, new LightningVexModel(context.bakeLayer(ModelLayers.VEX)), 0.3F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    protected int getBlockLightLevel(LightningVexEntity pEntity, BlockPos pPos) {
        return 15;
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(LightningVexEntity pEntity) {
        return pEntity.isCharging() ? VEX_CHARGING_LOCATION : VEX_LOCATION;
    }
}