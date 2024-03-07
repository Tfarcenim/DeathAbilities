package tfar.deathabilities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import tfar.deathabilities.init.ModEntityTypes;
import tfar.deathabilities.init.ModItems;
import tfar.deathabilities.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class DeathAbilities {

    public static final String MOD_ID = "deathabilities";
    public static final String MOD_NAME = "DeathAbilities";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        Services.PLATFORM.superRegister(ModItems.class, BuiltInRegistries.ITEM, Item.class);
        //Services.PLATFORM.superRegister(ModCreativeTabs.class, BuiltInRegistries.CREATIVE_MODE_TAB, CreativeModeTab.class);
        //Services.PLATFORM.superRegister(ModEnchantments.class, BuiltInRegistries.ENCHANTMENT, Enchantment.class);
        Services.PLATFORM.superRegister(ModEntityTypes.class, BuiltInRegistries.ENTITY_TYPE, EntityType.class);
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
//todo- can summon a laser dolphin that has legs and can walk/ run around
//	- i can ride them by right clicking them, and they are as fast as the fastest horses and is invincible?
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
//	- i have a water wand i can right click two points to fill in the space between with water (works like world edit tool with /fill but only with water)
//		- can place water in nether
//		- uses replace function when filling so it will replace blocks and things like lava with water
//	- have infinite breath under water
//
//===================================================================================================================
//
//Earth death = suffocate in gravel or sand (gnome)
//
//- can convert the ground under hunters into quicksand by throwing a "sand bomb"
//	- can just be an egg retextured into something else
//
//- sandfish
//	- silver fish retextured/ modeled to look like sand worms
//	- they pop out of the ground where im looking when i activate the power via keybind
//	- they start rapidly "eating" the ground under the feet of the hunters
//	- they gravitate towards hunters don't have to attack them but stay near them but can also attack them as long as they are destroying blocks under the hunters feet
//
//===================================================================================================================
//
//die to Fire or lava (immortal pheonix)
//	- turn into a flying firey mist
//	- fire heals me now/ magma blocks/ lava
//	- my fire effects are permenant fire effects aka you can't put them out even with water
//
//- I can pick mobs ups and throw them and they explode in a giant firey explosion
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