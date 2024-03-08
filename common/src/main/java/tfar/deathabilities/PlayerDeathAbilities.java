package tfar.deathabilities;

import net.minecraft.nbt.IntArrayTag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerDeathAbilities {

    private final Set<DeathInfo> enabled = new HashSet<>();


    public Set<DeathInfo> getEnabled() {
        return enabled;
    }

    public boolean enable(DeathInfo ability) {
        return enabled.add(ability);
    }

    public boolean disable(DeathInfo ability) {
        return enabled.remove(ability);
    }

    public Set<DeathInfo> getDisabled() {
        Set<DeathInfo> disabled = Arrays.stream(DeathInfo.values()).collect(Collectors.toSet());
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
            playerDeathAbilities.enabled.add(DeathInfo.values()[i]);
        }
        return playerDeathAbilities;
    }

}
