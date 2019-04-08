package aiPakSam;

import ai.AI;
import model.*;
import model.cards.Card;
import model.cards.PathCard;

import java.util.ArrayList;
import java.util.Set;

public class AIPakSam extends AI {

    boolean isMiner;
    int[] goalData = {0,0,0}; // 0 Unknwon, -1 Rock, 1 Gold (Top, Mid, Bot)
    HeuChecker hc;

    ArrayList<Card> currHand = new ArrayList<>();
    RolePredictor rp;

    ArrayList<MoveHeu> cardsHeu = new ArrayList<>();



    GameLogicController game;


    /**
     * TO-DO LIST
     * [V] Buat method untuk menghitung HueCard untuk Path
     * [V] Buat method untuk menghitung HueCard untuk Map
     * [V] Buat method untuk menghitung HueCard untuk Rock-Fall
     *
     * [ ] Buat method untuk prediksi musuh/teman
     *      [ ] Pathcard
     *      [ ] Rock-Fall
     *      [ ] Block
     *      [ ] Repair
     *      [ ] Discard
     *
     * [ ] Buat method untuk menghitung HueCard untuk Block
     * [ ] Buat method untuk menghitung HueCard untuk Repair
     * [ ] Buat method untuk menghitung HueCard untuk Discard
     */

    /**
     * Creates an object representing an AI for the game
     *
     * @param name the Player's name
     */
    public AIPakSam(String name) {
        super(name);
    }

    @Override
    protected Move makeDecision() {
        calcHandHeu();

//        System.out.println("Goal TOP : " + goalData[0]);
//        System.out.println("Goal MID : " + goalData[1]);
//        System.out.println("Goal BOT : " + goalData[2]);
        // Pick the best heu in "MoveHeu cardsHeu"

        // Return the best move

        Move m;
       /*
        for(MoveHeu moveHeu:cardsHeu) {
            if(moveHeu.m.type() == Move.Type.PLAY_PATH) {
                m = moveHeu.m;
                System.out.println("FOUND MAP MOVE");
            }
        }
        */

       m = hc.maxHeu(cardsHeu).m;
       if(m == null) {
           System.out.println("Move empty!!!");
           return Move.NewDiscardMove(index(), 0);
       } else {
           return m;
       }

    }

    private void calcHandHeu(){
        ArrayList<Move> tempMoves;
        currHand = hand();
        cardsHeu.clear();

        System.out.print("List of Cards: ");

        for (int cardIndex = 0; cardIndex < currHand.size(); ++cardIndex) {

            tempMoves = new ArrayList<>();

            Card c = currHand.get(cardIndex);
            if(c instanceof PathCard && !isSabotaged()) {
                System.out.print("Path ");
                //Check Possible Card Location
                tempMoves.addAll(generatePossiblePaths(cardIndex,(PathCard) c));

                // Only If card can be placed
                if(!tempMoves.isEmpty()) {
                    cardsHeu.add(hc.calcHeuCardPath((PathCard)c, tempMoves, isMiner));
                }

            }

            if(c.type() == Card.Type.BLOCK) {
                System.out.print("Block ");
            }

            if(c.type() == Card.Type.REPAIR) {
                System.out.print("Repair ");
            }

            if(c.type() == Card.Type.MAP) {
                System.out.print("Map ");
                cardsHeu.add(hc.calcHeuCardMap(index(),cardIndex, goalData));

            }

            if(c.type() == Card.Type.ROCKFALL) {
                System.out.print("Rockfall ");

                Board board = game().board();

                tempMoves.addAll(generatePossibleRockfall(cardIndex));
                cardsHeu.add(hc.calcHeuCardRockFall(tempMoves, isMiner, board));

            }

        }
        System.out.println("");
        System.out.println("CardsHeu size() = " + cardsHeu.size());

        if(!cardsHeu.isEmpty()) {
            for (MoveHeu temp: cardsHeu) {

                Move x = temp.m;
                int index = x.handIndex();
                Card card = currHand.get(index);
                Card.Type tipe = card.type();

//            currHand.get(temp.m.handIndex()).type()
//                System.out.println("Card Index: "+tipe+" ("+temp.heu+")");
                System.out.print("Card "+tipe+": ("+temp.heu +") ");

                if(tipe == Card.Type.PATHWAY || tipe == Card.Type.DEADEND) {
                    System.out.println("at "+x.args()[0]+","+x.args()[1]+" rotated="+x.args()[2]);
                } else if(tipe == Card.Type.MAP) {
                    System.out.println("to "+x.args()[0]);
                } else if(tipe == Card.Type.ROCKFALL) {
                    System.out.println("at "+x.args()[0]+","+x.args()[1]);
                }
            }

        }

        System.out.println("");
    }

    private ArrayList<Move> generatePossiblePaths(int cardIndex, PathCard card) {
        ArrayList<Move> possibleMoves = new ArrayList<>();

        card.setRotated(false);
        Set<Position> posNormal = game().board().getPlaceable(card);
        posNormal.forEach(p -> possibleMoves.add(Move.NewPathMove(index(), cardIndex, p.x, p.y, false)));

        card.setRotated(true);
        Set<Position> posRotated = game().board().getPlaceable(card);
        posRotated.forEach(p -> possibleMoves.add(Move.NewPathMove(index(), cardIndex, p.x, p.y, true)));


//    //System.out.print(card + " => ");
//    //System.out.print(posNormal);
//    //System.out.println(posRotated);
        return possibleMoves;
    }

