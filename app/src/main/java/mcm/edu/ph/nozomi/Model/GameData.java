package mcm.edu.ph.nozomi.Model;

public class GameData {

        int healthPt;
        int manaPt;
        String name;
        int atkMin;
        int atkMax;
        int lvl;

        public GameData(){}

        //SETTERS
        public void setHealthPt(int healthPt) { this.healthPt = healthPt; }
        public void setManaPt(int manaPt) { this.manaPt = manaPt; }
        public void setName(String name) { this.name = name; }
        public void setAtkMin(int atkMin) { this.atkMin = atkMin; }
        public void setAtkMax(int atkMax) { this.atkMax = atkMax; }
        public void setLvl(int lvl) { this.lvl = lvl; }


        //GETTERS
        public int getHealthPt() { return healthPt; }
        public int getManaPt() { return manaPt; }
        public String getName() { return name; }
        public int getAtkMin() { return atkMin; }
        public int getAtkMax() { return atkMax; }
        public int getLvl() { return lvl; }


}

