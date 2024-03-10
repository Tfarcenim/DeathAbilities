package tfar.deathabilities.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import tfar.deathabilities.KeyAction;
import tfar.deathabilities.PlayerDeathAbilities;
import tfar.deathabilities.ducks.PlayerDuck;

public class C2SKeyActionPacket implements C2SModPacket{


    private final KeyAction action;

    public C2SKeyActionPacket(KeyAction action) {
        this.action = action;
    }

    public C2SKeyActionPacket(FriendlyByteBuf buf) {
        this.action = KeyAction.values()[buf.readInt()];
    }

    @Override
    public void handleServer(ServerPlayer player) {
        PlayerDeathAbilities playerDeathAbilities = PlayerDuck.of(player).getDeathAbilities();
        if (playerDeathAbilities.isEnabled(action.ability)) {
            action.activate.accept(player);
        }
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(action.ordinal());
    }
}
