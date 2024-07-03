package it.polimi.ingsw.am45.controller;

import it.polimi.ingsw.am45.connection.rmi.RMIClient;
import it.polimi.ingsw.am45.connection.rmi.RMIServer;
import it.polimi.ingsw.am45.connection.socket.server.Server;
import it.polimi.ingsw.am45.connection.socket.server.ServerHandler;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.model.Game;
import it.polimi.ingsw.am45.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class GamesControllerTest {
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
    public void testGetInstance() {
        GamesController instance1 = GamesController.getInstance();
        GamesController instance2 = GamesController.getInstance();
        // Assert that the two instances are the same
        assertSame(instance1, instance2);
    }

    @Test
    public void testGamesControllerConstructor() {
        GamesController instance = GamesController.getInstance();
        // Use reflection to access the private fields
        try {
            Field gamesField = GamesController.class.getDeclaredField("games");
            gamesField.setAccessible(true);
            HashMap games = (HashMap) gamesField.get(instance);
            Field clientsField = GamesController.class.getDeclaredField("clients");
            clientsField.setAccessible(true);
            HashMap clients = (HashMap) clientsField.get(instance);
            // Assert that the fields are not null
            assertNotNull(games);
            assertNotNull(clients);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access field: " + e.getMessage());
        }
    }


    @Test
    public void testGetGames() {
        GamesController instance = GamesController.getInstance();
        assertNotNull(instance.getGames());
        assertNotNull(instance.getGames());
    }

    @Test
    public void testGetClients() {
        GamesController instance = GamesController.getInstance();
        assertNotNull(instance.getClients());
        assertTrue(instance.getClients() instanceof HashMap);
    }

    @Test
    public void testCreateGame() {
        int gameID1 = 1;
        int numOfPlayers = 4;
        String nickname = "testCreateGame";
        Server server1 = new Server(2889, RMIServer.getServer());
        RMIClient rmiClient = null;
        try {
            rmiClient = new RMIClient("test");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ServerHandler serverHandler2 = new ServerHandler(rmiClient, server1);
        // Create a game
        Game game = instance1.createGame(gameID1, numOfPlayers, nickname, TokenColor.RED, serverHandler2);
        // Verify that the game was created correctly
        assertNotNull(game);
        assertEquals(gameID1, game.getGameID());
        assertTrue(game.getPlayers().stream().anyMatch(player -> player.getNickname().equals(nickname)));

    }

    @Test
    public void testDeleteGame() {
        int gameID2 = 2;
        int numOfPlayers = 4;
        String nickname = "testDeleteGame";
        //instance its own serverhandler and server
        Server server2 = new Server(2887, RMIServer.getServer());
        RMIClient rmiClient = null;
        try {
            rmiClient = new RMIClient("test");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ServerHandler serverHandler3 = new ServerHandler(rmiClient, server2);
        // Create a game
        Game game = instance1.createGame(gameID2, numOfPlayers, nickname, TokenColor.RED, serverHandler3);
        //Delete the game
        instance1.deleteGame(gameID2);
        assertFalse(instance1.getGames().containsKey(gameID2));
    }

    @Test
    public void testJoinGame() {
        int gameID = 4;
        String nickname = "TestJoinGamePlayer";
        TokenColor color = TokenColor.RED;
        // Create a game
        Game game = instance1.createGame(gameID, 3, nickname, color, serverHandler1);
        assertNotNull(game);
        assertEquals(gameID, game.getGameID());

        System.out.println("here are the players" + game.getPlayerNicknames());

        instance1.joinGame(gameID, "pl1", color, serverHandler1);
        instance1.joinGame(gameID, "pl2", color, serverHandler1);
        System.out.println("here are the players" + game.getPlayerNicknames());

        System.out.println("here are the players after joining" + game.getPlayerNicknames());

        assertFalse(game.getPlayerNicknames().contains("pl1"));
        assertFalse(game.getPlayerNicknames().contains("pl2"));

    }

    @Test
    public void testStartGame() {
        int gameID = 5;
        String nickname = "TestStartGamePlayer";
        TokenColor color = TokenColor.RED;
        // Create a game
        Game game = instance1.createGame(gameID, 2, nickname, color, serverHandler1);
        // Join the game with another player
        instance1.joinGame(gameID, "pl1", color, serverHandler1);
        System.out.println(game.getPlayerNicknames().size());
        // Start the game
        instance1.startGame(gameID);
        assertNotNull(game);
        // Verify that the game has started
        assertFalse(game.isStarted());

    }

    @Test
    public void testWinnerUpdate() {
        // Use the setup
        int gameID = 6;
        String nickname = "testWinnerPlayer";
        TokenColor color = TokenColor.RED;
        // Create a game
        Game game = instance1.createGame(gameID, 2, nickname, color, serverHandler1);
        assertNotNull(game);
        assertEquals(gameID, game.getGameID());
        // Join the game with another player
        instance1.joinGame(gameID, "player2", color, serverHandler1);
        // Start the game
        instance1.startGame(gameID);
        // Simulate the end of the game and declare a winner
        List<String> winnerPlayers = List.of("testWinnerPlayer");
        instance1.winnerUpdate(game, winnerPlayers);

    }

    @Test
    public void testRequestUpdate() {
        int gameID = 7;
        String nickname = "TestUpdatePlayer";
        TokenColor color = TokenColor.RED;
        Game game = instance1.createGame(gameID, 2, nickname, color, serverHandler1);
        instance1.joinGame(gameID, "pl1", color, serverHandler1);
        instance1.startGame(gameID);
        instance1.requestUpdate(gameID);
    }

    @Test
    public void testTurnsUpdate() {
        int gameID = 8;
        Game game = instance1.createGame(gameID, 2, "testupdate", TokenColor.RED, serverHandler1);
        instance1.turnsUpdate(game, "nickname");
    }

    @Test
    public void testMarketUpdate() {
        int gameID = 9;
        Game game = instance1.createGame(gameID, 2, "testupdate", TokenColor.RED, serverHandler1);
        instance1.turnsUpdate(game, "nickname");
        List<String> marketUpdate = Arrays.asList("update1", "update2");
        instance1.marketUpdate(game, marketUpdate);
    }

    @Test
    public void testChatUpdate() {
        int gameID = 10;
        Game game = instance1.createGame(gameID, 2, "testupdate", TokenColor.RED, serverHandler1);
        instance1.turnsUpdate(game, "nickname");
        Stack<String> messages = new Stack<>();
        messages.push("message1");
        messages.push("message2");
        instance1.chatUpdate(game, messages);
    }

    @Test
    public void testCards() {
        // Use the setup
        int gameID = 11;
        int numOfPlayers = 4;
        String nickname = "testflippingcard";
        Server server3 = new Server(2886, RMIServer.getServer());
        RMIClient rmiClient = null;
        try {
            rmiClient = new RMIClient("test");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ServerHandler serverHandler4 = new ServerHandler(rmiClient, server3);

        Game game1 = instance1.createGame(gameID, 3, "Playertestupdate", TokenColor.YEllOW, serverHandler4);
        Player player1 = new Player("pl1", TokenColor.RED);
        Player player2 = new Player("pl2", TokenColor.BLUE);
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        serverHandler4.setNickname("pl1");
        serverHandler4.setNickname("pl2");
        game1.startGame();
        serverHandler4.setGame(game1);
        assertTrue(game1.isStarted());
        instance1.flipCard(0, serverHandler4);
        instance1.flipMarketCard(0, serverHandler4);
        instance1.playCard(0, 0, 0, serverHandler4);
        instance1.drawCard(0, serverHandler4);

    }

    @Test
    public void testSendMessageandOnConnection() {
        // Use the setup
        int gameID = 12;
        int numOfPlayers = 4;
        String nickname = "testflippingcard";
        Server server3 = new Server(2886, RMIServer.getServer());
        RMIClient rmiClient = null;
        try {
            rmiClient = new RMIClient("test");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ServerHandler serverHandler4 = new ServerHandler(rmiClient, server3);

        Game game1 = instance1.createGame(gameID, 3, "Playertestupdate", TokenColor.YEllOW, serverHandler4);
        Player player1 = new Player("pl1", TokenColor.RED);
        Player player2 = new Player("pl2", TokenColor.BLUE);
        game1.addPlayer(player1);
        game1.addPlayer(player2);
        serverHandler4.setNickname("pl1");
        serverHandler4.setNickname("pl2");
        game1.startGame();
        serverHandler4.setGame(game1);
        instance1.sendMessage("testmessage", serverHandler4);
        instance1.onDisconnect(serverHandler4, game1);
    }

}





