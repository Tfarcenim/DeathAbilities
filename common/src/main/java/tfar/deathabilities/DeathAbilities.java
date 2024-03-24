package tfar.deathabilities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.deathabilities.ducks.*;
import tfar.deathabilities.init.ModBlocks;
import tfar.deathabilities.init.ModEntityTypes;
import tfar.deathabilities.init.ModItems;
import tfar.deathabilities.init.ModMobEffects;
import tfar.deathabilities.platform.Services;

import java.util.List;
import java.util.Optional;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class DeathAbilities {

    public static final String MOD_ID = "deathabilities";
    public static final String MOD_NAME = "DeathAbilities";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static boolean enable_dragon = true;
    public static int damage_per_stage = 25;

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        Services.PLATFORM.superRegister(ModBlocks.class,BuiltInRegistries.BLOCK, Block.class);
        Services.PLATFORM.superRegister(ModItems.class, BuiltInRegistries.ITEM, Item.class);
        Services.PLATFORM.superRegister(ModMobEffects.class, BuiltInRegistries.MOB_EFFECT, MobEffect.class);
        //Services.PLATFORM.superRegister(ModCreativeTabs.class, BuiltInRegistries.CREATIVE_MODE_TAB, CreativeModeTab.class);
        //Services.PLATFORM.superRegister(ModEnchantments.class, BuiltInRegistries.ENCHANTMENT, Enchantment.class);
        Services.PLATFORM.superRegister(ModEntityTypes.class, BuiltInRegistries.ENTITY_TYPE, EntityType.class);
    }

    public static void onTrack(ServerPlayer player,Entity entity) {
        if (entity instanceof ClientSyncable clientSyncable) {
            clientSyncable.syncToTracking();//could sync to only this player instead of everyone
        }
    }

    public static void onClone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean wasDeath) {
        PlayerDuck playerDuck1 = PlayerDuck.of(oldPlayer);
        PlayerDuck playerDuck2 = PlayerDuck.of(newPlayer);

        playerDuck2.setDeathAbilities(playerDuck1.getDeathAbilities());//copy abilities
        PlayerDeathAbilities playerDeathAbilities = playerDuck2.getDeathAbilities();
        for (DeathAbility deathAbility : playerDeathAbilities.getEnabled()) {
            deathAbility.onEnable.accept(newPlayer);
        }
    }

    public static boolean onAttack(DamageSource source, LivingEntity living, float damage) {
        if (living instanceof ServerPlayer player && source.is(DamageTypeTags.IS_FIRE)) {
            PlayerDuck playerDuck = PlayerDuck.of(player);
            if (playerDuck.isFireMist()) {
                if (player.getUUID().equals(HunterData.runner)) {
                    player.heal(damage);
                }
                return true;
            }
        }
        return false;
    }

    public static void onDamage(DamageSource source, LivingEntity living, float damage) {
        Entity attacker = source.getEntity();
        if (attacker instanceof Player player) {
            PlayerDuck playerDuck = PlayerDuck.of(player);
            PlayerDeathAbilities playerDeathAbilities = playerDuck.getDeathAbilities();
            if (playerDeathAbilities.isEnabled(DeathAbility.lightning) && player.getMainHandItem().is(Items.LIGHTNING_ROD)) {
                if (living instanceof Mob mob) {
                    MobEntityDuck.of(mob).setTargetHunters(true);
                    if (mob.getTarget() == player) {
                        mob.setTarget(null);
                    }
                }
            } else if (playerDeathAbilities.isEnabled(DeathAbility.fire)) {
                if (!player.getUUID().equals(HunterData.runner)) {
                    living.setSecondsOnFire(5);
                }
            }
        }

        if (living instanceof EnderDragon enderDragon) {
            EnderDragonDuck enderDragonDuck = EnderDragonDuck.of(enderDragon);
            enderDragonDuck.addDamage(damage);
        }
    }

    public static boolean onDeath(DamageSource source, LivingEntity living) {
        if (living instanceof Player player) {
            DeathAbility deathAbility = DeathAbility.getDeathInfo(source);
            if (deathAbility != null) {
                DeathAbilitiesCommands.enableAbility((ServerPlayer) player, deathAbility);
                return true;
            }
        }
        return false;
    }

    public static void handleFireBreath(EnderDragon enderDragon, AreaEffectCloud flame) {
        if (enable_dragon) {
            EnderDragonDuck enderDragonDuck = EnderDragonDuck.of(enderDragon);
            DeathAbility phase = enderDragonDuck.getPhase();

            AreaEffectCloudDuck areaEffectCloudDuck = AreaEffectCloudDuck.of(flame);
            areaEffectCloudDuck.setType(phase);

            switch (phase) {
                case fire -> {
                    flame.addEffect(new MobEffectInstance(ModMobEffects.BURNING));
                    flame.setParticle(ParticleTypes.FLAME);
                }
                case lightning -> {
                    flame.setParticle(ParticleTypes.END_ROD);
                }
            }
        }
    }

    public static void cloudTick(AreaEffectCloud areaEffectCloud,DeathAbility deathAbility) {
        if (!areaEffectCloud.level().isClientSide) {
            switch (deathAbility) {
                case water -> {
                    if (areaEffectCloud.tickCount % 20 == 0) {
                        Guardian guardian = EntityType.GUARDIAN.spawn((ServerLevel) areaEffectCloud.level(), areaEffectCloud.blockPosition(), MobSpawnType.COMMAND);
                    }
                }
                case lightning -> {
                    if (areaEffectCloud.tickCount % 10 == 0) {
                        List<LivingEntity> entities = areaEffectCloud.level().getEntitiesOfClass(LivingEntity.class, areaEffectCloud.getBoundingBox());

                        Vec3 pos = areaEffectCloud.position();

                        if (!entities.isEmpty()) {
                            pos = entities.get(0).position();
                        }

                        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(areaEffectCloud.level());
                        lightningBolt.setPos(pos);
                        areaEffectCloud.level().addFreshEntity(lightningBolt);
                    }
                }
            }
        }
    }

    public static Optional<Entity> rayTrace(LivingEntity living,float distance) {
        if (living != null) {
            
            Vec3 vec3 = living.getEyePosition(0);
            double d1 = distance * distance;

            Vec3 vec31 = living.getViewVector(1.0F);
            Vec3 vec32 = vec3.add(vec31.x * distance, vec31.y * distance, vec31.z * distance);
            AABB aabb = living.getBoundingBox().expandTowards(vec31.scale(distance)).inflate(1.0D, 1.0D, 1.0D);
            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(living, vec3, vec32, aabb, (entity) -> {
                return !entity.isSpectator() && entity.isPickable();
            }, d1);
            return Optional.of(entityhitresult.getEntity());
        }
        return Optional.empty();
    }

    public static void onFall(LivingEntity living) {
        LivingEntityDuck livingEntityDuck = LivingEntityDuck.of(living);
        if (livingEntityDuck.explodeOnImpact()) {
            livingEntityDuck.setExplodeOnImpact(false);
            living.level().explode(living, living.getX(), living.getY(), living.getZ(), 3, true, Level.ExplosionInteraction.MOB);
        }
    }
}

