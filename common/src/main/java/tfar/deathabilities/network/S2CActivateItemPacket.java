package tfar.deathabilities.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class S2CActivateItemPacket implements S2CModPacket {

    ItemStack item;

    public S2CActivateItemPacket(FriendlyByteBuf buf) {
        item = buf.readItem();
    }

    public S2CActivateItemPacket(ItemStack item) {
        this.item = item;
    }



    @Override
    public void write(FriendlyByteBuf to) {
        to.writeItem(item);
    }

    @Override
    public void handleClient() {
        Minecraft.getInstance().gameRenderer.displayItemActivation(item);
    }
}
