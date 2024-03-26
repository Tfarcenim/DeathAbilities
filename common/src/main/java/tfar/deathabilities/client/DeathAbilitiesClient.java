package tfar.deathabilities.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.DeathAbility;
import tfar.deathabilities.KeyAction;
import tfar.deathabilities.ducks.EnderDragonDuck;
import tfar.deathabilities.network.C2SKeyActionPacket;
import tfar.deathabilities.platform.Services;

import java.util.EnumMap;
import java.util.Map;

public class DeathAbilitiesClient {

    private static final EnumMap<KeyAction, KeyMapping> map = new EnumMap<>(KeyAction.class);
    public static boolean hunter;

    public static void registerKeybinds() {
        registerKeybind(KeyAction.SPAWN_SANDFISH, ModKeybinds.SPAWN_SANDFISH);
        registerKeybind(KeyAction.SPAWN_ATTACK_SQUID, ModKeybinds.SPAWN_ATTACK_SQUID);
        registerKeybind(KeyAction.FIRE_MIST_TOGGLE, ModKeybinds.FIRE_MIST_TOGGLE);
        registerKeybind(KeyAction.PICKUP_MOB,ModKeybinds.PICKUP_MOB);
        registerKeybind(KeyAction.BLAZING_TRAIL_TOGGLE,ModKeybinds.FIRE_TRAIL_TOGGLE);
    }

    private static void registerKeybind(KeyAction action, KeyMapping keyMapping) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
        map.put(action, keyMapping);
    }

    public static void keyPressed() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            for (Map.Entry<KeyAction, KeyMapping> entry : map.entrySet()) {
                KeyAction action = entry.getKey();
                KeyMapping mapping = entry.getValue();
                if (action.hunter == hunter && mapping.consumeClick()) {
                    Services.PLATFORM.sendToServer(new C2SKeyActionPacket(action));
                    return;
                }
            }
        }
    }

    public static void textureHook(EnderDragon enderDragon, CallbackInfoReturnable<ResourceLocation> cir) {
        if (DeathAbilities.enable_dragon) {
            DeathAbility phase = EnderDragonDuck.of(enderDragon).getPhase();
            cir.setReturnValue(phase.getBody());
        }
    }

    public enum RenderTypes {
        FIRE(DeathAbility.fire),
        EARTH(DeathAbility.earth),
        WATER(DeathAbility.water),
        LIGHTNING(DeathAbility.lightning);

        public final RenderType body;
        public final RenderType eyes;
        RenderTypes(DeathAbility deathAbility) {
            body = RenderType.entityCutoutNoCull(deathAbility.getBody());
            eyes = RenderType.eyes(deathAbility.getEyes());
        }
    }

    public static RenderType getRenderTypeBody(DeathAbility deathAbility) {
        switch (deathAbility){
            case fire -> {return RenderTypes.FIRE.body;}
            case water -> {return RenderTypes.WATER.body;}
            case earth -> {return RenderTypes.EARTH.body;}
            case lightning -> {return RenderTypes.LIGHTNING.body;}
        }
        return null;
    }

    public static RenderType getRenderTypeEyes(DeathAbility deathAbility) {
        switch (deathAbility){
            case fire -> {return RenderTypes.FIRE.eyes;}
            case water -> {return RenderTypes.WATER.eyes;}
            case earth -> {return RenderTypes.EARTH.eyes;}
            case lightning -> {return RenderTypes.LIGHTNING.eyes;}
        }
        return null;
    }

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }


}
