package tfar.deathabilities.entity.boss.phases;

import net.minecraft.sounds.SoundEvents;
import tfar.deathabilities.entity.boss.ElementalDragon;

public class ElementalDragonSittingAttackingPhase extends AbstractElementalDragonSittingPhase {
   private static final int ROAR_DURATION = 40;
   private int attackingTicks;

   public ElementalDragonSittingAttackingPhase(ElementalDragon pDragon) {
      super(pDragon);
   }

   /**
    * Generates particle effects appropriate to the phase (or sometimes sounds).
    * Called by dragon's onLivingUpdate. Only used when worldObj.isRemote.
    */
   @Override
   public void doClientTick() {
      this.dragon.level().playLocalSound(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ(), SoundEvents.ENDER_DRAGON_GROWL, this.dragon.getSoundSource(), 2.5F, 0.8F + this.dragon.getRandom().nextFloat() * 0.3F, false);
   }

   /**
    * Gives the phase a chance to update its status.
    * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
    */
   @Override
   public void doServerTick() {
      if (this.attackingTicks++ >= ROAR_DURATION) {
         this.dragon.getPhaseManager().setPhase(ElementalDragonPhase.SITTING_FLAMING);
      }

   }

   /**
    * Called when this phase is set to active
    */
   @Override
   public void begin() {
      this.attackingTicks = 0;
   }

   @Override
   public ElementalDragonPhase<ElementalDragonSittingAttackingPhase> getPhase() {
      return ElementalDragonPhase.SITTING_ATTACKING;
   }
}