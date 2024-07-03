package it.polimi.ingsw.am45.view.TUI;

import it.polimi.ingsw.am45.view.TUI.commandhandler.AsciiArtHelper.AsciiArtHelper;
import it.polimi.ingsw.am45.view.ViewController;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the hand view in the game.
 * It handles the display of the hand cards and the personal objective card.
 */
public class HandView{
    private final GameView gameView;
    private boolean isStarted = false;

    private final ArrayList<String> handCards;

    private final ArrayList<String> asciiHandCards;

    private static HandView instance;

    private String personalObjectiveCard;

    /**
     * Constructs a new HandView with the specified game view.
     * Initializes the handCards and asciiHandCards lists.
     *
     * @param gameView the game view to be used
     */
    public HandView(GameView gameView) {
        this.handCards = new ArrayList<>();
        this.gameView = gameView;
        this.asciiHandCards = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the HandView.
     * If the instance does not exist, it is created.
     *
     * @param gameView the game view to be used
     * @return the singleton instance of the HandView
     */
    public static HandView getInstance(GameView gameView) {
        if (instance == null) {
            instance = new HandView(gameView);
        }
        return instance;
    }

    /**
     * Returns the singleton instance of the HandView.
     *
     * @return the singleton instance of the HandView
     */
    public static HandView getInstance() {
        return instance;
    }

    /**
     * Initializes the HandView and sets isStarted to true.
     */
    public void init() {
        isStarted = true;
    }

    /**
     * Updates the hand view with the given hand cards.
     * Waits for the HandView to be started before updating.
     *
     * @param hand the list of hand cards
     */
    @SuppressWarnings("BusyWait")
    public synchronized void updateHandView(List<String> hand) {
        AsciiArtHelper asciiArtHelper = new AsciiArtHelper();
        while (!isStarted) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.handCards.contains(hand.get(0)) && this.handCards.contains(hand.get(1)) && this.handCards.contains(hand.get(2))) {
            return;
        }
        this.handCards.clear();
        this.asciiHandCards.clear();
        handCards.add(hand.get(0));
        asciiHandCards.addAll(asciiArtHelper.printPlayableCardASCII(handCards.get(0)));
        handCards.add(hand.get(1));
        asciiHandCards.addAll(asciiArtHelper.printPlayableCardASCII(handCards.get(1)));
        handCards.add(hand.get(2));
        asciiHandCards.addAll(asciiArtHelper.printPlayableCardASCII(handCards.get(2)));
    }

    /**
     * Flips the hand card with the given card ID.
     *
     * @param cardId the ID of the card to be flipped
     */
    public void flipHandCard(int cardId) {
        ViewController.getClientHandler().flipCard(cardId);
    }

    /**
     * Plays the card with the given card ID at the given position.
     *
     * @param cardId the ID of the card to be played
     * @param position the position to play the card at
     * @return true if the card was played successfully, false otherwise
     */
    public boolean playCard(int cardId, int[] position) {
        return gameView.playCard(cardId, position);
    }

    /**
     * Draws a card from the market with the given card index.
     *
     * @param cardIndex the index of the card to be drawn
     * @return true if the card was drawn successfully, false otherwise
     */
    public boolean drawCardFromMarket(int cardIndex) {
        return gameView.drawCard(cardIndex);
    }

    /**
     * Sets the personal objective card.
     *
     * @param objCard the personal objective card to be set
     */
    public void setPersonaObjectiveCard(String objCard) {
        this.personalObjectiveCard = objCard;
    }

    /**
     * Prints the hand view in the terminal user interface (TUI).
     *
     * @param output the list of strings to be printed
     * @return the list of strings to be printed
     */
    public synchronized ArrayList<String> printHandTUI(ArrayList<String> output) {
        output.add("                                            YOUR HAND                                            ");
        output.addAll(asciiHandCards);
        return output;
    }

    /**
     * Returns the personal objective card.
     *
     * @return the personal objective card
     */
    public String getPersonalObjectiveCard() {
        return personalObjectiveCard;
    }
}