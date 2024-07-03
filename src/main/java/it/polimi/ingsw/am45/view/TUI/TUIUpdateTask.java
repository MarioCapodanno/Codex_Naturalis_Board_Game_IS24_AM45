package it.polimi.ingsw.am45.view.TUI;

import java.util.Stack;

/**
 * This class represents a task that updates the chat view in a separate thread.
 * It implements the Runnable interface, so its instances can be executed by a thread.
 */
public class TUIUpdateTask implements Runnable {
    private final ChatView chatView;
    private final Stack<String> messages;

    /**
     * Constructs a new TUIUpdateTask with the specified chat view and messages.
     *
     * @param chatView the chat view to be updated
     * @param messages the messages to be displayed in the chat view
     */
    public TUIUpdateTask(ChatView chatView, Stack<String> messages) {
        this.chatView = chatView;
        this.messages = messages;
    }

    /**
     * The run method is called when the thread executing this task is started.
     * It updates the chat view with the messages passed to the constructor.
     * If the messages stack is the same as the chat view's messages stack, it does nothing.
     * Otherwise, it clears the chat view's global and private messages,
     * then it iterates over the messages stack and adds each message to the chat view's global messages,
     * and if a message is a private message, it also adds it to the chat view's private messages.
     */
    @Override
    public void run() {

        if (this.messages == chatView.messages ) return;

        chatView.globalMessages.clear();
        chatView.privateMessages.clear();

        for (String message : messages) {
            int start = message.indexOf("/");
            int end = message.indexOf(" ", start);
            if (start != -1 && end != -1) {
                String nick = message.substring(start + 1, end);
                if (nick.equals(chatView.getGameView().getNick())) {
                    String pvtMessage = message;
                    pvtMessage = pvtMessage.replace("/" + nick, "private message: ");
                    chatView.privateMessages.add(pvtMessage);
                    chatView.messages.add(pvtMessage);
                }
            }
            else {
                chatView.globalMessages.add(message);
                chatView.messages.add(message);
            }
        }
    }
}