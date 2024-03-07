package tfar.deathabilities.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {


    public ModDamageTypeTagsProvider(PackOutput p_270719_, CompletableFuture<HolderLookup.Provider> p_270256_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_270719_, p_270256_, DeathAbilities.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ModTags.IS_SUFFOCATION).add(DamageTypes.IN_WALL);
    }
}
