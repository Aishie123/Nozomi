package mcm.edu.ph.dones_turnbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mcm.edu.ph.dones_turnbasedgame.Controller.BattleRandomizer;
import mcm.edu.ph.dones_turnbasedgame.Model.DialogueData;
import mcm.edu.ph.dones_turnbasedgame.Model.EnemySelector;
import mcm.edu.ph.dones_turnbasedgame.Model.HeroSelector;
import mcm.edu.ph.dones_turnbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.dones_turnbasedgame.Controller.DialogueRandomizer;
import mcm.edu.ph.dones_turnbasedgame.Model.HeroData;
import mcm.edu.ph.dones_turnbasedgame.Model.EnemyData;
import mcm.edu.ph.dones_turnbasedgame.R;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "SwitchStatementWithoutDefaultBranch"})
public class BattleScreen extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private ImageView enemy_idleSprite, enemy_hitSprite, enemy_deathSprite, enemy_runSprite, enemy_atkSprite,
            hero_idleSprite, hero_runSprite, hero_atkSprite, hero_ss1Sprite, hero_ss2Sprite, hero_hitSprite, hero_deathSprite, btnMenu;
    private ImageButton btnSS1, btnSS2, btnTurn;
    private TextView txtLog, txtQuote, txtBtn, txtEnemy, txtUser, txtHeroHP, txtHeroMP, txtEnemyHP;
    private View heroHPBar, heroMPBar, enemyHPBar;
    private Handler timer;

    private double heroHPB, heroMPB, enemyHPB; //for HP bar
    private int fullHeroHP, curHeroHP, fullHeroMP, curHeroMP, fullEnemyHP, curEnemyHP, quoteCounter, enemyNum;
    private int HERO_RUN_DUR, HERO_ATK_DUR, HERO_SS1_DUR, HERO_SS2_DUR, HERO_HIT_DUR, HERO_DEATH_DUR,
            ENEMY_RUN_DUR, ENEMY_ATK_DUR, ENEMY_HIT_DUR, ENEMY_DEATH_DUR;
    private float HERO_ORIG_POS, ENEMY_ORIG_POS, HERO_RUN1_POS, HERO_RUN2_POS, HERO_ATK_POS, HERO_SS1_POS,
            ENEMY_RUN1_POS, ENEMY_ATK_POS;
    private final String TAG = "BattleScreen";
    private boolean noRestart = false; // enable restart in the menu

    private AnimationDrawable hero_idleAnim, hero_runAnim, hero_atkAnim, hero_ss1Anim, hero_ss2Anim, hero_hitAnim, hero_deathAnim,
            enemy_idleAnim, enemy_walkAnim, enemy_atkAnim, enemy_hitAnim, enemy_deathAnim;
    private ObjectAnimator heroRun, enemyWalk;

    private MusicPlayerService musicPlayerService;
    private HeroData hero = new HeroData();
    private EnemyData enemy = new EnemyData();
    private final BattleRandomizer battleR = new BattleRandomizer();
    private final DialogueData dialogue = new DialogueData();
    private final DialogueRandomizer dialogueR = new DialogueRandomizer(this);
    private final EnemySelector selectEnemy = new EnemySelector(this);

    private int counter = 0; // turn counter
    private int SS1C = 8;
    private int SS2C = 10;
    private int heroNum = 1; // temporary

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_battle_screen);

        btnMenu = findViewById(R.id.btnBattleMenu);
        btnTurn = findViewById(R.id.btnStart);
        btnSS1 = findViewById(R.id.btnSS1);
        btnSS2 = findViewById(R.id.btnSS2);

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

        enemy_idleSprite = findViewById(R.id.enemy_idleSprite);
        enemy_runSprite = findViewById(R.id.enemy_walkSprite);
        enemy_atkSprite = findViewById(R.id.enemy_attackSprite);
        enemy_hitSprite = findViewById(R.id.enemy_hitSprite);
        enemy_deathSprite = findViewById(R.id.enemy_deathSprite);

        hero_idleSprite = findViewById(R.id.hero_idleSprite);
        hero_runSprite = findViewById(R.id.hero_runSprite);
        hero_atkSprite = findViewById(R.id.hero_attkSprite);
        hero_ss1Sprite = findViewById(R.id.hero_ss1Sprite);
        hero_ss2Sprite = findViewById(R.id.hero_ss2Sprite);
        hero_hitSprite = findViewById(R.id.hero_hitSprite);
        hero_deathSprite = findViewById(R.id.hero_deathSprite);

        hero_idleSprite.setImageResource(R.drawable.hero1_idleanim);
        hero_runSprite.setImageResource(R.drawable.hero1_runanim);
        hero_atkSprite.setImageResource(R.drawable.hero1_attackanim);
        hero_ss1Sprite.setImageResource(R.drawable.hero1_ss1anim);
        hero_ss2Sprite.setImageResource(R.drawable.hero1_ss2anim);
        hero_hitSprite.setImageResource(R.drawable.hero1_hitanim);
        hero_deathSprite.setImageResource(R.drawable.hero1_deathanim);

        Intent intent = getIntent();
        String enemyName = intent.getExtras().getString("enemy");
        String userName = intent.getExtras().getString("user");

        hero = new HeroData(userName, 1, 150, 15, 10, 7, 5, 4, 15, 20);
        fullHeroHP = hero.getHealthPt();
        curHeroHP = fullHeroHP;
        fullHeroMP = hero.getManaPt();
        curHeroMP = fullHeroMP;

        enemy = new EnemyData(enemyName, 10, 40, 300, 20 );
        fullEnemyHP = enemy.getHealthPt();
        curEnemyHP = fullEnemyHP;

        txtEnemy.setText(enemyName);
        txtUser.setText(userName);
        txtHeroHP.setText(String.valueOf(curHeroHP));
        txtHeroMP.setText(String.valueOf(curHeroMP));
        txtEnemyHP.setText(String.valueOf(curEnemyHP));

        btnTurn.setOnClickListener(this);
        btnSS1.setOnClickListener(this);
        btnSS2.setOnClickListener(this);

        enemy_deathSprite.setVisibility(View.INVISIBLE);
        hero_deathSprite.setVisibility(View.INVISIBLE);
        enemy_idleSprite.setVisibility(View.VISIBLE);
        hero_idleSprite.setVisibility(View.VISIBLE);

        quoteCounter = dialogueR.dialogueAtkRandomizer();

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        timer = new Handler(Looper.getMainLooper()); // for delay

        generateHero();
        generateEnemy();
        enableTurnOnly();
        idleSprite();
        heroHPBar();
        heroMPBar();
        enemyHPBar();
        press();
    }

    public void generateHero(){
        final HeroSelector hero = new HeroSelector();
        heroNum--;
        HERO_RUN1_POS = hero.getRun1Pos(heroNum);
        HERO_RUN2_POS = hero.getRun2Pos(heroNum);
        HERO_ATK_POS = hero.getAtkPos(heroNum);
        HERO_SS1_POS = hero.getSS1Pos(heroNum);
        HERO_RUN_DUR = hero.getRunDur(heroNum);
        HERO_ATK_DUR = hero.getAtkDur(heroNum);
        HERO_SS1_DUR = hero.getSS1Dur(heroNum);
        HERO_SS2_DUR = hero.getSS2Dur(heroNum);
        HERO_HIT_DUR = hero.getHitDur(heroNum);
        HERO_DEATH_DUR = hero.getDeathDur(heroNum);
    }

    public void generateEnemy(){
        enemyNum = battleR.randomizeEnemy();
        enemy_idleSprite.setImageResource(selectEnemy.getIdleSprite(enemyNum));
        enemy_runSprite.setImageResource(selectEnemy.getRunSprite(enemyNum));
        enemy_atkSprite.setImageResource(selectEnemy.getAtkSprite(enemyNum));
        enemy_hitSprite.setImageResource(selectEnemy.getHitSprite(enemyNum));
        enemy_deathSprite.setImageResource(selectEnemy.getDeathSprite(enemyNum));
        ENEMY_RUN1_POS = selectEnemy.getRun1Pos(enemyNum);
        ENEMY_ATK_POS = selectEnemy.getAtkPos(enemyNum);
        ENEMY_RUN_DUR = selectEnemy.getRunDur(enemyNum);
        ENEMY_ATK_DUR = selectEnemy.getAtkDur(enemyNum);
        ENEMY_HIT_DUR = selectEnemy.getHitDur(enemyNum);
        ENEMY_DEATH_DUR = selectEnemy.getDeathDur(enemyNum);
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

        btnMenu.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnMenu.setImageResource(R.drawable.btn_p_menu);
                    Log.d(TAG, "btnMenu pressed");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnMenu.setImageResource(R.drawable.btn_up_menu);
                    Log.d(TAG, "btnMenu unpressed");
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

        hero_idleAnim = (AnimationDrawable) hero_idleSprite.getDrawable();
        hero_idleAnim.start(); // starts "idle" animation

        enemy_idleAnim = (AnimationDrawable) enemy_idleSprite.getDrawable();
        enemy_idleAnim.start(); // starts "idle" animation

        hero_idleSprite.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                HERO_ORIG_POS = hero_idleSprite.getLeft(); // gets X position of hero
                hero_idleSprite.getViewTreeObserver().removeOnGlobalLayoutListener(this); // removes the listener to prevent being called again
            }
        });

        enemy_idleSprite.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                ENEMY_ORIG_POS = enemy_idleSprite.getLeft(); // gets X position of enemy
                enemy_idleSprite.getViewTreeObserver().removeOnGlobalLayoutListener(this); // removes the listener to prevent being called again
            }
        });

        if (enemyNum == 1){
            ENEMY_ORIG_POS-=50f;
        }
        enemy_idleSprite.setX(ENEMY_ORIG_POS);
        enemy_runSprite.setX(ENEMY_ORIG_POS);
        enemy_atkSprite.setX(ENEMY_ORIG_POS);
        enemy_hitSprite.setX(ENEMY_ORIG_POS);
        enemy_deathSprite.setX(ENEMY_ORIG_POS);
    }


    //HERO SPRITES ----------------------------------------------------------------------------------------------------------------
    // animation for when hero runs
    public void heroRunSprite(){
        hero_runSprite.setVisibility(View.VISIBLE);
        hero_idleSprite.setVisibility(View.INVISIBLE);

        hero_runAnim = (AnimationDrawable) hero_runSprite.getDrawable();
        heroRun.setDuration(HERO_RUN_DUR); // sets duration of movement to 0.8 seconds, same as the animation duration

        heroRun.start(); // moves the position of the sprite
        hero_runAnim.start(); // starts "run" animation

        heroRun.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                hero_runSprite.setVisibility(View.INVISIBLE);
                hero_runAnim.stop();
                hero_runSprite.setX(HERO_ORIG_POS); // moves the sprite back to original position when done and invisible
            }
        });
    }

    // animation for when hero attacks
    public void heroAtkSprite(){
        hero_atkSprite.setVisibility(View.VISIBLE);
        hero_atkSprite.setX(HERO_ATK_POS);

        hero_atkAnim = (AnimationDrawable) hero_atkSprite.getDrawable();
        hero_atkAnim.setOneShot(true);
        hero_atkAnim.start(); // starts "attack" animation
    }

    // animation for when hero takes hit
    public void heroHitSprite(){
        hero_hitSprite.setVisibility(View.VISIBLE);

        hero_hitAnim = (AnimationDrawable) hero_hitSprite.getDrawable();
        hero_hitAnim.setOneShot(true);
        hero_hitAnim.start(); // starts "take hit" animation
    }

    public void heroSS1Sprite(){
        hero_ss1Sprite.setVisibility(View.VISIBLE);
        hero_ss1Sprite.setX(HERO_SS1_POS);

        hero_ss1Anim = (AnimationDrawable) hero_ss1Sprite.getDrawable();
        hero_ss1Anim.setOneShot(true);
        hero_ss1Anim.start(); // starts "Double Slash" animation
    }

    public void heroSS2Sprite(){
        hero_ss2Sprite.setVisibility(View.VISIBLE);

        hero_ss2Anim = (AnimationDrawable) hero_ss2Sprite.getDrawable();
        hero_ss2Anim.setOneShot(true);
        hero_ss2Anim.start(); // starts "Healing Shield" animation
    }

    public void heroDeathSprite(){
        hero_deathSprite.setVisibility(View.VISIBLE);

        hero_deathAnim = (AnimationDrawable) hero_deathSprite.getDrawable();
        hero_deathAnim.setOneShot(true);
        hero_deathAnim.start(); // starts "death" animation
    }


    //ENEMY SPRITES ---------------------------------------------------------------------------------------------------------------

    public void enemyWalkSprite(){
        enemy_runSprite.setVisibility(View.VISIBLE);
        enemy_idleSprite.setVisibility(View.INVISIBLE);

        enemy_walkAnim = (AnimationDrawable) enemy_runSprite.getDrawable();
        enemyWalk = ObjectAnimator.ofFloat(enemy_runSprite,"translationX", ENEMY_RUN1_POS);
        enemyWalk.setDuration(ENEMY_RUN_DUR);

        enemyWalk.start(); // moves the position of the sprite
        enemy_walkAnim.start(); // starts "walk" animation

        enemyWalk.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                enemy_runSprite.setVisibility(View.INVISIBLE);
                enemy_walkAnim.stop();
                enemy_runSprite.setX(ENEMY_ORIG_POS); // moves the sprite back to original position when done and invisible
            }
        });

    }

    public void enemyAtkSprite(){
        enemy_atkSprite.setVisibility(View.VISIBLE);
        enemy_atkSprite.setX(ENEMY_ATK_POS);

        enemy_atkAnim = (AnimationDrawable) enemy_atkSprite.getDrawable();
        enemy_atkAnim.setOneShot(true);
        enemy_atkAnim.start(); // starts "attack" animation
    }

    public void enemyDeathSprite(){
        enemy_deathSprite.setVisibility(View.VISIBLE);

        enemy_deathAnim = (AnimationDrawable) enemy_deathSprite.getDrawable();
        enemy_deathAnim.setOneShot(true);
        enemy_deathAnim.start(); // starts "take hit" animation

    }

    public void enemyHitSprite(){
        enemy_hitSprite.setVisibility(View.VISIBLE);

        enemy_hitAnim = (AnimationDrawable) enemy_hitSprite.getDrawable();
        enemy_hitAnim.setOneShot(true);
        enemy_hitAnim.start(); // starts "death" animation
    }

    // onClick --------------------------------------------------------------------------------------------
    @SuppressLint({"SetTextI18n", "NonConstantResourceId", "ObsoleteSdkInt"})
    public void onClick(View v) {
        int sfxN = battleR.randomizeAtkSFX();
        int hero1AtkN = battleR.randomizeAttack(hero.getAtkMin(),hero.getAtkMax());
        int enemy1AtkN = battleR.randomizeAttack(enemy.getAtkMin(),enemy.getAtkMax());

        switch (v.getId()){
            case R.id.btnStart:

                //restart battleR --------------------------------------------------------------------------------------------
                if (counter == -1){
                    recreate();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

                //start of battleR --------------------------------------------------------------------------------------------
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
                    txtQuote.setText("'" + dialogue.getHeroAtkDialogue(quoteCounter) + "'");
                    heroRun = ObjectAnimator.ofFloat(hero_runSprite,"translationX", HERO_RUN1_POS);
                    disableButtons();
                    heroRunSprite();

                    heroRun.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {

                            musicPlayerService.playHeroAtkSFX(sfxN);
                            heroAtkSprite();

                            timer.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    enemy_idleSprite.setVisibility(View.INVISIBLE);
                                    enemyHitSprite();

                                    timer.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            enableTurnOnly();

                                            txtLog.setText(hero.getName() + " dealt "+ hero1AtkN + " damage to the enemy.");
                                            curEnemyHP -= hero1AtkN;
                                            negativeHealthCheck();
                                            enemyHPBar();

                                            hero_atkAnim.stop();
                                            hero_atkSprite.setVisibility(View.INVISIBLE);
                                            hero_idleSprite.setVisibility(View.VISIBLE);

                                            enemy_hitAnim.stop();
                                            enemy_hitSprite.setVisibility(View.INVISIBLE);
                                            enemy_idleSprite.setVisibility(View.VISIBLE);

                                            counter++;
                                            quoteCounter = dialogueR.dialogueAtkRandomizer();

                                            if(curEnemyHP <= 0){

                                                musicPlayerService.pauseMusic();
                                                musicPlayerService.playMusic(4);

                                                enemyDeathSprite();
                                                enemy_idleSprite.setVisibility(View.INVISIBLE);

                                                timer.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        enableTurnOnly();
                                                        txtQuote.setText("");
                                                        txtLog.setText(hero.getName() + " dealt "+ hero1AtkN + " damage to the enemy.\nYou won!");
                                                        counter = -1;
                                                        txtBtn.setText("Restart");
                                                    }
                                                }, ENEMY_DEATH_DUR);
                                            }
                                        }
                                    }, ENEMY_HIT_DUR);
                                }
                            }, HERO_ATK_DUR);
                        }
                    });
                }

                //enemy's turn --------------------------------------------------------------------------------------------
                else if(counter%2 !=1) {
                    txtBtn.setText("Attack");
                    txtQuote.setText("'" + dialogue.getEnemyAtkDialogue(quoteCounter) + "'");
                    disableButtons();
                    enemyWalkSprite();

                    enemyWalk.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {

                            musicPlayerService.playEnemyAtkSFX(enemyNum, sfxN);
                            enemyAtkSprite();

                            timer.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hero_idleSprite.setVisibility(View.INVISIBLE);

                                    heroHitSprite();

                                    timer.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            enableButtons();

                                            txtLog.setText(enemy.getName() + " dealt " + enemy1AtkN + " damage to the hero.");
                                            curHeroHP -= enemy1AtkN;
                                            negativeHealthCheck();
                                            heroHPBar();

                                            enemy_atkAnim.stop();
                                            enemy_atkSprite.setVisibility(View.INVISIBLE);
                                            enemy_idleSprite.setVisibility(View.VISIBLE);

                                            hero_hitAnim.stop();
                                            hero_hitSprite.setVisibility(View.INVISIBLE);
                                            hero_idleSprite.setVisibility(View.VISIBLE);

                                            counter++;

                                            if(curHeroHP <= 0){
                                                musicPlayerService.pauseMusic();
                                                musicPlayerService.playMusic(5);

                                                hero_deathSprite.setImageResource(R.drawable.hero1_deathanim);
                                                disableButtons();
                                                heroDeathSprite();
                                                hero_idleSprite.setVisibility(View.INVISIBLE);

                                                timer.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        enableTurnOnly();
                                                        txtQuote.setText("");
                                                        txtLog.setText(enemy.getName() + " dealt " + enemy1AtkN + " damage to the hero.\nGame over!");
                                                        counter = -1;
                                                        txtBtn.setText("Restart");
                                                    }
                                                }, HERO_DEATH_DUR);
                                            }
                                        }
                                    }, HERO_HIT_DUR);
                                }
                            }, ENEMY_ATK_DUR);

                        }
                    });
                }
                break;


            //SS1 - Double Slash --------------------------------------------------------------------------------------------
            case R.id.btnSS1:
                if ((curHeroMP-SS1C)<0){
                    Toast.makeText(getApplicationContext(),"Your MP is not enough for this skill.",Toast.LENGTH_LONG).show();
                }
                else{
                    curHeroMP-=SS1C;
                    heroMPBar();
                    txtQuote.setText(dialogueR.dialogueHeroSS());
                    txtBtn.setText("Next Turn");
                    heroRun = ObjectAnimator.ofFloat(hero_runSprite,"translationX", HERO_RUN2_POS);
                    heroRunSprite();
                    disableButtons();

                    heroRun.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            musicPlayerService.playHeroSS1SFX();
                            heroSS1Sprite();

                            timer.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    enemy_idleSprite.setVisibility(View.INVISIBLE);
                                    enemyHitSprite();

                                    timer.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            enableTurnOnly();

                                            txtLog.setText(hero.getName() + " used Double Slash, and dealt " + (hero1AtkN * 2) + " damage to the enemy.");
                                            curEnemyHP -= (hero1AtkN * 2);
                                            negativeHealthCheck();
                                            enemyHPBar();

                                            hero_ss1Anim.stop();
                                            hero_ss1Sprite.setVisibility(View.INVISIBLE);
                                            hero_idleSprite.setVisibility(View.VISIBLE);

                                            enemy_hitAnim.stop();
                                            enemy_hitSprite.setVisibility(View.INVISIBLE);
                                            enemy_idleSprite.setVisibility(View.VISIBLE);
                                            counter++;

                                            if (curEnemyHP <= 0) {
                                                musicPlayerService.pauseMusic();
                                                musicPlayerService.playMusic(4);

                                                disableButtons();
                                                enemyDeathSprite();
                                                enemy_idleSprite.setVisibility(View.INVISIBLE);

                                                timer.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        enableTurnOnly();
                                                        txtQuote.setText("");
                                                        txtLog.setText(hero.getName() + " used Double Slash, and dealt " + (hero1AtkN * 2) + " damage to the enemy. You won!");
                                                        counter = -1;
                                                        txtBtn.setText("Restart");
                                                    }
                                                }, ENEMY_DEATH_DUR);
                                            }
                                        }
                                    }, ENEMY_HIT_DUR);
                                }
                            }, HERO_SS1_DUR);
                        }
                    });
                }
                break;


            //SS2 - Healing Shield --------------------------------------------------------------------------------------------
            case R.id.btnSS2:
                if ((curHeroMP-SS2C)<0){
                    Toast.makeText(getApplicationContext(),"Your MP is not enough for this skill.",Toast.LENGTH_LONG).show();
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
                    txtQuote.setText(dialogueR.dialogueHeroSS());
                    musicPlayerService.playHeroSS2SFX();
                    heroSS2Sprite();
                    timer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enableTurnOnly();
                            hero_ss2Sprite.setVisibility(View.INVISIBLE);
                            txtLog.setText(hero.getName() + " used Healing Shield, and regained 50% health");
                            txtBtn.setText("Next Turn");
                            heroHPBar();
                            heroMPBar();
                            counter++;
                        }
                    }, HERO_SS2_DUR);
                }
                break;
        }
    }

    // opens menu
    public void battleToMenu(View v){
        Intent goToMenu = new Intent(BattleScreen.this, MenuScreen.class);
        goToMenu.putExtra("no restart", noRestart);
        startActivity(goToMenu);
    }


    // call this method to disable buttons ----------------------------------------------------------------------------------------------------------
    public void disableButtons(){
        btnTurn.setEnabled(false);
        btnSS1.setEnabled(false);
        btnSS2.setEnabled(false);
        btnSS1.setAlpha(0.5f);
        btnSS2.setAlpha(0.5f);
    }

    // call this method to enable buttons -----------------------------------------------------------------------------------------------------------
    public void enableButtons(){
        btnTurn.setEnabled(true);
        btnSS1.setEnabled(true);
        btnSS2.setEnabled(true);
        btnSS1.setAlpha(1f);
        btnSS2.setAlpha(1f);
    }

    // call this method to enable turn button only ---------------------------------------------------------------------------------------------------
    public void enableTurnOnly(){
        btnTurn.setEnabled(true);
        btnSS1.setEnabled(false);
        btnSS2.setEnabled(false);
        btnSS1.setAlpha(0.5f);
        btnSS2.setAlpha(0.5f);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------

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