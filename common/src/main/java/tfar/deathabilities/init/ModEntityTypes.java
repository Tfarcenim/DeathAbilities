package tfar.deathabilities.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import tfar.deathabilities.entity.*;
import tfar.deathabilities.platform.Services;

public class ModEntityTypes {

    public static final EntityType<? extends DolphinWithLegsEntity> DOLPHIN_WITH_LEGS = Services.PLATFORM.getType();
    public static final EntityType<QuickSandBombEntity> QUICKSAND_BOMB = EntityType.Builder.<QuickSandBombEntity>of(QuickSandBombEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("");
    public static final EntityType<SandFishEntity> SANDFISH = EntityType.Builder.of(SandFishEntity::new,MobCategory.MONSTER).sized(0.4F, 0.3F).clientTrackingRange(8).build("");
    public static final EntityType<FireDragonFireballEntity> FIRE_DRAGON_FIREBALL = EntityType.Builder.<FireDragonFireballEntity>of(FireDragonFireballEntity::new,MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10).build("");
    public static final EntityType<WaterDragonFireballEntity> WATER_DRAGON_FIREBALL = EntityType.Builder.<WaterDragonFireballEntity>of(WaterDragonFireballEntity::new,MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10).build("");
    public static final EntityType<LightningDragonFireballEntity> LIGHTNING_DRAGON_FIREBALL = EntityType.Builder.<LightningDragonFireballEntity>of(LightningDragonFireballEntity::new,MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10).build("");
    public static final EntityType<EarthDragonFireballEntity> EARTH_DRAGON_FIREBALL = EntityType.Builder.<EarthDragonFireballEntity>of(EarthDragonFireballEntity::new,MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10).build("");

    public static final EntityType<LightningVexEntity> LIGHTNING_VEX = EntityType.Builder.<LightningVexEntity>of(LightningVexEntity::new,MobCategory.MONSTER).fireImmune().sized(0.4F, 0.8F).clientTrackingRange(8).build("");
    public static final EntityType<LightningBoltEntity> LIGHTNING_BOLT = EntityType.Builder.<LightningBoltEntity>of(LightningBoltEntity::new,MobCategory.MISC).fireImmune().sized(0.4F, 0.8F).clientTrackingRange(8).build("");

}
