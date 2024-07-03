package it.polimi.ingsw.am45.enumeration;

/**
 * Enumeration of the connection types available in the game (RMI and TCP).
 */
public enum ConnectionType {
    RMI("RMI"),
    TCP("TCP");

    public final String connectionType;

    ConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String toString() {
        return this.connectionType;
    }
}