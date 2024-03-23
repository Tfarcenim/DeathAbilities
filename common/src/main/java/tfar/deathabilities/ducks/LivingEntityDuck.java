package tfar.deathabilities.ducks;

import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityDuck {

    boolean preventQuenching();
    void setPreventQuenching(boolean preventQuenching);
    boolean explodeOnImpact();
    void setExplodeOnImpact(boolean impact);
    static LivingEntityDuck of(LivingEntity living) {
        return (LivingEntityDuck) living;
    }

}
