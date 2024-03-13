package tfar.deathabilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.BlockHitResult;
import tfar.deathabilities.entity.SandFishEntity;
import tfar.deathabilities.init.ModEntityTypes;

import java.util.function.Consumer;

public enum KeyAction {
    SPAWN_SANDFISH(DeathAbility.earth, player -> {
        BlockHitResult hitResult = (BlockHitResult) player.pick(24,0,false);
        SandFishEntity sandFishEntity = ModEntityTypes.SANDFISH.spawn(player.serverLevel(),hitResult.getBlockPos().relative(hitResult.getDirection()), MobSpawnType.COMMAND);
    });

    public final DeathAbility ability;
    public final Consumer<ServerPlayer> runner_activate;

    KeyAction(DeathAbility ability, Consumer<ServerPlayer> runner_activate) {

        this.ability = ability;
        this.runner_activate = runner_activate;
    }
}