//- just make it so all abilities are always unlocked i just have to set keybinds for each one
//
//- items ill give to myself through creative mode or commands
//
//- commands to enable which death quest is currently active so when i kill myself in that way i dont actually die
//
//- custom totem death animation when i die to the quest or hunter dies to their quest
//	- like this one https://youtu.be/tO9fV9jcS8Q?si=BT1vvjaT00GH4KnW&t=94
//
//===================================================================================================================
//
//die by drowning/ water (call of the ocean)
//
//can summon a laser dolphin that has legs and can walk/ run around

//todo	- i can ride them by right clicking them, and they are as fast as the fastest horses and is invincible?
//	- it shoots its laser at things infront has a 90 degree view angle so it isnt shooting behind it or anything
//		- only shoots players and mobs that are aggro'd at me so not sheep or even zombie pigman etc...
//	- they can swim really fast in water
//		- i can ride it in the water as well, when in water its like im swimming and not riding a boat
//		- doesn't give other players (hunters) dolphins grace
//	- this dolphin can survive on land doesnt need water to survive
//	- dolphin has same health as highest health horse and same run speed as fastest horse
//	- dolphin is summoned via an item in my inventory when placed the item goes on cooldown so when the dolphin dies i can resummon it just have to wait for cooldown to finish
//
//todo	- i can summon an army of squids in the water to attack for me
//		- when summoned they shoot out tons of ink (just make a cloud effect using squid ink the the region they get summoned)
//		- anything in the squid ink vacinity gets glowing effect for me so that i can see them (i dont get glowing effect if im in the ink tho)
//		- the squids also get dolphins grace/ speed 2 and auto aggro on hunters
//
//todo	- i have a water wand i can right click two points to fill in the space between with water (works like world edit tool with /fill but only with water)
//		- can place water in nether
//		- uses replace function when filling so it will replace blocks and things like lava with water
//	- have infinite breath under water
//
//===================================================================================================================
//
//todo Earth death = suffocate in gravel or sand (gnome)
//
//todo - can convert the ground under hunters into quicksand by throwing a "sand bomb"
//	- can just be an egg retextured into something else
//
//todo - sandfish
//	- silver fish retextured/ modeled to look like sand worms
//	- they pop out of the ground where im looking when i activate the power via keybind
//	- they start rapidly "eating" the ground under the feet of the hunters
//	- they gravitate towards hunters don't have to attack them but stay near them but can also attack them as long as they are destroying blocks under the hunters feet
//
//===================================================================================================================
//
//todo die to Fire or lava (immortal pheonix)
//	- turn into a flying firey mist
//	- fire heals me now/ magma blocks/ lava
//	- my fire effects are permenant fire effects aka you can't put them out even with water
//
// todo - I can pick mobs ups and throw them and they explode in a giant firey explosion
//
//===================================================================================================================
//
//die to Lightning = place a copper rod down and it will summon lightnight on you (zeus)
//
//- lightning bolt
//	- throw a lightning bolt (like a loyal trident)
//		- when it hits a block or entity it summons lightning vex on that spot (10 vex)
//			- lightning vex
//				- they fly out in a swarm targeting hunters and entities other then me (if used in end we dont have them aggro enderman)
//				- after 15 seconds they summon lightning onto them selves that strikes anyone nearby (within 10 blocks of their last position) and explode into thin air where ever they are at the momment
//
//- hitting entities with copper rods charges them up, and they then become angry at hunters and go attack them
//	- when these charged mobs hit hunters they strike lightning down on the hunters
//
//===================================================================================================================
//
//Hunter death quest/ ability:
//
//Hunters die to fire or lava (fire gremlins)
//
//	- key bind that activate blazing trail so when they run they leave a trail of fire behind them and have speed 2 for 5 seconds
//	- when they hit entities it sets them on fire
//	- they become 1.2 blocks tall (yes specifically 1.2 blocks, i want them small but not small enough to just easily walk into 1 block tall spaces its ok if they get into spaces by crouching at least they will be a bit slower doing it that way)
//	- immune to fire
//	- they have
//
//===================================================================================================================
//
//- elemental dragon fight (elemental dragon is just normal dragon with things retextured in the themes of the elements)
//	- needs lighting, fire, earth and water abilties
//		- just different dragons breath effects
//		- different texture for this dragon with those themes in mind
//	- dragons breath that summons lightning down where it is untill it disapates
//	- dragons breath that explodes into fire
//	- dragons breath that summons guardians where it lands
//	- dragons breath that explodes the ground all over (the physics mod where blocks can go flying on explosions?)