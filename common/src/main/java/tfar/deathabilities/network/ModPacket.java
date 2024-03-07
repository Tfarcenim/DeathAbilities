package tfar.deathabilities.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import tfar.deathabilities.DeathAbilities;

public interface ModPacket {

    ResourceLocation activate_item = new ResourceLocation(DeathAbilities.MOD_ID,"activate_item");
    void write(FriendlyByteBuf to);

}
