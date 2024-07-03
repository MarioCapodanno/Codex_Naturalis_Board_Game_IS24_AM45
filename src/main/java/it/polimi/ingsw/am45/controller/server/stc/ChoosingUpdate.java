package it.polimi.ingsw.am45.controller.server.stc;


import it.polimi.ingsw.am45.view.modelview.Hand;

import java.io.Serializable;

public class ChoosingUpdate implements ServerToClient, Serializable {
    private final String initCard;
    private final String obj1;
    private final String obj2;

    /**
     * This method updates the game state by updating the hands of the players.
     */
    @Override
    public void update() {
        Hand.getInstance().updateHands(this);
    }

    /**
     * Getter
     *
     * @return initCard
     */
    public String getInitCard() {
        return initCard;
    }

    /**
     * Getter
     *
     * @return obj1
     */
    public String getObj1() {
        return obj1;
    }

    public String getObj2() {
        return obj2;
    }

    /**
     * Class constructor
     *
     * @param initialCard
     * @param obj1
     * @param obj2
     */
    public ChoosingUpdate(String initialCard, String obj1, String obj2) {
        this.initCard = initialCard;
        this.obj1 = obj1;
        this.obj2 = obj2;
    }
}
