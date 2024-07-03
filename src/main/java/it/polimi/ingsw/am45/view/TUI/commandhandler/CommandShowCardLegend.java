package it.polimi.ingsw.am45.view.TUI.commandhandler;

import java.util.Scanner;

/**
 * The CommandShowCardLegend class represents a command to show the card legend in the game.
 * It extends the Command class and overrides the execute method to perform the command.
 */
public class CommandShowCardLegend extends Command {
    /**
     * Constructs a new CommandShowCardLegend.
     */
    public CommandShowCardLegend() {
    }

    /**
     * Executes the command.
     * Calls the showCardLegend method to display the card legend.
     */
    @Override
    public void execute() {
        showCardLegend();
    }

    /**
     * Shows the card legend.
     * Prints the card legend and waits for the player to press entering to return to the menu.
     */
    private void showCardLegend() {

        System.out.println("NORMAL RESOURCE");
        System.out.println("┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐");
        System.out.println("│ ♥ =  │ │ ♦ =  │ │ ♣ =  │ │ ♠ =  │");
        System.out.println("│ RED  │ │ BLUE │ │GREEN │ │VIOLET│");
        System.out.println("└──────┘ └──────┘ └──────┘ └──────┘\n");

        System.out.println("SPECIAL RESOURCE");
        System.out.println("┌─────────┐ ┌─────────┐ ┌─────────┐");
        System.out.println("│ • =     │ │ ■ =     │ │ ▲ =     │");
        System.out.println("│ BOTTLE  │ │ SCROLL  │ │ FEATHER │");
        System.out.println("└─────────┘ └─────────┘ └─────────┘\n");

        System.out.println("TYPES OF CARDS");

        printCardFormats();

        System.out.println();
        System.out.println("Press enter to return to the menu...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * Prints the card formats.
     * Prints the formats of the different types of cards in the game.
     */
    public void printCardFormats() {
        String[] resourceCard = {
                "┌───┬─────────────────────┬───┐",
                "│   │                     │   │",
                "├───┘                     └───┤",
                "│                             │",
                "│                             │",
                "│                             │",
                "├───┐                     ┌───┤",
                "│   │                     │   │",
                "└───┴─────────────────────┴───┘",
                "         RESOURCE CARD          "
        };

        String[] resourceCardWithPoints = {
                "┌───┬────────┬───┬────────┬───┐",
                "│   │        │   │        │   │",
                "├───┘        └───┘        └───┤",
                "│                             │",
                "│                             │",
                "│                             │",
                "├───┐                     ┌───┤",
                "│   │                     │   │",
                "└───┴─────────────────────┴───┘",
                " RESOURCE CARD (WITH POINTS) "
        };

        String[] goldCard = {
                "┌───┬────────┬───┬────────┬───┐",
                "│   │        │   │        │   │",
                "├───┘        └───┘        └───┤",
                "│                             │",
                "│                             │",
                "│                             │",
                "├───┐       ┌─────┐       ┌───┤",
                "│   │       │     │       │   │",
                "└───┴───────┴─────┴───────┴───┘",
                "            GOLD CARD           ",
        };

        String[] backCard = {
                "┌───┬─────────────────────┬───┐",
                "│   │                     │   │",
                "├───┘                     └───┤",
                "│            ┌───┐            │",
                "│            │   │            │",
                "│            └───┘            │",
                "├───┐                     ┌───┤",
                "│   │                     │   │",
                "└───┴─────────────────────┴───┘",
                "           CARD BACK            "
        };

        String[] objectiveCard = {
                "┌┬───┬────────────────────────┐",
                "││   │                        │",
                "│└───┘                        │",
                "│                             │",
                "│                             │",
                "│                             │",
                "│                             │",
                "│                             │",
                "└─────────────────────────────┘",
                "         OBJECTIVE CARD        "
        };

        String[] initialCard = {
                "┌───┬─────────────────────┬───┐",
                "│   │                     │   │",
                "├───┘        ┌───┐        └───┤",
                "│            │   │            │",
                "│            │   │            │",
                "│            │   │            │",
                "├───┐        └───┘        ┌───┤",
                "│   │                     │   │",
                "└───┴─────────────────────┴───┘",
                "         INITIAL CARD         "
        };

        for (int i = 0; i < resourceCard.length; i++) {
            System.out.println(resourceCard[i] + "     " + resourceCardWithPoints[i] + "     " + goldCard[i]);
        }

        System.out.println();

        for (int i = 0; i < backCard.length; i++) {
            System.out.println(backCard[i] + "     " + objectiveCard[i] + "     " + initialCard[i]);
        }
    }
}