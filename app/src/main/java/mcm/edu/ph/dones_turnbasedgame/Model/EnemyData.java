package mcm.edu.ph.dones_turnbasedgame.Model;

public class EnemyData extends GameData{

    private String name;
    private int atkMin, atkMax, healthPt, manaPt;


    public EnemyData(){}

    public EnemyData(String name, int atkMin, int atkMax, int healthPt, int manaPt){
        this.name = name;
        this.atkMin = atkMin;
        this.atkMax = atkMax;
        this.healthPt = healthPt;
        this.manaPt = manaPt;

        super.name = name;
        super.atkMin = atkMin;
        super.atkMax = atkMax;
        super.healthPt = healthPt;
        super.manaPt = manaPt;
    }
}