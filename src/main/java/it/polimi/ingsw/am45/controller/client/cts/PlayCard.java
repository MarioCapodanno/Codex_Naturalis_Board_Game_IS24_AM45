package it.polimi.ingsw.am45.controller.client.cts;

import it.polimi.ingsw.am45.controller.GamesController;

import java.io.Serializable;

public class PlayCard extends ClientToServer implements Serializable {

    private final int position;
    private final int x;
    private final int y;

    @Override
    public void update() {
        GamesController.getInstance().playCard(position, x, y, this.getServerSocket());
    }

    /**
     This constructor initializes a PlayCard object with the given position, x, and y coordinates.
     It is used to represent a card play action in the game.
     * @param position
     * @param x
     * @param y
     */
    public PlayCard(int position, int x, int y) {
        this.position = position;
        this.x = x;
        this.y = y;
    }

    /**
     * Getter of the position
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Getter of the x coordinate
     */
    public int getX() {
        return this.x;
    }

}
