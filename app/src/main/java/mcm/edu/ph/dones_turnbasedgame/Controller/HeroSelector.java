package mcm.edu.ph.dones_turnbasedgame.Controller;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class HeroSelector {
    private float heroRun1Pos, heroRun2Pos, heroAtkPos, heroSS1Pos;
    private int heroRunDur, heroAtkDur, heroSS1Dur, heroSS2Dur, heroHitDur, heroDeathDur,
            heroNumber;

    // default constructor
    public HeroSelector() {

        heroRun1Pos = getHeroRun1Pos(heroNumber);
        heroRun2Pos = getHeroRun2Pos(heroNumber);
        heroAtkPos = getHeroAtkPos(heroNumber);
        heroSS1Pos = getHeroSS1Pos(heroNumber);

        heroRunDur = getHeroRunDur(heroNumber);
        heroAtkDur = getHeroAtkDur(heroNumber);
        heroSS1Dur = getHeroSS1Dur(heroNumber);
        heroSS2Dur = getHeroSS2Dur(heroNumber);
        heroHitDur = getHeroHitDur(heroNumber);
        heroDeathDur = getHeroDeathDur(heroNumber);

    }

    // Hero Positions ----------------------------------------------------------------------------------

    public float getHeroRun1Pos(int heroNumber) {
        float[] heroRun1Pos = new float[]{
                725f, //Hero 1
                0f
        };
        return heroRun1Pos[heroNumber];
    }

    public float getHeroRun2Pos(int heroNumber) {
        float[] heroRun2Pos = new float[]{
                600f, //Hero 1
                0f
        };
        return heroRun2Pos[heroNumber];
    }

    public float getHeroAtkPos(int heroNumber) {
        float[] heroAtkPos = new float[]{
                700f, //Hero 1
                0f
        };
        return heroAtkPos[heroNumber];
    }

    public float getHeroSS1Pos(int heroNumber) {
        float[] heroSS1Pos = new float[]{
                500f, //Hero 1
                0f
        };
        return heroSS1Pos[heroNumber];
    }

    // Hero Animation Durations ---------------------------------------------------------------------------

    public int getHeroRunDur(int heroNumber) {
        int[] heroRunDur = new int[]{
                800, //Hero 1
                0
        };
        return heroRunDur[heroNumber];
    }

    public int getHeroAtkDur(int heroNumber) {
        int[] heroAtkDur = new int[]{
                300, //Hero 1
                0
        };
        return heroAtkDur[heroNumber];

    }

    public int getHeroSS1Dur(int heroNumber) {
        int[] heroSS1Dur = new int[]{
                300, //Hero 1
                0
        };
        return heroSS1Dur[heroNumber];
    }

    public int getHeroSS2Dur(int heroNumber) {
        int[] heroSS2Dur = new int[]{
                600, //Hero 1
                0
        };
        return heroSS2Dur[heroNumber];
    }

    public int getHeroHitDur(int heroNumber) {
        int[] heroHitDur = new int[]{
                650, //Hero 1
                0
        };
        return heroHitDur[heroNumber];
    }

    public int getHeroDeathDur(int heroNumber) {
        int[] heroDeathDur = new int[]{
                1100, //Hero 1
                0
        };
        return heroDeathDur[heroNumber];
    }


}
