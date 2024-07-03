package it.polimi.ingsw.am45.view.TUI.commandhandler;

import it.polimi.ingsw.am45.view.TUI.GameView;
import it.polimi.ingsw.am45.view.TUI.HandView;
import it.polimi.ingsw.am45.view.TUI.MarketView;
import it.polimi.ingsw.am45.view.modelview.Market;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The CommandPlayCard class represents a command to play a card in the game.
 * It extends the Command class and overrides the execute method to perform the command.
 */
public class CommandPlayCard {
    private final HandView handView;
    private final GameView gameView;

    private final MarketView marketView;
    private List<String> hand;
    private boolean exiting = false;

    private boolean cardPlayed = false;

    /**
     * Constructs a new CommandPlayCard.
     * Initializes the handView, marketView, gameView and asciiArtHelper.
     *
     * @param gameView the game view to be used
     */
    public CommandPlayCard(GameView gameView) {
        this.gameView = gameView;
        this.handView = gameView.getHandViewController();
        this.marketView = gameView.getMarketViewController();
    }

    /**
     * Executes the command.
     * Handles the playing of a card by the user.
     */
    public void execute() {

        // Get the user's hand
        this.hand = handView.printHandTUI(new ArrayList<>());
        int cardCounter = 1;

        for (int i = 0; i < hand.size(); i++) {
            if (i % 10 == 0) {
                System.out.println(cardCounter + ". " + hand.get(i));
                cardCounter++;
            } else {
                System.out.println("   " + hand.get(i));
            }
        }

        while (!cardPlayed) {
            // Get the user's choice
            int chosenCard = getUserChoice();
            if (this.exiting) {
                return;
            }

            // Ask for the position to play the card
            int[] position = getCardPosition();

            // Play the chosen card
            if (playCard(chosenCard, position)) {
                cardPlayed = true;
            } else {
                System.out.println("Card could not be played. Please try again.");
            }
        }

        // Draw a card from the market
        drawCardFromMarket();
        this.gameView.setIsPlaying(false);
    }

    /**
     * Gets the user's choice of card to play.
     *
     * @return the index of the chosen card
     */
    private int getUserChoice() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int chosenCard = -1;
        while (chosenCard < 1 || chosenCard > hand.size()) {
            try {
                System.out.println("Choose a card to play (enter a number between 1 and 3 ");
                String input = reader.readLine();
                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a number.");
                    continue;
                }
                if (input.equalsIgnoreCase("exit")) {
                    this.exiting = true;
                    break;
                }
                chosenCard = Integer.parseInt(input);
            } catch (IOException | NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return chosenCard;
    }

    /**
     * Gets the position to play the card.
     *
     * @return an array containing the row and column to play the card
     */
    private int[] getCardPosition() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int[] position = new int[2];
        position[0] = 1000;
        position[1] = 1000;
        while (position[0] == 1000 || position[1] == 1000) {
            try {
                System.out.println("Enter the row to play the card:");
                String input = reader.readLine();
                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a number.");
                    continue;
                }
                position[0] = Integer.parseInt(input);

                System.out.println("Enter the column to play the card:");
                input = reader.readLine();
                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a number.");
                    continue;
                }
                position[1] = Integer.parseInt(input);
            } catch (IOException | NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return position;
    }

    /**
     * Plays the chosen card at the specified position.
     *
     * @param chosenCard the index of the chosen card
     * @param position   the position to play the card
     * @return true if the card was played successfully, false otherwise
     */
    private boolean playCard(int chosenCard, int[] position) {
        System.out.println("Playing card: at position: " + Arrays.toString(position));

        return handView.playCard(chosenCard - 1, position);
    }

    /**
     * Draws a card from the market after playing a card.
     */
    private void drawCardFromMarket() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int chosenOption = -1;


        List<String> marketCards = marketView.printMarketTUI(new ArrayList<>());
        int counter = 1;
        for (int i = 0; i < marketCards.size(); i++) {
            if (i % 9 == 0) {
                System.out.println(counter + ". " + marketCards.get(i));
                counter++;
            } else {
                System.out.println("   " + marketCards.get(i));
            }
        }

        // Get the card type to draw from the deck or market
        chosenOption = getDrawCardType(reader);

        while (!handView.drawCardFromMarket(chosenOption -1)) {
            System.out.println("Card could not be drawn from the market. Please try again.");
            chosenOption = getDrawCardType(reader);
        }

        System.out.println("Card drawn successfully");
    }

    /**
     * Gets the type of card to draw from the deck or market.
     *
     * @param reader The BufferedReader to read user input
     */
    private static int getDrawCardType(BufferedReader reader) {
        int chosenCard = -1;
            // Draw a card from the deck
            System.out.println("Choose a card to draw from the market (1,2,3,4,5,6):");
            while (chosenCard < 1 || chosenCard > 6) {
                try {
                    String input = reader.readLine();
                    if (input.isEmpty()) {
                        System.out.println("Input cannot be empty. Please enter a number.");
                        continue;
                    }
                    chosenCard = Integer.parseInt(input) ;
                } catch (IOException | NumberFormatException e) {
                    System.out.println("Invalid input. Please retry.");
                }
            }
            return chosenCard;
    }
}