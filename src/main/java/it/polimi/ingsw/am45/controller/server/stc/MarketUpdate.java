package it.polimi.ingsw.am45.controller.server.stc;


import it.polimi.ingsw.am45.view.modelview.Market;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

public class MarketUpdate implements ServerToClient, Serializable {
    private List<String> market = new Stack<>();

    /**
     This method updates the state of the market by calling the updateMarket method on the Singleton instance of Market.
     It passes the current instance of MarketUpdate to the updateMarket method.
     */
    @Override
    public void update() {
        Market.getInstance().updateMarket(this);
    }

    /**
     * Getter
     * @return market
     */
    public List<String> getMarket() {
        return market;
    }

    /**
     * Class constructor
     * @param market
     */
    public MarketUpdate(List<String> market) {
        this.market = market;
    }
}
