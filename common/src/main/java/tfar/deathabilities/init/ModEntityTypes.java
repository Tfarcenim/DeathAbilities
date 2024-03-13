package tfar.deathabilities.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import tfar.deathabilities.entity.DolphinWithLegsEntity;
import tfar.deathabilities.entity.FireDragonFireball;
import tfar.deathabilities.entity.QuickSandBombEntity;
import tfar.deathabilities.entity.SandFishEntity;
import tfar.deathabilities.platform.Services;

public class ModEntityTypes {

    public static final EntityType<? extends DolphinWithLegsEntity> DOLPHIN_WITH_LEGS = Services.PLATFORM.getType();
    public static final EntityType<QuickSandBombEntity> QUICKSAND_BOMB = EntityType.Builder.<QuickSandBombEntity>of(QuickSandBombEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("");
    public static final EntityType<SandFishEntity> SANDFISH = EntityType.Builder.of(SandFishEntity::new,MobCategory.MONSTER).sized(0.4F, 0.3F).clientTrackingRange(8).build("");
    public static final EntityType<FireDragonFireball> FIRE_DRAGON_FIREBALL = EntityType.Builder.<FireDragonFireball>of(FireDragonFireball::new,MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10).build("");
}
