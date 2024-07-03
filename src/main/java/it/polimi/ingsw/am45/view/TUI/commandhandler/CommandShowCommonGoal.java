package it.polimi.ingsw.am45.view.TUI.commandhandler;

import it.polimi.ingsw.am45.view.TUI.commandhandler.AsciiArtHelper.AsciiArtHelper;
import it.polimi.ingsw.am45.view.modelview.Market;

import java.util.List;
import java.util.Scanner;

/**
 * The CommandShowCommonGoal class represents a command to show the common goal of the game.
 * It extends the Command class and overrides the execute method to perform the command.
 * It uses the AsciiArtHelper to print the common goal in ASCII art format.
 */
public class CommandShowCommonGoal extends Command {
    private List<String> commonGoal;
    private final AsciiArtHelper asciiArtHelper;

    /**
     * Constructs a new CommandShowCommonGoal.
     * Initializes the common goal with the common goal from the Market.
     * Initializes the asciiArtHelper with a new AsciiArtHelper.
     */
    public CommandShowCommonGoal() {
        this.commonGoal = Market.getInstance().getCommonGoal();
        this.asciiArtHelper = new AsciiArtHelper();
    }

    /**
     * Executes the command.
     * Calls the showCommonGoal method to display the common goal of the game.
     */
    @Override
    public void execute() {
        showCommonGoal();
    }

    /**
     * Shows the common goal of the game.
     * Prints the common goal and waits for the player to press enter to return to the menu.
     */
    private void showCommonGoal() {
        System.out.println("COMMON GOAL ");
        this.commonGoal = Market.getInstance().getCommonGoal();
        init();
        System.out.println("Press enter to return to the menu...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * Initializes the command.
     * Prints the common goal in ASCII art format using the asciiArtHelper.
     */
    private void init() {
        asciiArtHelper.printCommonGoalASCII(commonGoal);
    }
}