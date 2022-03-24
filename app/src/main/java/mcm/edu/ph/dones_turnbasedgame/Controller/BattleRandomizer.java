package mcm.edu.ph.dones_turnbasedgame.Controller;

import java.util.Random;

public class BattleRandomizer {

        private final Random random = new Random();

        public BattleRandomizer() {}

        public int attack(int atkMin,int atkMax){
            return random.nextInt(atkMax - atkMin) + atkMin;
        }

    public int selectEnemy(){
        return random.nextInt(2);
    }

}
