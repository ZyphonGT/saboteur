package aiPakSam;

import model.*;
import model.cards.Card;
import model.cards.PathCard;

import java.util.ArrayList;
import java.util.Set;

import static java.lang.Float.MAX_VALUE;
import static java.lang.Float.floatToIntBits;

public class HeuChecker {

    public float k0,k1,k2,k3,k4,k5,c1,c2,c3,c4;
    int possibleGold = 1;

    public HeuChecker(float k0, float k1, float k2, float k3, float k4, float k5, float c1, float c2, float c3, float c4) {
        this.k0 = k0;
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
        this.k4 = k4;
        this.k5 = k5;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;
    }


    /**
     * Move
     *   - int      playerIndex()
     *   - int      handIndex()
     *   - int()    args()              {x,y}
     *   - bool     rotated
     */

    /**
     * PathCard (PATHWAY or DEADEND)
     *   - Side[]   sides()             {top, right, bottom, left}
     *
     * Side
     *   - int      val()               {-2(Rock), 0(Deadend), 1(Path)}
     */

    protected MoveHeu calcHeuCardPath(PathCard path, ArrayList<Move> targets, boolean isMiner) {

        /*
//        NOT USED, since targets is guaranteed to be NOT empty
        if(targets.size() == 0) {
            return new MoveHeu(null, -MAX_VALUE);
        }
        */
        float bmove = 0;
        ArrayList<MoveHeu> allMoveHeu = new ArrayList<>();

//        System.out.println("Inside heuChecker");

        for(Move trgtMove : targets) {
            bmove = 0;
            //     Cell's Top Row      AND    Card has RightPath     AND    Card has BottomPath
            if(trgtMove.args()[1] == 0 && path.sides()[1].val() == 1 && path.sides()[2].val() == 1 ) {
                bmove = bmove + 0.3f*(2);
//                System.out.println("1 Met, bmove="+bmove);
            }

            //Cell's Between TopBottom Row
            if(trgtMove.args()[1] < 4 && trgtMove.args()[1] > 0 ) {
                int top     = path.sides()[0].val();
                int right   = path.sides()[1].val();
                int bot     = path.sides()[2].val();

                bmove = bmove + 0.3f*(top+right+bot)+0.1f;
//                System.out.println("2 Met, bmove="+bmove);
            }

            //     Cell's Bot Row      AND    Card has RightPath     AND    Card has TopPath
            if(trgtMove.args()[1] == 4 && path.sides()[1].val() == 1 && path.sides()[0].val() == 1 ) {
                bmove = bmove + 0.3f*(2);
//                System.out.println("3 Met, bmove="+bmove);
            }

            // CardType is Pathway AND I'm Gold Miner
            if(path.type() == Card.Type.PATHWAY && isMiner) {
                bmove = bmove + 0.1f * (1 + trgtMove.args()[0]) / possibleGold;
//                System.out.println("4 Met, bmove="+bmove);
            }

            // CardType is Pathway AND I'm Saboteur
            if(path.type() == Card.Type.PATHWAY && !isMiner) {
                bmove = bmove + 0.1f * (9 - trgtMove.args()[0]) / possibleGold;
//                System.out.println("5 MET, bmove="+bmove);
            }

            // CardType is Deadend AND I'm Gold Miner
            if(path.type() == Card.Type.DEADEND && isMiner) {
                bmove = bmove + 0.1f * (9 - trgtMove.args()[0]) / possibleGold;
//                System.out.println("6 Met, bmove="+bmove);
            }

            // CardType is Deadend AND I'm Saboteur
            if(path.type() == Card.Type.DEADEND && !isMiner) {
                bmove = bmove + 0.1f * (1 + trgtMove.args()[0]) / possibleGold;
//                System.out.println("7 Met, bmove="+bmove);
            }

            bmove = bmove + k0;
//            System.out.println("Added k0, bmove="+bmove);

            allMoveHeu.add(new MoveHeu(trgtMove,bmove));



        }

        /*
        for(MoveHeu moveHeu:allMoveHeu) {
            System.out.print("Type "+moveHeu.m.type() + " ");
            System.out.print("handIndex "+moveHeu.m.handIndex() + " ");
            System.out.print("Position "+moveHeu.m.args()[0] + "."+ moveHeu.m.args()[1] + " ");
            System.out.print("Heu "+moveHeu.heu + " ");
            System.out.println("");
        }
        */
        return maxHeu(allMoveHeu);
    }

