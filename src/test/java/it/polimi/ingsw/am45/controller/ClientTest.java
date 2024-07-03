package it.polimi.ingsw.am45.controller;

import it.polimi.ingsw.am45.connection.rmi.RMIClient;
import it.polimi.ingsw.am45.connection.rmi.RMIServer;
import it.polimi.ingsw.am45.connection.socket.server.Server;
import it.polimi.ingsw.am45.connection.socket.server.ServerHandler;
import it.polimi.ingsw.am45.controller.client.cts.*;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.model.Game;
import it.polimi.ingsw.am45.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.rmi.RemoteException;


public class ClientTest {
    private GamesController gamesController;

    @Mock
    private java.net.Socket socket;

    @Mock
    private ServerHandler serverHandler;

    @Mock
    private Game game;

    @Mock
    private Player player;
    @Mock
    private ServerHandler serverHandler1;
    @Mock
    private GamesController instance1 = GamesController.getInstance();

    @BeforeEach
    public void setup() throws RemoteException {
        Server server = new Server(2888, RMIServer.getServer());
        RMIClient rmiClient = new RMIClient("test");
        serverHandler1 = new ServerHandler(rmiClient, server);
        //new Thread(serverHandler1).start();
    }

    @Test
    public void testclient() {

        ChooseOBJ chooseOBJ = new ChooseOBJ(true);
        int gameID = 13;
        int numOfPlayers = 4;
        String nickname = "testChooseOBJ";
        Server server4 = new Server(2880, RMIServer.getServer());
        RMIClient rmiClient = null;
        try {
            rmiClient = new RMIClient("test");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ServerHandler serverHandler7 = new ServerHandler(rmiClient, server4);

        Game game2 = instance1.createGame(gameID, 3, "Playertestupdate", TokenColor.YEllOW, serverHandler7);
        Player player1 = new Player("pl1", TokenColor.RED);
        Player player2 = new Player("pl2", TokenColor.BLUE);
        game2.addPlayer(player1);
        game2.addPlayer(player2);
        serverHandler7.setNickname("pl1");
        serverHandler7.setNickname("pl2");
        game2.startGame();
        serverHandler7.setGame(game2);

        chooseOBJ.setGameId(gameID);
        chooseOBJ.setServerSocket(serverHandler7);
        chooseOBJ.setNickname(nickname);
        chooseOBJ.setGame(game2);

        chooseOBJ.getPosition();
        chooseOBJ.update();
        chooseOBJ.getPosition();
        chooseOBJ.getGameId();
        chooseOBJ.getNickname();
        chooseOBJ.getServerSocket();
        chooseOBJ.setGame(game2);

        DrawCard drawcard = new DrawCard(0);
        drawcard.setGameId(gameID);
        drawcard.setServerSocket(serverHandler7);
        drawcard.setNickname(nickname);
        drawcard.setGame(game2);
        drawcard.getPosition();

        FlipCard flipcard = new FlipCard(0);
        flipcard.setGameId(gameID);
        flipcard.setServerSocket(serverHandler7);
        flipcard.setNickname(nickname);
        flipcard.setGame(game2);
        flipcard.getPosition();
        flipcard.update();

        FlipMarketCard fmc = new FlipMarketCard(0);
        fmc.setGameId(gameID);
        fmc.setServerSocket(serverHandler7);
        fmc.setNickname(nickname);
        fmc.setGame(game2);
        fmc.getPosition();
        fmc.update();

        JoinGame jg = new JoinGame(gameID, TokenColor.RED);
        jg.setGameId(gameID);
        jg.setServerSocket(serverHandler7);
        jg.setNickname(nickname);
        jg.setGame(game2);
        jg.update();
        jg.getGameID();

        JoinUpdate ju = new JoinUpdate();
        ju.setGameId(gameID);
        ju.setServerSocket(serverHandler7);
        System.out.println("serversocket " + ju.getServerSocket().inGame);
        ju.update();

        NewGame ng = new NewGame(gameID, 4, TokenColor.RED);
        ng.setGameId(gameID);
        ng.setServerSocket(serverHandler7);
        ng.setNickname(nickname);
        ng.setGame(game2);
        ng.update();
        ng.getGameID();
        ng.getGameID();

        NewPlayer newPlayer = new NewPlayer("newPlayer");
        newPlayer.setServerSocket(serverHandler7);
        newPlayer.setGameId(1);
        newPlayer.setGame(game2);
        newPlayer.setNickname("newPlayer");
        newPlayer.update();
        newPlayer.getNickname();

        PlayCard pc = new PlayCard(0, 0, 0);
        pc.setGameId(gameID);
        pc.setServerSocket(serverHandler7);
        pc.setNickname(nickname);
        pc.setGame(game2);
        pc.getPosition();
        pc.getX();

        PongUpdate pu = new PongUpdate();
        pu.setGameId(gameID);
        pu.setServerSocket(serverHandler7);
        pu.setNickname(nickname);
        pu.setGame(game2);
        pu.update();

        RequestUpdate ru = new RequestUpdate(true);
        ru.setGameId(gameID);
        ru.setServerSocket(serverHandler7);
        ru.setNickname(nickname);
        ru.setGame(game2);
        ru.update();

        SendMessage sm = new SendMessage("testmessage");
        sm.setGameId(gameID);
        sm.setServerSocket(serverHandler7);
        sm.setNickname(nickname);
        sm.setGame(game2);
        sm.update();
        sm.getMessage();
    }
}