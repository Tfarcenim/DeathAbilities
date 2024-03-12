package tfar.deathabilities.entity.boss.phases;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import tfar.deathabilities.entity.boss.ElementalDragonEntity;

import javax.annotation.Nullable;

public class ElementalDragonTakeoffPhase extends AbstractElementalDragonPhaseInstance {
   private boolean firstTick;
   @Nullable
   private Path currentPath;
   @Nullable
   private Vec3 targetLocation;

   public ElementalDragonTakeoffPhase(ElementalDragonEntity pDragon) {
      super(pDragon);
   }

   /**
    * Gives the phase a chance to update its status.
    * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
    */
   @Override
   public void doServerTick() {
      if (!this.firstTick && this.currentPath != null) {
         BlockPos blockpos = this.dragon.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.dragon.getFightOrigin()));
         if (!blockpos.closerToCenterThan(this.dragon.position(), 10.0D)) {
            this.dragon.getPhaseManager().setPhase(ElementalDragonPhase.HOLDING_PATTERN);
         }
      } else {
         this.firstTick = false;
         this.findNewTarget();
      }

   }

   /**
    * Called when this phase is set to active
    */
   @Override
   public void begin() {
      this.firstTick = true;
      this.currentPath = null;
      this.targetLocation = null;
   }

   private void findNewTarget() {
      int i = this.dragon.findClosestNode();
      Vec3 vec3 = this.dragon.getHeadLookVector(1.0F);
      int j = this.dragon.findClosestNode(-vec3.x * 40.0D, 105.0D, -vec3.z * 40.0D);
      if (this.dragon.getDragonFight() != null && this.dragon.getDragonFight().getCrystalsAlive() > 0) {
         j %= 12;
         if (j < 0) {
            j += 12;
         }
      } else {
         j -= 12;
         j &= 7;
         j += 12;
      }

      this.currentPath = this.dragon.findPath(i, j, (Node)null);
      this.navigateToNextPathNode();
   }

   private void navigateToNextPathNode() {
      if (this.currentPath != null) {
         this.currentPath.advance();
         if (!this.currentPath.isDone()) {
            Vec3i vec3i = this.currentPath.getNextNodePos();
            this.currentPath.advance();

            double d0;
            do {
               d0 = (double)((float)vec3i.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
            } while(d0 < (double)vec3i.getY());

            this.targetLocation = new Vec3((double)vec3i.getX(), d0, (double)vec3i.getZ());
         }
      }

   }

   /**
    * Returns the location the dragon is flying toward
    */
   @Override
   @Nullable
   public Vec3 getFlyTargetLocation() {
      return this.targetLocation;
   }

   @Override
   public ElementalDragonPhase<ElementalDragonTakeoffPhase> getPhase() {
      return ElementalDragonPhase.TAKEOFF;
   }
}