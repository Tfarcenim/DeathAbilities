package tfar.deathabilities.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import tfar.deathabilities.init.ModBlocks;
import tfar.deathabilities.init.ModEntityTypes;
import tfar.deathabilities.init.ModItems;

public class QuickSandBombEntity extends ThrowableItemProjectile {
    public QuickSandBombEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public QuickSandBombEntity(double $$1, double $$2, double $$3, Level $$4) {
        super(ModEntityTypes.QUICKSAND_BOMB, $$1, $$2, $$3, $$4);
    }

    public QuickSandBombEntity(LivingEntity $$1, Level $$2) {
        super(ModEntityTypes.QUICKSAND_BOMB, $$1, $$2);
    }


    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);

        if (!level().isClientSide) {
            int r = 2;
            BlockPos pos = hitResult.getBlockPos();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (int y= -r;y < r+1;y++) {
                for (int z= -r;z < r+1;z++) {
                    for (int x= -r;x < r+1;x++) {
                        mutable.set(pos.getX() +x,pos.getY() +y,pos.getZ() +z);
                        BlockState state = level().getBlockState(mutable);
                        if (!state.isAir() && !state.is(BlockTags.WITHER_IMMUNE) && !state.hasBlockEntity()) {
                            level().setBlock(mutable, ModBlocks.QUICKSAND.defaultBlockState(),3);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.QUICKSAND_BOMB;
    }
}
