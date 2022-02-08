package mcm.edu.ph.nozomi;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView enemy1_idleSprite, hero1_idleSprite, hero1_runSprite, hero1_attkSprite;
    ImageButton btnSS1, btnSS2, btnAttack, btnReset;
    TextView txtLog, txtBtn, txtReset, txtEnemy, txtUser, txtHeroHP, txtHeroMP, txtEnemyHP;
    View heroHPBar, heroMPBar, enemyHPBar;
    double heroHPB, heroMPB, enemyHPB; //for HP bar
    int fullHeroHP, curHeroHP, fullHeroMP, curHeroMP, fullEnemyHP, curEnemyHP;
    String TAG = "MainActivity";

    AnimationDrawable hero1_idleAnim, hero1_runAnim, hero1_attkAnim, enemy1_idleAnim;
    ObjectAnimator hero1Run, hero1Attk;

    HeroData hero = new HeroData();
    MonsterData enemy = new MonsterData();
    BattleAlgorithm battle = new BattleAlgorithm();

    int counter = 0;
    int SS1C = 8;
    int SS2C = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_main);

        btnReset = findViewById(R.id.btnReset);
        btnAttack = findViewById(R.id.btnAttk);
        btnSS1 = findViewById(R.id.btnSS1);
        btnSS2 = findViewById(R.id.btnSS2);

        txtReset = findViewById(R.id.txtReset);
        txtBtn = findViewById(R.id.txtAttack);
        txtEnemy = findViewById(R.id.txtEnemy);
        txtUser = findViewById(R.id.txtUser);
        txtLog = findViewById(R.id.txtCombatLog);
        txtEnemyHP = findViewById(R.id.txtEnemyHP);
        txtHeroHP = findViewById(R.id.txtHeroHP);
        txtHeroMP = findViewById(R.id.txtHeroMP);

        heroHPBar = findViewById(R.id.heroHPBar);
        heroMPBar = findViewById(R.id.heroMPBar);
        enemyHPBar = findViewById(R.id.enemyHPBar);

        enemy1_idleSprite = findViewById(R.id.enemy1_idleSprite);
        hero1_idleSprite = findViewById(R.id.hero1_idleSprite);
        hero1_runSprite = findViewById(R.id.hero1_runSprite);
        hero1_attkSprite = findViewById(R.id.hero1_attkSprite);

        Intent intent = getIntent();
        String enemyName = intent.getExtras().getString("enemy");
        String userName = intent.getExtras().getString("user");


        hero = new HeroData(userName, 1, 200, 15, 10, 7, 5, 4, 15, 20);
        fullHeroHP = hero.getHealthPt();
        curHeroHP = fullHeroHP;
        fullHeroMP = hero.getManaPt();
        curHeroMP = fullHeroMP;

        enemy = new MonsterData(enemyName, 10, 40, 400, 20 );
        fullEnemyHP = enemy.getHealthPt();
        curEnemyHP = fullEnemyHP;

        txtEnemy.setText(enemyName);
        txtUser.setText(userName);
        txtHeroHP.setText(String.valueOf(curHeroHP));
        txtHeroMP.setText(String.valueOf(curHeroMP));
        txtEnemyHP.setText(String.valueOf(curEnemyHP));

        enemy1_idleSprite.setImageResource(R.drawable.enemy1_idleanim);
        hero1_idleSprite.setImageResource(R.drawable.hero1_idleanim);
        hero1_runSprite.setImageResource(R.drawable.hero1_runanim);
        hero1_attkSprite.setImageResource(R.drawable.hero1_attackanim);

        btnAttack.setOnClickListener(this);
        btnSS1.setOnClickListener(this);
        btnSS2.setOnClickListener(this);

        hero1_idleSprite.setX(90f);
        hero1_runSprite.setX(90f);
        hero1_attkSprite.setX(800f);

        heroIdleSprite();
        heroHPBar();
        heroMPBar();
        enemyHPBar();
        press();
    }

    //idle sprites of characters
    public void heroIdleSprite(){
        enemy1_idleAnim = (AnimationDrawable)enemy1_idleSprite.getDrawable();
        enemy1_idleAnim.start();

        hero1_idleAnim = (AnimationDrawable)hero1_idleSprite.getDrawable();
        hero1_idleAnim.start();
    }

    //adjusting Hero HP Bar
    public void heroHPBar(){
        heroHPB = ((double) curHeroHP / (double) fullHeroHP) * 10000;
        heroHPBar.getBackground().setLevel((int) heroHPB);
        txtHeroHP.setText(String.valueOf(curHeroHP));
        Log.d(TAG, "User's current HP is " + curHeroHP);
    }

    //adjusting Hero MP Bar
    public void heroMPBar(){
        heroMPB = ((double) curHeroMP /(double) fullHeroMP) * 10000;
        heroMPBar.getBackground().setLevel((int) heroMPB);
        txtHeroMP.setText(String.valueOf(curHeroMP));
        Log.d(TAG, "User's current MP is " + curHeroMP);
    }

    //adjusting Enemy HP Bar
    public void enemyHPBar(){
        enemyHPB = ((double) curEnemyHP / (double) fullEnemyHP) * 10000;
        enemyHPBar.getBackground().setLevel((int) enemyHPB);
        txtEnemyHP.setText(String.valueOf(curEnemyHP));
        Log.d(TAG, "Enemy's current HP is " + curEnemyHP);
    }

    //changing button shades when pressed
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btnSS1.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnSS1.setImageAlpha(25);
                    Log.d(TAG, "btnSS1 pressed");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnSS1.setImageAlpha(255);
                    Log.d(TAG, "btnSS1 unpressed");
                }
                return false;
            }
        });

        btnSS2.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnSS2.setImageAlpha(25);
                    Log.d(TAG, "btnSS2 pressed");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnSS2.setImageAlpha(255);
                    Log.d(TAG, "btnSS2 unpressed");
                }
                return false;
            }
        });

        btnAttack.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnAttack.setImageResource(R.drawable.btn_pressed);
                    Log.d(TAG, "btnAttack pressed");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnAttack.setImageResource(R.drawable.btn_unpressed);
                    Log.d(TAG, "btnAttack unpressed");
                }
                return false;
            }
        });

    }

    //BAWAL NEGATIVE HEALTH HNGGG
    public void negativeHealthCheck() {
        if (curEnemyHP < 0){
            curEnemyHP = 0;
        }

        if (curHeroHP < 0){
            curHeroHP = 0;
        }
    }

    public void heroRunSprite(){
        hero1_runSprite.setVisibility(View.VISIBLE);
        hero1_idleSprite.setVisibility(View.INVISIBLE);

        hero1_runAnim = (AnimationDrawable)hero1_runSprite.getDrawable();
        hero1Run = ObjectAnimator.ofFloat(hero1_runSprite,"translationX",800f);
        hero1Run.setDuration(800);

        hero1Run.start();
        hero1_runAnim.start();

        hero1Run.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                hero1_runSprite.setVisibility(View.INVISIBLE);
                hero1_runAnim.stop();
                hero1_runSprite.setX(90f);
            }
        });
    }

    public void heroAttkSprite(){
        hero1_attkSprite.setVisibility(View.VISIBLE);

        hero1_attkAnim = (AnimationDrawable)hero1_attkSprite.getDrawable();
        hero1Attk = ObjectAnimator.ofFloat(hero1_runSprite,"translationX",0f);
        hero1Attk.setDuration(700);

        hero1Attk.start();
        hero1_attkAnim.start();
    }

    //attacks + iskillz
    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    public void onClick(View v) {
        Random randomizer = new Random();
        int hero1AtkN = battle.attack(hero.getAtkMin(),hero.getAtkMax());
        int enemy1AtkN = battle.attack(enemy.getAtkMin(),enemy.getAtkMax());

        switch (v.getId()){
            case R.id.btnAttk:

                if (counter == 0) {
                    curHeroHP = fullHeroHP;
                    curEnemyHP = fullEnemyHP;
                    curHeroMP = fullHeroMP;
                    heroHPBar();
                    heroMPBar();
                    enemyHPBar();
                    txtBtn.setText("Attack");
                    txtLog.setText("Ready for Battle!");
                    counter++;
                }

                else if(counter%2 == 1){
                    btnSS1.setEnabled(false);
                    btnSS2.setEnabled(false);
                    txtBtn.setText("Next Turn");
                    heroRunSprite();

                    hero1Run.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            heroAttkSprite();

                            hero1Attk.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    curEnemyHP -= hero1AtkN;
                                    negativeHealthCheck();
                                    enemyHPBar();
                                    txtLog.setText(hero.getName() + " dealt "+ hero1AtkN + " damage to the enemy.");
                                    counter++;

                                    hero1_attkAnim.stop();
                                    hero1_attkSprite.setVisibility(View.INVISIBLE);
                                    hero1_idleSprite.setVisibility(View.VISIBLE);

                                    if(curEnemyHP <= 0){
                                        txtLog.setText(hero.getName() + " dealt "+ hero1AtkN + " damage to the enemy.\nYou won!");
                                        counter = 0;
                                        txtBtn.setText("Reset Game");

                                    }
                                }
                            });
                        }
                    });
                }

                else if(counter%2 !=1) {
                    btnSS1.setEnabled(true);
                    btnSS2.setEnabled(true);
                    txtBtn.setText("Attack");
                    curHeroHP -= enemy1AtkN;
                    txtLog.setText(enemy.getName() + " dealt " + enemy1AtkN + " damage to the hero.");
                    negativeHealthCheck();
                    heroHPBar();
                    counter++;

                    if (curHeroHP <= 0) {
                        txtLog.setText(enemy.getName() + " dealt " + enemy1AtkN + " damage to the hero.\nGame over!");
                        counter = 0;
                        txtBtn.setText("Reset Game");
                    }
                }
                break;

            case R.id.btnSS2:
                if ((curHeroMP-SS2C)<0){
                    txtLog.setText("Your MP is not enough for this skill.");
                }
                else{
                    if ((curHeroHP+(fullHeroHP*0.50) > fullHeroHP)){
                        curHeroHP = fullHeroHP;
                    }
                    txtLog.setText(hero.getName() + " used Healing Shield, and regained 50% health");
                    curHeroHP+=(fullHeroHP*0.50);
                    curHeroMP-=SS2C;
                    txtBtn.setText("Next Turn");
                    heroHPBar();
                    heroMPBar();
                    counter++;
                }
                break;

            case R.id.btnSS1:
                if ((curHeroMP-SS1C)<0){
                    txtLog.setText("Your MP is not enough for this skill.");
                }
                else{
                    curEnemyHP -= (hero1AtkN * 2);
                    txtLog.setText(hero.getName() + " used Double Sword, and dealt " + (hero1AtkN * 2) + " damage to the enemy.");
                    curHeroMP-=SS1C;
                    txtBtn.setText("Next Turn");
                    enemyHPBar();
                    heroMPBar();
                    counter++;

                    if(curEnemyHP <= 0){
                        txtLog.setText(hero.getName() + " used Double Sword, and dealt " + (hero1AtkN * 2) + " damage to the enemy. You won!");
                        counter = 0;
                        txtBtn.setText("Reset Game");

                    }
                }
                break;

        }
    }
}


