package tfar.deathabilities.mixin;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import tfar.deathabilities.ducks.ServerLevelDuck;
import tfar.deathabilities.entity.boss.EndElementalDragonFight;

@Mixin(ServerLevel.class)
public class ServerLevelMixin implements ServerLevelDuck {

    private EndElementalDragonFight elementalDragonFight;
    @Override
    public EndElementalDragonFight getFight() {
        return elementalDragonFight;
    }
}
