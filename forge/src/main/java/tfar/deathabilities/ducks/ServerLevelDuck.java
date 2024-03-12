package tfar.deathabilities.ducks;

import net.minecraft.server.level.ServerLevel;
import tfar.deathabilities.entity.boss.EndElementalDragonFight;

public interface ServerLevelDuck {

    EndElementalDragonFight getFight();

    static ServerLevelDuck of (ServerLevel serverLevel) {
        return (ServerLevelDuck) serverLevel;
    }

}
