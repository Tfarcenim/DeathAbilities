package tfar.deathabilities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.client.DeathAbilitiesClient;
import tfar.deathabilities.ducks.EnderDragonDuck;

@Mixin(EnderDragonRenderer.class)
public class EnderDragonRendererMixin {


    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"),cancellable = true)
    private void modifyTexture(EnderDragon enderDragon, CallbackInfoReturnable<ResourceLocation> cir) {
        DeathAbilitiesClient.textureHook(enderDragon, cir);
    }

    @ModifyVariable(method = "render(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    ,at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/entity/EnderDragonRenderer$DragonModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"
    ,ordinal = 2),ordinal = 0)
    private VertexConsumer change(VertexConsumer original, EnderDragon pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (DeathAbilities.enable_dragon) {
            VertexConsumer vertexConsumer = pBuffer.getBuffer(DeathAbilitiesClient.getRenderTypeBody(EnderDragonDuck.of(pEntity).getPhase()));
            return vertexConsumer;
        }
        return original;
    }

    @ModifyVariable(method = "render(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
            ,at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/entity/EnderDragonRenderer$DragonModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"
            ,ordinal = 3),ordinal = 0)
    private VertexConsumer change1(VertexConsumer original, EnderDragon pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (DeathAbilities.enable_dragon) {
            VertexConsumer vertexConsumer = pBuffer.getBuffer(DeathAbilitiesClient.getRenderTypeEyes(EnderDragonDuck.of(pEntity).getPhase()));
            return vertexConsumer;
        }
        return original;
    }

}
