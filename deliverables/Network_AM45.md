# NETWORK DOCUMENTATIOIN

Alberti, Capodanno, Duina, Dussin.

Group AM-45


## 1) IMPLEMENTATION

- SERVER: Through ServerMain.Class the server will be activated on a port, chosen by the user, letting the client create a connession that abilitates communication. Once the connection has been accepted, a ServerSocket will be started as a thread to manage the listening and sending of packages to the client. Every ServerSocket uses as parameters: Game, GameID, Nickname and client's ping.

- CLIENT: Through ClientMain class, once the user specifies IP, server port and desired nickname, a ClientSocket will be started as a thread. This thread abilitates a connection with the server and manages both the sending and the receiption of objects and initializes every Listener class for the management of the game's phases.


- GAMESCONTROLLER: To manage multiple games the controller has an HashMap that links every game to an assosciated ServerSocket list. 


## 2) CLIENT TO SERVER

Every message for the server is extended to a ClientToServer, which has every data related to its ServerSocket (null at the start of the message sending and then updated when the server will receive them).
After creating an object:

- DrawCard
- FlipCard
- FlipMarketCard
- JoinGame
- JoinUpdate
- NewGame
- NewPlayer
- PlayCard
- PongRequest
- SendMessage

ClientSocket will send an object to the server, which will update it with game data and will  call the function update(). This function is shared between every ClientToServer messages and, based on the message type, fetches the GameController instance and calls its specific function.


### EXAMPLE

Client:

```java
  ClientSocket.getInstance().sendObject(new DrawCard(card));
```

Object:

```java
   public class DrawCard extends ClientToServer implements Serializable {
        private final int position;
        @Override
        public void update() {
            GamesController.getInstance().drawCard(position, this.getServerSocket());
        }
        public DrawCard(int position){
            this.position = position;
        }
        public int getPosition(){
            return this.position;
        }
    }
```

Server:

```java
  private synchronized void handleMessages() throws IOException, ClassNotFoundException {
            while (connection) {
                Object object = inpus.readObject();
                ClientToServer message = (ClientToServer) object;
                message.setServerSocket(this);
                message.setGameId(gameID);
                message.setGame(game);
                message.setNickname(nickname);
                GamesController.getInstance().onCommandReceived(message);
            }

    }
```


## 4) SERVER TO CLIENT

Every message that goes to the client implements the ServerToClient interface and overwrites the update() function. When ClientSocket receives a message, it calls its update function. Based on the message type:

- BoardsUpdate
- ChatUpdate
- ChoosingUpdate
- HandUpdate
- MarketUpdate
- MessageUpdate
- PingUpdate
- PlayersUpdate
- ResourceUpdate
- TurnsUpdate

It fetches the corresponding ModelView class, which updates received data and notifies every listener. The listeners notifies the current scene's controller of the game or lobby changes.


### EXAMPLE

Server:

```java
   serverRequestSocket.sendMessage(new PlayersUpdate(nicknames, pings, points));
```

Object:

```java
  public class PlayersUpdate implements ServerToClient, Serializable {
        private Stack<String> players = new Stack<>();
        private Stack<Long> pings = new Stack<>();
        private final Stack<Integer> ponts = new Stack<>();
        @Override
        public void update() {
            Msg.getInstance().updateNicknames(this);

        }
        public Stack<String> getPlayers() {
            return players;
        }
        public Stack<Integer> getPoints() {
            return points;
        }
        public Stack<Long> getPings() {
            return ping;
        }
        public PlayersUpdate(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
            this.players = players;
            this.pings = pings;
            this.points = points;
        }
    }
```

Client:

```java
  @Override
        public void updatePlayers() {
            ViewController.getController().updatePlayers(Msg.getInstance().lookNicknames(),
                       Msg.getInstance().lookPings(), Msg.getInstance().lookPoints());
        }
```
