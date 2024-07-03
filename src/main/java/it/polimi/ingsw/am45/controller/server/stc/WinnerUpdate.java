package it.polimi.ingsw.am45.controller.server.stc;

import it.polimi.ingsw.am45.view.modelview.End;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WinnerUpdate implements ServerToClient, Serializable {
    private List<String> winners = new ArrayList<>();

    /**
     * This method updates the game state by updating the winners of the game.
     */
    @Override
    public void update() {
        End.getInstance().updateWinners(this);
    }


    public WinnerUpdate(List<String> players) {
        this.winners = players;
    }


    /**
     * Getter
     * @return winners
     */
    public List<String> getWinners() {
        return winners;
    }
}
