package tfar.deathabilities.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import tfar.deathabilities.init.ModEntityTypes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EarthDragonFireballEntity extends AbstractDragonFireballEntity {

    public EarthDragonFireballEntity(EntityType<? extends EarthDragonFireballEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public EarthDragonFireballEntity(Level pLevel, LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ, int pExplosionPower) {
        super(ModEntityTypes.EARTH_DRAGON_FIREBALL, pShooter, pOffsetX, pOffsetY, pOffsetZ, pLevel, pExplosionPower);
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!level().isClientSide) {
            createCrater();
        }
    }

    protected void createCrater() {
        Set<BlockPos> affected = new HashSet<>();

        int r = 2;
        for (int y = -r; y <= 1;y++){
            for (int z = -r;z < r;z++) {
                for (int x = -r;x < r;x++) {
                    BlockPos pos = new BlockPos(blockPosition().getX()+x,blockPosition().getY()+y,blockPosition().getZ()+z);
                    affected.add(pos);
                }
            }
        }

        affected.add(blockPosition().below());

        for (BlockPos pos : affected) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level(),pos,level().getBlockState(pos));
            fallingBlockEntity.addDeltaMovement(new Vec3(0,0.15,0));
            this.level().explode(this, this.getX(), this.getY() - r, this.getZ(), (float)this.pExplosionPower, true, Level.ExplosionInteraction.NONE);
        }
    }



    @Override
    protected void createAreaOfEffectCloud() {
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaeffectcloud.setOwner((LivingEntity)entity);
        }

        areaeffectcloud.setParticle(ParticleTypes.END_ROD);
        areaeffectcloud.setRadius(3.0F);
        areaeffectcloud.setDuration(600);
        areaeffectcloud.setRadiusPerTick((7.0F - areaeffectcloud.getRadius()) / (float)areaeffectcloud.getDuration());
        areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
        // AreaEffectCloudDuck.of(areaeffectcloud).setType(DeathAbility.lightning);
        if (!list.isEmpty()) {
            for(LivingEntity livingentity : list) {
                double d0 = this.distanceToSqr(livingentity);
                if (d0 < 16.0D) {
                    areaeffectcloud.setPos(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                    break;
                }
            }
        }

        this.level().levelEvent(LevelEvent.PARTICLES_DRAGON_FIREBALL_SPLASH, this.blockPosition(), this.isSilent() ? -1 : 1);
        this.level().addFreshEntity(areaeffectcloud);
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
