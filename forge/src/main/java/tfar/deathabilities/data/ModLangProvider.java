package tfar.deathabilities.data;

import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.init.ModBlocks;
import tfar.deathabilities.init.ModEntityTypes;
import tfar.deathabilities.init.ModItems;

import java.util.function.Supplier;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, DeathAbilities.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addDefaultItem(() -> ModItems.EARTH_SCROLL);
        addDefaultItem(() -> ModItems.FIRE_SCROLL);
        addDefaultItem(() -> ModItems.LIGHTNING_SCROLL);
        addDefaultItem(() -> ModItems.WATER_SCROLL);
        addDefaultItem(() -> ModItems.QUICKSAND_BOMB);
        addDefaultItem(() -> ModItems.LIGHTNING_BOLT);
        addDefaultItem(() -> ModItems.WATER_WAND);
        addDefaultItem(() -> ModItems.COPPER_DOLPHIN);

        addDefaultBlock(() -> ModBlocks.QUICKSAND);

        addEntityType(() -> ModEntityTypes.DOLPHIN_WITH_LEGS,"Dolphin With Legs");
        addEntityType(() -> ModEntityTypes.QUICKSAND_BOMB,"Quicksand Bomb");
        addEntityType(() -> ModEntityTypes.LIGHTNING_VEX,"Lightning Vex");
        addEntityType(() -> ModEntityTypes.SANDFISH,"Sandfish");
        addEntityType(() -> ModEntityTypes.FIRE_DRAGON_FIREBALL,"Fire Dragon Fireball");

    }


    protected void addTab(CreativeModeTab tab, String translation) {
        Component display  = tab.getDisplayName();
        ComponentContents contents = display.getContents();
        if (contents instanceof TranslatableContents translatableContents) {
            add(translatableContents.getKey(), translation);
        } else {
            throw new RuntimeException("Not translatable: "+tab);
        }
    }

    protected void addDefaultItem(Supplier<? extends Item> supplier) {
        addItem(supplier,getNameFromItem(supplier.get()));
    }

    protected void addDefaultBlock(Supplier<? extends Block> supplier) {
        addBlock(supplier,getNameFromBlock(supplier.get()));
    }

    protected void addDefaultEnchantment(Supplier<? extends Enchantment> supplier) {
        addEnchantment(supplier,getNameFromEnchantment(supplier.get()));
    }

    public static String getNameFromItem(Item item) {
        return StringUtils.capitaliseAllWords(item.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromBlock(Block block) {
        return StringUtils.capitaliseAllWords(block.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromEnchantment(Enchantment enchantment) {
        return StringUtils.capitaliseAllWords(enchantment.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

}
