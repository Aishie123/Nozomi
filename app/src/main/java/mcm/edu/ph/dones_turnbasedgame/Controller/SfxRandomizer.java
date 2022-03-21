package mcm.edu.ph.dones_turnbasedgame.Controller;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Random;

import mcm.edu.ph.dones_turnbasedgame.R;

public class SfxRandomizer extends Service {

    private final Random random = new Random();

    public int randomizeAtkSFX(){
        return random.nextInt(3);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
