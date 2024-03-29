package tfar.deathabilities.entity.boss.phases;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.phys.Vec3;
import tfar.deathabilities.entity.boss.ElementalDragonEntity;

import javax.annotation.Nullable;

public class ElementalDragonLandingPhase extends AbstractElementalDragonPhaseInstance {
   @Nullable
   private Vec3 targetLocation;

   public ElementalDragonLandingPhase(ElementalDragonEntity pDragon) {
      super(pDragon);
   }

   /**
    * Generates particle effects appropriate to the phase (or sometimes sounds).
    * Called by dragon's onLivingUpdate. Only used when worldObj.isRemote.
    */
   @Override
   public void doClientTick() {
      Vec3 vec3 = this.dragon.getHeadLookVector(1.0F).normalize();
      vec3.yRot((-(float)Math.PI / 4F));
      double d0 = this.dragon.head.getX();
      double d1 = this.dragon.head.getY(0.5D);
      double d2 = this.dragon.head.getZ();

      for(int i = 0; i < 8; ++i) {
         RandomSource randomsource = this.dragon.getRandom();
         double d3 = d0 + randomsource.nextGaussian() / 2.0D;
         double d4 = d1 + randomsource.nextGaussian() / 2.0D;
         double d5 = d2 + randomsource.nextGaussian() / 2.0D;
         Vec3 vec31 = this.dragon.getDeltaMovement();
         this.dragon.level().addParticle(ParticleTypes.DRAGON_BREATH, d3, d4, d5, -vec3.x * (double)0.08F + vec31.x, -vec3.y * (double)0.3F + vec31.y, -vec3.z * (double)0.08F + vec31.z);
         vec3.yRot(0.19634955F);
      }

   }

   /**
    * Gives the phase a chance to update its status.
    * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
    */
   @Override
   public void doServerTick() {
      if (this.targetLocation == null) {
         this.targetLocation = Vec3.atBottomCenterOf(this.dragon.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.dragon.getFightOrigin())));
      }

      if (this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ()) < 1.0D) {
         this.dragon.getPhaseManager().getPhase(ElementalDragonPhase.SITTING_FLAMING).resetFlameCount();
         this.dragon.getPhaseManager().setPhase(ElementalDragonPhase.SITTING_SCANNING);
      }

   }

   /**
    * Returns the maximum amount dragon may rise or fall during this phase
    */
   @Override
   public float getFlySpeed() {
      return 1.5F;
   }

   @Override
   public float getTurnSpeed() {
      float f = (float)this.dragon.getDeltaMovement().horizontalDistance() + 1.0F;
      float f1 = Math.min(f, 40.0F);
      return f1 / f;
   }

   /**
    * Called when this phase is set to active
    */
   @Override
   public void begin() {
      this.targetLocation = null;
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
   public ElementalDragonPhase<ElementalDragonLandingPhase> getPhase() {
      return ElementalDragonPhase.LANDING;
   }
}