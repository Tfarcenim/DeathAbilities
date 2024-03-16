package tfar.deathabilities.ducks;

import net.minecraft.world.entity.AreaEffectCloud;
import org.jetbrains.annotations.Nullable;
import tfar.deathabilities.DeathAbility;

public interface AreaEffectCloudDuck {

    @Nullable DeathAbility getType();
    void setType(DeathAbility deathAbility);
    static AreaEffectCloudDuck of(AreaEffectCloud areaEffectCloud) {
        return(AreaEffectCloudDuck) areaEffectCloud;
    }

}
