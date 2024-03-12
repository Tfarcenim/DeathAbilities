package tfar.deathabilities.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.tuple.Pair;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.DeathAbilitiesForge;
import tfar.deathabilities.entity.DolphinWithLegsEntity;
import tfar.deathabilities.entity.DolphinWithLegsEntityGeo;
import tfar.deathabilities.entity.boss.ElementalDragonEntity;
import tfar.deathabilities.network.C2SModPacket;
import tfar.deathabilities.network.PacketHandlerForge;
import tfar.deathabilities.network.S2CModPacket;
import tfar.deathabilities.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public <T extends Registry<? extends F>,F> void superRegister(Class<?> clazz, T registry, Class<F> filter) {
        List<Pair<ResourceLocation, Supplier<?>>> list = DeathAbilitiesForge.registerLater.computeIfAbsent(registry, k -> new ArrayList<>());
        for (Field field : clazz.getFields()) {
            MappedRegistry<? extends F> forgeRegistry = (MappedRegistry<? extends F>) registry;
            forgeRegistry.unfreeze();
            try {
                Object o = field.get(null);
                if (filter.isInstance(o)) {
                    list.add(Pair.of(new ResourceLocation(DeathAbilities.MOD_ID,field.getName().toLowerCase(Locale.ROOT)),() -> o));
                }
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public EntityType<? extends DolphinWithLegsEntity> getType() {
        return EntityType.Builder.of(DolphinWithLegsEntityGeo::new, MobCategory.MISC)
                .sized(1,1.75f).build("");
    }

    @Override
    public EntityType<? extends Mob> getDragonType() {
        return EntityType.Builder.of(ElementalDragonEntity::new, MobCategory.MONSTER).fireImmune().sized(16.0F, 8.0F).clientTrackingRange(10).build("");
    }

    @Override
    public boolean postMobGriefingEvent() {
        return false;
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLEnvironment.dist.isClient();
    }

    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        PacketHandlerForge.sendToClient(msg,player);
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        PacketHandlerForge.sendToServer(msg);
    }

    @Override
    public boolean canEntityDestroyHook(Level level, BlockPos pos, LivingEntity entity) {
        return ForgeHooks.canEntityDestroy(level, pos, entity);
    }

    @Override
    public int getExperienceDropHook(LivingEntity entity, Player attackingPlayer, int originalExperience) {
        return ForgeEventFactory.getExperienceDrop(entity, attackingPlayer, originalExperience);
    }
}