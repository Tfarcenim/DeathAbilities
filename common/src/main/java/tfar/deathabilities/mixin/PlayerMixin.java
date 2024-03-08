package tfar.deathabilities.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import tfar.deathabilities.DeathAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.deathabilities.PlayerDeathAbilities;
import tfar.deathabilities.ducks.PlayerDuck;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {

    private PlayerDeathAbilities playerDeathAbilities = new PlayerDeathAbilities();

    @Inject(method = "addAdditionalSaveData",at = @At("RETURN"))
    private void addExtra(CompoundTag tag, CallbackInfo ci) {
        PlayerDeathAbilities ability = getDeathAbilities();
        tag.put(DeathAbilities.MOD_ID,ability.serialize());
    }

    @Inject(method = "readAdditionalSaveData",at = @At("RETURN"))
    private void readExtra(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains(DeathAbilities.MOD_ID, Tag.TAG_INT_ARRAY)) {
            setDeathAbilities(PlayerDeathAbilities.deserialize(tag.getIntArray(DeathAbilities.MOD_ID)));
        }
    }



    @Override
    public PlayerDeathAbilities getDeathAbilities() {
        return playerDeathAbilities;
    }

    @Override
    public void setDeathAbilities(PlayerDeathAbilities playerDeathAbilities) {
        this.playerDeathAbilities = playerDeathAbilities;
    }
}