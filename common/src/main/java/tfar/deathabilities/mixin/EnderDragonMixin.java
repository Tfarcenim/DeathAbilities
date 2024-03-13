package tfar.deathabilities.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import tfar.deathabilities.DeathAbility;
import tfar.deathabilities.ducks.EnderDragonDuck;
import tfar.deathabilities.network.S2CSyncDragonAbilityPacket;
import tfar.deathabilities.platform.Services;

@Mixin(EnderDragon.class)
public abstract class EnderDragonMixin extends LivingEntity implements EnderDragonDuck {

    private DeathAbility phase = DeathAbility.fire;

    protected EnderDragonMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public DeathAbility getPhase() {
        return phase;
    }

    @Override
    public void setPhase(DeathAbility deathAbility) {
        phase = deathAbility;
        if (!this.level().isClientSide)
            Services.PLATFORM.sendToTrackingClients(new S2CSyncDragonAbilityPacket(this,phase), this);
    }
}
