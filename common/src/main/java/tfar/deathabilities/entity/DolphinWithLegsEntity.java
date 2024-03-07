package tfar.deathabilities.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DolphinWithLegsEntity extends Mob implements OwnableEntity, PlayerRideableJumping {

    @Nullable
    private UUID owner;

    public DolphinWithLegsEntity(EntityType<? extends Mob> $$0, Level $$1) {
        super($$0, $$1);
        this.setMaxUpStep(1.0F);
        setInvulnerable(true);
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30).add(Attributes.MOVEMENT_SPEED, 1.35 * 0.25).add(Attributes.ATTACK_DAMAGE, 3);
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return owner;
    }

    @Override
    public void onPlayerJump(int var1) {

    }

    @Override
    public InteractionResult mobInteract(Player $$0, InteractionHand $$1) {
        if (this.isVehicle() || this.isBaby()) {
            return super.mobInteract($$0, $$1);
        } else {
            ItemStack $$2 = $$0.getItemInHand($$1);
            if (!$$2.isEmpty()) {
                InteractionResult $$3 = $$2.interactLivingEntity($$0, this, $$1);
                if ($$3.consumesAction()) {
                    return $$3;
                }
            }

            this.doPlayerRide($$0);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
    }

    protected void doPlayerRide(Player $$0) {
        if (!this.level().isClientSide) {
            $$0.setYRot(this.getYRot());
            $$0.setXRot(this.getXRot());
            $$0.startRiding(this);
        }
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int var1) {

    }

    @Override
    public void handleStopJump() {

    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 vec3) {
        float $$2 = player.xxa * 0.5F;
        float $$3 = player.zza;
        if ($$3 <= 0.0F) {
            $$3 *= 0.25F;
        }
        return new Vec3($$2, 0.0, $$3);
    }

    @Override
    protected float getRiddenSpeed(Player pPlayer) {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        if (this.getOwnerUUID() != null) {
            $$0.putUUID("Owner", this.getOwnerUUID());
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Mob) {
            return (Mob) entity;
        } else {
            if (entity instanceof Player) {
                return (Player) entity;
            }
            return null;
        }
    }

    @Override
    protected void tickRidden(Player pPlayer, Vec3 pTravelVector) {
        super.tickRidden(pPlayer, pTravelVector);
        Vec2 vec2 = this.getRiddenRotation(pPlayer);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if (this.isControlledByLocalInstance()) {


            if (this.onGround()) {
                this.setIsJumping(false);
              //  if (this.playerJumpPendingScale > 0.0F && !this.isJumping()) {
              //      this.executeRidersJump(this.playerJumpPendingScale, pTravelVector);
              //  }

               // this.playerJumpPendingScale = 0.0F;
            }
        }

    }
    protected boolean isJumping;

    public boolean isJumping() {
        return this.isJumping;
    }


    public void setIsJumping(boolean pJumping) {
        this.isJumping = pJumping;
    }

    protected Vec2 getRiddenRotation(LivingEntity pEntity) {
        return new Vec2(pEntity.getXRot() * 0.5F, pEntity.getYRot());
    }

    public void setOwnerUUID(@Nullable UUID $$0) {
        this.owner = $$0;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        UUID $$1;
        if ($$0.hasUUID("Owner")) {
            $$1 = $$0.getUUID("Owner");
        } else {
            String $$2 = $$0.getString("Owner");
            $$1 = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), $$2);
        }

        if ($$1 != null) {
            this.setOwnerUUID($$1);
        }
    }
}
