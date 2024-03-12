package tfar.deathabilities.entity.boss.phases;

import net.minecraft.world.phys.Vec3;
import tfar.deathabilities.entity.boss.ElementalDragon;

import javax.annotation.Nullable;

public class ElementalDragonHoverPhase extends AbstractElementalDragonPhaseInstance {
   @Nullable
   private Vec3 targetLocation;

   public ElementalDragonHoverPhase(ElementalDragon pDragon) {
      super(pDragon);
   }

   /**
    * Gives the phase a chance to update its status.
    * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
    */
   @Override
   public void doServerTick() {
      if (this.targetLocation == null) {
         this.targetLocation = this.dragon.position();
      }

   }

   @Override
   public boolean isSitting() {
      return true;
   }

   /**
    * Called when this phase is set to active
    */
   @Override
   public void begin() {
      this.targetLocation = null;
   }

   /**
    * Returns the maximum amount dragon may rise or fall during this phase
    */
   @Override
   public float getFlySpeed() {
      return 1.0F;
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
   public ElementalDragonPhase<ElementalDragonHoverPhase> getPhase() {
      return ElementalDragonPhase.HOVERING;
   }
}