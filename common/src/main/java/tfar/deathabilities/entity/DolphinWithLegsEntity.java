package tfar.deathabilities.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Predicate;

public class DolphinWithLegsEntity extends PathfinderMob implements OwnableEntity, PlayerRideableJumping {

    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(DolphinWithLegsEntity.class, EntityDataSerializers.INT);

    @Nullable
    private UUID owner;
    @Nullable
    private LivingEntity clientSideCachedAttackTarget;
    private int clientSideAttackTime;
    public DolphinWithLegsEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
        this.setMaxUpStep(1.0F);
        setInvulnerable(true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new LaserAttackGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, new DolphinAttackSelector(this)));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30).add(Attributes.MOVEMENT_SPEED, 1.35 * 0.25).add(Attributes.ATTACK_DAMAGE, 3);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_ATTACK_TARGET, 0);
    }

    public int getAttackDuration() {
        return 80;
    }

    void setActiveAttackTarget(int $$0) {
        this.entityData.set(DATA_ID_ATTACK_TARGET, $$0);
    }

    public boolean hasActiveAttackTarget() {
        return this.entityData.get(DATA_ID_ATTACK_TARGET) != 0;
    }

    @javax.annotation.Nullable
    public LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) {
            return null;
        } else if (this.level().isClientSide) {
            if (this.clientSideCachedAttackTarget != null) {
                return this.clientSideCachedAttackTarget;
            } else {
                Entity $$0 = this.level().getEntity(this.entityData.get(DATA_ID_ATTACK_TARGET));
                if ($$0 instanceof LivingEntity) {
                    this.clientSideCachedAttackTarget = (LivingEntity)$$0;
                    return this.clientSideCachedAttackTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }

    public void aiStep() {
        if (this.isAlive()) {
            if (this.level().isClientSide) {
                Vec3 $$1;
                if (!this.isInWater()) {
                    $$1 = this.getDeltaMovement();

                    if (this.isInWater()) {//isMoving
                        $$1 = this.getViewVector(0.0F);

                        for (int $$2 = 0; $$2 < 2; ++$$2) {
                            this.level().addParticle(ParticleTypes.BUBBLE, this.getRandomX(0.5) - $$1.x * 1.5, this.getRandomY() - $$1.y * 1.5, this.getRandomZ(0.5) - $$1.z * 1.5, 0.0, 0.0, 0.0);
                        }
                    }

                    if (this.hasActiveAttackTarget()) {
                        if (this.clientSideAttackTime < this.getAttackDuration()) {
                            ++this.clientSideAttackTime;
                        }

                        LivingEntity activeAttackTarget = this.getActiveAttackTarget();
                        if (activeAttackTarget != null) {
                            this.getLookControl().setLookAt(activeAttackTarget, 90.0F, 90.0F);
                            this.getLookControl().tick();
                            double $$4 = this.getAttackAnimationScale(0.0F);
                            double distX = activeAttackTarget.getX() - this.getX();
                            double distY = activeAttackTarget.getY(0.5) - this.getEyeY();
                            double distZ = activeAttackTarget.getZ() - this.getZ();
                            double dist = Math.sqrt(distX * distX + distY * distY + distZ * distZ);
                            distX /= dist;
                            distY /= dist;
                            distZ /= dist;
                            double $$9 = this.random.nextDouble();

                            while ($$9 < dist) {
                                $$9 += 1.8 - $$4 + this.random.nextDouble() * (1.7 - $$4);
                                this.level().addParticle(ParticleTypes.BUBBLE, this.getX() + distX * $$9, this.getEyeY() + distY * $$9, this.getZ() + distZ * $$9, 0.0, 0.0, 0.0);
                            }
                        }
                    }
                }

                if (this.isInWaterOrBubble()) {
                } else if (this.onGround()) {
                    //this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F, 0.5, (this.random.nextFloat() * 2.0F - 1.0F) * 0.4F));
                    this.setYRot(this.random.nextFloat() * 360.0F);
                    this.setOnGround(false);
                    this.hasImpulse = true;
                }

                if (this.hasActiveAttackTarget()) {
                    this.setYRot(this.yHeadRot);
                }
            }
        }
        super.aiStep();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> $$0) {
        super.onSyncedDataUpdated($$0);
        if (DATA_ID_ATTACK_TARGET.equals($$0)) {
            this.clientSideAttackTime = 0;
            this.clientSideCachedAttackTarget = null;
        }

    }
    public float getAttackAnimationScale(float $$0) {
        return ((float)this.clientSideAttackTime + $$0) / (float)this.getAttackDuration();
    }

    public float getClientSideAttackTime() {
        return (float)this.clientSideAttackTime;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
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
    protected float getWaterSlowDown() {
        return .99f;
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
    public void tick() {
        super.tick();
        updatePose();
    }

    protected static final EntityDimensions SWIMMING_DIMENSIONS = EntityDimensions.fixed(0.75F, 0.75F);

    @Override
    public EntityDimensions getDimensions(Pose $$0) {
        return $$0 == Pose.SWIMMING ? SWIMMING_DIMENSIONS : super.getDimensions($$0);
    }

    @Override
    public double getPassengersRidingOffset() {
        Pose pose = getPose();
        return pose ==  Pose.SWIMMING ?  1.5 * this.getDimensions(pose).height : super.getPassengersRidingOffset();
    }

    protected void updatePose() {
        if (isInWater()) {
            setPose(Pose.SWIMMING);
        } else {
            setPose(Pose.STANDING);
        }
    }

    @Override
    protected void tickRidden(Player pPlayer, Vec3 pTravelVector) {
        super.tickRidden(pPlayer, pTravelVector);

        Vec2 vec2 = this.getRiddenRotation(pPlayer);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();

        if (isInWater()  && pPlayer.isInWater()) {

            double y = pPlayer.getXRot();
            double yVel = -Math.sin(y * Math.PI / 180) / 4;
            setDeltaMovement(getDeltaMovement().x, yVel, getDeltaMovement().z);
        }

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


    static class LaserAttackGoal extends Goal {
        private final DolphinWithLegsEntity dolphinWithLegs;
        private int attackTime;

        public LaserAttackGoal(DolphinWithLegsEntity $$0) {
            this.dolphinWithLegs = $$0;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity $$0 = this.dolphinWithLegs.getTarget();
            return $$0 != null && $$0.isAlive();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && (this.dolphinWithLegs.getTarget() != null && this.dolphinWithLegs.distanceToSqr(this.dolphinWithLegs.getTarget()) > 9.0);
        }

        public void start() {
            this.attackTime = -10;
            this.dolphinWithLegs.getNavigation().stop();
            LivingEntity $$0 = this.dolphinWithLegs.getTarget();
            if ($$0 != null) {
                this.dolphinWithLegs.getLookControl().setLookAt($$0, 90.0F, 90.0F);
            }

            this.dolphinWithLegs.hasImpulse = true;
        }

        public void stop() {
            this.dolphinWithLegs.setActiveAttackTarget(0);
            this.dolphinWithLegs.setTarget(null);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = this.dolphinWithLegs.getTarget();
            if (target != null) {
                this.dolphinWithLegs.getNavigation().stop();
                this.dolphinWithLegs.getLookControl().setLookAt(target, 90.0F, 90.0F);
                if (!this.dolphinWithLegs.hasLineOfSight(target)) {
                    this.dolphinWithLegs.setTarget(null);
                } else {
                    ++this.attackTime;
                    if (this.attackTime == 0) {
                        this.dolphinWithLegs.setActiveAttackTarget(target.getId());
                        if (!this.dolphinWithLegs.isSilent()) {
                            //this.dolphinWithLegs.level().broadcastEntityEvent(this.dolphinWithLegs, (byte)21);
                        }
                    } else if (this.attackTime >= this.dolphinWithLegs.getAttackDuration()) {
                        float $$1 = 1.0F;
                        if (this.dolphinWithLegs.level().getDifficulty() == Difficulty.HARD) {
                            $$1 += 2.0F;
                        }

                        target.hurt(this.dolphinWithLegs.damageSources().indirectMagic(this.dolphinWithLegs, this.dolphinWithLegs), $$1);
                        target.hurt(this.dolphinWithLegs.damageSources().mobAttack(this.dolphinWithLegs), (float)this.dolphinWithLegs.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        this.dolphinWithLegs.setTarget(null);
                    }

                    super.tick();
                }
            }
        }
    }

    private static class DolphinAttackSelector implements Predicate<LivingEntity> {
        private final DolphinWithLegsEntity dolphinWithLegsEntity;

        public DolphinAttackSelector(DolphinWithLegsEntity dolphinWithLegsEntity) {
            this.dolphinWithLegsEntity = dolphinWithLegsEntity;
        }

        public boolean test(@Nullable LivingEntity living) {
            LivingEntity owner = dolphinWithLegsEntity.getOwner();
            return living != null && ((living instanceof Player && living != owner) ||

                    (living instanceof Mob mob && mob.getTarget() == owner))
                    && living.distanceToSqr(this.dolphinWithLegsEntity) > 9.0;
        }
    }

}
