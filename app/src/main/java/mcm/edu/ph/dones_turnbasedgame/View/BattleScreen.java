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
import android.media.MediaPlayer;
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

import mcm.edu.ph.dones_turnbasedgame.Controller.BattleAlgorithm;
import mcm.edu.ph.dones_turnbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.dones_turnbasedgame.Controller.QuoteRandomizer;
import mcm.edu.ph.dones_turnbasedgame.Controller.SfxController;
import mcm.edu.ph.dones_turnbasedgame.Model.HeroData;
import mcm.edu.ph.dones_turnbasedgame.Model.MonsterData;
import mcm.edu.ph.dones_turnbasedgame.R;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "SwitchStatementWithoutDefaultBranch"})
public class BattleScreen extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private ImageView enemy1_idleSprite, enemy1_hitSprite, enemy1_deathSprite, enemy1_walkSprite, enemy1_atkSprite,
            hero1_idleSprite, hero1_runSprite, hero1_atkSprite, hero1_ss1Sprite, hero1_ss2Sprite, hero1_hitSprite, hero1_deathSprite, btnMenu;
    private ImageButton btnSS1, btnSS2, btnTurn;
    private TextView txtLog, txtQuote, txtBtn, txtEnemy, txtUser, txtHeroHP, txtHeroMP, txtEnemyHP;
    private View heroHPBar, heroMPBar, enemyHPBar;
    private Handler timer;

    private double heroHPB, heroMPB, enemyHPB; //for HP bar
    private int fullHeroHP, curHeroHP, fullHeroMP, curHeroMP, fullEnemyHP, curEnemyHP, quoteCounter, n;
    private final String TAG = "BattleScreen";
    private boolean noRestart = false; // can restart in the menu

    private AnimationDrawable hero_idleAnim, hero_runAnim, hero_atkAnim, hero_ss1Anim, hero_ss2Anim, hero_hitAnim, hero_deathAnim,
            enemy_idleAnim, enemy_walkAnim, enemy_atkAnim, enemy_hitAnim, enemy_deathAnim;
    private ObjectAnimator heroRun, enemyWalk;

    private MusicPlayerService musicPlayerService;
    private HeroData hero = new HeroData();
    private MonsterData enemy = new MonsterData();
    private BattleAlgorithm battle = new BattleAlgorithm();
    private QuoteRandomizer quote = new QuoteRandomizer(this);
    private SfxController sfx = new SfxController();

    private int counter = 0;
    private int SS1C = 8;
    private int SS2C = 10;

    private float HERO1_ORIG_POS;
    private final float HERO1_RUN1_POS = 725f;
    private final float HERO1_RUN2_POS = 500f;
    private final float HERO1_ATK_POS = 700f;
    private final float HERO1_SS1_POS = 500f;
    private final float ENEMY1_WALK_POS = -800f;
    private float ENEMY1_ORIG_POS;
    private final float ENEMY1_ATK_POS = 200f;

    private final int HERO1_RUN_DUR = 800; // 0.8 seconds
    private final int HERO1_ATK_DUR = 300; // 0.3 seconds (not exact duration; delay before hit)
    private final int HERO1_SS1_DUR = 300; // 0.3 seconds (not exact duration; delay before hit)
    private final int HERO1_SS2_DUR = 600; // 0.6 seconds
    private final int HERO1_HIT_DUR = 650; // 0.65 seconds
    private final int HERO1_DEATH_DUR = 1100; // 1.1 seconds
    private final int ENEMY1_WALK_DUR = 1600; // 1.6 seconds
    private final int ENEMY1_ATK_DUR = 300; // 0.3 seconds (not exact duration; delay before hit)
    private final int ENEMY1_HIT_DUR = 600; // 0.6 seconds
    private final int ENEMY1_DEATH_DUR = 600; // 0.6 seconds


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

        btnTurn.setOnClickListener(this);
        btnSS1.setOnClickListener(this);
        btnSS2.setOnClickListener(this);

        enemy1_deathSprite.setVisibility(View.INVISIBLE);
        hero1_deathSprite.setVisibility(View.INVISIBLE);
        enemy1_idleSprite.setVisibility(View.VISIBLE);
        hero1_idleSprite.setVisibility(View.VISIBLE);

        quoteCounter = quote.quoteCounter();

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        timer = new Handler(Looper.getMainLooper()); // for delay

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

        hero_idleAnim = (AnimationDrawable)hero1_idleSprite.getDrawable();
        hero_idleAnim.start(); // starts "idle" animation

        enemy_idleAnim = (AnimationDrawable)enemy1_idleSprite.getDrawable();
        enemy_idleAnim.start(); // starts "idle" animation

        hero1_idleSprite.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                HERO1_ORIG_POS = hero1_idleSprite.getLeft(); // gets X position of hero
                hero1_idleSprite.getViewTreeObserver().removeOnGlobalLayoutListener(this); // removes the listener to prevent being called again
            }
        });

        enemy1_idleSprite.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                ENEMY1_ORIG_POS = enemy1_idleSprite.getLeft(); // gets X position of enemy
                enemy1_idleSprite.getViewTreeObserver().removeOnGlobalLayoutListener(this); // removes the listener to prevent being called again
            }
        });

    }


    //HERO SPRITES ----------------------------------------------------------------------------------------------------------------
    // animation for when hero runs
    public void heroRunSprite(){
        hero1_runSprite.setVisibility(View.VISIBLE);
        hero1_idleSprite.setVisibility(View.INVISIBLE);

        hero_runAnim = (AnimationDrawable)hero1_runSprite.getDrawable();
        heroRun.setDuration(HERO1_RUN_DUR); // sets duration of movement to 0.8 seconds, same as the animation duration

        heroRun.start(); // moves the position of the sprite
        hero_runAnim.start(); // starts "run" animation

        heroRun.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                hero1_runSprite.setVisibility(View.INVISIBLE);
                hero_runAnim.stop();
                hero1_runSprite.setX(HERO1_ORIG_POS); // moves the sprite back to original position when done and invisible
            }
        });
    }

    // animation for when hero attacks
    public void heroAtkSprite(){
        hero1_atkSprite.setVisibility(View.VISIBLE);
        hero1_atkSprite.setX(HERO1_ATK_POS);

        hero_atkAnim = (AnimationDrawable) hero1_atkSprite.getDrawable();
        hero_atkAnim.setOneShot(true);
        hero_atkAnim.start(); // starts "attack" animation
    }

    // animation for when hero takes hit
    public void heroHitSprite(){
        hero1_hitSprite.setVisibility(View.VISIBLE);

        hero_hitAnim = (AnimationDrawable)hero1_hitSprite.getDrawable();
        hero_hitAnim.setOneShot(true);
        hero_hitAnim.start(); // starts "take hit" animation
    }

    public void heroSS1Sprite(){
        hero1_ss1Sprite.setVisibility(View.VISIBLE);
        hero1_ss1Sprite.setX(HERO1_SS1_POS);

        hero_ss1Anim = (AnimationDrawable) hero1_ss1Sprite.getDrawable();
        hero_ss1Anim.setOneShot(true);
        hero_ss1Anim.start(); // starts "Double Slash" animation
    }

    public void heroSS2Sprite(){
        hero1_ss2Sprite.setVisibility(View.VISIBLE);

        hero_ss2Anim = (AnimationDrawable)hero1_ss2Sprite.getDrawable();
        hero_ss2Anim.setOneShot(true);
        hero_ss2Anim.start(); // starts "Healing Shield" animation
    }

    public void heroDeathSprite(){
        hero1_deathSprite.setVisibility(View.VISIBLE);

        hero_deathAnim = (AnimationDrawable)hero1_deathSprite.getDrawable();
        hero_deathAnim.setOneShot(true);
        hero_deathAnim.start(); // starts "death" animation
    }


    //ENEMY SPRITES ---------------------------------------------------------------------------------------------------------------

    public void enemyWalkSprite(){
        enemy1_walkSprite.setVisibility(View.VISIBLE);
        enemy1_idleSprite.setVisibility(View.INVISIBLE);

        enemy_walkAnim = (AnimationDrawable)enemy1_walkSprite.getDrawable();
        enemyWalk = ObjectAnimator.ofFloat(enemy1_walkSprite,"translationX",ENEMY1_WALK_POS);
        enemyWalk.setDuration(ENEMY1_WALK_DUR);

        enemyWalk.start(); // moves the position of the sprite
        enemy_walkAnim.start(); // starts "walk" animation

        enemyWalk.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                enemy1_walkSprite.setVisibility(View.INVISIBLE);
                enemy_walkAnim.stop();
                enemy1_walkSprite.setX(ENEMY1_ORIG_POS); // moves the sprite back to original position when done and invisible
            }
        });

    }

    public void enemyAtkSprite(){
        enemy1_atkSprite.setVisibility(View.VISIBLE);
        enemy1_atkSprite.setX(ENEMY1_ATK_POS);

        enemy_atkAnim = (AnimationDrawable) enemy1_atkSprite.getDrawable();
        enemy_atkAnim.setOneShot(true);
        enemy_atkAnim.start(); // starts "attack" animation
    }

    public void enemyDeathSprite(){
        enemy1_deathSprite.setVisibility(View.VISIBLE);

        enemy_deathAnim = (AnimationDrawable)enemy1_deathSprite.getDrawable();
        enemy_deathAnim.setOneShot(true);
        enemy_deathAnim.start(); // starts "take hit" animation

    }

    public void enemyHitSprite(){
        enemy1_hitSprite.setVisibility(View.VISIBLE);

        enemy_hitAnim = (AnimationDrawable)enemy1_hitSprite.getDrawable();
        enemy_hitAnim.setOneShot(true);
        enemy_hitAnim.start(); // starts "death" animation
    }

    // onClick --------------------------------------------------------------------------------------------
    @SuppressLint({"SetTextI18n", "NonConstantResourceId", "ObsoleteSdkInt"})
    public void onClick(View v) {
        n = sfx.randomizeAtkSFX();
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
                    heroRun = ObjectAnimator.ofFloat(hero1_runSprite,"translationX", HERO1_RUN1_POS);
                    disableButtons();
                    heroRunSprite();

                    heroRun.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {

                            musicPlayerService.playHeroAtkSFX(n);
                            heroAtkSprite();

                            timer.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    enemy1_idleSprite.setVisibility(View.INVISIBLE);
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

                                                timer.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        enableTurnOnly();
                                                        txtQuote.setText("");
                                                        enemy_deathAnim.stop();
                                                        enemy1_deathSprite.setImageResource(R.drawable.enemy1_death4);
                                                        txtLog.setText(hero.getName() + " dealt "+ hero1AtkN + " damage to the enemy.\nYou won!");
                                                        counter = -1;
                                                        txtBtn.setText("Restart");
                                                    }
                                                }, ENEMY1_DEATH_DUR);
                                            }
                                        }
                                    }, ENEMY1_HIT_DUR);
                                }
                            }, HERO1_ATK_DUR);
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

                            musicPlayerService.playEnemyAtkSFX(n);
                            enemyAtkSprite();

                            timer.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hero1_idleSprite.setVisibility(View.INVISIBLE);

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

                                                timer.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        enableTurnOnly();
                                                        txtQuote.setText("");
                                                        hero_deathAnim.stop();
                                                        hero1_deathSprite.setImageResource(R.drawable.hero1_death11);
                                                        txtLog.setText(enemy.getName() + " dealt " + enemy1AtkN + " damage to the hero.\nGame over!");
                                                        counter = -1;
                                                        txtBtn.setText("Restart");
                                                    }
                                                }, HERO1_DEATH_DUR);
                                            }
                                        }
                                    }, HERO1_HIT_DUR);
                                }
                            }, ENEMY1_ATK_DUR);

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
                    txtQuote.setText(quote.quoteHeroSS());
                    txtBtn.setText("Next Turn");
                    heroRun = ObjectAnimator.ofFloat(hero1_runSprite,"translationX", HERO1_RUN2_POS);
                    heroRunSprite();
                    disableButtons();

                    heroRun.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            musicPlayerService.playHeroSS1SFX();
                            heroSS1Sprite();

                            timer.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    enemy1_idleSprite.setVisibility(View.INVISIBLE);
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

                                                timer.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        enableTurnOnly();
                                                        txtQuote.setText("");
                                                        enemy_deathAnim.stop();
                                                        enemy1_deathSprite.setImageResource(R.drawable.enemy1_death4);
                                                        txtLog.setText(hero.getName() + " used Double Slash, and dealt " + (hero1AtkN * 2) + " damage to the enemy. You won!");
                                                        counter = -1;
                                                        txtBtn.setText("Restart");
                                                    }
                                                }, ENEMY1_DEATH_DUR);
                                            }
                                        }
                                    }, ENEMY1_HIT_DUR);
                                }
                            }, HERO1_SS1_DUR);
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
                    txtQuote.setText(quote.quoteHeroSS());
                    musicPlayerService.playHeroSS2SFX();
                    heroSS2Sprite();
                    timer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enableTurnOnly();
                            hero1_ss2Sprite.setVisibility(View.INVISIBLE);
                            txtLog.setText(hero.getName() + " used Healing Shield, and regained 50% health");
                            txtBtn.setText("Next Turn");
                            heroHPBar();
                            heroMPBar();
                            counter++;
                        }
                    }, HERO1_SS2_DUR);
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