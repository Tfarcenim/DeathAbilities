package tfar.deathabilities.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import tfar.deathabilities.init.ModEntityTypes;

public class FireDragonFireballEntity extends AbstractDragonFireballEntity {

    public FireDragonFireballEntity(EntityType<? extends FireDragonFireballEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireDragonFireballEntity(Level pLevel, LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ, int pExplosionPower) {
        super(ModEntityTypes.FIRE_DRAGON_FIREBALL, pShooter, pOffsetX, pOffsetY, pOffsetZ, pLevel,pExplosionPower);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            // TODO 1.19.3: The creation of Level.ExplosionInteraction means this code path will fire EntityMobGriefingEvent twice. Should we try and fix it? -SS
            //boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this.getOwner());
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)this.pExplosionPower, true, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level().isClientSide) {
            Entity entity = pResult.getEntity();
            Entity owner = this.getOwner();
            entity.hurt(this.damageSources().fireball(this, owner), 6.0F);
            if (owner instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)owner, entity);
            }

        }
    }
}
