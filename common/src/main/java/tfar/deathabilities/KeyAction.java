package tfar.deathabilities;

import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public enum KeyAction {
    SPAWN_SANDFISH(DeathInfo.EARTH,player -> {});

    private final DeathInfo ability;
    private final Consumer<ServerPlayer> activate;

    KeyAction(DeathInfo ability, Consumer<ServerPlayer> activate) {

        this.ability = ability;
        this.activate = activate;
    }
}
