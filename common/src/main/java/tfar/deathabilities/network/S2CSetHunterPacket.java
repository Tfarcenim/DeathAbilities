package tfar.deathabilities.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.client.DeathAbilitiesClient;

public class S2CSetHunterPacket implements S2CModPacket {

    private static final ResourceLocation ID = new ResourceLocation(DeathAbilities.MOD_ID,"set_hunter");
    boolean hunter;
    public S2CSetHunterPacket(FriendlyByteBuf buf) {
        hunter = buf.readBoolean();
    }

    public S2CSetHunterPacket(boolean hunter) {
        this.hunter = hunter;
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeBoolean(hunter);
    }

    @Override
    public void handleClient() {
        DeathAbilitiesClient.hunter = hunter;
    }
}
