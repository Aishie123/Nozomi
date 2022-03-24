package mcm.edu.ph.dones_turnbasedgame.Controller;

import android.content.Context;
import java.util.Random;
import mcm.edu.ph.dones_turnbasedgame.R;

public class QuoteRandomizer {

    private final Random random = new Random();
    private final Context context;


    public QuoteRandomizer(Context context){
        this.context = context;
    }

    public int quoteCounter(){
        return random.nextInt(20);
    }

    public String quoteHeroAtk(int quoteCounter){

        String[] quoteArray_heroAtk = context.getResources().getStringArray(R.array.heroAtk);

        return quoteArray_heroAtk[quoteCounter];
    }

    public String quoteEnemyAtk(int quoteCounter){

        String[] quoteArray_enemyAtk = context.getResources().getStringArray(R.array.enemyAtk);

        return quoteArray_enemyAtk[quoteCounter];
    }

    public String quoteHeroSS(){

        int quoteCounter = random.nextInt(10);
        String[] quoteArray_heroSS = context.getResources().getStringArray(R.array.heroSS);
        String qHeroSS = quoteArray_heroSS[quoteCounter];

        return qHeroSS;
    }

}
