package it.polimi.ingsw.am45.controller.server.stc;


import it.polimi.ingsw.am45.view.modelview.Hand;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

public class HandUpdate implements ServerToClient, Serializable {
    private List<String> hand = new Stack<>();

    /**
     This method updates the state of the hand by calling the updateHands method on the Singleton instance of Hand.
     It passes the current instance of HandUpdate to the updateHands method.
     */
    @Override
    public void update() {
        Hand.getInstance().updateHands(this);
    }

    /**
     * Getter
     * @return hand
     */
    public List<String> getHand() {
        return hand;
    }

    /**
     * Class constructor
     * @param hand
     */
    public HandUpdate(List<String> hand) {
        this.hand = hand;
    }
}
