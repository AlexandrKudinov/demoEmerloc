package websocket;

import com.google.gson.Gson;
import logic.MainWindow;
import logic.Pipeline;
import logic.Structure;
import logic.WaterSupplyMap;

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

    public void createGameSession() {
        Session playerOneSession = playersSessions.remove();
        Session playerTwoSession = playersSessions.remove();
        GameSession gameSession = new GameSession(playerOneSession, playerTwoSession);
        gameSessions.add(gameSession);
        currentGameSession=gameSession;
        gameSession.sendMessageToPlayers("The game starts!");
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
            createGameSession();
        }
    }


    @OnClose
    public void onClose(Session userSession) {
        GameSession gameSession = getGameSession(userSession);
        if (gameSession == null) {
            playersSessions.remove(userSession);
        } else {
            Session session = gameSession.getAnotherPlayerSession(userSession);
            session.getAsyncRemote().sendText("another player leaved game in fear, you win!");
            onOpen(session);
            gameSessions.remove(gameSession);
        }
        System.out.println("user " + userSession.getId() + " removed!");
    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
        System.out.println(userSession.getId() + " : " + message);
        GameSession gameSession = getGameSession(userSession);
        currentGameSession=gameSession;
        if (gameSession == null) {
            userSession.getAsyncRemote().sendText(" waiting another player");
        } else {
            gameSession.sendMessageToPlayers(message);
            gameSession.generateGson();
        }
    }
}

