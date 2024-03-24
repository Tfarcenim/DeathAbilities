package tfar.deathabilities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import tfar.deathabilities.ducks.LivingEntityDuck;
import tfar.deathabilities.ducks.PlayerDuck;
import tfar.deathabilities.entity.AttackSquid;
import tfar.deathabilities.entity.SandFishEntity;
import tfar.deathabilities.init.ModEntityTypes;
import tfar.deathabilities.network.S2CDropMobPacket;
import tfar.deathabilities.network.S2CPickupMobPacket;
import tfar.deathabilities.platform.Services;

import java.util.List;
import java.util.function.Consumer;

public enum KeyAction {
    SPAWN_SANDFISH(DeathAbility.earth, player -> {
        BlockHitResult hitResult = (BlockHitResult) player.pick(24,0,false);
        SandFishEntity sandFishEntity = ModEntityTypes.SANDFISH.spawn(player.serverLevel(),hitResult.getBlockPos().relative(hitResult.getDirection()), MobSpawnType.COMMAND);
    }, true),
    SPAWN_ATTACK_SQUID(DeathAbility.water,player -> {

        for (int y = 0 ; y < 2;y++) {
            for (int z = 0 ; z < 2;z++) {
                for (int x = 0 ; x < 2;x++) {
                    AttackSquid attackSquid = ModEntityTypes.ATTACK_SQUID.spawn(player.serverLevel(),player.blockPosition().offset(2*x - 1,2*y,2*z - 1),
                            MobSpawnType.COMMAND);
                    if (attackSquid != null) {
                        attackSquid.setOwnerUUID(player.getUUID());
                    }
                }
            }
        }


        RandomSource random = player.getRandom();
        double r = 9/2d;
        for(int $$1 = 0; $$1 < 100; ++$$1) {
            double offsetX = r * (2 * random.nextFloat() - 1);
            double offsetY = r * (2 * random.nextFloat() - 1);
            double offsetZ = r * (2 * random.nextFloat() - 1);
            Vec3 pos = player.position().add(0,1,0);

            ((ServerLevel)player.level()).sendParticles(ParticleTypes.SQUID_INK, pos.x + offsetX, pos.y + offsetY,pos.z+offsetZ,
                    1,0, 0, 0, 0.1);
        }

        TargetingConditions targetingConditions = TargetingConditions.forNonCombat().range(64);

        List<Player> nearbyPlayers = player.level().getNearbyPlayers(targetingConditions,player,player.getBoundingBox().inflate(r));
        nearbyPlayers.forEach(player1 -> player1.addEffect(new MobEffectInstance(MobEffects.GLOWING,20 * 60)));

    }, true),
    FIRE_MIST_TOGGLE(DeathAbility.fire, player -> {
        PlayerDuck playerDuck = PlayerDuck.of(player);
        playerDuck.toggleFireMist();
    }, true),
    PICKUP_MOB(DeathAbility.fire,player -> {

        if (player.getPassengers().isEmpty()) {
            DeathAbilities.rayTrace(player, 5).ifPresent(entity -> {
                entity.startRiding(player);
                Services.PLATFORM.sendToClient(new S2CPickupMobPacket(entity), player);
            });
        } else {
            Entity passenger = player.getFirstPassenger();
            passenger.stopRiding();
            passenger.addDeltaMovement(player.getLookAngle());
            if (passenger instanceof LivingEntity livingPassenger) {
                LivingEntityDuck livingEntityDuck = LivingEntityDuck.of(livingPassenger);
                livingEntityDuck.setExplodeOnImpact(true);
                Services.PLATFORM.sendToClient(new S2CDropMobPacket(),player);
            }
        }
    }, true),
    BLAZING_TRAIL_TOGGLE(DeathAbility.fire, player -> {
        PlayerDuck playerDuck = PlayerDuck.of(player);

        boolean wasActive = playerDuck.isFireMist();

        playerDuck.toggleFireMist();
        if (wasActive) {
            Services.PLATFORM.resetScale(player);
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        } else {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1,false,false));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,-1,0,false,false));
            Services.PLATFORM.scalePlayer(player, 2 / 3f);
        }

    },false)
    ;

    public final DeathAbility ability;
    public final Consumer<ServerPlayer> activate;
    public final boolean hunter;

    KeyAction(DeathAbility ability, Consumer<ServerPlayer> activate,boolean hunter) {
        this.ability = ability;
        this.activate = activate;
        this.hunter = hunter;
    }
}
