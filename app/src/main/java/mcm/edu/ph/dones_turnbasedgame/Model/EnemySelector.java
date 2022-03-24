package mcm.edu.ph.dones_turnbasedgame.Model;

import android.content.Context;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "UnnecessaryLocalVariable"})
public class EnemySelector extends GameData{

    private float run1Pos, atkPos;
    private int runDur, atkDur, hitDur, deathDur, num,
    idleSprite, runSprite, atkSprite, hitSprite, deathSprite;
    private final Context context;

    public EnemySelector(Context context){ this.context = context; } // default constructor

    // main data catching constructor
    public EnemySelector(float run1Pos, float atkPos, int runDur, int atkDur, int hitDur, int deathDur, int num,
                         int idleSprite, int runSprite, int atkSprite, int hitSprite, int deathSprite, Context context){

        this.run1Pos = run1Pos;
        this.atkPos = atkPos;
        this.runDur = runDur;
        this.atkDur = atkDur;
        this.hitDur = hitDur;
        this.deathDur = deathDur;
        this.num = num;
        this.context = context;

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
                -1000f
        };
        return run1Pos[num];
    }

    public float getAtkPos(int num) {
        float[] atkPos = new float[]{
                200f, // Enemy 1
                0f
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

    // Enemy Animation Sprites ----------------------------------------------------------------------------------------

    public int getIdleSprite(int num) {
        String[] uri = new String[]{
                "enemy1_idleanim", // if num = 1
                "enemy2_idleanim", // if num = 2
        };
        idleSprite = context.getResources().getIdentifier(uri[num], "drawable", context.getPackageName());
        return idleSprite;
    }

    public int getRunSprite(int num) {
        String[] uri = new String[]{
                "enemy1_runanim", // if num = 1
                "enemy2_runanim", // if num = 2
        };
        runSprite = context.getResources().getIdentifier(uri[num], "drawable", context.getPackageName());
        return runSprite;
    }

    public int getAtkSprite(int num) {
        String[] uri = new String[]{
                "enemy1_attackanim", // if num = 1
                "enemy2_attackanim", // if num = 2
        };
        atkSprite = context.getResources().getIdentifier(uri[num], "drawable", context.getPackageName());
        return atkSprite;
    }

    public int getHitSprite(int num) {
        String[] uri = new String[]{
                "enemy1_hitanim", // if num = 1
                "enemy2_hitanim", // if num = 2
        };
        hitSprite = context.getResources().getIdentifier(uri[num], "drawable", context.getPackageName());
        return hitSprite;
    }

    public int getDeathSprite(int num) {
        String[] uri = new String[]{
                "enemy1_deathanim", // if num = 1
                "enemy2_deathanim", // if num = 2
        };
        deathSprite = context.getResources().getIdentifier(uri[num], "drawable", context.getPackageName());
        return deathSprite;
    }

}
