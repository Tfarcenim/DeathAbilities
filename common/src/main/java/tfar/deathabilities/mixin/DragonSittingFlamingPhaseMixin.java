package tfar.deathabilities.mixin;

import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonSittingPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonSittingFlamingPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.deathabilities.DeathAbilities;

import javax.annotation.Nullable;

@Mixin(DragonSittingFlamingPhase.class)
abstract class DragonSittingFlamingPhaseMixin extends AbstractDragonSittingPhase {

    @Shadow @Nullable private AreaEffectCloud flame;

    public DragonSittingFlamingPhaseMixin(EnderDragon $$0) {
        super($$0);
    }

    @Inject(method = "doServerTick",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private void handleTick(CallbackInfo ci) {
        DeathAbilities.handleFireBreath(this.dragon,this.flame);
    }
}
