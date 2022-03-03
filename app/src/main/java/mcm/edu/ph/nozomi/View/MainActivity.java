package mcm.edu.ph.nozomi.View;

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

import mcm.edu.ph.nozomi.Controller.BattleAlgorithm;
import mcm.edu.ph.nozomi.Model.HeroData;
import mcm.edu.ph.nozomi.Model.MonsterData;
import mcm.edu.ph.nozomi.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView enemy1_idleSprite, enemy1_hitSprite, enemy1_deathSprite, enemy1_walkSprite, enemy1_atkSprite,
            hero1_idleSprite, hero1_runSprite, hero1_atkSprite, hero1_ss1Sprite, hero1_ss2Sprite, hero1_hitSprite, hero1_deathSprite;
    ImageButton btnSS1, btnSS2, btnTurn, btnReset;
    TextView txtLog, txtBtn, txtReset, txtEnemy, txtUser, txtHeroHP, txtHeroMP, txtEnemyHP;
    View heroHPBar, heroMPBar, enemyHPBar;
    double heroHPB, heroMPB, enemyHPB; //for HP bar
    int fullHeroHP, curHeroHP, fullHeroMP, curHeroMP, fullEnemyHP, curEnemyHP;
    String TAG = "MainActivity";

    AnimationDrawable hero_idleAnim, hero_runAnim, hero_atkAnim, hero_ss1Anim, hero_ss2Anim, hero_hitAnim, hero_deathAnim,
            enemy_idleAnim, enemy_walkAnim, enemy_atkAnim, enemy_hitAnim, enemy_deathAnim;
    ObjectAnimator heroRun, heroAtk, heroSS1, heroSS2, heroHit, heroDeath,
            enemyWalk, enemyAtk, enemyHit, enemyDeath;

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
        btnTurn = findViewById(R.id.btnAttk);
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
        enemy1_walkSprite = findViewById(R.id.enemy1_walkSprite);
        enemy1_atkSprite = findViewById(R.id.enemy1_attackSprite);
        enemy1_hitSprite = findViewById(R.id.enemy1_hitSprite);
        enemy1_deathSprite = findViewById(R.id.enemy1_deathSprite);

        hero1_idleSprite = findViewById(R.id.hero1_idleSprite);
        hero1_runSprite = findViewById(R.id.hero1_runSprite);
        hero1_atkSprite = findViewById(R.id.hero1_attkSprite);
        hero1_ss1Sprite = findViewById(R.id.hero1_ss1Sprite);
        hero1_ss2Sprite = findViewById(R.id.hero1_ss2Sprite);
        hero1_hitSprite = findViewById(R.id.hero1_hitSprite);
        hero1_deathSprite = findViewById(R.id.hero1_deathSprite);

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
        enemy1_walkSprite.setImageResource(R.drawable.enemy1_walkanim);
        enemy1_atkSprite.setImageResource(R.drawable.enemy1_attackanim);
        enemy1_hitSprite.setImageResource(R.drawable.enemy1_hitanim);
        enemy1_deathSprite.setImageResource(R.drawable.enemy1_deathanim);
        hero1_idleSprite.setImageResource(R.drawable.hero1_idleanim);
        hero1_runSprite.setImageResource(R.drawable.hero1_runanim);
        hero1_atkSprite.setImageResource(R.drawable.hero1_attackanim);
        hero1_ss1Sprite.setImageResource(R.drawable.hero1_ss1anim);
        hero1_ss2Sprite.setImageResource(R.drawable.hero1_ss2anim);
        hero1_hitSprite.setImageResource(R.drawable.hero1_hitanim);
        hero1_deathSprite.setImageResource(R.drawable.hero1_deathanim);

        btnTurn.setOnClickListener(this);
        btnSS1.setOnClickListener(this);
        btnSS2.setOnClickListener(this);

        btnSS1.setEnabled(false);
        btnSS2.setEnabled(false);

        hero1_idleSprite.setX(0f);
        hero1_runSprite.setX(0f);
        hero1_atkSprite.setX(850f);
        hero1_ss1Sprite.setX(725f);
        enemy1_idleSprite.setX(0f);
        enemy1_walkSprite.setX(0f);
        enemy1_atkSprite.setX(-900f);

        idleSprite();
        heroHPBar();
        heroMPBar();
        enemyHPBar();
        press();
    }