    protected ArrayList<Float> calcHeuCardPathFloats(PathCard path, ArrayList<Move> targets, boolean isMiner) {

        ArrayList<Float> heus = new ArrayList<>();

        float bmove = 0;

        for(Move trgtMove : targets) {
            bmove = 0;
            if(trgtMove.args()[1] == 0 && path.sides()[1].val() == 1 && path.sides()[2].val() == 1 ) {
                bmove = bmove + 0.3f*(2);
            }

            if(trgtMove.args()[1] < 4 && trgtMove.args()[1] > 0 ) {
                int top     = path.sides()[0].val();
                int right   = path.sides()[1].val();
                int bot     = path.sides()[2].val();

                bmove = bmove + 0.3f*(top+right+bot)+0.1f;
            }

            if(trgtMove.args()[1] == 4 && path.sides()[1].val() == 1 && path.sides()[0].val() == 1 ) {
                bmove = bmove + 0.3f*(2);
            }

            if(path.type() == Card.Type.PATHWAY && isMiner) {
                bmove = bmove + 0.1f * (1 + trgtMove.args()[0]) / possibleGold;
            }

            if(path.type() == Card.Type.PATHWAY && !isMiner) {
                bmove = bmove + 0.1f * (9 - trgtMove.args()[0]) / possibleGold;
            }

            if(path.type() == Card.Type.DEADEND && isMiner) {
                bmove = bmove + 0.1f * (9 - trgtMove.args()[0]) / possibleGold;
            }

            if(path.type() == Card.Type.DEADEND && !isMiner) {
                bmove = bmove + 0.1f * (1 + trgtMove.args()[0]) / possibleGold;
            }

            bmove = bmove + k0;
            heus.add(bmove);
        }

        return heus;
    }

    protected float calcHeuCellPath(PathCard path, Move target, boolean isMiner) {
        float bmove = 0;

//        System.out.println("Inside heuChecker");

            //     Cell's Top Row      AND    Card has RightPath     AND    Card has BottomPath
            if(target.args()[1] == 0 && path.sides()[1].val() == 1 && path.sides()[2].val() == 1 ) {
                bmove = bmove + 0.3f*(2);
//                System.out.println("1 Met, bmove="+bmove);
            }

            //Cell's Between TopBottom Row
            if(target.args()[1] < 4 && target.args()[1] > 0 ) {
                int top     = path.sides()[0].val();
                int right   = path.sides()[1].val();
                int bot     = path.sides()[2].val();

                bmove = bmove + 0.3f*(top+right+bot)+0.1f;
//                System.out.println("2 Met, bmove="+bmove);
            }

            //     Cell's Bot Row      AND    Card has RightPath     AND    Card has TopPath
            if(target.args()[1] == 4 && path.sides()[1].val() == 1 && path.sides()[0].val() == 1 ) {
                bmove = bmove + 0.3f*(2);
//                System.out.println("3 Met, bmove="+bmove);
            }

            // CardType is Pathway AND I'm Gold Miner
            if(path.type() == Card.Type.PATHWAY && isMiner) {
                bmove = bmove + 0.1f * (1 + target.args()[0]) / possibleGold;
//                System.out.println("4 Met, bmove="+bmove);
            }

            // CardType is Pathway AND I'm Saboteur
            if(path.type() == Card.Type.PATHWAY && !isMiner) {
                bmove = bmove + 0.1f * (9 - target.args()[0]) / possibleGold;
//                System.out.println("5 Met, bmove="+bmove);
            }

            // CardType is Deadend AND I'm Gold Miner
            if(path.type() == Card.Type.DEADEND && isMiner) {
                bmove = bmove + 0.1f * (9 - target.args()[0]) / possibleGold;
//                System.out.println("6 Met, bmove="+bmove);
            }

            // CardType is Deadend AND I'm Saboteur
            if(path.type() == Card.Type.DEADEND && !isMiner) {
                bmove = bmove + 0.1f * (1 + target.args()[0]) / possibleGold;
//                System.out.println("7 Met, bmove="+bmove);
            }

            bmove = bmove + k0;
//        System.out.println("added k0, bmove="+bmove);



        return bmove;
    }

