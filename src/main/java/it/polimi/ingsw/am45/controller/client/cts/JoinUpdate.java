package it.polimi.ingsw.am45.controller.client.cts;

import it.polimi.ingsw.am45.controller.GamesController;

import java.io.Serializable;

public class JoinUpdate extends ClientToServer implements Serializable {

    /**
     This method updates the game state by setting the server socket's inGame status to true and starting the game.
     It uses the Singleton instance of GamesController to perform this action.
     */
    @Override
    public void update() {
        this.getServerSocket().inGame = true;
        GamesController.getInstance().startGame(this.getGameId());

    }
}
