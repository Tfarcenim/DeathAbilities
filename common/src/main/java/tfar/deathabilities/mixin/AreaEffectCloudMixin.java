package tfar.deathabilities.mixin;

import net.minecraft.world.entity.AreaEffectCloud;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.DeathAbility;
import tfar.deathabilities.ducks.AreaEffectCloudDuck;

@Mixin(AreaEffectCloud.class)
public class AreaEffectCloudMixin implements AreaEffectCloudDuck {

    private DeathAbility type;
    @Override
    public @Nullable DeathAbility getType() {
        return type;
    }

    @Override
    public void setType(DeathAbility deathAbility) {
        type = deathAbility;
    }

    @Inject(method = "tick",at = @At("RETURN"))
    private void onCloudTick(CallbackInfo ci) {
        if (type != null) {
            DeathAbilities.cloudTick((AreaEffectCloud) (Object)this,type);
        }
    }

}
