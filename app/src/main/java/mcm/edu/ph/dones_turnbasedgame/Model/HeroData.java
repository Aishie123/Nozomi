package mcm.edu.ph.dones_turnbasedgame.Model;

@SuppressWarnings("FieldCanBeLocal")
public class HeroData extends GameData {

    private int courage, confidence, hope, love;
    private String name;
    private int healthPt, manaPt, atkMin, atkMax, lvl;

    public HeroData(){}

    public HeroData(String name, int lvl, int hp, int mp, int cou, int con, int hope, int love, int atkMin, int atkMax){

        this.name = name;
        this.lvl = lvl;
        this.healthPt = hp;
        this.manaPt = mp;
        this.atkMin = atkMin;
        this.atkMax = atkMax;

        this.courage = cou;
        this.confidence = con;
        this.hope = hope;
        this.love = love;

        super.setName(name);
        super.setLvl(lvl);
        super.setHealthPt(hp);
        super.setManaPt(mp);
        super.setAtkMin(atkMin);
        super.setAtkMax(atkMax);

    }

    //GETTERS
    public int getCourage() { return courage; }
    public int getConfidence() { return confidence; }
    public int getLove() { return love;}

    //SETTERS
    public void setCourage(int courage) { this.courage = courage; }
    public void setConfidence(int confidence) { this.confidence = confidence; }
    public void setLove(int love) { this.love = love; }


    @Override
    public int getHealthPt() {
        this.healthPt = super.getHealthPt();
        this.healthPt += (lvl * 25);
        return healthPt;
    }

    @Override
    public int getManaPt() {
        this.manaPt = super.getManaPt();
        this.manaPt += (lvl * 10);
        return manaPt;
    }

    @Override
    public int getAtkMin() {
        this.atkMin = super.getAtkMin();
        this.atkMin += (hope * 2);
        return atkMin;
    }

    @Override
    public int getAtkMax() {
        this.atkMax = super.getAtkMax();
        this.atkMax += (courage * 2);
        return atkMax;
    }


}

