package tfar.deathabilities.entity.boss.phases;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.slf4j.Logger;
import tfar.deathabilities.entity.boss.ElementalDragon;

import javax.annotation.Nullable;

public class ElementalDragonPhaseManager {

    private static final Logger LOGGER = LogUtils.getLogger();
    private final ElementalDragon dragon;
    private final ElementalDragonPhaseInstance[] phases = new ElementalDragonPhaseInstance[ElementalDragonPhase.getCount()];
    @Nullable
    private ElementalDragonPhaseInstance currentPhase;

    public ElementalDragonPhaseManager(ElementalDragon $$0) {
        this.dragon = $$0;
        this.setPhase(ElementalDragonPhase.HOVERING);
    }

    public void setPhase(ElementalDragonPhase<?> $$0) {
        if (this.currentPhase == null || $$0 != this.currentPhase.getPhase()) {
            if (this.currentPhase != null) {
                this.currentPhase.end();
            }

            this.currentPhase = this.getPhase($$0);
            if (!this.dragon.level().isClientSide) {
                this.dragon.getEntityData().set(EnderDragon.DATA_PHASE, $$0.getId());
            }

            LOGGER.debug("Dragon is now in phase {} on the {}", $$0, this.dragon.level().isClientSide ? "client" : "server");
            this.currentPhase.begin();
        }
    }

    public ElementalDragonPhaseInstance getCurrentPhase() {
        return this.currentPhase;
    }

    public <T extends ElementalDragonPhaseInstance> T getPhase(ElementalDragonPhase<T> $$0) {
        int $$1 = $$0.getId();
        if (this.phases[$$1] == null) {
            this.phases[$$1] = $$0.createInstance(this.dragon);
        }

        return (T) this.phases[$$1];
    }

}
