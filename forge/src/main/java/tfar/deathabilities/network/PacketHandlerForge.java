package tfar.deathabilities.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import tfar.deathabilities.DeathAbilities;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class PacketHandlerForge {

    public static SimpleChannel INSTANCE;
    public static void registerMessages() {

        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(DeathAbilities.MOD_ID, DeathAbilities.MOD_ID), () -> "1.0", s -> true, s -> true);

        int i = 0;

        ///////server to client

        INSTANCE.registerMessage(i++,
                S2CActivateItemPacket.class,
                S2CActivateItemPacket::write,
                S2CActivateItemPacket::new,
                wrapS2C());

        INSTANCE.registerMessage(i++,
                S2CSyncDragonAbilityPacket.class,
                S2CSyncDragonAbilityPacket::write,
                S2CSyncDragonAbilityPacket::new,
                wrapS2C());

        INSTANCE.registerMessage(i++,
                S2CSyncPlayerFireMistPacket.class,
                S2CSyncPlayerFireMistPacket::write,
                S2CSyncPlayerFireMistPacket::new,
                wrapS2C());

        INSTANCE.registerMessage(i++,
                S2CPickupMobPacket.class,
                S2CPickupMobPacket::write,
                S2CPickupMobPacket::new,
                wrapS2C());

        INSTANCE.registerMessage(i++,
                S2CDropMobPacket.class,
                S2CDropMobPacket::write,
                S2CDropMobPacket::new,
                wrapS2C());

        INSTANCE.registerMessage(i++,
                S2CSetHunterPacket.class,
                S2CSetHunterPacket::write,
                S2CSetHunterPacket::new,
                wrapS2C());


        //client to server

        INSTANCE.registerMessage(i++,
                C2SKeyActionPacket.class,
                C2SKeyActionPacket::write,
                C2SKeyActionPacket::new,
                wrapC2S());
    }

    private static <MSG extends S2CModPacket> BiConsumer<MSG, Supplier<NetworkEvent.Context>> wrapS2C() {
        return ((msg, contextSupplier) -> {
            contextSupplier.get().enqueueWork(msg::handleClient);
            contextSupplier.get().setPacketHandled(true);
        });
    }

    private static <MSG extends C2SModPacket> BiConsumer<MSG, Supplier<NetworkEvent.Context>> wrapC2S() {
        return ((msg, contextSupplier) -> {
            ServerPlayer player = contextSupplier.get().getSender();
            contextSupplier.get().enqueueWork(() -> msg.handleServer(player));
            contextSupplier.get().setPacketHandled(true);
        });
    }

    public static <MSG> void sendToClient(MSG packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendToServer(MSG packet) {
        INSTANCE.sendToServer(packet);
    }
}
