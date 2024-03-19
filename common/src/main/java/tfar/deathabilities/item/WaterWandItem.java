package tfar.deathabilities.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaterWandItem extends Item {
    public WaterWandItem(Properties $$0) {
        super($$0);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        CompoundTag tag = stack.getTag();
        if (!level.isClientSide) {
            if (tag != null && tag.contains("first_pos")) {
                int[] ints = tag.getIntArray("first_pos");
                BlockPos firstPos = new BlockPos(ints[0], ints[1], ints[2]);
                long filledBlocks = Math.abs(pos.getX() - firstPos.getX() + 1L) * Math.abs(pos.getY() - firstPos.getY() + 1L)
                        * (Math.abs(pos.getZ() - firstPos.getZ() + 1L));
                if (filledBlocks > 1_000_000) {
                    if (player != null) {
                        player.sendSystemMessage(Component.literal("Attempted to fill " + filledBlocks + " blocks, aborting"));
                    }
                } else {
                    for (BlockPos pos1 : BlockPos.betweenClosed(pos, firstPos)) {
                        BlockState state = level.getBlockState(pos1);
                        if (state.getDestroySpeed(level, pos1) >= 0) {
                            level.setBlock(pos1, Blocks.WATER.defaultBlockState(), 3);
                        }
                    }
                    tag.remove("first_pos");
                }
            } else {
                stack.getOrCreateTag().putIntArray("first_pos",new int[]{pos.getX(),pos.getY(),pos.getZ()});

            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }


    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        super.appendHoverText($$0, $$1, $$2, $$3);
    }
}
