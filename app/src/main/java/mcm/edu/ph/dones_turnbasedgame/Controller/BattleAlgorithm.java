package mcm.edu.ph.dones_turnbasedgame.Controller;

import java.util.Random;

public class BattleAlgorithm {

        private final Random random = new Random();

        public BattleAlgorithm() {}

        public int attack(int atkMin,int atkMax){
            return random.nextInt(atkMax - atkMin) + atkMin;
        }

}
