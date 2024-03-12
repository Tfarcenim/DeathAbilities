package tfar.deathabilities.entity.boss.phases;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import tfar.deathabilities.entity.boss.ElementalDragon;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ElementalDragonPhase<T extends ElementalDragonPhaseInstance> {
    private static ElementalDragonPhase<?>[] phases = new ElementalDragonPhase[0];
    public static final ElementalDragonPhase<ElementalDragonHoldingPatternPhase> HOLDING_PATTERN = create(ElementalDragonHoldingPatternPhase.class, "HoldingPattern");
    public static final ElementalDragonPhase<ElementalDragonStrafePlayerPhase> STRAFE_PLAYER = create(ElementalDragonStrafePlayerPhase.class, "StrafePlayer");
    public static final ElementalDragonPhase<ElementalDragonLandingApproachPhase> LANDING_APPROACH = create(ElementalDragonLandingApproachPhase.class, "LandingApproach");
    public static final ElementalDragonPhase<ElementalDragonLandingPhase> LANDING = create(ElementalDragonLandingPhase.class, "Landing");
    public static final ElementalDragonPhase<ElementalDragonTakeoffPhase> TAKEOFF = create(ElementalDragonTakeoffPhase.class, "Takeoff");
    public static final ElementalDragonPhase<ElementalDragonSittingFlamingPhase> SITTING_FLAMING = create(ElementalDragonSittingFlamingPhase.class, "SittingFlaming");
    public static final ElementalDragonPhase<ElementalDragonSittingScanningPhase> SITTING_SCANNING = create(ElementalDragonSittingScanningPhase.class, "SittingScanning");
    public static final ElementalDragonPhase<ElementalDragonSittingAttackingPhase> SITTING_ATTACKING = create(ElementalDragonSittingAttackingPhase.class, "SittingAttacking");
    public static final ElementalDragonPhase<ElementalDragonChargePlayerPhase> CHARGING_PLAYER = create(ElementalDragonChargePlayerPhase.class, "ChargingPlayer");
    public static final ElementalDragonPhase<ElementalDragonDeathPhase> DYING = create(ElementalDragonDeathPhase.class, "Dying");
    public static final ElementalDragonPhase<ElementalDragonHoverPhase> HOVERING = create(ElementalDragonHoverPhase.class, "Hover");
    private final Class<? extends ElementalDragonPhaseInstance> instanceClass;
    private final int id;
    private final String name;

    private ElementalDragonPhase(int pId, Class<? extends ElementalDragonPhaseInstance> pInstanceClass, String pName) {
        this.id = pId;
        this.instanceClass = pInstanceClass;
        this.name = pName;
    }

    public ElementalDragonPhaseInstance createInstance(ElementalDragon pDragon) {
        try {
            Constructor<? extends ElementalDragonPhaseInstance> constructor = this.getConstructor();
            return constructor.newInstance(pDragon);
        } catch (Exception exception) {
            throw new Error(exception);
        }
    }

    protected Constructor<? extends ElementalDragonPhaseInstance> getConstructor() throws NoSuchMethodException {
        return this.instanceClass.getConstructor(EnderDragon.class);
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return this.name + " (#" + this.id + ")";
    }

    /**
     * Gets a phase by its ID. If the phase is out of bounds (negative or beyond the end of the phase array), returns
     * {@link #HOLDING_PATTERN}.
     */
    public static ElementalDragonPhase<?> getById(int pId) {
        return pId >= 0 && pId < phases.length ? phases[pId] : HOLDING_PATTERN;
    }

    public static int getCount() {
        return phases.length;
    }

    private static <T extends ElementalDragonPhaseInstance> ElementalDragonPhase<T> create(Class<T> pPhase, String pName) {
        ElementalDragonPhase<T> enderdragonphase = new ElementalDragonPhase<>(phases.length, pPhase, pName);
        phases = Arrays.copyOf(phases, phases.length + 1);
        phases[enderdragonphase.getId()] = enderdragonphase;
        return enderdragonphase;
    }
}