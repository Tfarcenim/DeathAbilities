package tfar.deathabilities.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

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


        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
