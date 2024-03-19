package tfar.deathabilities.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public abstract class AbstractDragonFireballEntity extends Fireball {

    protected int pExplosionPower = 1;

    public AbstractDragonFireballEntity(EntityType<? extends AbstractDragonFireballEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public AbstractDragonFireballEntity(EntityType<?extends AbstractDragonFireballEntity> type, LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ, Level pLevel,int pExplosionPower) {
        super(type, pShooter, pOffsetX, pOffsetY, pOffsetZ, pLevel);
        this.pExplosionPower = pExplosionPower;
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (pResult.getType() != HitResult.Type.ENTITY || !this.ownedBy(((EntityHitResult)pResult).getEntity())) {
            if (!this.level().isClientSide) {
                createAreaOfEffectCloud();
                this.discard();
            }
        }
    }

    protected void createAreaOfEffectCloud() {

    }


    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putByte("ExplosionPower", (byte)this.pExplosionPower);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("ExplosionPower", Tag.TAG_ANY_NUMERIC)) {
            this.pExplosionPower = pCompound.getByte("ExplosionPower");
        }

    }


}
