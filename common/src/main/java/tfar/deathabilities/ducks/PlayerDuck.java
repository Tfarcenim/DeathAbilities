package tfar.deathabilities.ducks;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import tfar.deathabilities.PlayerDeathAbilities;

public interface PlayerDuck {
    PlayerDeathAbilities getDeathAbilities();
    void setDeathAbilities(PlayerDeathAbilities playerDeathAbilities);

    static PlayerDuck of(Player player) {
        return (PlayerDuck) player;
    }

}
