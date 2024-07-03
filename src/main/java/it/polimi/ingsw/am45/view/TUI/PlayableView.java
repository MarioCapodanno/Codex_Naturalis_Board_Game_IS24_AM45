package it.polimi.ingsw.am45.view.TUI;

import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.view.TUI.commandhandler.AsciiArtHelper.AsciiArtHelper;
import it.polimi.ingsw.am45.view.ViewController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class PlayableView {

    private final GameView gameView;

    /**
     * SelectedObj is used to store the selected objective card (true if the first option is selected, false if the second option is selected)
     */
    private int selectedObj = -1;
    /**
     * BufferedReader to read user input from the console.
     */
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    /**
     * AsciiArtHelper to print ASCII art of the cards on the console.
     */
    private final AsciiArtHelper asciiArtHelper;
    /**
     * Strings to store the initial card.
     */
    private String initialCard;
    /**
     * Strings to store the objective cards.
     */
    private String obj1Card;
    /**
     * Strings to store the objective cards.
     */
    private String obj2Card;

    /**
     * Boolean to check if the player has chosen the objective card.
     */
    private boolean hasChosen = false;
    /**
     * Boolean to check if the game has started before handling the selection.
     */
    private boolean isStarted = false;

    public PlayableView(GameView gameView) {
        this.gameView = gameView;
        this.asciiArtHelper = new AsciiArtHelper();
    }

    public void init() {
        System.out.println("PlayableView initialized");
        isStarted = true;
    }

    /**
     * This method updates the initial card and the objective cards to be displayed on the console.
     *
     * @param card The initial card to be displayed on the console.
     * @param obj1 The first objective card to be displayed on the console.
     * @param obj2 The second objective card to be displayed on the console.
     */
    @SuppressWarnings("BusyWait")
    public void updateChoosableCard(String card, String obj1, String obj2) {
        while (!isStarted) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Error while updating choosable cards: " + e.getMessage());
            }
        }
        initialCard = card;
        obj1Card = obj1;
        obj2Card = obj2;
    }

    /**
     * This method flips the initial card during the first phase of the game (chose of the secret objective cards and the initial card).
     */
    public void flipInitialCard() {
        ViewController.getClientHandler().flipCard(3);
    }


    /**
     * This method sets the selected objective card to the index of the objective card selected by the player.
     *
     * @param obJCardIndex The index of the objective card selected by the player.
     */
    public void objectiveCardSelected(int obJCardIndex) {
        selectedObj = obJCardIndex;
    }

    /**
     * This method confirms the selected objective card and sends the choice to the server.
     */
    public void confirmSelected() {
        if (selectedObj == -1) {
            gameView.showServerMsg(new MessageUpdate(Messages.CHOOSE));
            return;
        }
        gameView.playInitialCard();
        gameView.chooseObjective((selectedObj == 1));
        if (selectedObj == 1) {
            HandView.getInstance(gameView).setPersonaObjectiveCard(obj1Card);
            System.out.println("Objective card selected: " + selectedObj);
        } else {
            HandView.getInstance(gameView).setPersonaObjectiveCard(obj2Card);
            System.out.println("Objective card selected: " + selectedObj);
        }
        setHasChosen(true);
        gameView.setIsChoosing(false);
    }

    /**
     * This method handles the selection of the objective card by the player and sends the choice to the server.
     * It also prints the ASCII art of the cards on the console.
     */
    @SuppressWarnings("BusyWait")
    public void handleSelection() {
        String input;
        while (!hasChosen) {
            // Check if the cards are loaded
            if (initialCard == null || obj1Card == null || obj2Card == null) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    while (initialCard == null || obj1Card == null || obj2Card == null) {
                        System.out.println("Loading cards, please wait...");
                        try {
                            Thread.sleep(1000); // Sleep for 1 second before checking again
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt(); // Restore interrupted status
                        }
                    }
                });
                future.join();
            }
            printChoosableCard();
            try {
                input = reader.readLine();
                if (input.isEmpty()) {
                    if (selectedObj == -1) {
                        System.out.println("Please select an objective card.");
                        continue;
                    }
                    confirmSelected();
                    continue;
                }
                int selection = Integer.parseInt(input);
                if (selection == 3) {
                    flipInitialCard();
                } else if (selection == 1 || selection == 2) {
                    objectiveCardSelected(selection);
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("Error reading input from the console.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * @param hasChosen The boolean to set if the player has chosen the objective card.
     */
    public void setHasChosen(boolean hasChosen) {
        this.hasChosen = hasChosen;
    }

    /**
     * This method prints the ASCII art of the initial card and the objective cards on the console.
     */
    private void printChoosableCard() {
        System.out.print("\033[H\033[2J");
        System.out.println("                              INITIAL CARD: ");
        asciiArtHelper.printInitialCardASCII(initialCard);
        System.out.println("\n                     OBJECTIVE CARDS TO CHOOSE FROM: ");
        printObjCardASCII();
        if (selectedObj == 1) {
            System.out.println("\n ---------------------> Objective card 1 is selected\n");
        } else if (selectedObj == 2) {
            System.out.println("\n---------------------> Objective card 2 is selected\n");
        }

        System.out.println("Select your objective card (1 or 2) by typing the corresponding number and pressing enter.");
        System.out.println("If you want to flip the initial card, type 3");
        System.out.println("Press enter to confirm your selection");
    }

    /**
     * This method prints the ASCII art of the objective cards on the console.
     */
    public void printObjCardASCII() {
        ArrayList<String> objCardList = new ArrayList<>();
        objCardList.add(obj1Card);
        objCardList.add(obj2Card);
        asciiArtHelper.printCommonGoalASCII(objCardList);
    }

}
