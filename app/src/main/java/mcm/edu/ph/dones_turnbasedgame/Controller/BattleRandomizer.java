package mcm.edu.ph.dones_turnbasedgame.Controller;

import java.util.Random;

public class BattleRandomizer {

        private final Random random = new Random();

        public BattleRandomizer() {}

        public int randomizeAttack(int atkMin, int atkMax){ return random.nextInt(atkMax - atkMin) + atkMin; }

        public int randomizeAtkSFX(){
        return random.nextInt(3);
    }

        public int randomizeEnemy(){
        return random.nextInt(2);
    }

}
