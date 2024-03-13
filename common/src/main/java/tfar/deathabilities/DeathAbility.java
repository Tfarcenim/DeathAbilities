package tfar.deathabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import tfar.deathabilities.init.ModItems;

public enum DeathAbility {
    water(DamageTypeTags.IS_DROWNING, ModItems.WATER_SCROLL),
    earth(ModTags.IS_SUFFOCATION, ModItems.EARTH_SCROLL),
    fire(DamageTypeTags.IS_FIRE, ModItems.FIRE_SCROLL),
    lightning(DamageTypeTags.IS_LIGHTNING, ModItems.LIGHTNING_SCROLL);

    private final TagKey<DamageType> tagKey;
    private final Item icon;
    private final ResourceLocation body;
    private final ResourceLocation eyes;

    DeathAbility(TagKey<DamageType> tagKey, Item icon) {
        this.tagKey = tagKey;
        this.icon = icon;
        ResourceLocation dragonFolder = new ResourceLocation(DeathAbilities.MOD_ID,"textures/entity/elementaldragon/"+name()+"/");
        body = dragonFolder.withSuffix("body.png");
        eyes = dragonFolder.withSuffix("eyes.png");
    }

    public Item icon() {
        return icon;
    }

    public ResourceLocation getBody() {
        return body;
    }

    public ResourceLocation getEyes() {
        return eyes;
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
