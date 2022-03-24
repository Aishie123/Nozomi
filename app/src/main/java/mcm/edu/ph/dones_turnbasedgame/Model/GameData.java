package mcm.edu.ph.dones_turnbasedgame.Model;

public class GameData {

        int healthPt, manaPt, atkMin, atkMax, lvl, run1Dur, atkDur, hitDur, deathDur, num;
        String name;
        float atkPos, run1Pos;

        public GameData(){}

        //SETTERS
        public void setHealthPt(int healthPt) { this.healthPt = healthPt; }
        public void setManaPt(int manaPt) { this.manaPt = manaPt; }
        public void setName(String name) { this.name = name; }
        public void setAtkMin(int atkMin) { this.atkMin = atkMin; }
        public void setAtkMax(int atkMax) { this.atkMax = atkMax; }
        public void setLvl(int lvl) { this.lvl = lvl; }

        public void setNum(int num) { this.num = num; }
        public void setRun1Pos(float run1Pos) { this.run1Pos = run1Pos; }
        public void setAtkPos(float atkPos) { this.atkPos = atkPos; }
        public void setRunDur(int runDur) { this.run1Dur = runDur; }
        public void setAtkDur(int atkDur) { this.atkDur = atkDur; }
        public void setHitDur(int hitDur) { this.hitDur = hitDur; }
        public void setDeathDur(int deathDur) { this.deathDur = deathDur; }

        //GETTERS
        public int getHealthPt() { return healthPt; }
        public int getManaPt() { return manaPt; }
        public String getName() { return name; }
        public int getAtkMin() { return atkMin; }
        public int getAtkMax() { return atkMax; }
        public int getLvl() { return lvl; }

        public int getNum() { return num; }
        public float getRun1Pos() { return run1Pos; }
        public float getAtkPos() { return atkPos; }
        public int getRunDur() { return run1Dur; }
        public int getAtkDur() { return atkDur; }
        public int getHitDur() { return hitDur; }
        public int getDeathDur() { return deathDur; }

}

