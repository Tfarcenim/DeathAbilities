package tfar.deathabilities;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import tfar.deathabilities.init.ModItems;

public enum DeathInfo {
    WATER(DamageTypeTags.IS_DROWNING, ModItems.WATER_SCROLL),
    EARTH(ModTags.IS_SUFFOCATION, ModItems.EARTH_SCROLL),
    FIRE(DamageTypeTags.IS_FIRE, ModItems.FIRE_SCROLL),
    LIGHTNING(DamageTypeTags.IS_LIGHTNING, ModItems.LIGHTNING_SCROLL);

    private final TagKey<DamageType> tagKey;
    private final Item icon;

    DeathInfo(TagKey<DamageType> tagKey, Item icon) {
        this.tagKey = tagKey;
        this.icon = icon;
    }

    public Item icon() {
        return icon;
    }



    public static DeathInfo getDeathInfo(DamageSource source) {
        for (DeathInfo deathInfo : DeathInfo.values()) {
            if (source.is(deathInfo.tagKey)) {
                return deathInfo;
            }
        }
        return null;
    }

}
