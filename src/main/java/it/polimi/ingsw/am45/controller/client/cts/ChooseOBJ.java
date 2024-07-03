package it.polimi.ingsw.am45.controller.client.cts;

import it.polimi.ingsw.am45.controller.GamesController;

import java.io.Serializable;

public class ChooseOBJ extends ClientToServer implements Serializable {

    private final Boolean position;

    /**
    This method updates the game state by choosing an object based on the position.
    It uses the Singleton instance of GamesController to perform this action.
    */
    @Override
    public void update() {
        GamesController.getInstance().chooseObj(position, this.getServerSocket());
    }

    /**
     * Class constructor
     * @param position
     */
    public ChooseOBJ(Boolean position) {
        this.position = position;
    }

    /**
     * Getter
     * @return position
     */
    public Boolean getPosition() {return this.position;}


}
