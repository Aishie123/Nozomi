package mcm.edu.ph.nozomi.Model;

public class MonsterData extends GameData{

    String name;
    int atkMin, atkMax, healthPt, manaPt;


    public MonsterData (){}

    public MonsterData(String name, int atkMin, int atkMax, int healthPt, int manaPt){
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