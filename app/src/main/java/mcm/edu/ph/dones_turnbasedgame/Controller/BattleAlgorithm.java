package mcm.edu.ph.dones_turnbasedgame.Controller;

import java.util.Random;

public class BattleAlgorithm {


        Random randomizer = new Random();

        public BattleAlgorithm() {}

        public int attack(int atkMin,int atkMax){
            return randomizer.nextInt(atkMax - atkMin) + atkMin;
        }

}