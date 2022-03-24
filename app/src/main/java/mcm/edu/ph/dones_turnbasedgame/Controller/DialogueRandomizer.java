package mcm.edu.ph.dones_turnbasedgame.Controller;

import android.content.Context;
import java.util.Random;

import mcm.edu.ph.dones_turnbasedgame.Model.DialogueData;
import mcm.edu.ph.dones_turnbasedgame.R;

public class DialogueRandomizer {

    private final Random random = new Random();
    private final Context context;
    private final DialogueData dialogue = new DialogueData();

    public DialogueRandomizer(Context context){
        this.context = context;
    }

    public int dialogueAtkRandomizer(){
        return random.nextInt(20);
    }

    public String dialogueHeroSS(){
        int dialogueCounter = random.nextInt(10);
        return dialogue.getHeroSSDialogue(dialogueCounter);
    }

}
