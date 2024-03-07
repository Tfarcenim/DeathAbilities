package tfar.deathabilities;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class ModTags {

    public static final TagKey<DamageType> IS_SUFFOCATION = create("is_suffocation");

    private static TagKey<DamageType> create(String pName) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(DeathAbilities.MOD_ID,pName));
    }

}
