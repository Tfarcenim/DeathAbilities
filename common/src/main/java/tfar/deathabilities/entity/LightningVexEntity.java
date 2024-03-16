package tfar.deathabilities.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class LightningVexEntity extends Monster implements TraceableEntity {
    public static final float FLAP_DEGREES_PER_TICK = 45.836624F;
    public static final int TICKS_PER_FLAP = Mth.ceil(3.9269907F);
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(LightningVexEntity.class, EntityDataSerializers.BYTE);
    private static final int FLAG_IS_CHARGING = 1;
    private static final double RIDING_OFFSET = 0.4D;
    @Nullable
    LivingEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean hasLimitedLife;
    private int limitedLifeTicks;

    public LightningVexEntity(EntityType<? extends LightningVexEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new VexMoveControl(this);
        this.xpReward = 3;
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height - 0.28125F;
    }

    @Override
    public boolean isFlapping() {
        return this.tickCount % TICKS_PER_FLAP == 0;
    }

    @Override
    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);
        this.checkInsideBlocks();
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
            strikeNearbyEntity();
            discard();
        }
    }

    protected void strikeNearbyEntity() {
        List<Entity> nearby = level().getEntities(this,getBoundingBox().inflate(10), e -> e instanceof LivingEntity l && AttackEverythingButOwnerGoal.makeCanAttackPredicate(this).test(l));
        if (!nearby.isEmpty()) {
            LivingEntity first = (LivingEntity) nearby.get(0);
            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
            if (lightningbolt != null) {
                lightningbolt.moveTo(first.position());
                lightningbolt.setCause(owner instanceof ServerPlayer ? (ServerPlayer) owner : null);
                this.level().addFreshEntity(lightningbolt);
                this.level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2, Level.ExplosionInteraction.TNT);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new VexChargeAttackGoal());
        this.goalSelector.addGoal(8, new VexRandomMoveGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(4, new AttackEverythingButOwnerGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(pCompound.getInt("BoundX"), pCompound.getInt("BoundY"), pCompound.getInt("BoundZ"));
        }

        if (pCompound.contains("LifeTicks")) {
            this.setLimitedLife(pCompound.getInt("LifeTicks"));
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.boundOrigin != null) {
            pCompound.putInt("BoundX", this.boundOrigin.getX());
            pCompound.putInt("BoundY", this.boundOrigin.getY());
            pCompound.putInt("BoundZ", this.boundOrigin.getZ());
        }

        if (this.hasLimitedLife) {
            pCompound.putInt("LifeTicks", this.limitedLifeTicks);
        }

    }

    /**
     * Returns null or the entityliving it was ignited by
     */
    @Override
    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos pBoundOrigin) {
        this.boundOrigin = pBoundOrigin;
    }

    private boolean getVexFlag(int pMask) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        return (i & pMask) != 0;
    }

    private void setVexFlag(int pMask, boolean pValue) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        if (pValue) {
            i |= pMask;
        } else {
            i &= ~pMask;
        }

        this.entityData.set(DATA_FLAGS_ID, (byte)(i & 0xff));
    }

    public boolean isCharging() {
        return this.getVexFlag(FLAG_IS_CHARGING);
    }

    public void setIsCharging(boolean pCharging) {
        this.setVexFlag(FLAG_IS_CHARGING, pCharging);
    }

    public void setOwner(LivingEntity pOwner) {
        this.owner = pOwner;
    }

    public void setLimitedLife(int pLimitedLifeTicks) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = pLimitedLifeTicks;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.VEX_HURT;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = pLevel.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        this.populateDefaultEquipmentEnchantments(randomsource, pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    /**
     * Returns the Y Offset of this entity.
     */
    @Override
    public double getMyRidingOffset() {
        return 0.4D;
    }

    class VexChargeAttackGoal extends Goal {
        public VexChargeAttackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        @Override
        public boolean canUse() {
            LivingEntity livingentity = LightningVexEntity.this.getTarget();
            if (livingentity != null && livingentity.isAlive() && !LightningVexEntity.this.getMoveControl().hasWanted() && LightningVexEntity.this.random.nextInt(reducedTickDelay(7)) == 0) {
                return LightningVexEntity.this.distanceToSqr(livingentity) > 4.0D;
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean canContinueToUse() {
            return LightningVexEntity.this.getMoveControl().hasWanted() && LightningVexEntity.this.isCharging() && LightningVexEntity.this.getTarget() != null && LightningVexEntity.this.getTarget().isAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void start() {
            LivingEntity livingentity = LightningVexEntity.this.getTarget();
            if (livingentity != null) {
                Vec3 vec3 = livingentity.getEyePosition();
                LightningVexEntity.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
            }

            LightningVexEntity.this.setIsCharging(true);
            LightningVexEntity.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        @Override
        public void stop() {
            LightningVexEntity.this.setIsCharging(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void tick() {
            LivingEntity livingentity = LightningVexEntity.this.getTarget();
            if (livingentity != null) {
                if (LightningVexEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                    LightningVexEntity.this.doHurtTarget(livingentity);
                    LightningVexEntity.this.setIsCharging(false);
                } else {
                    double d0 = LightningVexEntity.this.distanceToSqr(livingentity);
                    if (d0 < 9.0D) {
                        Vec3 vec3 = livingentity.getEyePosition();
                        LightningVexEntity.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
                    }
                }

            }
        }
    }

    class VexMoveControl extends MoveControl {
        public VexMoveControl(LightningVexEntity pVex) {
            super(pVex);
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                Vec3 vec3 = new Vec3(this.wantedX - LightningVexEntity.this.getX(), this.wantedY - LightningVexEntity.this.getY(), this.wantedZ - LightningVexEntity.this.getZ());
                double d0 = vec3.length();
                if (d0 < LightningVexEntity.this.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    LightningVexEntity.this.setDeltaMovement(LightningVexEntity.this.getDeltaMovement().scale(0.5D));
                } else {
                    LightningVexEntity.this.setDeltaMovement(LightningVexEntity.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05D / d0)));
                    if (LightningVexEntity.this.getTarget() == null) {
                        Vec3 vec31 = LightningVexEntity.this.getDeltaMovement();
                        LightningVexEntity.this.setYRot(-((float)Mth.atan2(vec31.x, vec31.z)) * (180F / (float)Math.PI));
                        LightningVexEntity.this.yBodyRot = LightningVexEntity.this.getYRot();
                    } else {
                        double d2 = LightningVexEntity.this.getTarget().getX() - LightningVexEntity.this.getX();
                        double d1 = LightningVexEntity.this.getTarget().getZ() - LightningVexEntity.this.getZ();
                        LightningVexEntity.this.setYRot(-((float)Mth.atan2(d2, d1)) * (180F / (float)Math.PI));
                        LightningVexEntity.this.yBodyRot = LightningVexEntity.this.getYRot();
                    }
                }

            }
        }
    }

    static class AttackEverythingButOwnerGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public AttackEverythingButOwnerGoal(LightningVexEntity vex) {
            super(vex, LivingEntity.class, 0, true, true, makeCanAttackPredicate(vex));
        }

        private static Predicate<LivingEntity> makeCanAttackPredicate(LightningVexEntity lightningVexEntity){
            return target -> {
                if (!target.attackable() || target instanceof LightningVexEntity) return false;
                if (lightningVexEntity.getOwner() == target) return false;

                if (target instanceof OwnableEntity ownableEntity && ownableEntity.getOwner() == lightningVexEntity.getOwner()) return false;

                if (lightningVexEntity.level().dimension() == Level.END && target instanceof EnderMan) return false;
                return true;
            };
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            super.start();
            this.mob.setNoActionTime(0);
        }
    }


    class VexRandomMoveGoal extends Goal {
        public VexRandomMoveGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        @Override
        public boolean canUse() {
            return !LightningVexEntity.this.getMoveControl().hasWanted() && LightningVexEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean canContinueToUse() {
            return false;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void tick() {
            BlockPos blockpos = LightningVexEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = LightningVexEntity.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(LightningVexEntity.this.random.nextInt(15) - 7, LightningVexEntity.this.random.nextInt(11) - 5, LightningVexEntity.this.random.nextInt(15) - 7);
                if (LightningVexEntity.this.level().isEmptyBlock(blockpos1)) {
                    LightningVexEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (LightningVexEntity.this.getTarget() == null) {
                        LightningVexEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }
}