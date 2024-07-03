package it.polimi.ingsw.am45.view.TUI;

import it.polimi.ingsw.am45.view.TUI.commandhandler.AsciiArtHelper.AsciiArtHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;


/**
 * The MarketView class represents the market view in the game.
 * It handles the display of the market cards and the player view in the terminal user interface (TUI).
 */
public class MarketView {

    /**
     * The expected size of the market.
     */
    private static final int EXPECTED_MARKET_SIZE = 6;

    /**
     * The singleton instance of the MarketView.
     */
    private static MarketView instance;

    /**
     * Flag to check if the MarketView has started.
     */
    private boolean isStarted = false;

    /**
     * List to store the market cards.
     */
    private final ArrayList<String> marketCards;

    /**
     * List to store the ASCII representation of the market cards.
     */
    private final ArrayList<String> asciiMarketCards;

    /**
     * List to store the common goal cards.
     */
    private final ArrayList<String> commonGoal;

    /**
     * List to store the player texts.
     */
    private final ArrayList<String> playerTexts;

    /**
     * Constructs a new MarketView.
     * Initializes the playerTexts, marketCards, commonGoal, and asciiMarketCards lists.
     */
    public MarketView() {
        this.playerTexts = new ArrayList<>(Arrays.asList("", "", "", ""));
        this.marketCards = new ArrayList<>();
        this.commonGoal = new ArrayList<>();
        this.asciiMarketCards = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the MarketView.
     * If the instance does not exist, it is created.
     *
     * @return the singleton instance of the MarketView
     */
    public static synchronized MarketView getInstance() {
        if (instance == null) {
            instance = new MarketView();
        }
        return instance;
    }

    /**
     * Initializes the MarketView and sets isStarted to true.
     */
    public void init() {
        //System.out.println("MarketView initialized");
        isStarted = true;
    }

    /**
     * Clears the player texts.
     */
    public void cleanPlayers() {
        for (int i = 0; i < 4; i++) {
            playerTexts.set(i, "");
        }
    }

    /**
     * Updates the market view with the given market cards.
     * Waits for the MarketView to be started before updating.
     *
     * @param market the list of market cards
     */
    @SuppressWarnings("BusyWait")
    public synchronized void updateMarketView(List<String> market) {
        AsciiArtHelper asciiArtHelper = new AsciiArtHelper();
        while (!isStarted) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.err.println("Thread was interrupted: " + e.getMessage());
                Thread.currentThread().interrupt(); // Preserve the interrupt
            }
        }
        this.marketCards.clear();
        this.commonGoal.clear();
        this.asciiMarketCards.clear();
        for (int i = 0; i < EXPECTED_MARKET_SIZE; i++) {
            marketCards.add(market.get(i));
        }
        for (int i = EXPECTED_MARKET_SIZE ; i < market.size()  ; i++) {
            commonGoal.add(market.get(i));
        }
        for (String card : marketCards) {
            asciiMarketCards.addAll(asciiArtHelper.printPlayableCardASCII(card));
        }
    }

    /**
     * Prints the market view in the terminal user interface (TUI).
     *
     * @param output the list of strings to be printed
     * @return the list of strings to be printed
     */
    public synchronized ArrayList<String> printMarketTUI(ArrayList<String> output) {

        output.addAll(asciiMarketCards);

        return output;
    }

    /**
     * Updates the player view with the given players, pings and points.
     *
     * @param players list of players in the game in the order they joined.
     * @param pings   list of pings of the players. The order is the same as the player list.
     * @param points  list of points of the players.
     */
    public void updatePlayerView(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
        waitForStart();
        cleanPlayers();
        updatePlayerTexts(players, pings, points);
    }

    /**
     * Shows the player ordered by points in the player view.
     *
     * @param i           index of the player to show in the player view.
     * @param playerTexts list of player texts to be shown in the player view.
     */
    private static void showPlayerOrderedByPoints(int i, ArrayList<String> playerTexts) {
        switch (i) {
            case 0:
                playerTexts.set(0, playerTexts.get(0) + "\uD83E\uDD47");
                break;
            case 1:
                playerTexts.set(1, playerTexts.get(1) + "\uD83E\uDD48");
                break;
            case 2:
                playerTexts.set(2, playerTexts.get(2) + "\uD83E\uDD49");
                break;
        }
    }

    /**
     * Waits for the game to be started before updating the player view.
     */
    @SuppressWarnings("BusyWait")
    private void waitForStart() {
        while (!isStarted) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted" + e.getMessage());
                Thread.currentThread().interrupt(); // Preserve the interrupt
            }
        }
    }

    /**
     * Updates the current player texts to be shown in the player view.
     *
     * @param players Stack of the players in the game in the order they joined.
     * @param pings   Stack of the players ping. The order is the same as the player list.
     * @param points  Stack of the players points to be shown.
     */
    private void updatePlayerTexts(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
        for (int i = 0; i < players.size(); i++) {
            String playerText = (pings != null) ? players.get(i) + "/" + pings.get(i) + "ms  " + points.get(i) : players.get(i);
            playerTexts.set(i, playerText);
            showPlayerOrderedByPoints(i, playerTexts);
        }
    }

}