package tfar.deathabilities.entity.boss.phases;

import com.mojang.logging.LogUtils;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import tfar.deathabilities.entity.boss.ElementalDragon;

import javax.annotation.Nullable;

public class ElementalDragonChargePlayerPhase extends AbstractElementalDragonPhaseInstance {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int CHARGE_RECOVERY_TIME = 10;
   @Nullable
   private Vec3 targetLocation;
   private int timeSinceCharge;

   public ElementalDragonChargePlayerPhase(ElementalDragon pDragon) {
      super(pDragon);
   }

   /**
    * Gives the phase a chance to update its status.
    * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
    */
   @Override
   public void doServerTick() {
      if (this.targetLocation == null) {
         LOGGER.warn("Aborting charge player as no target was set.");
         this.dragon.getPhaseManager().setPhase(ElementalDragonPhase.HOLDING_PATTERN);
      } else if (this.timeSinceCharge > 0 && this.timeSinceCharge++ >= 10) {
         this.dragon.getPhaseManager().setPhase(ElementalDragonPhase.HOLDING_PATTERN);
      } else {
         double d0 = this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
         if (d0 < 100.0D || d0 > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
            ++this.timeSinceCharge;
         }

      }
   }

   /**
    * Called when this phase is set to active
    */
   @Override
   public void begin() {
      this.targetLocation = null;
      this.timeSinceCharge = 0;
   }

   public void setTarget(Vec3 pTargetLocation) {
      this.targetLocation = pTargetLocation;
   }

   /**
    * Returns the maximum amount dragon may rise or fall during this phase
    */
   @Override
   public float getFlySpeed() {
      return 3.0F;
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
   public ElementalDragonPhase<ElementalDragonChargePlayerPhase> getPhase() {
      return ElementalDragonPhase.CHARGING_PLAYER;
   }
}