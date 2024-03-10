package tfar.deathabilities;

import net.minecraft.nbt.IntArrayTag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerDeathAbilities {

    private final Set<DeathAbility> enabled = new HashSet<>();


    public Set<DeathAbility> getEnabled() {
        return enabled;
    }

    public boolean enable(DeathAbility ability) {
        return enabled.add(ability);
    }
    public boolean isEnabled(DeathAbility ability) {
        return enabled.contains(ability);
    }

    public boolean disable(DeathAbility ability) {
        return enabled.remove(ability);
    }

    public Set<DeathAbility> getDisabled() {
        Set<DeathAbility> disabled = Arrays.stream(DeathAbility.values()).collect(Collectors.toSet());
        disabled.removeAll(enabled);
        return disabled;
    }

    public IntArrayTag serialize() {
        Set<Integer> present = new HashSet<>();
        enabled.forEach(deathInfo -> present.add(deathInfo.ordinal()));
        return new IntArrayTag(present.stream().toList());
    }

    public static PlayerDeathAbilities deserialize(int[] tag){
        int[] arr = tag;
        PlayerDeathAbilities playerDeathAbilities = new PlayerDeathAbilities();
        for (int i : arr) {
            playerDeathAbilities.enabled.add(DeathAbility.values()[i]);
        }
        return playerDeathAbilities;
    }

}
