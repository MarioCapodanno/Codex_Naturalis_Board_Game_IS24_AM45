package it.polimi.ingsw.am45.view.modelview;

import it.polimi.ingsw.am45.controller.server.stc.MarketUpdate;
import it.polimi.ingsw.am45.controller.server.stc.ResourceUpdate;
import it.polimi.ingsw.am45.view.TUI.commandhandler.AsciiArtHelper.AsciiArtHelper;
import it.polimi.ingsw.am45.view.listener.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class represents the game's Market.
 */
public class Market {
    private static Market instance;
    protected final List<Listener> listeners = new Stack<>();
    private List<String> market = new ArrayList<>();
    private List<Integer> resources = new ArrayList<>();

    /**
     * This method returns the instance of the Market class.
     * If the instance does not exist, it creates a new one.
     * @return the instance of the Market class.
     */
    public static Market getInstance() {
        if (instance == null) instance = new Market();
        return instance;
    }

    private Market() {
    }

    /**
     * This method registers a listener to the market.
     * @param listener is the listener to be registered.
     */
    public void registerObserver(Listener listener) {
        listeners.add(listener);
    }

    /**
     * This method removes a listener to the market.
     * @param listener is the listener to be removed.
     */
    public void removeObserver(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * This method notifies all listeners of a player update.
     */
    public void notifyObserversUpdate() {
        for (Listener listener : listeners) {
            listener.updatePlayers();
        }
    }

    /**
     * This method notifies all listeners of a resource update.
     */
    public void notifyObserversUpdateResource() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }

    /**
     * This method updates the market and notifies all listeners of the change.
     * @param marketUpdate
     */
    public void updateMarket(MarketUpdate marketUpdate) {
        this.market = marketUpdate.getMarket();
        notifyObserversUpdate();
    }

    /**
     * This method updates the resource counter and notifies all listeners of the change.
     * @param resourceUpdate
     */
    public void updateMarket(ResourceUpdate resourceUpdate) {
        this.resources = resourceUpdate.getResource();
        notifyObserversUpdateResource();
    }

    /**
     * This method gets the cards that will be shown in the public market.
     * @return market
     */
    public List<String> getCards() {
        if (market.isEmpty()) return null;
        return market;
    }

    /**
     * This method fetches informations for the players' resources
     * @return
     */
    public List<Integer> getResource() {
        if (resources.isEmpty()) return null;
        return resources;
    }

    /**
     * This method sets the global goal cards.
     * @return
     */
    public List<String> getCommonGoal() {
        return market.subList(6, market.size());
    }

    /**
     * This method prints the market on the terminal (for TUI users).
     * @param output The output list to be used.
     * @return The output list with the printed cards.
     */
    public ArrayList<String> printMarketTUI(ArrayList<String> output) {
        AsciiArtHelper asciiArtHelper = new AsciiArtHelper();
        List<String> cards =this.market.subList(0, 6);

        for (String card : cards) {
            ArrayList<String> cardAscii = asciiArtHelper.printPlayableCardASCII(card);
            output.addAll(cardAscii);
        }

        return output;
    }

    /**
     * This method prints the resources counter on the terminal (for TUI users).
     * @param output The output list to be used.
     * @return The output list with the updated player's resources.
     */
    public ArrayList<String> printResourceTUI(ArrayList<String> output) {
        String[] symbols = {"♥", "♦", "♣", "♠", "•", "■", "▲"};
        String[] colors = {"RED", "BLUE", "GREEN", "VIOLET", "BOTTLE", "SCROLL", "FEATHER"};

        StringBuilder resourceLine = new StringBuilder();
        resourceLine.append("┌────────────────────────────────────────RESOURCE─GAINED─────────────────────────────────────────┐\n");

        for (int i = 0; i < this.resources.size(); i++) {
            resourceLine.append(symbols[i]).append(" = ").append(resources.get(i)).append(" (").append(colors[i]).append(") ");
        }

        resourceLine.append("\n");
        resourceLine.append("└────────────────────────────────────────────────────────────────────────────────────────────────┘\n");

        output.add(resourceLine.toString());
        return output;
    }

    public void setMarket(List<String> market) {
        this.market = market;
    }

}
