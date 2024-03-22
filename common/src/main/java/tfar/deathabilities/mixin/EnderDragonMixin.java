package tfar.deathabilities.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.DeathAbility;
import tfar.deathabilities.ducks.EnderDragonDuck;

@Mixin(EnderDragon.class)
public abstract class EnderDragonMixin extends LivingEntity implements EnderDragonDuck {

    private DeathAbility phase = DeathAbility.fire;
    private float totalDamage;

    protected EnderDragonMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public DeathAbility getPhase() {
        return phase;
    }

    @Override
    public void addDamage(float damage) {
        totalDamage += damage;
    }

    public void checkPhase() {
        int i = (int) (totalDamage/ DeathAbilities.damage_per_stage) % DeathAbility.values().length;//(0 - 3)
        DeathAbility newA = DeathAbility.values()[i];
        if (newA != phase) {
            setPhase(newA);
        }
    }

    @Inject(method = "aiStep",at = @At("RETURN"))
    private void tickEvent(CallbackInfo ci) {
        if (!level().isClientSide) {
            checkPhase();
        }
    }

    @Override
    public void setPhase(DeathAbility deathAbility) {
        phase = deathAbility;
        if (!this.level().isClientSide) {
            syncToTracking();
        }
    }

    @Inject(method = "addAdditionalSaveData",at = @At("RETURN"))
    private void addModData(CompoundTag tag, CallbackInfo ci) {
        tag.putFloat("totalDamage",totalDamage);
    }

    @Inject(method = "readAdditionalSaveData",at = @At("RETURN"))
    private void readModData(CompoundTag tag, CallbackInfo ci) {
        totalDamage = tag.getFloat("totalDamage");
    }
}
