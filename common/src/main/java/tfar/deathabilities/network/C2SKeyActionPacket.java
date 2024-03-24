package tfar.deathabilities.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.HunterData;
import tfar.deathabilities.KeyAction;
import tfar.deathabilities.PlayerDeathAbilities;
import tfar.deathabilities.ducks.PlayerDuck;

public class C2SKeyActionPacket implements C2SModPacket{

    private static final ResourceLocation ID = new ResourceLocation(DeathAbilities.MOD_ID,"keybind");
    private final KeyAction action;

    public C2SKeyActionPacket(KeyAction action) {
        this.action = action;
    }

    public C2SKeyActionPacket(FriendlyByteBuf buf) {
        this.action = KeyAction.values()[buf.readInt()];
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        PlayerDeathAbilities playerDeathAbilities = PlayerDuck.of(player).getDeathAbilities();
        if (playerDeathAbilities.isEnabled(action.ability)) {
            boolean isHunter = player.getUUID().equals(HunterData.runner);
            if (isHunter == action.hunter) {
                action.activate.accept(player);
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(action.ordinal());
    }
}
