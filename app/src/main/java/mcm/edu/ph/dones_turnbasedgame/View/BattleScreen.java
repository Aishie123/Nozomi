package mcm.edu.ph.dones_turnbasedgame.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mcm.edu.ph.dones_turnbasedgame.Controller.BattleAlgorithm;
import mcm.edu.ph.dones_turnbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.dones_turnbasedgame.Controller.QuoteRandomizer;
import mcm.edu.ph.dones_turnbasedgame.Controller.SfxRandomizer;
import mcm.edu.ph.dones_turnbasedgame.Model.HeroData;
import mcm.edu.ph.dones_turnbasedgame.Model.MonsterData;
import mcm.edu.ph.dones_turnbasedgame.R;

public class BattleScreen extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    ImageView enemy1_idleSprite, enemy1_hitSprite, enemy1_deathSprite, enemy1_walkSprite, enemy1_atkSprite,
            hero1_idleSprite, hero1_runSprite, hero1_atkSprite, hero1_ss1Sprite, hero1_ss2Sprite, hero1_hitSprite, hero1_deathSprite,
            enemyHPBar_base, btnHome;
    ImageButton btnSS1, btnSS2, btnTurn, btnReset;
    TextView txtLog, txtQuote, txtBtn, txtReset, txtEnemy, txtUser, txtHeroHP, txtHeroMP, txtEnemyHP;
    View heroHPBar, heroMPBar, enemyHPBar;
    double heroHPB, heroMPB, enemyHPB; //for HP bar
    int fullHeroHP, curHeroHP, fullHeroMP, curHeroMP, fullEnemyHP, curEnemyHP, quoteCounter, n;
    String TAG = "BattleScreen";
    Intent goToHome;
    Context context;

    MusicPlayerService musicPlayerService;
    MediaPlayer heroSS1SFX, heroSS2SFX;
    MediaPlayer [] heroAtkSFX = new MediaPlayer[3];
    MediaPlayer [] enemyAtkSFX = new MediaPlayer[3];

    AnimationDrawable hero_idleAnim, hero_runAnim, hero_atkAnim, hero_ss1Anim, hero_ss2Anim, hero_hitAnim, hero_deathAnim,
            enemy_idleAnim, enemy_walkAnim, enemy_atkAnim, enemy_hitAnim, enemy_deathAnim;
    ObjectAnimator heroRun, heroAtk, heroSS1, heroSS2, heroHit, heroDeath,
            enemyWalk, enemyAtk, enemyHit, enemyDeath;
    Typeface bold, clean;

    HeroData hero = new HeroData();
    MonsterData enemy = new MonsterData();
    BattleAlgorithm battle = new BattleAlgorithm();
    QuoteRandomizer quote = new QuoteRandomizer(this);
    SfxRandomizer sfx = new SfxRandomizer();

    int counter = 0;
    int SS1C = 8;
    int SS2C = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_battle_screen);

        btnHome = findViewById(R.id.btnHome);
        btnReset = findViewById(R.id.btnReset);
        btnTurn = findViewById(R.id.btnStart);
        btnSS1 = findViewById(R.id.btnSS1);
        btnSS2 = findViewById(R.id.btnSS2);

        txtReset = findViewById(R.id.txtReset);
        txtBtn = findViewById(R.id.txtStart);
        txtEnemy = findViewById(R.id.txtEnemy);
        txtUser = findViewById(R.id.txtUser);
        txtLog = findViewById(R.id.txtCombatLog);
        txtQuote = findViewById(R.id.txtQuoteLog);
        txtEnemyHP = findViewById(R.id.txtEnemyHP);
        txtHeroHP = findViewById(R.id.txtHeroHP);
        txtHeroMP = findViewById(R.id.txtHeroMP);

        heroHPBar = findViewById(R.id.heroHPBar);
        heroMPBar = findViewById(R.id.heroMPBar);
        enemyHPBar = findViewById(R.id.enemyHPBar);
        enemyHPBar_base = findViewById(R.id.enemyHPBar_base);

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

        hero = new HeroData(userName, 1, 150, 15, 10, 7, 5, 4, 15, 20);
        fullHeroHP = hero.getHealthPt();
        curHeroHP = fullHeroHP;
        fullHeroMP = hero.getManaPt();
        curHeroMP = fullHeroMP;

        enemy = new MonsterData(enemyName, 10, 40, 300, 20 );
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

        btnHome.setOnClickListener(this);
        btnTurn.setOnClickListener(this);
        btnSS1.setOnClickListener(this);
        btnSS2.setOnClickListener(this);

        hero1_idleSprite.setX(0f);
        hero1_runSprite.setX(0f);
        hero1_atkSprite.setX(850f);
        hero1_ss1Sprite.setX(725f);
        enemy1_idleSprite.setX(0f);
        enemy1_walkSprite.setX(0f);
        enemy1_atkSprite.setX(-900f);

        enemy1_deathSprite.setVisibility(View.INVISIBLE);
        hero1_deathSprite.setVisibility(View.INVISIBLE);
        enemy1_idleSprite.setVisibility(View.VISIBLE);
        hero1_idleSprite.setVisibility(View.VISIBLE);

        quoteCounter = quote.quoteCounter();

        clean = getResources().getFont(R.font.nicoclean_regular);
        bold = getResources().getFont(R.font.nicobold_regular);
        txtLog.setTypeface(clean);

        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        enableTurnOnly();
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
        heroHPB = ((double) curHeroHP / (double) fullHeroHP) * 10000; // 100% HP = 10000

        if (curHeroHP<=fullHeroHP && curHeroHP>(fullHeroHP*0.60)){
            heroHPBar.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.hp_greenbarbg, null));
        }
        else if (curHeroHP<=(fullHeroHP*0.60) && curHeroHP>(fullHeroHP*0.25)){
            heroHPBar.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.hp_yellowbarbg, null));
        }
        else if (curHeroHP<=(fullHeroHP*0.25) && curHeroHP>=0){
            heroHPBar.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.hp_redbarbg, null));
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
            enemyHPBar.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.hp_greenbarbg, null));
        }
        else if (curEnemyHP<=(fullEnemyHP*0.60) && curEnemyHP>(fullEnemyHP*0.25)){
            enemyHPBar.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.hp_yellowbarbg, null));
        }
        else if (curEnemyHP<=(fullEnemyHP*0.25) && curEnemyHP>=0){
            enemyHPBar.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.hp_redbarbg, null));
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

        btnHome.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnTurn.setImageResource(R.drawable.btn_p_home);
                    Log.d(TAG, "btnTurn pressed");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnTurn.setImageResource(R.drawable.btn_up_home);
                    Log.d(TAG, "btnTurn unpressed");
                }
                return false;
            }
        });


    }




    // call this method to check if health goes below zero -------------------------------------------------------------------------------------------------
    public void negativeHealthCheck() {
        if (curEnemyHP < 0){
            curEnemyHP = 0;
        }

        if (curHeroHP < 0){
            curHeroHP = 0;
        }
    }


    // idle sprites of characters --------------------------------------------------------------------------------------------------
    public void idleSprite(){
        enemy_idleAnim = (AnimationDrawable)enemy1_idleSprite.getDrawable();
        enemy_idleAnim.start();

        hero_idleAnim = (AnimationDrawable)hero1_idleSprite.getDrawable();
        hero_idleAnim.start();
    }


    //HERO SPRITES ----------------------------------------------------------------------------------------------------------------
    // animation for when hero runs
    public void heroRunSprite(){
        hero1_runSprite.setVisibility(View.VISIBLE);
        hero1_idleSprite.setVisibility(View.INVISIBLE);

        hero_runAnim = (AnimationDrawable)hero1_runSprite.getDrawable();
        heroRun.setDuration(800); // sets duration of movement to 0.8 seconds, same as the animation duration

        heroRun.start(); // moves the position of the sprite
        hero_runAnim.start(); // starts "run" animation

        heroRun.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                hero1_runSprite.setVisibility(View.INVISIBLE);
                hero_runAnim.stop();
                hero1_runSprite.setX(0f); // moves the sprite back to original position when done and invisible
            }
        });
    }

    // animation for when hero attacks
    public void heroAtkSprite(){
        hero1_atkSprite.setVisibility(View.VISIBLE);

        hero_atkAnim = (AnimationDrawable) hero1_atkSprite.getDrawable();
        heroAtk = ObjectAnimator.ofFloat(hero1_atkSprite,"translationX",850f); // stays at the same position as Run sprite
        heroAtk.setDuration(400); // sets duration of movement to 0.4 seconds, same as the animation duration

        heroAtk.start(); // moves the position of the sprite
        hero_atkAnim.start(); // starts "attack" animation
    }

    // animation for when hero takes hit
    public void heroHitSprite(){
        hero1_hitSprite.setVisibility(View.VISIBLE);

        hero_hitAnim = (AnimationDrawable)hero1_hitSprite.getDrawable();
        heroHit = ObjectAnimator.ofFloat(hero1_hitSprite,"translationX",0f); // stays at the same position
        // ObjectAnimator is used as a timer instead
        heroHit.setDuration(650); // sets duration of movement to 0.6 seconds, same as the animation duration

        heroHit.start(); // moves the position of the sprite
        hero_hitAnim.start(); // starts "take hit" animation
    }

    public void heroSS1Sprite(){
        hero1_ss1Sprite.setVisibility(View.VISIBLE);

        hero_ss1Anim = (AnimationDrawable) hero1_ss1Sprite.getDrawable();
        heroSS1 = ObjectAnimator.ofFloat(hero1_ss1Sprite,"translationX",725f);
        heroSS1.setDuration(400); // sets duration of movement to 0.4 seconds, same as the animation duration

        heroSS1.start(); // moves the position of the sprite
        hero_ss1Anim.start();
    }

    public void heroSS2Sprite(){
        hero1_ss2Sprite.setVisibility(View.VISIBLE);

        hero_ss2Anim = (AnimationDrawable)hero1_ss2Sprite.getDrawable();
        heroSS2 = ObjectAnimator.ofFloat(hero1_ss2Sprite,"translationX",0f);
        heroSS2.setDuration(600);

        heroSS2.start(); // moves the position of the sprite
        hero_ss2Anim.start();
    }

    public void heroDeathSprite(){
        hero1_deathSprite.setVisibility(View.VISIBLE);

        hero_deathAnim = (AnimationDrawable)hero1_deathSprite.getDrawable();
        heroDeath = ObjectAnimator.ofFloat(hero1_deathSprite,"translationX",0f);
        heroDeath.setDuration(1400);

        heroDeath.start(); // moves the position of the sprite
        hero_deathAnim.start();
    }


    //ENEMY SPRITES ---------------------------------------------------------------------------------------------------------------
    public void enemyWalkSprite(){
        enemy1_walkSprite.setVisibility(View.VISIBLE);
        enemy1_idleSprite.setVisibility(View.INVISIBLE);

        enemy_walkAnim = (AnimationDrawable)enemy1_walkSprite.getDrawable();
        enemyWalk = ObjectAnimator.ofFloat(enemy1_walkSprite,"translationX",-900f);
        enemyWalk.setDuration(1200);

        enemyWalk.start(); // moves the position of the sprite
        enemy_walkAnim.start();

        enemyWalk.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                enemy1_walkSprite.setVisibility(View.INVISIBLE);
                enemy_walkAnim.stop();
                enemy1_walkSprite.setX(1200f); // moves the sprite back to original position when done and invisible
            }
        });

    }

    public void enemyAtkSprite(){
        enemy1_atkSprite.setVisibility(View.VISIBLE);

        enemy_atkAnim = (AnimationDrawable) enemy1_atkSprite.getDrawable();
        enemyAtk = ObjectAnimator.ofFloat(enemy1_atkSprite,"translationX",-900f); // stays at the same position as Walk sprite
        enemyAtk.setDuration(300); // sets duration of movement to 0.3 seconds
        // ObjectAnimator is used as a timer instead

        enemyAtk.start(); // moves the position of the sprite
        enemy_atkAnim.start();

    }

    public void enemyDeathSprite(){
        enemy1_deathSprite.setVisibility(View.VISIBLE);

        enemy_deathAnim = (AnimationDrawable)enemy1_deathSprite.getDrawable();
        enemyDeath = ObjectAnimator.ofFloat(enemy1_deathSprite,"translationX",0f);
        enemyDeath.setDuration(600);

        enemyDeath.start(); // moves the position of the sprite
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
    @SuppressLint({"SetTextI18n", "NonConstantResourceId", "ObsoleteSdkInt"})
    public void onClick(View v) {
        int hero1AtkN = battle.attack(hero.getAtkMin(),hero.getAtkMax());
        int enemy1AtkN = battle.attack(enemy.getAtkMin(),enemy.getAtkMax());

        switch (v.getId()){
            case R.id.btnStart:

                //restart battle --------------------------------------------------------------------------------------------
                if (counter == -1){
                    recreate();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

                //start of battle --------------------------------------------------------------------------------------------
                else if (counter == 0) {
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
                    txtQuote.setText("'" + quote.quoteHeroAtk(quoteCounter) + "'");
                    heroRun = ObjectAnimator.ofFloat(hero1_runSprite,"translationX",850f);
                    disableButtons();
                    heroRunSprite();

                    heroRun.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {

                            playHeroAtkSFX();
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
                                             quoteCounter = quote.quoteCounter();

                                             if(curEnemyHP <= 0){
                                                 musicPlayerService.pauseMusic();
                                                 musicPlayerService.playMusic(4);

                                                 enemy1_deathSprite.setImageResource(R.drawable.enemy1_deathanim);
                                                 enemyDeathSprite();
                                                 enemy1_idleSprite.setVisibility(View.INVISIBLE);
                                                 enemyDeath.addListener(new AnimatorListenerAdapter() {
                                                     public void onAnimationEnd(Animator animation){
                                                         enableTurnOnly();
                                                         txtQuote.setText("");
                                                         enemy_deathAnim.stop();
                                                         enemy1_deathSprite.setImageResource(R.drawable.enemy1_death4);
                                                         txtLog.setText(hero.getName() + " dealt "+ hero1AtkN + " damage to the enemy.\nYou won!");
                                                         counter = -1;
                                                         txtBtn.setText("Restart Game");
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
                    txtQuote.setText("'" + quote.quoteEnemyAtk(quoteCounter) + "'");
                    disableButtons();
                    enemyWalkSprite();

                    enemyWalk.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {

                            playEnemyAtkSFX();
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
                                                musicPlayerService.pauseMusic();
                                                musicPlayerService.playMusic(5);

                                                hero1_deathSprite.setImageResource(R.drawable.hero1_deathanim);
                                                disableButtons();
                                                heroDeathSprite();
                                                hero1_idleSprite.setVisibility(View.INVISIBLE);

                                                heroDeath.addListener(new AnimatorListenerAdapter() {
                                                    public void onAnimationEnd(Animator animation) {
                                                        enableTurnOnly();
                                                        txtQuote.setText("");
                                                        hero_deathAnim.stop();
                                                        hero1_deathSprite.setImageResource(R.drawable.hero1_death11);
                                                        txtLog.setText(enemy.getName() + " dealt " + enemy1AtkN + " damage to the hero.\nGame over!");
                                                        counter = -1;
                                                        txtBtn.setText("Exit Game");
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
                    txtQuote.setText(quote.quoteHeroSS());
                    txtBtn.setText("Next Turn");
                    heroRun = ObjectAnimator.ofFloat(hero1_runSprite,"translationX",725f);
                    heroRunSprite();
                    disableButtons();

                    heroRun.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            playHeroSS1SFX();
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
                                            negativeHealthCheck();
                                            enemyHPBar();

                                            hero_ss1Anim.stop();
                                            hero1_ss1Sprite.setVisibility(View.INVISIBLE);
                                            hero1_idleSprite.setVisibility(View.VISIBLE);

                                            enemy_hitAnim.stop();
                                            enemy1_hitSprite.setVisibility(View.INVISIBLE);
                                            enemy1_idleSprite.setVisibility(View.VISIBLE);
                                            counter++;

                                            if (curEnemyHP <= 0) {
                                                musicPlayerService.pauseMusic();
                                                musicPlayerService.playMusic(4);

                                                enemy1_deathSprite.setImageResource(R.drawable.enemy1_deathanim);
                                                disableButtons();
                                                enemyDeathSprite();
                                                enemy1_idleSprite.setVisibility(View.INVISIBLE);

                                                enemyDeath.addListener(new AnimatorListenerAdapter() {
                                                    public void onAnimationEnd(Animator animation) {
                                                        enableTurnOnly();
                                                        txtQuote.setText("");
                                                        enemy_deathAnim.stop();
                                                        enemy1_deathSprite.setImageResource(R.drawable.enemy1_death4);
                                                        txtLog.setText(hero.getName() + " used Double Slash, and dealt " + (hero1AtkN * 2) + " damage to the enemy. You won!");
                                                        counter = -1;
                                                        txtBtn.setText("Exit Game");
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
                    txtQuote.setText(quote.quoteHeroSS());
                    playHeroSS2SFX();
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

            case R.id.btnHome:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(BattleScreen.this)
                                    .setTitle("Exit Battle")
                                    .setMessage("Go back to home screen?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            goToHome = new Intent(BattleScreen.this, HomeScreen.class);
                                            startActivity(goToHome);
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(),"You remained in battle.",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .show();
                        }
                    }
                });

        }
    }


    public void playHeroAtkSFX(){
        heroAtkSFX[0] = MediaPlayer.create(this, R.raw.sfx_heroatk1);
        heroAtkSFX[1] = MediaPlayer.create(this, R.raw.sfx_heroatk2);
        heroAtkSFX[2] = MediaPlayer.create(this, R.raw.sfx_heroatk3);
        n = sfx.randomizeAtkSFX();
        heroAtkSFX[n].setVolume(100,100);
        heroAtkSFX[n].setLooping(false);
        heroAtkSFX[n].start();
    }

    public void playEnemyAtkSFX(){
        enemyAtkSFX[0] = MediaPlayer.create(this, R.raw.sfx_enemyatk1);
        enemyAtkSFX[1] = MediaPlayer.create(this, R.raw.sfx_enemyatk2);
        enemyAtkSFX[2] = MediaPlayer.create(this, R.raw.sfx_enemyatk3);
        n = sfx.randomizeAtkSFX();
        enemyAtkSFX[n].setVolume(100,100);
        enemyAtkSFX[n].setLooping(false);
        enemyAtkSFX[n].start();
    }

    public void playHeroSS1SFX(){
        heroSS1SFX = MediaPlayer.create(this, R.raw.sfx_heross1);
        heroSS1SFX.setVolume(100,100);
        heroSS1SFX.setLooping(false);
        heroSS1SFX.start();
    }

    public void playHeroSS2SFX(){
        heroSS2SFX = MediaPlayer.create(this, R.raw.sfx_heross2);
        heroSS2SFX.setVolume(100,100);
        heroSS2SFX.setLooping(false);
        heroSS2SFX.start();
    }

    public void releaseHeroSSSFX(){
        if (heroSS1SFX != null && heroSS1SFX.isPlaying()){
            heroSS1SFX.release();
        }
        if (heroSS2SFX != null && heroSS2SFX.isPlaying()){
            heroSS2SFX.release();
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

    @Override
    public void onPause(){
        super.onPause();
        if(musicPlayerService!=null){
            musicPlayerService.pauseMusic();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(musicPlayerService!=null){
            if(musicPlayerService.currentTrack ==  3){
                musicPlayerService.unpauseMusic();
            }else{
                musicPlayerService.playMusic(3);
            }

        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicPlayerService.MyBinder binder = (MusicPlayerService.MyBinder) iBinder;
        if(binder != null) {
            musicPlayerService = binder.getService();
            musicPlayerService.playMusic(3);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }


}