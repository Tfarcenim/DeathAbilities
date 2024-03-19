package tfar.deathabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import tfar.deathabilities.init.ModItems;

import java.util.function.Consumer;

public enum DeathAbility {
    fire(DamageTypeTags.IS_FIRE, ModItems.FIRE_SCROLL,player -> {}),
    water(DamageTypeTags.IS_DROWNING, ModItems.WATER_SCROLL,player -> {player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING,-1,0,false,false));}),
    earth(ModTags.IS_SUFFOCATION, ModItems.EARTH_SCROLL,player -> {}),
    lightning(DamageTypeTags.IS_LIGHTNING, ModItems.LIGHTNING_SCROLL,player -> {});

    private final TagKey<DamageType> tagKey;
    private final Item icon;
    private final ResourceLocation body;
    private final ResourceLocation eyes;
    public final Consumer<ServerPlayer> onEnable;

    DeathAbility(TagKey<DamageType> tagKey, Item icon, Consumer<ServerPlayer> onEnable) {
        this.tagKey = tagKey;
        this.icon = icon;
        this.onEnable = onEnable;
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
