package tfar.deathabilities.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.DeathAbility;
import tfar.deathabilities.ducks.EnderDragonDuck;

public class S2CSyncDragonAbilityPacket implements S2CModPacket {

    int id;
    DeathAbility deathAbility;
    private static final ResourceLocation ID = new ResourceLocation(DeathAbilities.MOD_ID,"sync_dragon_ability");

    public S2CSyncDragonAbilityPacket(FriendlyByteBuf buf) {
        id = buf.readInt();
        deathAbility = DeathAbility.values()[buf.readInt()];
    }

    public S2CSyncDragonAbilityPacket(Entity entity, DeathAbility deathAbility) {
        this.id = entity.getId();
        this.deathAbility = deathAbility;
    }


    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(id);
        to.writeInt(deathAbility.ordinal());
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void handleClient() {
        Entity entity = Minecraft.getInstance().level.getEntity(id);
        if (entity instanceof EnderDragon enderDragon) {
            EnderDragonDuck.of(enderDragon).setPhase(deathAbility);
        }
    }
}