package tfar.deathabilities.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import tfar.deathabilities.item.BeefoSummonerItem;
import tfar.deathabilities.item.LightningBoltItem;
import tfar.deathabilities.item.QuickSandBombItem;
import tfar.deathabilities.item.WaterWandItem;

public class ModItems {

    public static final Item WATER_WAND = new WaterWandItem(new Item.Properties());
    public static final Item QUICKSAND_BOMB = new QuickSandBombItem(new Item.Properties());
    public static final Item QUICKSAND = new BlockItem(ModBlocks.QUICKSAND,new Item.Properties());
    public static final Item EARTH_SCROLL = new Item(new Item.Properties());
    public static final Item FIRE_SCROLL = new Item(new Item.Properties());
    public static final Item LIGHTNING_SCROLL = new Item(new Item.Properties());
    public static final Item WATER_SCROLL = new Item(new Item.Properties());
    public static final Item LIGHTNING_BOLT = new LightningBoltItem(new Item.Properties());
    public static final Item BEEFO_SUMMONER = new BeefoSummonerItem(new Item.Properties());

}
