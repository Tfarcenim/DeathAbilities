package tfar.deathabilities.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.data.tags.ModBlockTagProvider;
import tfar.deathabilities.data.tags.ModDamageTypeTagsProvider;
import tfar.deathabilities.data.tags.ModItemTagProvider;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class Datagen {

    public static void gather(GatherDataEvent event) {
        boolean client = event.includeClient();
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        dataGenerator.addProvider(client,new ModModelProvider(packOutput));
        dataGenerator.addProvider(client,new ModLangProvider(packOutput));

        ModBlockTagProvider blockTagProvider = new ModBlockTagProvider(packOutput,lookupProvider,helper);
        dataGenerator.addProvider(true,blockTagProvider);
        dataGenerator.addProvider(true,new ModItemTagProvider(packOutput,lookupProvider,blockTagProvider.contentsGetter(),helper));
        dataGenerator.addProvider(true,new ModDamageTypeTagsProvider(packOutput,lookupProvider,helper));
    }

    public static Stream<Block> getKnownBlocks() {
        return getKnown(BuiltInRegistries.BLOCK);
    }
    public static Stream<Item> getKnownItems() {
        return getKnown(BuiltInRegistries.ITEM);
    }

    public static <V> Stream<V> getKnown(Registry<V> registry) {
        return registry.stream().filter(o -> registry.getKey(o).getNamespace().equals(DeathAbilities.MOD_ID));
    }
}
