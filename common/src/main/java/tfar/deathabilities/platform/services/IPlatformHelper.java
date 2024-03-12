package tfar.deathabilities.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import tfar.deathabilities.entity.DolphinWithLegsEntity;
import tfar.deathabilities.network.C2SModPacket;
import tfar.deathabilities.network.S2CModPacket;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    boolean isPhysicalClient();

    void sendToClient(S2CModPacket msg, ServerPlayer player);
    void sendToServer(C2SModPacket msg);


    <T extends Registry<? extends F>,F> void superRegister(Class<?> clazz, T registry, Class<F> filter);

    EntityType<? extends DolphinWithLegsEntity> getType();

    boolean postMobGriefingEvent();


    boolean canEntityDestroyHook(Level level, BlockPos pos, LivingEntity entity);

    int getExperienceDropHook(LivingEntity entity, Player attackingPlayer, int originalExperience);

}