//---------------------------------------------------------------------------------------------------------------------------------

    @SuppressLint("UseCompatLoadingForDrawables")
    //adjusting Hero HP Bar
    public void heroHPBar(){
        heroHPB = ((double) curHeroHP / (double) fullHeroHP) * 10000;

        if (curHeroHP<=fullHeroHP && curHeroHP>(fullHeroHP*0.60)){
            heroHPBar.setBackground(getResources().getDrawable(R.drawable.hp_greenbarbg));
        }
        else if (curHeroHP<=(fullHeroHP*0.60) && curHeroHP>(fullHeroHP*0.25)){
            heroHPBar.setBackground(getResources().getDrawable(R.drawable.hp_yellowbarbg));
        }
        else if (curHeroHP<=(fullHeroHP*0.25) && curHeroHP>=0){
            heroHPBar.setBackground(getResources().getDrawable(R.drawable.hp_redbarbg));
        }

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

    @SuppressLint("UseCompatLoadingForDrawables")
    //adjusting Enemy HP Bar
    public void enemyHPBar(){
        enemyHPB = ((double) curEnemyHP / (double) fullEnemyHP) * 10000;

        if (curEnemyHP<=fullEnemyHP && curEnemyHP>(fullEnemyHP*0.60)){
            enemyHPBar.setBackground(getResources().getDrawable(R.drawable.hp_greenbarbg));
        }
        else if (curEnemyHP<=(fullEnemyHP*0.60) && curEnemyHP>(fullEnemyHP*0.25)){
            enemyHPBar.setBackground(getResources().getDrawable(R.drawable.hp_yellowbarbg));
        }
        else if (curEnemyHP<=(fullEnemyHP*0.25) && curEnemyHP>=0){
            enemyHPBar.setBackground(getResources().getDrawable(R.drawable.hp_redbarbg));
        }

        enemyHPBar.getBackground().setLevel((int) enemyHPB);
        txtEnemyHP.setText(String.valueOf(curEnemyHP));
        Log.d(TAG, "Enemy's current HP is " + curEnemyHP);
    }


    //changing button shades when pressed -----------------------------------------------------------------------------------------
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

        btnTurn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnTurn.setImageResource(R.drawable.btn_pressed);
                    Log.d(TAG, "btnTurn pressed");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnTurn.setImageResource(R.drawable.btn_unpressed);
                    Log.d(TAG, "btnTurn unpressed");
                }
                return false;
            }
        });

    }




    //BAWAL NEGATIVE HEALTH HNGGG -------------------------------------------------------------------------------------------------
    public void negativeHealthCheck() {
        if (curEnemyHP < 0){
            curEnemyHP = 0;
        }

        if (curHeroHP < 0){
            curHeroHP = 0;
        }
    }


    //idle sprites of characters --------------------------------------------------------------------------------------------------
    public void idleSprite(){
        enemy_idleAnim = (AnimationDrawable)enemy1_idleSprite.getDrawable();
        enemy_idleAnim.start();

        hero_idleAnim = (AnimationDrawable)hero1_idleSprite.getDrawable();
        hero_idleAnim.start();
    }


    //HERO SPRITES ----------------------------------------------------------------------------------------------------------------
    public void heroRunSprite(){
        hero1_runSprite.setVisibility(View.VISIBLE);
        hero1_idleSprite.setVisibility(View.INVISIBLE);

        hero_runAnim = (AnimationDrawable)hero1_runSprite.getDrawable();
        heroRun.setDuration(800);

        heroRun.start();
        hero_runAnim.start();

        heroRun.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                hero1_runSprite.setVisibility(View.INVISIBLE);
                hero_runAnim.stop();
                hero1_runSprite.setX(0f);
            }
        });
    }

    public void heroAtkSprite(){
        hero1_atkSprite.setVisibility(View.VISIBLE);

        hero_atkAnim = (AnimationDrawable) hero1_atkSprite.getDrawable();
        heroAtk = ObjectAnimator.ofFloat(hero1_atkSprite,"translationX",850f);
        heroAtk.setDuration(400);

        heroAtk.start();
        hero_atkAnim.start();
    }

    public void heroHitSprite(){
        hero1_hitSprite.setVisibility(View.VISIBLE);

        hero_hitAnim = (AnimationDrawable)hero1_hitSprite.getDrawable();
        heroHit = ObjectAnimator.ofFloat(hero1_hitSprite,"translationX",0f);
        heroHit.setDuration(600);

        heroHit.start();
        hero_hitAnim.start();
    }

    public void heroSS1Sprite(){
        hero1_ss1Sprite.setVisibility(View.VISIBLE);

        hero_ss1Anim = (AnimationDrawable) hero1_ss1Sprite.getDrawable();
        heroSS1 = ObjectAnimator.ofFloat(hero1_ss1Sprite,"translationX",725f);
        heroSS1.setDuration(400);

        heroSS1.start();
        hero_ss1Anim.start();
    }

    public void heroSS2Sprite(){
        hero1_ss2Sprite.setVisibility(View.VISIBLE);

        hero_ss2Anim = (AnimationDrawable)hero1_ss2Sprite.getDrawable();
        heroSS2 = ObjectAnimator.ofFloat(hero1_ss2Sprite,"translationX",0f);
        heroSS2.setDuration(600);

        heroSS2.start();
        hero_ss2Anim.start();
    }

    public void heroDeathSprite(){
        hero1_deathSprite.setVisibility(View.VISIBLE);

        hero_deathAnim = (AnimationDrawable)hero1_deathSprite.getDrawable();
        heroDeath = ObjectAnimator.ofFloat(hero1_deathSprite,"translationX",0f);
        heroDeath.setDuration(1400);

        heroDeath.start();
        hero_deathAnim.start();
    }


    //ENEMY SPRITES ---------------------------------------------------------------------------------------------------------------
    public void enemyWalkSprite(){
        enemy1_walkSprite.setVisibility(View.VISIBLE);
        enemy1_idleSprite.setVisibility(View.INVISIBLE);

        enemy_walkAnim = (AnimationDrawable)enemy1_walkSprite.getDrawable();
        enemyWalk = ObjectAnimator.ofFloat(enemy1_walkSprite,"translationX",-900f);
        enemyWalk.setDuration(1200);

        enemyWalk.start();
        enemy_walkAnim.start();

        enemyWalk.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                enemy1_walkSprite.setVisibility(View.INVISIBLE);
                enemy_walkAnim.stop();
                enemy1_walkSprite.setX(1200f);
            }
        });

    }

    public void enemyAtkSprite(){
        enemy1_atkSprite.setVisibility(View.VISIBLE);

        enemy_atkAnim = (AnimationDrawable) enemy1_atkSprite.getDrawable();
        enemyAtk = ObjectAnimator.ofFloat(enemy1_atkSprite,"translationX",-900f);
        enemyAtk.setDuration(400);

        enemyAtk.start();
        enemy_atkAnim.start();

    }

    public void enemyDeathSprite(){
        enemy1_deathSprite.setVisibility(View.VISIBLE);

        enemy_deathAnim = (AnimationDrawable)enemy1_deathSprite.getDrawable();
        enemyDeath = ObjectAnimator.ofFloat(enemy1_deathSprite,"translationX",0f);
        enemyDeath.setDuration(600);

        enemyDeath.start();
        enemy_deathAnim.start();

    }

    public void enemyHitSprite(){
        enemy1_hitSprite.setVisibility(View.VISIBLE);

        enemy_hitAnim = (AnimationDrawable)enemy1_hitSprite.getDrawable();
        enemyHit = ObjectAnimator.ofFloat(enemy1_hitSprite,"translationX",0f);
        enemyHit.setDuration(600);

        enemyHit.start();
        enemy_hitAnim.start();
    }



    //attacks + iskillz + animation TT --------------------------------------------------------------------------------------------
    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    public void onClick(View v) {
        Random randomizer = new Random();
        int hero1AtkN = battle.attack(hero.getAtkMin(),hero.getAtkMax());
        int enemy1AtkN = battle.attack(enemy.getAtkMin(),enemy.getAtkMax());


        switch (v.getId()){
            case R.id.btnAttk:

                //start of battle --------------------------------------------------------------------------------------------
                if (counter == 0) {
                    enemy1_deathSprite.setVisibility(View.INVISIBLE);
                    hero1_deathSprite.setVisibility(View.INVISIBLE);
                    enemy1_idleSprite.setVisibility(View.VISIBLE);
                    hero1_idleSprite.setVisibility(View.VISIBLE);
                    curHeroHP = fullHeroHP;
                    curEnemyHP = fullEnemyHP;
                    curHeroMP = fullHeroMP;
                    heroHPBar();
                    heroMPBar();
                    enemyHPBar();
                    txtBtn.setText("Attack");
                    txtLog.setText("Ready for Battle!");
                    enableButtons();
                    counter++;
                }

                //hero's turn --------------------------------------------------------------------------------------------
                else if(counter%2 == 1){
                    txtBtn.setText("Next Turn");
                    heroRun = ObjectAnimator.ofFloat(hero1_runSprite,"translationX",850f);
                    disableButtons();
                    heroRunSprite();

                    heroRun.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {

                            heroAtkSprite();

                            heroAtk.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    enemy1_idleSprite.setVisibility(View.INVISIBLE);

                                    enemyHitSprite();

                                    enemyHit.addListener(new AnimatorListenerAdapter() {

                                         public void onAnimationEnd(Animator animation) {
                                             enableTurnOnly();

                                             txtLog.setText(hero.getName() + " dealt "+ hero1AtkN + " damage to the enemy.");
                                             curEnemyHP -= hero1AtkN;
                                             negativeHealthCheck();
                                             enemyHPBar();

                                             hero_atkAnim.stop();
                                             hero1_atkSprite.setVisibility(View.INVISIBLE);
                                             hero1_idleSprite.setVisibility(View.VISIBLE);

                                             enemy_hitAnim.stop();
                                             enemy1_hitSprite.setVisibility(View.INVISIBLE);
                                             enemy1_idleSprite.setVisibility(View.VISIBLE);

                                             counter++;

                                             if(curEnemyHP <= 0){
                                                 enemy1_deathSprite.setImageResource(R.drawable.enemy1_deathanim);
                                                 enemyDeathSprite();
                                                 enemy1_idleSprite.setVisibility(View.INVISIBLE);
                                                 enemyDeath.addListener(new AnimatorListenerAdapter() {
                                                     public void onAnimationEnd(Animator animation){
                                                         enableTurnOnly();
                                                         enemy_deathAnim.stop();
                                                         enemy1_deathSprite.setImageResource(R.drawable.enemy1_death4);
                                                         txtLog.setText(hero.getName() + " dealt "+ hero1AtkN + " damage to the enemy.\nYou won!");
                                                         counter = 0;
                                                         txtBtn.setText("Reset Game");
                                                     }
                                                 });
                                             }

                                        }
                                    });
                                }
                            });
                        }
                    });
                }

                //enemy's turn --------------------------------------------------------------------------------------------
                else if(counter%2 !=1) {
                    txtBtn.setText("Attack");
                    disableButtons();
                    enemyWalkSprite();

                    enemyWalk.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {

                            enemyAtkSprite();

                            enemyAtk.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    hero1_idleSprite.setVisibility(View.INVISIBLE);

                                    heroHitSprite();

                                    heroHit.addListener(new AnimatorListenerAdapter() {

                                        public void onAnimationEnd(Animator animation) {
                                            enableButtons();

                                            txtLog.setText(enemy.getName() + " dealt " + enemy1AtkN + " damage to the hero.");
                                            curHeroHP -= enemy1AtkN;
                                            negativeHealthCheck();
                                            heroHPBar();

                                            enemy_atkAnim.stop();
                                            enemy1_atkSprite.setVisibility(View.INVISIBLE);
                                            enemy1_idleSprite.setVisibility(View.VISIBLE);

                                            hero_hitAnim.stop();
                                            hero1_hitSprite.setVisibility(View.INVISIBLE);
                                            hero1_idleSprite.setVisibility(View.VISIBLE);

                                            counter++;

                                            if(curHeroHP <= 0){
                                                hero1_deathSprite.setImageResource(R.drawable.hero1_deathanim);
                                                disableButtons();
                                                heroDeathSprite();
                                                hero1_idleSprite.setVisibility(View.INVISIBLE);

                                                heroDeath.addListener(new AnimatorListenerAdapter() {
                                                    public void onAnimationEnd(Animator animation) {
                                                        enableTurnOnly();
                                                        hero_deathAnim.stop();
                                                        hero1_deathSprite.setImageResource(R.drawable.hero1_death11);
                                                        txtLog.setText(enemy.getName() + " dealt " + enemy1AtkN + " damage to the hero.\nGame over!");
                                                        counter = 0;
                                                        txtBtn.setText("Reset Game");
                                                    }
                                                });
                                            }

                                        }
                                    });
                                }
                            });

                        }
                    });
                }
                break;


            //SS1 - Double Slash --------------------------------------------------------------------------------------------
            case R.id.btnSS1:
                if ((curHeroMP-SS1C)<0){
                    txtLog.setText("Your MP is not enough for this skill.");
                }
                else{
                    curHeroMP-=SS1C;
                    heroMPBar();
                    txtBtn.setText("Next Turn");
                    heroRun = ObjectAnimator.ofFloat(hero1_runSprite,"translationX",725f);
                    heroRunSprite();
                    disableButtons();

                    heroRun.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            heroSS1Sprite();

                            heroSS1.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    enemy1_idleSprite.setVisibility(View.INVISIBLE);

                                    enemyHitSprite();

                                    enemyHit.addListener(new AnimatorListenerAdapter() {

                                        public void onAnimationEnd(Animator animation) {
                                            enableTurnOnly();

                                            txtLog.setText(hero.getName() + " used Double Slash, and dealt " + (hero1AtkN * 2) + " damage to the enemy.");
                                            curEnemyHP -= (hero1AtkN * 2);
                                            enemyHPBar();

                                            hero_ss1Anim.stop();
                                            hero1_ss1Sprite.setVisibility(View.INVISIBLE);
                                            hero1_idleSprite.setVisibility(View.VISIBLE);

                                            enemy_hitAnim.stop();
                                            enemy1_hitSprite.setVisibility(View.INVISIBLE);
                                            enemy1_idleSprite.setVisibility(View.VISIBLE);
                                            counter++;

                                            if (curEnemyHP <= 0) {
                                                enemy1_deathSprite.setImageResource(R.drawable.enemy1_deathanim);
                                                disableButtons();
                                                enemyDeathSprite();
                                                enemy1_idleSprite.setVisibility(View.INVISIBLE);

                                                enemyDeath.addListener(new AnimatorListenerAdapter() {
                                                    public void onAnimationEnd(Animator animation) {
                                                        enableTurnOnly();
                                                        enemy_deathAnim.stop();
                                                        enemy1_deathSprite.setImageResource(R.drawable.enemy1_death4);
                                                        txtLog.setText(hero.getName() + " used Double Slash, and dealt " + (hero1AtkN * 2) + " damage to the enemy. You won!");
                                                        counter = 0;
                                                        txtBtn.setText("Reset Game");
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                break;


            //SS2 - Healing Shield --------------------------------------------------------------------------------------------
            case R.id.btnSS2:
                if ((curHeroMP-SS2C)<0){
                    txtLog.setText("Your MP is not enough for this skill.");
                }
                else {
                    curHeroMP-=SS2C;
                    if ((curHeroHP+(fullHeroHP*0.50) > fullHeroHP)){
                        curHeroHP = fullHeroHP;
                    }
                    else{
                        curHeroHP+=(fullHeroHP*0.50);
                    }

                    disableButtons();
                    heroSS2Sprite();
                    heroSS2.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            enableTurnOnly();
                            hero1_ss2Sprite.setVisibility(View.INVISIBLE);
                            hero_ss2Anim.stop();
                            txtLog.setText(hero.getName() + " used Healing Shield, and regained 50% health");
                            txtBtn.setText("Next Turn");
                            heroHPBar();
                            heroMPBar();
                            counter++;
                        }
                    });

                }
                break;
        }
    }


    // call this method to disable buttons -----------------------------------------------------------
    public void disableButtons(){
        btnTurn.setEnabled(false);
        btnSS1.setEnabled(false);
        btnSS2.setEnabled(false);
    }

    // call this method to enable buttons -----------------------------------------------------------
    public void enableButtons(){
        btnTurn.setEnabled(true);
        btnSS1.setEnabled(true);
        btnSS2.setEnabled(true);
    }

    // call this method to enable turn button only -----------------------------------------------------------
    public void enableTurnOnly(){
        btnTurn.setEnabled(true);
        btnSS1.setEnabled(false);
        btnSS2.setEnabled(false);
    }

}