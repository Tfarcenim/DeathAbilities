package tfar.deathabilities.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public abstract class S2CEntityDataPacket implements S2CModPacket {

    int id;

    public S2CEntityDataPacket(FriendlyByteBuf buf) {
        id = buf.readInt();
    }

    public S2CEntityDataPacket(Entity entity) {
        this.id = entity.getId();
    }

    public Entity getEntity() {
        return Minecraft.getInstance().level.getEntity(id);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(id);
    }
}
