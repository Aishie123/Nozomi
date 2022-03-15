package mcm.edu.ph.dones_turnbasedgame.Controller;

import java.util.Random;

public class SfxRandomizer {

    private final Random random = new Random();

    public int randomizeAtkSFX(){
        return random.nextInt(3);
    }

}
