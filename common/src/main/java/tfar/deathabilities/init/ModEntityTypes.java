package tfar.deathabilities.init;

import net.minecraft.world.entity.EntityType;
import tfar.deathabilities.entity.DolphinWithLegsEntity;
import tfar.deathabilities.platform.Services;

public class ModEntityTypes {

    public static final EntityType<? extends DolphinWithLegsEntity> DOLPHIN_WITH_LEGS = Services.PLATFORM.getType();

}
