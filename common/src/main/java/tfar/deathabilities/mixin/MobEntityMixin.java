package tfar.deathabilities.mixin;

import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import tfar.deathabilities.ducks.MobEntityDuck;

@Mixin(Mob.class)
public class MobEntityMixin implements MobEntityDuck {
    private boolean targetHunters;
    @Override
    public boolean targetHunters() {
        return targetHunters;
    }

    @Override
    public void setTargetHunters(boolean target) {
        targetHunters = target;
    }
}
