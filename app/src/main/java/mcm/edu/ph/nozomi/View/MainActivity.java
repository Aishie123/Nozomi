package mcm.edu.ph.nozomi.View;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.transition.Explode;
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
    ImageButton btnSS1, btnSS2, btnAttack, btnReset;
    TextView txtLog, txtBtn, txtReset, txtEnemy, txtUser, txtHeroHP, txtHeroMP, txtEnemyHP;
    View heroHPBar, heroMPBar, enemyHPBar;
    double heroHPB, heroMPB, enemyHPB; //for HP bar
    int fullHeroHP, curHeroHP, fullHeroMP, curHeroMP, fullEnemyHP, curEnemyHP;
    String TAG = "MainActivity";

    AnimationDrawable hero1_idleAnim, hero1_runAnim, hero1_atkAnim, hero1_ss1Anim, hero1_ss2Anim, hero1_hitAnim, hero1_deathAnim,
            enemy1_idleAnim, enemy1_walkAnim, enemy1_atkAnim, enemy1_hitAnim, enemy1_deathAnim;
    ObjectAnimator hero1Run, hero1Atk, hero1SS1, hero1SS2, hero1Hit, hero1Death,
            enemy1Walk, enemy1Atk, enemy1Hit, enemy1Death;

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

        btnAttack.setOnClickListener(this);
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
        enemy1_idleAnim = (AnimationDrawable)enemy1_idleSprite.getDrawable();
        enemy1_idleAnim.start();

        hero1_idleAnim = (AnimationDrawable)hero1_idleSprite.getDrawable();
        hero1_idleAnim.start();
    }


    //HERO SPRITES ----------------------------------------------------------------------------------------------------------------
    public void heroRunSprite(){
        btnAttack.setEnabled(false);
        btnSS1.setEnabled(false);
        btnSS2.setEnabled(false);
        hero1_runSprite.setVisibility(View.VISIBLE);
        hero1_idleSprite.setVisibility(View.INVISIBLE);

        hero1_runAnim = (AnimationDrawable)hero1_runSprite.getDrawable();
        hero1Run.setDuration(800);

        hero1Run.start();
        hero1_runAnim.start();

        hero1Run.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                hero1_runSprite.setVisibility(View.INVISIBLE);
                hero1_runAnim.stop();
                hero1_runSprite.setX(0f);
            }
        });
    }

    public void heroAtkSprite(){
        hero1_atkSprite.setVisibility(View.VISIBLE);

        hero1_atkAnim = (AnimationDrawable) hero1_atkSprite.getDrawable();
        hero1Atk = ObjectAnimator.ofFloat(hero1_atkSprite,"translationX",850f);
        hero1Atk.setDuration(400);

        hero1Atk.start();
        hero1_atkAnim.start();
    }

    public void heroHitSprite(){
        hero1_hitSprite.setVisibility(View.VISIBLE);

        hero1_hitAnim = (AnimationDrawable)hero1_hitSprite.getDrawable();
        hero1Hit = ObjectAnimator.ofFloat(hero1_hitSprite,"translationX",0f);
        hero1Hit.setDuration(600);

        hero1Hit.start();
        hero1_hitAnim.start();
    }

    public void heroSS1Sprite(){
        hero1_ss1Sprite.setVisibility(View.VISIBLE);

        hero1_ss1Anim = (AnimationDrawable) hero1_ss1Sprite.getDrawable();
        hero1SS1 = ObjectAnimator.ofFloat(hero1_ss1Sprite,"translationX",725f);
        hero1SS1.setDuration(400);

        hero1SS1.start();
        hero1_ss1Anim.start();
    }

    public void heroSS2Sprite(){
        btnAttack.setEnabled(false);
        btnSS1.setEnabled(false);
        btnSS2.setEnabled(false);
        hero1_ss2Sprite.setVisibility(View.VISIBLE);

        hero1_ss2Anim = (AnimationDrawable)hero1_ss2Sprite.getDrawable();
        hero1SS2 = ObjectAnimator.ofFloat(hero1_ss2Sprite,"translationX",0f);
        hero1SS2.setDuration(600);

        hero1SS2.start();
        hero1_ss2Anim.start();
    }

    public void heroDeathSprite(){
        btnAttack.setEnabled(false);
        btnSS1.setEnabled(false);
        btnSS2.setEnabled(false);
        hero1_deathSprite.setVisibility(View.VISIBLE);

        hero1_deathAnim = (AnimationDrawable)hero1_deathSprite.getDrawable();
        hero1Death = ObjectAnimator.ofFloat(hero1_deathSprite,"translationX",0f);
        hero1Death.setDuration(1400);

        hero1Death.start();
        hero1_deathAnim.start();
    }


    //ENEMY SPRITES ---------------------------------------------------------------------------------------------------------------
    public void enemyWalkSprite(){
        btnAttack.setEnabled(false);
        btnSS1.setEnabled(false);
        btnSS2.setEnabled(false);
        enemy1_walkSprite.setVisibility(View.VISIBLE);
        enemy1_idleSprite.setVisibility(View.INVISIBLE);

        enemy1_walkAnim = (AnimationDrawable)enemy1_walkSprite.getDrawable();
        enemy1Walk = ObjectAnimator.ofFloat(enemy1_walkSprite,"translationX",-900f);
        enemy1Walk.setDuration(1200);

        enemy1Walk.start();
        enemy1_walkAnim.start();

        enemy1Walk.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                enemy1_walkSprite.setVisibility(View.INVISIBLE);
                enemy1_walkAnim.stop();
                enemy1_walkSprite.setX(1200f);
            }
        });

    }

    public void enemyAtkSprite(){
        enemy1_atkSprite.setVisibility(View.VISIBLE);

        enemy1_atkAnim = (AnimationDrawable) enemy1_atkSprite.getDrawable();
        enemy1Atk = ObjectAnimator.ofFloat(enemy1_atkSprite,"translationX",-900f);
        enemy1Atk.setDuration(400);

        enemy1Atk.start();
        enemy1_atkAnim.start();

    }

    public void enemyDeathSprite(){
        btnAttack.setEnabled(false);
        btnSS1.setEnabled(false);
        btnSS2.setEnabled(false);
        enemy1_deathSprite.setVisibility(View.VISIBLE);

        enemy1_deathAnim = (AnimationDrawable)enemy1_deathSprite.getDrawable();
        enemy1Death = ObjectAnimator.ofFloat(enemy1_deathSprite,"translationX",0f);
        enemy1Death.setDuration(600);

        enemy1Death.start();
        enemy1_deathAnim.start();

    }

    public void enemyHitSprite(){
        enemy1_hitSprite.setVisibility(View.VISIBLE);

        enemy1_hitAnim = (AnimationDrawable)enemy1_hitSprite.getDrawable();
        enemy1Hit = ObjectAnimator.ofFloat(enemy1_hitSprite,"translationX",0f);
        enemy1Hit.setDuration(600);

        enemy1Hit.start();
        enemy1_hitAnim.start();
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
                    btnSS1.setEnabled(true);
                    btnSS2.setEnabled(true);
                    counter++;
                }

                //hero's turn --------------------------------------------------------------------------------------------
                else if(counter%2 == 1){
                    btnSS1.setEnabled(false);
                    btnSS2.setEnabled(false);
                    txtBtn.setText("Next Turn");
                    hero1Run = ObjectAnimator.ofFloat(hero1_runSprite,"translationX",850f);
                    heroRunSprite();

                    hero1Run.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            heroAtkSprite();

                            hero1Atk.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    enemy1_idleSprite.setVisibility(View.INVISIBLE);

                                    enemyHitSprite();

                                    enemy1Hit.addListener(new AnimatorListenerAdapter() {

                                         public void onAnimationEnd(Animator animation) {
                                             btnAttack.setEnabled(true);

                                             txtLog.setText(hero.getName() + " dealt "+ hero1AtkN + " damage to the enemy.");
                                             curEnemyHP -= hero1AtkN;
                                             negativeHealthCheck();
                                             enemyHPBar();

                                             hero1_atkAnim.stop();
                                             hero1_atkSprite.setVisibility(View.INVISIBLE);
                                             hero1_idleSprite.setVisibility(View.VISIBLE);

                                             enemy1_hitAnim.stop();
                                             enemy1_hitSprite.setVisibility(View.INVISIBLE);
                                             enemy1_idleSprite.setVisibility(View.VISIBLE);

                                             counter++;

                                             if(curEnemyHP <= 0){
                                                 enemy1_deathSprite.setImageResource(R.drawable.enemy1_deathanim);
                                                 enemyDeathSprite();
                                                 enemy1_idleSprite.setVisibility(View.INVISIBLE);
                                                 enemy1Death.addListener(new AnimatorListenerAdapter() {
                                                     public void onAnimationEnd(Animator animation){
                                                         btnAttack.setEnabled(true);

                                                         enemy1_deathAnim.stop();
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
                    btnAttack.setEnabled(true);
                    btnSS1.setEnabled(true);
                    btnSS2.setEnabled(true);
                    txtBtn.setText("Attack");
                    enemyWalkSprite();

                    enemy1Walk.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {

                            enemyAtkSprite();

                            enemy1Atk.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    hero1_idleSprite.setVisibility(View.INVISIBLE);

                                    heroHitSprite();

                                    hero1Hit.addListener(new AnimatorListenerAdapter() {

                                        public void onAnimationEnd(Animator animation) {
                                            btnAttack.setEnabled(true);
                                            btnSS1.setEnabled(true);
                                            btnSS2.setEnabled(true);

                                            txtLog.setText(enemy.getName() + " dealt " + enemy1AtkN + " damage to the hero.");
                                            curHeroHP -= enemy1AtkN;
                                            negativeHealthCheck();
                                            heroHPBar();

                                            enemy1_atkAnim.stop();
                                            enemy1_atkSprite.setVisibility(View.INVISIBLE);
                                            enemy1_idleSprite.setVisibility(View.VISIBLE);

                                            hero1_hitAnim.stop();
                                            hero1_hitSprite.setVisibility(View.INVISIBLE);
                                            hero1_idleSprite.setVisibility(View.VISIBLE);

                                            counter++;

                                            if(curHeroHP <= 0){
                                                hero1_deathSprite.setImageResource(R.drawable.hero1_deathanim);
                                                heroDeathSprite();
                                                hero1_idleSprite.setVisibility(View.INVISIBLE);

                                                hero1Death.addListener(new AnimatorListenerAdapter() {
                                                    public void onAnimationEnd(Animator animation) {
                                                        btnAttack.setEnabled(true);

                                                        hero1_deathAnim.stop();
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
                    hero1Run = ObjectAnimator.ofFloat(hero1_runSprite,"translationX",725f);
                    heroRunSprite();

                    hero1Run.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            heroSS1Sprite();

                            hero1SS1.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    enemy1_idleSprite.setVisibility(View.INVISIBLE);

                                    enemyHitSprite();

                                    enemy1Hit.addListener(new AnimatorListenerAdapter() {

                                        public void onAnimationEnd(Animator animation) {
                                            btnAttack.setEnabled(true);

                                            txtLog.setText(hero.getName() + " used Double Slash, and dealt " + (hero1AtkN * 2) + " damage to the enemy.");
                                            curEnemyHP -= (hero1AtkN * 2);
                                            enemyHPBar();

                                            hero1_ss1Anim.stop();
                                            hero1_ss1Sprite.setVisibility(View.INVISIBLE);
                                            hero1_idleSprite.setVisibility(View.VISIBLE);

                                            enemy1_hitAnim.stop();
                                            enemy1_hitSprite.setVisibility(View.INVISIBLE);
                                            enemy1_idleSprite.setVisibility(View.VISIBLE);
                                            counter++;

                                            if (curEnemyHP <= 0) {
                                                enemy1_deathSprite.setImageResource(R.drawable.enemy1_deathanim);
                                                enemyDeathSprite();
                                                enemy1_idleSprite.setVisibility(View.INVISIBLE);

                                                enemy1Death.addListener(new AnimatorListenerAdapter() {
                                                    public void onAnimationEnd(Animator animation) {
                                                        btnAttack.setEnabled(true);

                                                        enemy1_deathAnim.stop();
                                                        enemy1_deathSprite.setImageResource(R.drawable.enemy1_death4);
                                                        txtLog.setText(hero.getName() + " used Double Sword, and dealt " + (hero1AtkN * 2) + " damage to the enemy. You won!");
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
                else{
                    curHeroMP-=SS2C;
                    if ((curHeroHP+(fullHeroHP*0.50) > fullHeroHP)){
                        curHeroHP = fullHeroHP;
                    }
                    else{
                        curHeroHP+=(fullHeroHP*0.50);
                    }
                    heroSS2Sprite();
                    hero1SS2.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            btnAttack.setEnabled(true);
                            hero1_ss2Sprite.setVisibility(View.INVISIBLE);
                            hero1_ss2Anim.stop();
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
}


