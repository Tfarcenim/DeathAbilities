package tfar.deathabilities.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class QuickSandBlock extends Block {


    private static final float HORIZONTAL_PARTICLE_MOMENTUM_FACTOR = 1/12F;
    private static final float IN_BLOCK_HORIZONTAL_SPEED_MULTIPLIER = 0.9F;
    private static final float IN_BLOCK_VERTICAL_SPEED_MULTIPLIER = 1.5F;
    private static final float NUM_BLOCKS_TO_FALL_INTO_BLOCK = 2.5F;
    private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, .9, 1.0);
    private static final double MINIMUM_FALL_DISTANCE_FOR_SOUND = 4.0;
    private static final double MINIMUM_FALL_DISTANCE_FOR_BIG_SOUND = 7.0;
    public QuickSandBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean skipRendering(BlockState $$0, BlockState $$1, Direction $$2) {
        return $$1.is(this) || super.skipRendering($$0, $$1, $$2);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return Shapes.empty();
    }

    @Override
    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        if (!($$3 instanceof LivingEntity) || $$3.getFeetBlockState().is(this)) {
            $$3.makeStuckInBlock($$0, new Vec3(.9, 1.5, .9));
            if ($$1.isClientSide) {
                RandomSource $$4 = $$1.getRandom();
                boolean $$5 = $$3.xOld != $$3.getX() || $$3.zOld != $$3.getZ();
                if ($$5 && $$4.nextBoolean()) {
                    $$1.addParticle(ParticleTypes.SNOWFLAKE, $$3.getX(), $$2.getY() + 1, $$3.getZ(), Mth.randomBetween($$4, -1.0F, 1.0F) * HORIZONTAL_PARTICLE_MOMENTUM_FACTOR,
                            0.05000000074505806, Mth.randomBetween($$4, -1.0F, 1.0F) * HORIZONTAL_PARTICLE_MOMENTUM_FACTOR);
                }
            }
        }

        //$$3.setIsInPowderSnow(true);
    //    if (!$$1.isClientSide) {
       //     if ($$3.isOnFire() && ($$1.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || $$3 instanceof Player) && $$3.mayInteract($$1, $$2)) {
      //          $$1.destroyBlock($$2, false);
     //       }

      //      $$3.setSharedFlagOnFire(false);
     //   }

    }

    @Override
    public void fallOn(Level level, BlockState $$1, BlockPos $$2, Entity entity, float dist) {
        if (!(dist < MINIMUM_FALL_DISTANCE_FOR_SOUND) && entity instanceof LivingEntity living) {
            LivingEntity.Fallsounds $$7 = living.getFallSounds();
            SoundEvent $$8 = (double)dist < MINIMUM_FALL_DISTANCE_FOR_BIG_SOUND ? $$7.small() : $$7.big();
            entity.playSound($$8, 1.0F, 1.0F);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        if ($$3 instanceof EntityCollisionContext $$4) {
            Entity $$5 = $$4.getEntity();
            if ($$5 != null) {
                if ($$5.fallDistance > 2.5F) {
                    return FALLING_COLLISION_SHAPE;
                }

                boolean $$6 = $$5 instanceof FallingBlockEntity;
                if ($$6 || canEntityWalkOnQuickSand($$5) && $$3.isAbove(Shapes.block(), $$2, false) && !$$3.isDescending()) {
                    return super.getCollisionShape($$0, $$1, $$2, $$3);
                } else {
                    return Shapes.empty();
                }
            }
        }

        return super.getCollisionShape($$0, $$1, $$2, $$3);
    }

    @Override
    public VoxelShape getVisualShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return Shapes.empty();
    }

    public static boolean canEntityWalkOnQuickSand(Entity $$0) {
        if ($$0.getType().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
            return true;
        } else {
            return $$0 instanceof LivingEntity && ((LivingEntity) $$0).getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS);
        }
    }
    
    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return true;
    }
}
