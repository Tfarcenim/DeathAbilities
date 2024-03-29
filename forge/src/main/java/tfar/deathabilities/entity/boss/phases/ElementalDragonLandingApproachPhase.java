package tfar.deathabilities.entity.boss.phases;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import tfar.deathabilities.entity.boss.ElementalDragonEntity;

import javax.annotation.Nullable;

public class ElementalDragonLandingApproachPhase extends AbstractElementalDragonPhaseInstance {
   private static final TargetingConditions NEAR_EGG_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight();
   @Nullable
   private Path currentPath;
   @Nullable
   private Vec3 targetLocation;

   public ElementalDragonLandingApproachPhase(ElementalDragonEntity pDragon) {
      super(pDragon);
   }

   @Override
   public ElementalDragonPhase<ElementalDragonLandingApproachPhase> getPhase() {
      return ElementalDragonPhase.LANDING_APPROACH;
   }

   /**
    * Called when this phase is set to active
    */
   @Override
   public void begin() {
      this.currentPath = null;
      this.targetLocation = null;
   }

   /**
    * Gives the phase a chance to update its status.
    * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
    */
   @Override
   public void doServerTick() {
      double d0 = this.targetLocation == null ? 0.0D : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
      if (d0 < 100.0D || d0 > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
         this.findNewTarget();
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

   private void findNewTarget() {
      if (this.currentPath == null || this.currentPath.isDone()) {
         int i = this.dragon.findClosestNode();
         BlockPos blockpos = this.dragon.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.dragon.getFightOrigin()));
         Player player = this.dragon.level().getNearestPlayer(NEAR_EGG_TARGETING, this.dragon, blockpos.getX(), blockpos.getY(), blockpos.getZ());
         int j;
         if (player != null) {
            Vec3 vec3 = (new Vec3(player.getX(), 0.0D, player.getZ())).normalize();
            j = this.dragon.findClosestNode(-vec3.x * 40.0D, 105.0D, -vec3.z * 40.0D);
         } else {
            j = this.dragon.findClosestNode(40.0D, blockpos.getY(), 0.0D);
         }

         Node node = new Node(blockpos.getX(), blockpos.getY(), blockpos.getZ());
         this.currentPath = this.dragon.findPath(i, j, node);
         if (this.currentPath != null) {
            this.currentPath.advance();
         }
      }

      this.navigateToNextPathNode();
      if (this.currentPath != null && this.currentPath.isDone()) {
         this.dragon.getPhaseManager().setPhase(ElementalDragonPhase.LANDING);
      }

   }

   private void navigateToNextPathNode() {
      if (this.currentPath != null && !this.currentPath.isDone()) {
         Vec3i vec3i = this.currentPath.getNextNodePos();
         this.currentPath.advance();
         double d0 = vec3i.getX();
         double d1 = vec3i.getZ();

         double d2;
         do {
            d2 = (float)vec3i.getY() + this.dragon.getRandom().nextFloat() * 20.0F;
         } while(d2 < (double)vec3i.getY());

         this.targetLocation = new Vec3(d0, d2, d1);
      }

   }
}