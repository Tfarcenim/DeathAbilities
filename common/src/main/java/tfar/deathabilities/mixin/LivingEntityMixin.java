package tfar.deathabilities.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import tfar.deathabilities.ducks.LivingEntityDuck;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityDuck {
    private boolean preventQuenching;
    private boolean explodeOnImpact;

    public LivingEntityMixin(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public boolean preventQuenching() {
        return preventQuenching;
    }

    @Override
    public void setPreventQuenching(boolean preventQuenching) {
        this.preventQuenching = preventQuenching;
    }

    @Override
    public boolean explodeOnImpact() {
        return explodeOnImpact;
    }

    @Override
    public void setExplodeOnImpact(boolean impact) {
        explodeOnImpact = impact;
    }

    @Override
    public void clearFire() {
        if (!preventQuenching) {
            super.clearFire();
        }
    }
}
