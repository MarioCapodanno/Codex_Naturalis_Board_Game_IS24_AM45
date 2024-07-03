package it.polimi.ingsw.am45.view.TUI.commandhandler;

import it.polimi.ingsw.am45.view.TUI.GameView;

/**
 * The CommandHandler class is responsible for handling the execution of commands in the game.
 * It contains a switch statement that executes the appropriate command based on the command number.
 */
public class CommandHandler {
    /**
     * The GameView instance used to interact with the game's view.
     */
    private final GameView gameView;

    /**
     * Constructs a new CommandHandler with the specified game view.
     *
     * @param gameView the game view to be used
     */
    public CommandHandler(GameView gameView) {
        this.gameView = gameView;
    }

    /**
     * Handles the execution of a command based on the command number.
     * The command number corresponds to a specific command that is executed.
     *
     * @param command the number of the command to be executed
     */
    public void handleCommand(int command) {
        switch (command) {
            case 1:
                // Execute the command to show the personal goal
                new CommandShowPersonalGoal().execute();
                break;
            case 2:
                // Execute the command to show the common goal
                new CommandShowCommonGoal().execute();
                break;
            case 3:
                // Execute the command to open the global chat
                new CommandGlobalChat(gameView).execute();
                break;
            case 4:
                // Execute the command to open a private chat
                new CommandPrivateChat(gameView).execute();
                break;
            case 5:
                // Execute the command to show the card legend
                new CommandShowCardLegend().execute();
                break;
            case 6:
                // Show User Info
                new CommandShowPlayersInfo().execute();
                break;
            case 7:
                // Execute the command to show the hand card
                new CommandShowHandCard(gameView).execute();
                break;
            case 8:
                // Execute the command to play a card
                new CommandPlayCard(gameView).execute();
                break;
            default:
                // If the command number does not match any case, print an error message
                System.out.println("Command not found.");
        }
    }
}