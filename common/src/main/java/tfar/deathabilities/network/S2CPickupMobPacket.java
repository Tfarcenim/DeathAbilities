package tfar.deathabilities.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.client.DeathAbilitiesClient;

public class S2CPickupMobPacket extends S2CEntityDataPacket {

    private static final ResourceLocation ID = new ResourceLocation(DeathAbilities.MOD_ID,"pickup_mob");

    public S2CPickupMobPacket(FriendlyByteBuf buf) {
        super(buf);
    }

    public S2CPickupMobPacket(Entity entity) {
        super(entity);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf to) {
        super.write(to);
    }

    @Override
    public void handleClient() {
        Entity entity = getEntity();
        if (entity != null) {
            entity.startRiding(DeathAbilitiesClient.getClientPlayer());
        }
    }
}
