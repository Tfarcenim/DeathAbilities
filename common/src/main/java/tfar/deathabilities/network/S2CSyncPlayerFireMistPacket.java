package tfar.deathabilities.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.ducks.PlayerDuck;

public class S2CSyncPlayerFireMistPacket extends S2CEntityDataPacket{

    boolean fireMist;
    private static final ResourceLocation ID = new ResourceLocation(DeathAbilities.MOD_ID,"sync_fire_mist");

    public S2CSyncPlayerFireMistPacket(FriendlyByteBuf buf) {
        super(buf);
        fireMist = buf.readBoolean();
    }

    public S2CSyncPlayerFireMistPacket(Entity entity, boolean fireMist) {
        super(entity);
        this.fireMist = fireMist;
    }


    @Override
    public void write(FriendlyByteBuf to) {
        super.write(to);
        to.writeBoolean(fireMist);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void handleClient() {
        Entity entity = getEntity();
        if (entity instanceof Player enderDragon) {
            PlayerDuck.of(enderDragon).setFireMist(fireMist);
        }
    }
}
