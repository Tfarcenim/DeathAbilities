package tfar.deathabilities.ducks;

import net.minecraft.world.entity.Mob;

public interface MobEntityDuck {

    boolean targetHunters();
    void setTargetHunters(boolean target);

    static MobEntityDuck of(Mob mob) {
        return (MobEntityDuck) mob;
    }

}
