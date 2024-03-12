package tfar.deathabilities;

import java.util.UUID;

public class HunterData {

    public static UUID runner;

    public static boolean isActive() {
        return runner != null;
    }


}
