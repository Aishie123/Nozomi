package mcm.edu.ph.nozomi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView enemy1_idleSprite, hero1_idleSprite;
    ImageButton btnDefend, btnAttack;
    String TAG = "btn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_main);

        SpriteIdle();

        btnAttack = (ImageButton) findViewById(R.id.btnAttack);
        btnDefend = (ImageButton) findViewById(R.id.btnDefend);
        press();
    }


    public void SpriteIdle(){
        enemy1_idleSprite = (ImageView)findViewById(R.id.enemy1_idleSprite);
        enemy1_idleSprite.setImageResource(R.drawable.enemy1_idleanim);
        AnimationDrawable enemy1_idleAnim = (AnimationDrawable) enemy1_idleSprite.getDrawable();
        enemy1_idleAnim.start();

        hero1_idleSprite = (ImageView)findViewById(R.id.hero1_idleSprite);
        hero1_idleSprite.setImageResource(R.drawable.hero1_idleanim);
        AnimationDrawable hero1_idleAnim = (AnimationDrawable)hero1_idleSprite.getDrawable();
        hero1_idleAnim.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void press() {
        btnDefend.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnDefend.setImageResource(R.drawable.btn_pressed);
                    Log.d(TAG, "btnDefense pressed");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnDefend.setImageResource(R.drawable.btn_unpressed);
                    Log.d(TAG, "btnDefense unpressed");
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
}