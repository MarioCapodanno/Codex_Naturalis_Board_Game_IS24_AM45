package it.polimi.ingsw.am45.controller.server.stc;


import it.polimi.ingsw.am45.view.modelview.Msg;

import java.io.Serializable;
import java.util.Stack;

public class PlayersUpdate implements ServerToClient, Serializable {
    private Stack<String> players = new Stack<>();
    private Stack<Long> pings = new Stack<>();
    private Stack<Integer> points = new Stack<>();
    @Override
    public void update() {
        Msg.getInstance().updateNicknames(this);

    }

    public Stack<String> getPlayers() {
        return players;
    }
    public Stack<Integer> getPoints() {
        return points;
    }
    public Stack<Long> getPings() {
        return pings;
    }


    /**
     This constructor initializes a PlayersUpdate object with the given players stack.
     It sets the pings and points stacks to null.
     */
    public PlayersUpdate(Stack<String> players) {
        this.players = players;
        pings = null;
        points = null;
    }

    /**
     This constructor initializes a PlayersUpdate object with the given players, pings and points stacks.
     * @param players
     * @param pings
     * @param points
     */
    public PlayersUpdate(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
        this.players = players;
        this.pings = pings;
        this.points = points;
    }
}
