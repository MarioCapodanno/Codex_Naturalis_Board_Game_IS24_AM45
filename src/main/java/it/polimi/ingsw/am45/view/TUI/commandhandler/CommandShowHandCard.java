package it.polimi.ingsw.am45.view.TUI.commandhandler;

import it.polimi.ingsw.am45.view.TUI.GameView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * The CommandShowHandCard class represents a command to show the hand cards of a player.
 * It handles the display of the hand cards and the flipping of a card.
 */
public class CommandShowHandCard {
    private boolean exiting = false;
    private final GameView gameView;

    /**
     * Constructs a new CommandShowHandCard with the specified game view.
     *
     * @param gameView the game view to be used
     */
    public CommandShowHandCard(GameView gameView) {
        this.gameView = gameView;
    }

    /**
     * Executes the command.
     * Continuously handles the flipping of a card until the player chooses to exit.
     */
    public void execute() {
        while (!exiting) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            getUserChoice();
        }
    }

    /**
     * Gets the user's choice of card to flip.
     * Prompts the user to enter a number corresponding to a card or 'exit' to exit.
     * Flips the chosen card using the hand view controller of the game view.
     *
     * @return the chosen card
     */
    private int getUserChoice() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> hand = new ArrayList<>();

        int chosenCard = -1;

        while (chosenCard < 1 || chosenCard >= 3 || !exiting) {
            try {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                this.gameView.getHandViewController().printHandTUI(hand).forEach(System.out::println);
                System.out.println("If you want to flip a card, press 1,2, or 3. If you want to exit, type 'exit'.");
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
                this.gameView.getHandViewController().flipHandCard(chosenCard-1);
            } catch (IOException | NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return chosenCard;
    }
}