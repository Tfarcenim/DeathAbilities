package tfar.deathabilities.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.entity.PartEntity;

import javax.annotation.Nullable;

public class ElementalDragonPart extends PartEntity<ElementalDragonEntity> {
    public final ElementalDragonEntity parentMob;
    public final String name;
    private final EntityDimensions size;

    public ElementalDragonPart(ElementalDragonEntity pParentMob, String pName, float pWidth, float pHeight) {
        super(pParentMob);
        this.size = EntityDimensions.scalable(pWidth, pHeight);
        this.refreshDimensions();
        this.parentMob = pParentMob;
        this.name = pName;
    }

    protected void defineSynchedData() {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    /**
     * Returns {@code true} if other Entities should be prevented from moving through this Entity.
     */
    public boolean isPickable() {
        return true;
    }

    @Nullable
    public ItemStack getPickResult() {
        return this.parentMob.getPickResult();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean hurt(DamageSource pSource, float pAmount) {
        return this.isInvulnerableTo(pSource) ? false : this.parentMob.hurt(this, pSource, pAmount);
    }

    /**
     * Returns {@code true} if Entity argument is equal to this Entity
     */
    public boolean is(Entity pEntity) {
        return this == pEntity || this.parentMob == pEntity;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    public EntityDimensions getDimensions(Pose pPose) {
        return this.size;
    }

    public boolean shouldBeSaved() {
        return false;
    }
}
