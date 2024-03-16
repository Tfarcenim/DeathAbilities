package tfar.deathabilities.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import tfar.deathabilities.init.ModEntityTypes;
import tfar.deathabilities.init.ModItems;

public class LightningBoltEntity extends AbstractArrow {
    private ItemStack lightningBoltItem = new ItemStack(ModItems.LIGHTNING_BOLT);

    public LightningBoltEntity(EntityType<? extends LightningBoltEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public LightningBoltEntity(Level pLevel, LivingEntity pShooter, ItemStack pStack) {
        super(ModEntityTypes.LIGHTNING_BOLT, pShooter, pLevel);
        this.lightningBoltItem = pStack.copy();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.lightningBoltItem.copy();
    }


    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float f = 8.0F;
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.lightningBoltItem, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, entity1 == null ? this : entity1);
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingentity1) {
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }
        summonLightningVex(position());
    }

    protected void summonLightningVex(Vec3 vec3) {
        Entity owner = this.getOwner();
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float volume = 1.0F;
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
        if (lightningbolt != null) {
            lightningbolt.moveTo(vec3);
            lightningbolt.setCause(owner instanceof ServerPlayer ? (ServerPlayer) owner : null);
            lightningbolt.setVisualOnly(true);
            this.level().addFreshEntity(lightningbolt);
            soundevent = SoundEvents.TRIDENT_THUNDER;
            volume = 5.0F;
            for (int i = 0; i < 10;i++) {
                LightningVexEntity lightningVexEntity = ModEntityTypes.LIGHTNING_VEX.create(level());
                lightningVexEntity.setOwner(owner instanceof ServerPlayer ? (ServerPlayer) owner : null);
                lightningVexEntity.setPos(vec3);
                lightningVexEntity.setLimitedLife(300);
                level().addFreshEntity(lightningVexEntity);
            }
        }

        this.playSound(soundevent, volume, 1.0F);
        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        summonLightningVex(position());
    }


    @Override
    protected boolean tryPickup(Player pPlayer) {
        return super.tryPickup(pPlayer) || this.isNoPhysics() && this.ownedBy(pPlayer) && pPlayer.getInventory().add(this.getPickupItem());
    }

    /**
     * The sound made when an entity is hit by this projectile
     */
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void playerTouch(Player pEntity) {
        if (this.ownedBy(pEntity) || this.getOwner() == null) {
            super.playerTouch(pEntity);
        }

    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Lightning_Bolt", 10)) {
            this.lightningBoltItem = ItemStack.of(pCompound.getCompound("Lightning_Bolt"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put("Lightning_bolt", this.lightningBoltItem.save(new CompoundTag()));
    }


    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }
}