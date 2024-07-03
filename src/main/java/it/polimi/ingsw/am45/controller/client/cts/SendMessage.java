package it.polimi.ingsw.am45.controller.client.cts;

import it.polimi.ingsw.am45.controller.GamesController;

import java.io.Serializable;

public class SendMessage extends ClientToServer implements Serializable {

    private final String message;

    /**
     * This method updates the game state by sending a message to the server.
     * It uses the Singleton instance of GamesController to perform this action.
     */
    @Override
    public void update() {
        GamesController.getInstance().sendMessage(message, this.getServerSocket());
    }

    /**
     * Class constructor
     * @param message The message to be sent to the server.
     */
    public SendMessage(String message) {
        this.message = message;
    }

    /**
     * Getter
     * @return message The message to be sent to the server.
     */
    public String getMessage() {
        return message;
    }
}
