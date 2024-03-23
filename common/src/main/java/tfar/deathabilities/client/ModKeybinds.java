package tfar.deathabilities.client;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {
    public static final String CATEGORY = "key.categories.deathabilities";
    public static final KeyMapping SPAWN_SANDFISH = new KeyMapping("key.spawn_sandfish", GLFW.GLFW_KEY_V,CATEGORY);
    public static final KeyMapping SPAWN_ATTACK_SQUID = new KeyMapping("key.spawn_attack_squid", GLFW.GLFW_KEY_N,CATEGORY);
    public static final KeyMapping FIRE_MIST_TOGGLE = new KeyMapping("key.fire_mist_toggle", GLFW.GLFW_KEY_M,CATEGORY);
    public static final KeyMapping PICKUP_MOB = new KeyMapping("key.pickup_mob", GLFW.GLFW_KEY_L,CATEGORY);

}
