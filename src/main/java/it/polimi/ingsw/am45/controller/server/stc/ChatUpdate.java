package it.polimi.ingsw.am45.controller.server.stc;


import it.polimi.ingsw.am45.view.modelview.Boards;

import java.io.Serializable;
import java.util.Stack;

public class ChatUpdate implements ServerToClient, Serializable {
    private Stack<String> messages = new Stack<>();

    /**
     This method updates the chat by calling the updateChat method on the Singleton instance of Boards.
     It passes the current instance of ChatUpdate to the updateChat method.
     */
    @Override
    public void update() {
        Boards.getInstance().updateChat(this);
    }


    /**
     * Class constructor
     * @param messages
     */
    public ChatUpdate(Stack<String> messages) {
        this.messages = messages;
    }

    /**
     * Getter
     * @return messages
     */
    public Stack<String> getMessages() {
        return messages;
    }
}