    private ArrayList<Move> generatePossiblePaths(int cardIndex, PathCard card, Board oldBoard) {
        ArrayList<Move> possibleMoves = new ArrayList<>();

        card.setRotated(false);
        Set<Position> posNormal = oldBoard.getPlaceable(card);
        posNormal.forEach(p -> possibleMoves.add(Move.NewPathMove(index(), cardIndex, p.x, p.y, false)));

        card.setRotated(true);
        Set<Position> posRotated = oldBoard.getPlaceable(card);
        posRotated.forEach(p -> possibleMoves.add(Move.NewPathMove(index(), cardIndex, p.x, p.y, true)));


//    //System.out.print(card + " => ");
//    //System.out.print(posNormal);
//    //System.out.println(posRotated);
        return possibleMoves;
    }

    private ArrayList<Move> generatePossibleRockfall(int cardIndex) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        Set<Position> positions = game().board().getDestroyable();
        positions.forEach(p -> possibleMoves.add(Move.NewRockfallMove(index(), cardIndex, p.x, p.y)));
        return possibleMoves;
    }


    protected void onOtherPlayerMove(Move move) {

        /**
         * TODO
         * [ ] Pathcard (On Progress)
         * [ ] Rockfall
         * [ ] Blockcard
         * [ ] Repair
         * [ ] Discard
         *
         */

        if(move.type() == Move.Type.PLAY_PATH) {
            System.out.println("Player " + move.playerIndex() + " PATH");
            float z1, z2;
            int x,y,rotate;

            ArrayList<Move> m = new ArrayList<>();
            BoardDelta bd = (BoardDelta) move.delta();
            m.addAll(generatePossiblePaths(move.handIndex(),(PathCard) move.card(), bd.boardBefore()));

            MoveHeu bestMove = hc.calcHeuCardPath((PathCard) move.card(), m, isMiner);
            x = bestMove.m.args()[0];
            y = bestMove.m.args()[1];
            rotate = bestMove.m.args()[2];

            z1 = bestMove.heu;
            System.out.println("Best Possible = "+z1 +" at ("+x+","+y+") rotate="+rotate);

            z2 = hc.calcHeuCellPath((PathCard) move.card(), move, isMiner);
            System.out.println("Actual = "+z2 +" at ("+move.args()[0]+","+move.args()[1]+") rotate="+move.args()[2]);
        }

        if(move.type() == Move.Type.PLAY_ROCKFALL) {
            System.out.println("Player " + move.playerIndex() + " ROCKFALL");
        }

        if(move.type() == Move.Type.PLAY_PLAYER) {
//            Player p = game().playerAt(move.args()[0]);

            if(move.card().type() == Card.Type.BLOCK) {
                System.out.println("Player " + move.playerIndex() + " BLOCK");
            } else if(move.card().type() == Card.Type.REPAIR) {
                System.out.println("Player " + move.playerIndex() + " REPAIR");
            }
        }

        if(move.type() == Move.Type.PLAY_MAP) {
            System.out.println("Player " + move.playerIndex() + " MAP to " + move.args()[0]);
        }

        if(move.type() == Move.Type.DISCARD) {
            System.out.println("Player " + move.playerIndex() + " DISCARD");
        }
    }
    /*
    protected void onPlayerMove(Move move, Card newCard) {
        if (move.playerIndex() != index()) onOtherPlayerMove(move,newCard);
    }
    */

    /**
     * @param position  the goal position
     * @param goalType  the opened goal card
     * @param permanent marks if the goal card is opened permanently (a path connects to it)
     * @see GameObserver#onGoalOpen(Board.GoalPosition, GoalType, boolean)
     */
    @Override
    protected void onGoalOpen(Board.GoalPosition position, GoalType goalType, boolean permanent) {
        // Implement this to do something when a goal card is opened

        // This method is called with two conditions:
        // - you played a map card
        // - a path card is placed so that a goal card is reached
//        System.out.println("ONGOALOPEN");
        /**
         * [V] If we found 2 ROCKS, update last unknown to GOLD
         */

        if(goalType == GoalType.GOLD) {
            if(position == Board.GoalPosition.TOP) {
                goalData[0] = 1;
                goalData[1] = -1;
                goalData[2] = -1;
            }
            if(position == Board.GoalPosition.MIDDLE) {
                goalData[0] = -1;
                goalData[1] = 1;
                goalData[2] = -1;
            }
            if(position == Board.GoalPosition.BOTTOM) {
                goalData[0] = -1;
                goalData[1] = -1;
                goalData[2] = 1;
            }
        } else {
            if(position == Board.GoalPosition.TOP) {
                goalData[0] = -1;
            }
            if(position == Board.GoalPosition.MIDDLE) {
                goalData[1] = -1;
            }
            if(position == Board.GoalPosition.BOTTOM) {
                goalData[2] = -1;
            }
        }

        if(goalData[0] == -1 && goalData[1] == -1) goalData[2] = 1;
        if(goalData[1] == -1 && goalData[2] == -1) goalData[0] = 1;
        if(goalData[2] == -1 && goalData[0] == -1) goalData[1] = 1;
    }

    @Override
    public void initialize() {
        // Implement this to initialize the AI when the game starts
        // e.g. set this constant to some value, initialize all predictors
        if(role() == Role.GOLD_MINER) {
            isMiner = true;
        } else {
            isMiner = false;
        }

        hc = new HeuChecker(5, 50,3,3,3,1,25,30,5,5);

        rp = new RolePredictor(index(), game().numPlayers(), game().numSaboteurs(),2);

        game = game();
    }
}
