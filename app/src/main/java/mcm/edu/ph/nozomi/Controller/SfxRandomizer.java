package mcm.edu.ph.nozomi.Controller;

import java.util.Random;

public class SfxRandomizer {

    private final Random random = new Random();

    public int randomizeAtkSFX(){
        return random.nextInt(3);
    }

}
