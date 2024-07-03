package it.polimi.ingsw.am45.controller.client.cts;

import java.io.Serializable;

public class NewPlayer extends ClientToServer implements Serializable {
    private final String Nickname;

    /**
     * This method updates the game state by setting the nickname of the player.
     */
    @Override
    public void update() {
        this.getServerSocket().setNickname(Nickname);

    }

    /**
     * Class constructor
     */
    public String getNickname() {
        return Nickname;
    }

    /**
     * Getter
     * @return Nickname
     */
    public NewPlayer(String Nickname) {
        this.Nickname = Nickname;
    }

}
