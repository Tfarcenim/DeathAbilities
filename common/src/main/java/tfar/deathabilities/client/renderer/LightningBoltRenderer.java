package tfar.deathabilities.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import tfar.deathabilities.entity.LightningBoltEntity;
import tfar.deathabilities.init.ModItems;

public class LightningBoltRenderer extends EntityRenderer<LightningBoltEntity> {

    private final ItemRenderer itemRenderer;

    public LightningBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(LightningBoltEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.pushPose();
        Direction direction = pEntity.getDirection();
        double d0 = 0.46875D;
        pPoseStack.translate(direction.getStepX() * d0, direction.getStepY() * d0, direction.getStepZ() * d0);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(pEntity.getXRot()));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(pEntity.getYRot()));
        ItemStack itemstack = ModItems.LIGHTNING_BOLT.getDefaultInstance();
        if (!itemstack.isEmpty()) {
            pPoseStack.translate(0.0F, 0.0F, 0.4375F);
            this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pEntity.level(), pEntity.getId());
        }

        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(LightningBoltEntity lightningBoltEntity) {
        return null;
    }
}
