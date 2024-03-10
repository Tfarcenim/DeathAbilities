package tfar.deathabilities.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import tfar.deathabilities.DeathAbilities;

public interface ModPacket {

    ResourceLocation id();
    void write(FriendlyByteBuf to);

}
