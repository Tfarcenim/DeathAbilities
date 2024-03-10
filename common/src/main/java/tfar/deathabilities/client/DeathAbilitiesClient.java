package tfar.deathabilities.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.ArrayUtils;
import tfar.deathabilities.KeyAction;
import tfar.deathabilities.network.C2SKeyActionPacket;
import tfar.deathabilities.platform.Services;

import java.util.EnumMap;
import java.util.Map;

public class DeathAbilitiesClient {

    private static final EnumMap<KeyAction, KeyMapping> map = new EnumMap<>(KeyAction.class);

    public static void registerKeybinds() {
        registerKeybind(KeyAction.SPAWN_SANDFISH,ModKeybinds.SPAWN_SANDFISH);
    }

    private static void registerKeybind(KeyAction action,KeyMapping keyMapping) {
        ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
        map.put(action,keyMapping);
    }

    public static void keyPressed() {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                for (Map.Entry<KeyAction, KeyMapping> entry : map.entrySet()) {
                    KeyAction action = entry.getKey();
                    if (entry.getValue().consumeClick()) {
                        Services.PLATFORM.sendToClient(new C2SKeyActionPacket(action),);
                        return;
                    }
                }
        }
    }

}
