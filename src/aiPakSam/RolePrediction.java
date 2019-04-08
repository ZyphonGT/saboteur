package aiPakSam;

public class RolePrediction {
    int targetIndex;        // Index dari player yang tertuduh
    double p;               // Probability of Being a Gold Miner
    float score;
    int mistakes;

    public RolePrediction(int trgtIndex) {
        this.targetIndex = trgtIndex;
        this.p = -1;
        this.score = 0;
    }

}