    protected MoveHeu calcHeuCardMap(int playerIndex, int cardIndex, int[] goalData) {
        ArrayList<MoveHeu> allMoveHeu = new ArrayList<>();

        float topHeu = 1;
        float midHeu = 1;
        float botHeu = 1;

        if(goalData[0] == 0) {
            Move topMove = Move.NewMapMove(playerIndex, cardIndex, Board.GoalPosition.TOP);
            topHeu = k1;
            allMoveHeu.add(new MoveHeu(topMove, topHeu));
        }

        if(goalData[1] == 0) {
            Move topMove = Move.NewMapMove(playerIndex, cardIndex, Board.GoalPosition.MIDDLE);
            midHeu = k1;
            allMoveHeu.add(new MoveHeu(topMove, midHeu));
        }

        if(goalData[2] == 0) {
            Move topMove = Move.NewMapMove(playerIndex, cardIndex, Board.GoalPosition.BOTTOM);
            botHeu = k1;
            allMoveHeu.add(new MoveHeu(topMove, botHeu));
        }



        /*
        //PAK SAM's Algorithm
        Move topMove = Move.NewMapMove(playerIndex, cardIndex, Board.GoalPosition.TOP);
        Move midMove = Move.NewMapMove(playerIndex, cardIndex, Board.GoalPosition.MIDDLE);
        Move botMove = Move.NewMapMove(playerIndex, cardIndex, Board.GoalPosition.BOTTOM);



        float topHeu = (1+0.5f*goalData[0])*k1;
        float midHeu = (1+0.5f*goalData[1])*k1;
        float botHeu = (1+0.5f*goalData[2])*k1;


        allMoveHeu.add(new MoveHeu(topMove, topHeu));
        allMoveHeu.add(new MoveHeu(midMove, midHeu));
        allMoveHeu.add(new MoveHeu(botMove, botHeu));
        */
        return maxHeu(allMoveHeu);
    }

    protected MoveHeu calcHeuCardRockFall(ArrayList<Move> targets, boolean isMiner, Board board) {
        ArrayList<MoveHeu> allMoveHeu = new ArrayList<>();
        float heu = 0;

        for(Move m : targets) {
            if(m.args()[0] < 6) {
                heu = 0.5f*k2;
            } else {
                Cell trgtCell = board.cellAt(m.args()[0],m.args()[1]);
                int top = trgtCell.topSide().val();
                int right = trgtCell.rightSide().val();
                int bot = trgtCell.bottomSide().val();
                int left = trgtCell.leftSide().val();

                heu = top + right + bot + left;
            }
            allMoveHeu.add(new MoveHeu(m,heu));
        }

        if(isMiner) {
            return minHeu(allMoveHeu);
        } else {
            return maxHeu(allMoveHeu);
        }
    }

    public MoveHeu maxHeu(ArrayList<MoveHeu> possibleMoves) {
        MoveHeu bestMove = new MoveHeu(-MAX_VALUE);

        for (MoveHeu curr:possibleMoves) {
            if(curr.heu > bestMove.heu) {
                bestMove = curr;
            }
        }
        return bestMove;

    }

    private MoveHeu minHeu(ArrayList<MoveHeu> possibleMoves) {
        MoveHeu bestMove = new MoveHeu(MAX_VALUE);

        for (MoveHeu curr:possibleMoves) {
            if(curr.heu < bestMove.heu) {
                bestMove = curr;
            }
        }
        return bestMove;

    }


}
