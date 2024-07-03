package it.polimi.ingsw.am45.controller.client.cts;

import it.polimi.ingsw.am45.controller.GamesController;
import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.model.Game;

import java.io.Serializable;

public class NewGame extends ClientToServer implements Serializable {

    private final int GameId;
    private final int playerNum;
    private final TokenColor value;

     /**
     When the game is successfully created, it sets the game ID and game instance
     in the server socket and sends a 'CREATED' message.
     */
    @Override
    public void update() {
        Game game = GamesController.getInstance().createGame(GameId, playerNum, this.getNickname(), this.value, this.getServerSocket());
        if (game != null) {
            this.getServerSocket().setGameID(GameId);
            this.getServerSocket().setGame(game);
            this.getServerSocket().sendMessage(new MessageUpdate(Messages.CREATED));
        } else {
            this.getServerSocket().sendMessage(new MessageUpdate(Messages.ALREADYEXIST));
        }


    }

    /**
     * Class constructor
     * @param Gameid
     * @param playerNum
     */
    public NewGame(int Gameid, int playerNum, TokenColor value) {
        this.value = value;
        this.playerNum = playerNum;
        this.GameId = Gameid;
    }

    /**
     * Getter
     * @return GameId
     */
    public int getGameID() {
        return this.GameId;
    }

}
