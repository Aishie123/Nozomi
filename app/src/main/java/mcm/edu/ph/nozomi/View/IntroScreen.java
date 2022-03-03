package mcm.edu.ph.nozomi.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import mcm.edu.ph.nozomi.Model.HeroData;
import mcm.edu.ph.nozomi.R;

public class IntroScreen extends AppCompatActivity {

    private TextView nameQuestion, enemyQuestion;
    private EditText userInput;
    private ImageView btnBack;
    private ImageButton btnNext;
    private String userName, enemyName;
    private String TAG = "IntroScreen";
    private MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_intro_screen);

        nameQuestion = findViewById(R.id.nameQuestion);
        enemyQuestion = findViewById(R.id.enemyQuestion);
        btnBack = findViewById(R.id.btnBack);
        userInput = findViewById(R.id.userInput);
        btnNext = findViewById(R.id.btnStart);

        playBGMusic();
        userInput();


    }

    public void playBGMusic(){
        music = MediaPlayer.create(this, R.raw.music_intro);
        music.setLooping(true); // Set looping
        music.setVolume(100,100);
        music.start();
    }

    public void userInput(){

            btnNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    HeroData hero = new HeroData();
                    if (v.getId() == R.id.btnStart){

                        enemyQuestion.setVisibility(View.VISIBLE);
                        nameQuestion.setVisibility(View.INVISIBLE);
                        btnBack.setVisibility(View.VISIBLE);

                        userName = userInput.getText().toString();
                        userInput.setText("");

                        Log.d(TAG, "The user's name is " + userName);
                        hero.setName(userName);

                        btnNext.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                enemyName = userInput.getText().toString();
                                Log.d(TAG, "The enemy's name is " + enemyName);
                                Intent i = new Intent(IntroScreen.this, BattleScreen.class);
                                i.putExtra("enemy", enemyName);
                                i.putExtra("user", userName);
                                startActivity(i);
                                finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                music.release();

                            }
                        });
                    }
                    else{
                        enemyQuestion.setVisibility(View.INVISIBLE);
                        nameQuestion.setVisibility(View.VISIBLE);
                        btnBack.setVisibility(View.INVISIBLE);
                    }
                }
            });
    }

    public void back(View v){
        enemyQuestion.setVisibility(View.INVISIBLE);
        nameQuestion.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.INVISIBLE);
        userInput.setText("");
        userInput();
    }


}
