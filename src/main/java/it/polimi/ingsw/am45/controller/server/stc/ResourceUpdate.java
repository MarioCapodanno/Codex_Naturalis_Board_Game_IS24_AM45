package it.polimi.ingsw.am45.controller.server.stc;


import it.polimi.ingsw.am45.view.modelview.Market;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

public class ResourceUpdate implements ServerToClient, Serializable {
    private List<Integer> resource = new Stack<>();

    /**
     This method updates the state of the market by calling the updateMarket method on the Singleton instance of Market.
     It passes the current instance of MarketUpdate to the updateMarket method.
     */
    @Override
    public void update() {
        Market.getInstance().updateMarket(this);
    }

    public List<Integer> getResource() {
        return resource;
    }
    public ResourceUpdate(List<Integer> resource) {
        this.resource = resource;
    }
}
