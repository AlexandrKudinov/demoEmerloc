package websocket;


import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;


@ServerEndpoint(value = "/game", configurator = GameServerEndPointConfigurator.class)
public class GameServerEndPoint {

    private static Queue<Session> playersSessions = new ArrayBlockingQueue<Session>(2);

    private static List<GameSession> gameSessions = Collections.synchronizedList(new LinkedList<>());

    public static Queue<Session> getPlayersSessions() {
        return playersSessions;
    }

    private static GameSession currentGameSession;

    public static GameSession getCurrentGameSession() {
        return currentGameSession;
    }

    public GameSession createGameSession() {
        Session playerOneSession = playersSessions.remove();
        Session playerTwoSession = playersSessions.remove();
        GameSession gameSession = new GameSession(playerOneSession, playerTwoSession);
        gameSessions.add(gameSession);
        currentGameSession = gameSession;
        gameSession.sendMessageToPlayers("The game starts!");
        return gameSession;
    }

    public static GameSession getGameSession(Session session) {
        for (GameSession gameSession : gameSessions) {
            if (gameSession.containSession(session)) {
                return gameSession;
            }
        }
        return null;
    }

    @OnOpen
    public void onOpen(Session userSession) {
        playersSessions.add(userSession);
        System.out.println("user added: " + userSession.getId());
        if (playersSessions.size() == 2) {
            GameSession gameSession = createGameSession();
            String gameStage = gameSession.generateGson();
            gameSession.sendMessageToPlayers(gameStage);
        }
    }

    @OnClose
    public void onClose(Session userSession) {
        GameSession gameSession = getGameSession(userSession);
        if (gameSession == null) {
            playersSessions.remove(userSession);
        } else {
            Session session = gameSession.getAnotherPlayerSession(userSession);
            session.getAsyncRemote().sendText("-1");
            onOpen(session);
            gameSessions.remove(gameSession);
        }
        System.out.println("user " + userSession.getId() + " removed!");
    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
        int i = Integer.parseInt(userSession.getId());
        System.out.println(userSession.getId() + " : " + message);

        GameSession gameSession = getGameSession(userSession);
        currentGameSession = gameSession;

        if (gameSession == null) {
            userSession.getAsyncRemote().sendText("1");
        } else {
            if (message.equals("0")) {
                if(i%2==0){
                    gameSession.playerOneUp();
                }else {
                    gameSession.playerTwoUp();
                }
            }
            if (message.equals("1")) {
                if(i%2==0){
                    gameSession.playerOneLeft();
                }else {
                    gameSession.playerTwoLeft();
                }
            }
            if (message.equals("2")) {
                if(i%2==0){
                    gameSession.playerOneRight();
                }else {
                    gameSession.playerTwoRight();
                }
            }
            if (message.equals("3")) {
                if(i%2==0){
                    gameSession.playerOneDown();
                }else {
                    gameSession.playerTwoDown();
                }
            }
            //gameSession.sendMessageToPlayers(message);
            String gameStage = gameSession.generateGson();
            gameSession.sendMessageToPlayers(gameStage);
        }
    }
}

