package mcm.edu.ph.dones_turnbasedgame.Model;

import java.util.Random;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class EnemySelector extends GameData{

    private float run1Pos, atkPos;
    private int runDur, atkDur, hitDur, deathDur,
            num;

    public EnemySelector(){} // default constructor

    // main data catching constructor
    public EnemySelector(float run1Pos, float atkPos, int runDur, int atkDur, int hitDur, int deathDur, int num){

        this.run1Pos = run1Pos;
        this.atkPos = atkPos;
        this.runDur = runDur;
        this.atkDur = atkDur;
        this.hitDur = hitDur;
        this.deathDur = deathDur;
        this.num = num;

        super.setRun1Pos(run1Pos);
        super.setAtkPos(atkPos);
        super.setRunDur(runDur);
        super.setAtkDur(atkDur);
        super.setHitDur(hitDur);
        super.setDeathDur(deathDur);
        super.setNum(num);

    }

    // Enemy Positions ----------------------------------------------------------------------------------

    public float getRun1Pos(int num) {
        float[] run1Pos = new float[]{
                -800f, // Enemy 1
                -800f
        };
        return run1Pos[num];
    }

    public float getAtkPos(int num) {
        float[] atkPos = new float[]{
                200f, // Enemy 1
                200f
        };
        return atkPos[num];
    }

    // Enemy Animation Durations ---------------------------------------------------------------------------

    public int getRunDur(int num) {
        int[] runDur = new int[]{
                1600, // Enemy 1
                1600
        };
        return runDur[num];
    }

    public int getAtkDur(int num) {
        int[] atkDur = new int[]{
                300, // Enemy 1
                300
        };
        return atkDur[num];
    }

    public int getHitDur(int num) {
        int[] hitDur = new int[]{
                600, // Enemy 1
                600
        };
        return hitDur[num];
    }

    public int getDeathDur(int num) {
        int[] deathDur = new int[]{
                600, // Enemy 1
                600
        };
        return deathDur[num];
    }

}
