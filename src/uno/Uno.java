package uno;

import java.util.*;

public class Uno {

    public static Deck createDeck() {
        ArrayList<String> colors = new ArrayList<String>(Arrays.asList("Green", "Yellow", "Blue", "Red"));
        ArrayList<String> specials = new ArrayList<String>(Arrays.asList("Skip", "Reverse", "Draw 2", "Wild", "Wild and Draw 4"));
        Deck deck = new Deck();

        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 2; k++) {
                for (int j = 0; j <= 9; j++) {
                    deck.uppLoadDeck(new Card(colors.get(i), String.valueOf(j), false));
                }

                for (int j = 0; j < 3; j++) {
                    deck.uppLoadDeck(new Card(colors.get(i), specials.get(j), true));
                }
            }

            deck.uppLoadDeck(new Card("black", specials.get(3), true));
            deck.uppLoadDeck(new Card("black", specials.get(4), true));
        }

        return deck;
    }

    public static void writeTheWinner(String msg) {
        String borderLine = "";
        for (int i = 0; i < msg.length(); i++) {
            borderLine += "-";
        }
        System.out.println("\n" + borderLine + "\n" + msg + "\n" + borderLine + "\n");
    }

    public static void main(String[] args) {
        Deck deck = createDeck();

        Action movement = new Action(deck.getDeck(), deck.onTable);
        Player robot1 = new Player("Ichigo");
        Player robot2 = new Player("Gojo");

        movement.handout(robot1);
        movement.handout(robot2);

        deck.selectStarterCard();

        while (robot1.getHand().size() != 0 || (robot2.getHand().size() != 0)) {
            movement.checkAction(robot1, robot2);
            if (movement.checkWin(robot1)) {
                writeTheWinner(robot1.getName() + " win");
                break;
            }

            movement.checkAction(robot2, robot1);

            if (movement.checkWin(robot2)) {
                writeTheWinner(robot2.getName() + " win");
                break;
            }
        }
    }
}
