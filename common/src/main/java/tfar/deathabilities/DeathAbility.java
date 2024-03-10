package tfar.deathabilities;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import tfar.deathabilities.init.ModItems;

public enum DeathAbility {
    WATER(DamageTypeTags.IS_DROWNING, ModItems.WATER_SCROLL),
    EARTH(ModTags.IS_SUFFOCATION, ModItems.EARTH_SCROLL),
    FIRE(DamageTypeTags.IS_FIRE, ModItems.FIRE_SCROLL),
    LIGHTNING(DamageTypeTags.IS_LIGHTNING, ModItems.LIGHTNING_SCROLL);

    private final TagKey<DamageType> tagKey;
    private final Item icon;

    DeathAbility(TagKey<DamageType> tagKey, Item icon) {
        this.tagKey = tagKey;
        this.icon = icon;
    }

    public Item icon() {
        return icon;
    }



    public static DeathAbility getDeathInfo(DamageSource source) {
        for (DeathAbility deathAbility : DeathAbility.values()) {
            if (source.is(deathAbility.tagKey)) {
                return deathAbility;
            }
        }
        return null;
    }

}
