package mcm.edu.ph.dones_turnbasedgame.Model;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class HeroSelector extends GameData {

    private float run1Pos, run2Pos, atkPos;
    private int runDur, atkDur, SS1Dur, SS2Dur, hitDur, deathDur, num;
    
    public HeroSelector(){} // default constructor

    // main data catching constructor
    public HeroSelector(float run1Pos, float run2Pos, float atkPos,
                        int runDur, int atkDur, int SS1Dur, int SS2Dur, int hitDur, int deathDur, int num){

        this.run1Pos = run1Pos;
        this.atkPos = atkPos;
        this.runDur = runDur;
        this.atkDur = atkDur;
        this.hitDur = hitDur;
        this.deathDur = deathDur;
        this.num = num;
        
        this.run2Pos = run2Pos;
        this.SS1Dur = SS1Dur;
        this.SS2Dur = SS2Dur;

        super.setRun1Pos(run1Pos);
        super.setAtkPos(atkPos);
        super.setRunDur(runDur);
        super.setAtkDur(atkDur);
        super.setHitDur(hitDur);
        super.setDeathDur(deathDur);
        super.setNum(num);

    }

    // Hero Positions ----------------------------------------------------------------------------------

    public float getRun1Pos(int num) {
        float[] run1Pos = new float[]{
                725f, //Hero 1
                0f
        };
        return run1Pos[num];
    }

    public float getRun2Pos(int num) {
        float[] run2Pos = new float[]{
                600f, //Hero 1
                0f
        };
        return run2Pos[num];
    }

    public float getAtkPos(int num) {
        float[] atkPos = new float[]{
                700f, //Hero 1
                0f
        };
        return atkPos[num];
    }

    public float getSS1Pos(int num) {
        float[] SS1Pos = new float[]{
                500f, //Hero 1
                0f
        };
        return SS1Pos[num];
    }

    // Hero Animation Durations ---------------------------------------------------------------------------

    public int getRunDur(int num) {
        int[] runDur = new int[]{
                800, //Hero 1
                0
        };
        return runDur[num];
    }

    public int getAtkDur(int num) {
        int[] atkDur = new int[]{
                300, //Hero 1
                0
        };
        return atkDur[num];

    }

    public int getSS1Dur(int num) {
        int[] SS1Dur = new int[]{
                300, //Hero 1
                0
        };
        return SS1Dur[num];
    }

    public int getSS2Dur(int num) {
        int[] SS2Dur = new int[]{
                600, //Hero 1
                0
        };
        return SS2Dur[num];
    }

    public int getHitDur(int num) {
        int[] hitDur = new int[]{
                650, //Hero 1
                0
        };
        return hitDur[num];
    }

    public int getDeathDur(int num) {
        int[] deathDur = new int[]{
                1100, //Hero 1
                0
        };
        return deathDur[num];
    }

}
