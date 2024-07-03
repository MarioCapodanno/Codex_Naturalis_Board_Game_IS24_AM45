package it.polimi.ingsw.am45.view;

import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.utilities.CardData;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * The ClientView class is an abstract class that defines the methods for updating the client view.
 * It is extended by concrete classes that implement these methods for specific types of views (e.g., GUI, TUI).
 */
public abstract class ClientView {

    /**
     * Updates the message in the client view.
     */
    public void updateMessage(MessageUpdate messageUpdate) {
    }

    /**
     * Updates the players in the client view.
     */
    public void updatePlayers(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
    }

    /**
     * Updates the hand in the client view.
     */
    public void updateHand(List<String> hand) {
    }

    /**
     * Updates the turn in the client view.
     */
    public void updateTurns(String nickname) {
    }

    /**
     * Updates the market in the client view.
     */
    public void updateMarket(List<String> cards) {
    }

    /**
     * Updates the resource in the client view.
     */
    public void updateResource(List<Integer> resource) {
    }

    /**
     * Updates the choosing card in the client view.
     */
    public void updateChoosingCard(String cards, String obj1, String obj2) {
    }

    /**
     * Updates the boards in the client view.
     */
    public void updateBoards(HashMap<String, Stack<CardData>> boards) {
    }

    /**
     * Updates the chat in the client view.
     */
    public void updateChat(Stack<String> messages) {
    }

    /**
     * Updates the end in the client view.
     */
    public void updateEnd(List<String> winners) {
    }

}