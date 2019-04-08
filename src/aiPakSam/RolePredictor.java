package aiPakSam;

import java.util.ArrayList;

public class RolePredictor {
    ArrayList<RolePrediction> rp = new ArrayList<>();

    int myIndex;

    int totalPlayer;
    int totalSaboteur;
    int maxMistakes;


    public RolePredictor(int myIndex, int totalPlayer, int totalSaboteur, int maxMistakes) {
        this.myIndex = myIndex;
        this.totalPlayer = totalPlayer;
        this.totalSaboteur = totalSaboteur;
        this.maxMistakes = maxMistakes;

        //Initialize RolePredictions
        for(int i=0; i<totalPlayer; i++) {
            if(i != myIndex) {
                rp.add(new RolePrediction(i));
            }
        }
    }



}
