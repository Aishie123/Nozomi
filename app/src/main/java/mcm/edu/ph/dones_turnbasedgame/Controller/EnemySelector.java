package mcm.edu.ph.dones_turnbasedgame.Controller;

import java.util.Random;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class EnemySelector {
    private float enemyRunPos, enemyAtkPos;
    private int enemyRunDur, enemyAtkDur, enemyHitDur, enemyDeathDur,
            enemyNumber;
    private final Random random = new Random();

    // default constructor
    public EnemySelector() {

        enemyNumber = enemyRandomizer();
        enemyRunPos = getEnemyRunPos();
        enemyAtkPos = getEnemyAtkPos();
        enemyRunDur = getEnemyRunDur();
        enemyAtkDur = getEnemyAtkDur();
        enemyHitDur = getEnemyHitDur();
        enemyDeathDur = getEnemyDeathDur();

    }

    public int enemyRandomizer(){
        return random.nextInt(2);
    }

    // enemy Positions ----------------------------------------------------------------------------------

    public float getEnemyRunPos() {
        float[] enemyRun1Pos = new float[]{
                -800f, // Enemy 1
                -800f
        };
        return enemyRun1Pos[enemyNumber];
    }

    public float getEnemyAtkPos() {
        float[] enemyAtkPos = new float[]{
                200f, // Enemy 1
                200f
        };
        return enemyAtkPos[enemyNumber];
    }

    // enemy Animation Durations ---------------------------------------------------------------------------

    public int getEnemyRunDur() {
        int[] enemyRunDur = new int[]{
                1600, // Enemy 1
                1600
        };
        return enemyRunDur[enemyNumber];
    }

    public int getEnemyAtkDur() {
        int[] enemyAtkDur = new int[]{
                300, // Enemy 1
                300
        };
        return enemyAtkDur[enemyNumber];

    }

    public int getEnemyHitDur() {
        int[] enemyHitDur = new int[]{
                600, // Enemy 1
                600
        };
        return enemyHitDur[enemyNumber];
    }

    public int getEnemyDeathDur() {
        int[] enemyDeathDur = new int[]{
                600, // Enemy 1
                600
        };
        return enemyDeathDur[enemyNumber];
    }
}
