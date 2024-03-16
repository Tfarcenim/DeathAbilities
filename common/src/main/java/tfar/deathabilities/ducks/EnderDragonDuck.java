package tfar.deathabilities.ducks;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import tfar.deathabilities.DeathAbility;

public interface EnderDragonDuck {

    DeathAbility getPhase();
    void setPhase(DeathAbility deathAbility);
    static EnderDragonDuck of(EnderDragon enderDragon) {
        return (EnderDragonDuck) enderDragon;
    }
    void addDamage(float damage);

}
