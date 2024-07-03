package it.polimi.ingsw.am45.controller.client.cts;

import it.polimi.ingsw.am45.controller.GamesController;
import it.polimi.ingsw.am45.enumeration.TokenColor;

import java.io.Serializable;

public class JoinGame extends ClientToServer implements Serializable {


    private final int GameId;
    private final TokenColor color;

    /**
     This method updates the game state by joining a game based on the GameId.
     It uses the Singleton instance of GamesController to perform this action.
     */
    @Override
    public void update() {
        GamesController.getInstance().joinGame(GameId, this.getNickname(), color ,this.getServerSocket());

    }

    /**
     * Class constructor
     *
     * @param GameId
     * @param color
     */
    public JoinGame(int GameId, TokenColor color) {
        this.GameId = GameId;
        this.color = color;
    }

    /**
     * Getter
     * @return GameId
     */
    public int getGameID() {
        return this.GameId;
    }

}
