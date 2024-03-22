package tfar.deathabilities.ducks;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import tfar.deathabilities.DeathAbility;
import tfar.deathabilities.network.S2CSyncDragonAbilityPacket;
import tfar.deathabilities.platform.Services;

public interface EnderDragonDuck extends ClientSyncable {

    DeathAbility getPhase();
    void setPhase(DeathAbility deathAbility);

    @Override
    default void syncToTracking() {
        Services.PLATFORM.sendToTrackingClients(new S2CSyncDragonAbilityPacket(getSelf(),getPhase()),getSelf());
    }
    void addDamage(float damage);

    static EnderDragonDuck of(EnderDragon enderDragon) {
        return (EnderDragonDuck) enderDragon;
    }
    default EnderDragon getSelf() {
        return (EnderDragon) this;
    }

}
