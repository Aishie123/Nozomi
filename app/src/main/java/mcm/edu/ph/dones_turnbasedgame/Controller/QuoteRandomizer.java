package mcm.edu.ph.dones_turnbasedgame.Controller;

import android.content.Context;
import java.util.Random;
import mcm.edu.ph.dones_turnbasedgame.R;

public class QuoteRandomizer {

    private final Random random = new Random();
    private final Context context;
    String qHeroAtk, qEnemyAtk, qHeroSS;


    public QuoteRandomizer(Context current){
        this.context = current;
    }

    public int quoteCounter(){
        return random.nextInt(10);
    }

    public String quoteHeroAtk(int quoteCounter){

        String[] quoteArray_heroAtk = context.getResources().getStringArray(R.array.heroAtk);
        qHeroAtk = quoteArray_heroAtk[quoteCounter];

        return qHeroAtk;
    }

    public String quoteEnemyAtk(int quoteCounter){

        String[] quoteArray_enemyAtk = context.getResources().getStringArray(R.array.enemyAtk);
        qEnemyAtk = quoteArray_enemyAtk[quoteCounter];

        return qEnemyAtk;
    }

    public String quoteHeroSS(){

        int quoteCounter = random.nextInt(10);
        String[] quoteArray_enemyAtk = context.getResources().getStringArray(R.array.heroSS);
        qHeroSS = quoteArray_enemyAtk[quoteCounter];

        return qHeroSS;
    }

}
