package it.polimi.ingsw.am45.connection.rmi;

import it.polimi.ingsw.am45.controller.server.stc.ServerToClient;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.utilities.CardData;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public interface ClientInterface extends Remote {
    void writeObject(ServerToClient message) throws RemoteException;
    void MessageUpdate(Messages messages) throws RemoteException;
    void PlayersUpdate(Stack<String> players) throws RemoteException;
    void HandUpdate(List<String> hand) throws RemoteException;
    void ChoosingUpdate(String choice, String obj1, String obj2) throws RemoteException;
    void ResourceUpdate(List<Integer> resource) throws RemoteException;
    void MarketUpdate(List<String> gameboard) throws RemoteException;
    void PlayersUpdate(Stack<String> players, Stack<Long> pings, Stack<Integer> points) throws RemoteException;
    void boardsUpdate(HashMap<String, Stack<CardData>> boardsMap) throws RemoteException;
    void winnerUpdate(List<String> winnerPlayers) throws RemoteException;
    void turnsUpdate(String nickname) throws RemoteException;
    void chatUpdate(Stack<String> messages) throws RemoteException;
}
