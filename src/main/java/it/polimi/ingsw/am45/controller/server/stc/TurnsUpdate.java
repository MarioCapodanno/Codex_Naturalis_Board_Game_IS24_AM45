package it.polimi.ingsw.am45.controller.server.stc;


import it.polimi.ingsw.am45.view.modelview.Turn;

import java.io.Serializable;

public record TurnsUpdate(String nickname) implements ServerToClient, Serializable {
    /**
     * This method updates the game state by updating the turns of the players.
     */
    @Override
    public void update() {
        Turn.getInstance().updateTurns(this);
    }

}
