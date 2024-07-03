package it.polimi.ingsw.am45.view.TUI.commandhandler;

import it.polimi.ingsw.am45.view.TUI.HandView;
import it.polimi.ingsw.am45.view.TUI.commandhandler.AsciiArtHelper.AsciiArtHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The CommandShowPersonalGoal class represents a command to show the personal goal of a player.
 * It extends the Command class and overrides the execute method to perform the command.
 */
public class CommandShowPersonalGoal extends Command {
    private String personalGoal;
    private final AsciiArtHelper asciiArtHelper;

    /**
     * Constructs a new CommandShowPersonalGoal.
     * Initializes the personal goal with the personal objective card of the player.
     * Initializes the asciiArtHelper with a new AsciiArtHelper.
     */
    public CommandShowPersonalGoal() {
        this.personalGoal = HandView.getInstance().getPersonalObjectiveCard();
        this.asciiArtHelper = new AsciiArtHelper();
    }

    /**
     * Executes the command.
     * Calls the showPersonalGoal method to display the personal goal of the player.
     */
    @Override
    public void execute() {
        showPersonalGoal();
    }

    /**
     * Shows the personal goal of the player.
     * Prints the personal goal and waits for the player to press enter to return to the menu.
     */
    private void showPersonalGoal() {
        System.out.println("PERSONAL GOAL ");
        this.personalGoal = HandView.getInstance().getPersonalObjectiveCard();
        init();
        System.out.println("Press enter to return to the menu...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * Initializes the command.
     * Creates a list with the personal goal and prints it using the asciiArtHelper.
     */
    private void init() {
        List<String> personalGoalList = new ArrayList<>();
        personalGoalList.add(personalGoal);
        asciiArtHelper.printCommonGoalASCII(personalGoalList);
    }
}