package tfar.deathabilities.ducks;

import net.minecraft.world.entity.player.Player;
import tfar.deathabilities.PlayerDeathAbilities;
import tfar.deathabilities.network.S2CSyncPlayerFireMistPacket;
import tfar.deathabilities.platform.Services;

public interface PlayerDuck  extends ClientSyncable {
    PlayerDeathAbilities getDeathAbilities();
    void setDeathAbilities(PlayerDeathAbilities playerDeathAbilities);
    void setFireMist(boolean mist);
    boolean isFireMist();
    default void toggleFireMist() {
        setFireMist(!isFireMist());
    }

    @Override
    default void syncToTracking() {
        Services.PLATFORM.sendToTrackingClientsAndSelf(new S2CSyncPlayerFireMistPacket(getSelf(),isFireMist()),getSelf());
    }

    static PlayerDuck of(Player player) {
        return (PlayerDuck) player;
    }

    default Player getSelf() {
        return (Player) this;
    }

}
