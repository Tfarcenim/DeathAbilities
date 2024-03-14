package tfar.deathabilities.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import tfar.deathabilities.entity.LightningVexEntity;

public class LightningVexModel extends HierarchicalModel<LightningVexEntity> implements ArmedModel {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart head;

    public LightningVexModel(ModelPart pRoot) {
        super(RenderType::entityTranslucent);
        this.root = pRoot.getChild("root");
        this.body = this.root.getChild("body");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightWing = this.body.getChild("right_wing");
        this.leftWing = this.body.getChild("left_wing");
        this.head = this.root.getChild("head");
    }

    /**
     * Sets this entity's model rotation angles
     */
    @Override
    public void setupAnim(LightningVexEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
        float f = Mth.cos(pAgeInTicks * 5.5F * ((float) Math.PI / 180F)) * 0.1F;
        this.rightArm.zRot = ((float) Math.PI / 5F) + f;
        this.leftArm.zRot = -(((float) Math.PI / 5F) + f);
        if (pEntity.isCharging()) {
            this.body.xRot = 0.0F;
            this.setArmsCharging(pEntity.getMainHandItem(), pEntity.getOffhandItem(), f);
        } else {
            this.body.xRot = 0.15707964F;
        }

        this.leftWing.yRot = 1.0995574F + Mth.cos(pAgeInTicks * 45.836624F * ((float) Math.PI / 180F)) * ((float) Math.PI / 180F) * 16.2F;
        this.rightWing.yRot = -this.leftWing.yRot;
        this.leftWing.xRot = 0.47123888F;
        this.leftWing.zRot = -0.47123888F;
        this.rightWing.xRot = 0.47123888F;
        this.rightWing.zRot = 0.47123888F;
    }

    private void setArmsCharging(ItemStack pRightHandItem, ItemStack pLeftHandItem, float p_265125_) {
        if (pRightHandItem.isEmpty() && pLeftHandItem.isEmpty()) {
            this.rightArm.xRot = -1.2217305F;
            this.rightArm.yRot = 0.2617994F;
            this.rightArm.zRot = -0.47123888F - p_265125_;
            this.leftArm.xRot = -1.2217305F;
            this.leftArm.yRot = -0.2617994F;
            this.leftArm.zRot = 0.47123888F + p_265125_;
        } else {
            if (!pRightHandItem.isEmpty()) {
                this.rightArm.xRot = 3.6651914F;
                this.rightArm.yRot = 0.2617994F;
                this.rightArm.zRot = -0.47123888F - p_265125_;
            }

            if (!pLeftHandItem.isEmpty()) {
                this.leftArm.xRot = 3.6651914F;
                this.leftArm.yRot = -0.2617994F;
                this.leftArm.zRot = 0.47123888F + p_265125_;
            }

        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void translateToHand(HumanoidArm pSide, PoseStack pPoseStack) {
        boolean flag = pSide == HumanoidArm.RIGHT;
        ModelPart modelpart = flag ? this.rightArm : this.leftArm;
        this.root.translateAndRotate(pPoseStack);
        this.body.translateAndRotate(pPoseStack);
        modelpart.translateAndRotate(pPoseStack);
        pPoseStack.scale(0.55F, 0.55F, 0.55F);
        this.offsetStackPosition(pPoseStack, flag);
    }

    private void offsetStackPosition(PoseStack pPoseStack, boolean p_263414_) {
        if (p_263414_) {
            pPoseStack.translate(0.046875D, -0.15625D, 0.078125D);
        } else {
            pPoseStack.translate(-0.046875D, -0.15625D, 0.078125D);
        }

    }
}