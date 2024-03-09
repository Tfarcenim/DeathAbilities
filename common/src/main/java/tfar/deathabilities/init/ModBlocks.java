package tfar.deathabilities.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tfar.deathabilities.block.QuickSandBlock;

public class ModBlocks {

    public static final Block QUICKSAND = new QuickSandBlock(BlockBehaviour.Properties.copy(Blocks.SAND).isSuffocating((blockState, blockGetter, blockPos) -> true));

}
