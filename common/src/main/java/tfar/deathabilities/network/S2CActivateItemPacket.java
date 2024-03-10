package tfar.deathabilities.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tfar.deathabilities.DeathAbilities;

public class S2CActivateItemPacket implements S2CModPacket {

    ItemStack item;
    private static final ResourceLocation ID = new ResourceLocation(DeathAbilities.MOD_ID,"activate_item");

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
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void handleClient() {
        Minecraft.getInstance().gameRenderer.displayItemActivation(item);
    }
}
