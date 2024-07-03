package it.polimi.ingsw.am45.controller.client.cts;


import it.polimi.ingsw.am45.connection.socket.server.ServerHandler;
import it.polimi.ingsw.am45.model.Game;

public abstract class ClientToServer {
    private transient int gameId;
    private transient String nickname;
    private transient ServerHandler serverSocket;

    /**
     * This method updates the client's game.
     */
    public abstract void update();

    /**
     * Getter
     * @return serverSocket
     */
    public ServerHandler getServerSocket() {
        return serverSocket;
    }

    /**
     * Setter
     * @param serverSocket
     */
    public void setServerSocket(ServerHandler serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Getter
     * @return gameId
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Setter
     * @param gameId
     */
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Getter
     * @return game
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Setter
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Getter
     * @param game
     */
    public void setGame(Game game) {
    }

}
