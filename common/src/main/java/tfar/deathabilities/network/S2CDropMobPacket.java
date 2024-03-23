package tfar.deathabilities.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import tfar.deathabilities.DeathAbilities;

public class S2CDropMobPacket implements S2CModPacket {

    private static final ResourceLocation ID = new ResourceLocation(DeathAbilities.MOD_ID,"stop_riding");

    public S2CDropMobPacket(FriendlyByteBuf buf) {
    }

    public S2CDropMobPacket() {
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf to) {
    }

    @Override
    public void handleClient() {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            Entity passenger = localPlayer.getFirstPassenger();
            if (passenger != null) {
                passenger.stopRiding();
            }
        }
    }
}
