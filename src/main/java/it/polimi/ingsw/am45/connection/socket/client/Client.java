package it.polimi.ingsw.am45.connection.socket.client;

import it.polimi.ingsw.am45.controller.client.cts.NewPlayer;
import it.polimi.ingsw.am45.view.ViewController;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private final String serverAddress;
    private final int serverPort;
    private final String nickname;

    /**
     * Constructor for the Client class.
     * Initializes the server address, server port, and client's nickname.
     *
     * @param serverAddress the server's address
     * @param serverPort the server's port
     * @param nickname the client's nickname
     */
    public Client(String serverAddress, int serverPort, String nickname) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.nickname = nickname;
    }

    /**
     * Starts the client and establishes a connection with the server.
     * Creates a new ClientSocket, starts a new thread for it, and sends a new player message to the server.
     */
    public void startClient() throws IOException {

        Socket socket = new Socket(serverAddress, serverPort);
        System.out.println("Connected to server");

        ClientSocket clientSocket = new ClientSocket(socket);
        new Thread(clientSocket).start();
        ViewController.setClientHandler(clientSocket);
        clientSocket.sendObject(new NewPlayer(nickname));
        clientSocket.nickname = nickname;

        /*
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your nickname: ");
        String nickname = scanner.nextLine();
        clientSocket.sendObject(new NewPlayer(nickname));
        clientSocket.nickname = nickname;
        */

    }


}