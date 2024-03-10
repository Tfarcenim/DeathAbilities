package tfar.deathabilities;

import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public enum KeyAction {
    SPAWN_SANDFISH(DeathAbility.EARTH, player -> {});

    public final DeathAbility ability;
    public final Consumer<ServerPlayer> activate;

    KeyAction(DeathAbility ability, Consumer<ServerPlayer> activate) {

        this.ability = ability;
        this.activate = activate;
    }
}
