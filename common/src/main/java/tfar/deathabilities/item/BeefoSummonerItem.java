package tfar.deathabilities.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import tfar.deathabilities.entity.DolphinWithLegsEntity;
import tfar.deathabilities.init.ModEntityTypes;

import java.util.Objects;

public class BeefoSummonerItem extends Item {
    public BeefoSummonerItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = pContext.getItemInHand();
            BlockPos blockpos = pContext.getClickedPos();
            Direction direction = pContext.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }

            EntityType<? extends DolphinWithLegsEntity> entitytype = ModEntityTypes.DOLPHIN_WITH_LEGS;
            DolphinWithLegsEntity dolphinWithLegsEntity = entitytype.spawn((ServerLevel)level, itemstack, pContext.getPlayer(), blockpos1, MobSpawnType.MOB_SUMMONED, true,
                    !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
            if (dolphinWithLegsEntity != null) {
                dolphinWithLegsEntity.setOwnerUUID(pContext.getPlayer().getUUID());
                level.gameEvent(pContext.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
                pContext.getPlayer().getCooldowns().addCooldown(this,300 * 20);
            }

            return InteractionResult.CONSUME;
        }
    }



}
