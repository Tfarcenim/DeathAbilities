package tfar.deathabilities.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DolphinWithLegsEntityGeo extends DolphinWithLegsEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DolphinWithLegsEntityGeo(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this,"Idle/Walk/Swim",0,animationState ->{
            if (this.isInWater()) {
                return animationState.setAndContinue(animationState.isMoving() ? DefaultAnimations.SWIM : DefaultAnimations.IDLE);
            } else {
                return animationState.setAndContinue(animationState.isMoving() ? DefaultAnimations.WALK : DefaultAnimations.IDLE);
            }
        } ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
