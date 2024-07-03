package it.polimi.ingsw.am45.controller.server.stc;


import it.polimi.ingsw.am45.view.ViewController;

import java.io.Serializable;
import java.rmi.RemoteException;

public class PingUpdate implements ServerToClient, Serializable {

    /**
     * This method sends a pong message to the client.
     *
     * @throws RemoteException
     */
    @Override
    public void update() throws RemoteException {
        ViewController.getClientHandler().sendPong();

    }
}
