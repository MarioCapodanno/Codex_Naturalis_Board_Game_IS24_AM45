package it.polimi.ingsw.am45;

import it.polimi.ingsw.am45.enumeration.ConnectionType;
import it.polimi.ingsw.am45.view.TUI.TUI;
import it.polimi.ingsw.am45.view.GUI.StartingGUI;

import java.util.Scanner;

/**
 * The ClientMain class is the entry point of the client application.
 * It provides a command line interface for the user to select the type of view (TUI or GUI) and the type of connection (TCP or RMI).
 */
public class ClientMain {

    /**
     * The main method of the client application.
     * It provides a command line interface for the user to select the type of view (TUI or GUI) and the type of connection (TCP or RMI).
     * If the user does not make a valid selection, the application defaults to GUI mode with a TCP connection.
     */
    public static void main(String[] args) {
        String input;
        ConnectionType connectionType = null;

        int selection = 2;

        System.out.println("""
                   _____          _             _   _       _                   _ _    \s
                  / ____|        | |           | \\ | |     | |                 | (_)   \s
                 | |     ___   __| | _____  __ |  \\| | __ _| |_ _   _ _ __ __ _| |_ ___\s
                 | |    / _ \\ / _` |/ _ \\ \\/ / | . ` |/ _` | __| | | | '__/ _` | | / __|
                 | |___| (_) | (_| |  __/>  <  | |\\  | (_| | |_| |_| | | | (_| | | \\__ \\
                  \\_____\\___/ \\__,_|\\___/_/\\_\\ |_| \\_|\\__,_|\\__|\\__,_|_|  \\__,_|_|_|___/
                                                                                       \s
                                                                                        \
                """);
        if (!handleParameters(args)) {

            System.out.println("1. TUI + TCP");
            System.out.println("2. TUI + RMI");
            System.out.println("3. GUI");
            input = new Scanner(System.in).nextLine();

            try {
                selection = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, starting GUI mode and TCP connection...");
                new StartingGUI().start();
            }
            if (selection == 1 || selection == 3) {
                //start the client in cli mode
                connectionType = ConnectionType.TCP;

            } else if (selection == 2 || selection == 4) {
                //start the client in gui mode
                connectionType = ConnectionType.RMI;
            }

            if (selection == 1 || selection == 2) {
                //start the client in cli mode
                new TUI(connectionType).init();
                //clientCLI.launch();
            } else if (selection == 3) {
                //start the client in gui mode
                new StartingGUI().start();
            } else {
                System.out.println("Invalid input, starting GUI mode and TCP connection...");
                new StartingGUI().start();
            }
        }
    }

    /**
     * Handles the command line parameters passed to the application.
     * It checks for the "-tui" or "-gui" parameters followed by "-tcp" or "-rmi" to determine the type of view and connection.
     * If these parameters are not provided, the application defaults to GUI mode with a TCP connection.
     */
    private static boolean handleParameters(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-tui":
                    if (i + 1 < args.length) {
                        switch (args[i + 1].toLowerCase()) {
                            case "-tcp":
                                new TUI(ConnectionType.TCP).init();
                                return true;
                            case "-rmi":
                                new TUI(ConnectionType.RMI).init();
                                return true;
                        }
                    }
                    break;
                case "-gui":
                    if (i + 1 < args.length) {
                        switch (args[i + 1].toLowerCase()) {
                            case "-tcp", "-rmi":
                                new StartingGUI().start();
                                return true;
                        }
                    }
            }
        }
        return false;
    }

}