package mcm.edu.ph.dones_turnbasedgame.Model;

public class DialogueData {

    public DialogueData(){}; //default constructor


    /** GETTERS **/

    // Enemy Attack Dialogues
    public String getEnemyAtkDialogue(int dialogueN) {
        String[] enemyAtkDialogue = new String[]{
                "You are not as lucky as other people.", // dialogueN = 0
                "You can't do it.", // dialogueN = 1
                "There's nothing you can be happy about.", // dialogueN = 2
                "You always make mistakes.", // dialogueN = 3
                "Your head's too high up in the clouds.", // dialogueN = 4
                "You'll just stay feeling bad.", // dialogueN = 5
                "There's still a lot of things you don't know.", // dialogueN = 6
                "You're not good enough.", // dialogueN = 7
                "You can't do anything about your problems.", // dialogueN = 8
                "Your past scars still affect you.", // dialogueN = 9
                "A lot of people are better than you.", // dialogueN = 10
                "You can't do it.", // dialogueN = 11
                "You are not as lucky as other people.", // dialogueN = 12
                "Nobody cares about you.", // dialogueN = 13
                "You are not smart enough.", // dialogueN = 14
                "Nothing ever works out for you.", // dialogueN = 15
                "You're not worth anything.", // dialogueN = 16
                "You're totally inadequate.", // dialogueN = 17
                "You'll make a fool out of yourself.", // dialogueN = 18
                "You should just give up." // dialogueN = 19
        };
        return enemyAtkDialogue[dialogueN];
    }

    // Hero Counterattack Dialogues
    public String getHeroAtkDialogue(int dialogueN) {
        String[] heroAtkDialogue = new String[]{
                "I will write my own luck and have all the happiness in it.", // dialogueN = 0
                "I am going to try. I am not scared of failing; that is not what is important.", // dialogueN = 1
                "Happiness is a choice. There is always a reason to smile.", // dialogueN = 2
                "It's okay to make mistakes. Mistakes are how I grow.", // dialogueN = 3
                "I believe in myself.", // dialogueN = 4
                "Bad emotions will pass. I am going to get through this.", // dialogueN = 5
                "It's okay to not know everything.", // dialogueN = 6
                "I am worthy of all that I desire in this life, just like everyone else.", // dialogueN = 7
                "I will focus on what I can control.", // dialogueN = 8
                "I am healing and strengthening every day.", // dialogueN = 9
                "I will not compare myself to others.", // dialogueN = 10
                "I can do whatever I put my mind to.", // dialogueN = 11
                "Good things can and WILL happen for me again.", // dialogueN = 12
                "There are people in my life who do, in fact, care very much.", // dialogueN = 13
                "I am smart in a unique way, and I am capable of improving my knowledge.", // dialogueN = 14
                "My possibilities are endless.", // dialogueN = 15
                "I am a valuable person.", // dialogueN = 16
                "I am proud of myself and all that I have accomplished, big or small.", // dialogueN = 17
                "I choose FAITH over fear.", // dialogueN = 18
                "I was not made to give up." // dialogueN = 19
        };
        return heroAtkDialogue[dialogueN];
    }

    // Hero Special Skill Dialogues
    public String getHeroSSDialogue(int dialogueN){
        String[] heroSSDialogue = new String[]{
                "I am in the right place at the right time, doing the right thing.\n- LOUIS HAY", // dialogueN = 0
                "You are loved just for being who you are, just for existing.\n- RAM DASS", // dialogueN = 1
                "The chance to love and be loved exists no matter where you are.\n- OPRAH", // dialogueN = 2
                "Who you are inside is what helps you make and do everything in life.\n- FRED ROGERS", // dialogueN = 3
                "Your perspective is unique. It’s important and it counts.\n- GLENN CLOSE", // dialogueN = 4
                "Nothing can dim the light that shines from within.\n- MAYA ANGELOU", // dialogueN = 5
                "The secret of attraction is to love yourself.\n- DEEPAK CHOPRA", // dialogueN = 6
                "I say looking on the bright side of life never killed anybody.\n- JENNY HAN", // dialogueN = 7
                "We must accept finite disappointment, but never lose infinite hope.\n- MARTIN LUTHER KING JR.", // dialogueN = 8
                "You are enough just as you are.\n- MEGHAN MARKLE", // dialogueN = 9
        };
        return heroSSDialogue[dialogueN];
    }

}
