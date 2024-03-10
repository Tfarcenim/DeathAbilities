package tfar.deathabilities.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;
import tfar.deathabilities.KeyAction;

import java.util.EnumMap;

public class DeathAbilitiesClient {

    private static final EnumMap<KeyAction, KeyMapping> map = new EnumMap<>(KeyAction.class);

    public static void registerKeybinds() {
        registerKeybind(KeyAction.SPAWN_SANDFISH,ModKeybinds.SPAWN_SANDFISH);
    }

    private static void registerKeybind(KeyAction action,KeyMapping keyMapping) {
        ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
        map.put(action,keyMapping);
    }

}
