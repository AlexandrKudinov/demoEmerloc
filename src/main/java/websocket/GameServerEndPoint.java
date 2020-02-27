package websocket;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

@ServerEndpoint(value = "/game", configurator = GameServerEndPointConfigurator.class)
public class GameServerEndPoint {
    Gson gson = new Gson();
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
            String gameOneStage = gameSession.generateGson(gameSession.isPlayerOneOnEmergency());
            gameSession.getPlayerOneSession().getAsyncRemote().sendText(gameOneStage);
            String gameTwoStage = gameSession.generateGson(gameSession.isPlayerTwoOnEmergency());
            gameSession.getPlayerTwoSession().getAsyncRemote().sendText(gameTwoStage);

//            String gameStage = gameSession.generateGson();
//            gameSession.sendMessageToPlayers(gameStage);
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

    private static int[] toArray(String json, Gson parser) {
        return parser.fromJson(json, int[].class);
    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
        int i = Integer.parseInt(userSession.getId());
        System.out.println(message);
        //  System.out.println(userSession.getId() + " : " + message);
//        Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
//        List<Integer> numbers = new Gson().fromJson(message, listType);
//        System.out.println(numbers.size());

//        Gson parser = new Gson();
//
//        int[] arr = toArray(message, parser);
//        System.out.println();
        GameSession gameSession = getGameSession(userSession);
        currentGameSession = gameSession;

        if (gameSession != null) {
            if (gameSession.getPlayerOneScore() == 5) {
                gameSession.getPlayerTwoSession().getAsyncRemote().sendText("-5");
                gameSession.getPlayerOneSession().getAsyncRemote().sendText("5");
                return;
            }
            if (gameSession.getPlayerTwoScore() == 5) {
                gameSession.getPlayerTwoSession().getAsyncRemote().sendText("5");
                gameSession.getPlayerOneSession().getAsyncRemote().sendText("-5");
                return;
            }
        }
        if (gameSession == null) {
            userSession.getAsyncRemote().sendText("1");
        } else {
            if (message.equals("0")) {
                if (i % 2 == 0) {
                    gameSession.playerOneUp();
                    if (gameSession.playerOneOnEmergency()) {
                        String gameStage = gameSession.generateGson(gameSession.isPlayerOneOnEmergency());
                        gameSession.getPlayerOneSession().getAsyncRemote().sendText(gameStage);
                        return;
                    }
                } else {
                    gameSession.playerTwoUp();
                    if (gameSession.playerTwoOnEmergency()) {
                        String gameStage = gameSession.generateGson(gameSession.isPlayerTwoOnEmergency());
                        gameSession.getPlayerTwoSession().getAsyncRemote().sendText(gameStage);
                        return;
                    }
                }
            }
            if (message.equals("1")) {
                if (i % 2 == 0) {
                    gameSession.playerOneLeft();
                    if (gameSession.playerOneOnEmergency()) {
                        String gameStage = gameSession.generateGson(gameSession.isPlayerOneOnEmergency());
                        gameSession.getPlayerOneSession().getAsyncRemote().sendText(gameStage);
                        return;
                    }
                } else {
                    gameSession.playerTwoLeft();
                    if (gameSession.playerTwoOnEmergency()) {
                        String gameStage = gameSession.generateGson(gameSession.isPlayerTwoOnEmergency());
                        gameSession.getPlayerTwoSession().getAsyncRemote().sendText(gameStage);
                        return;
                    }
                }
            }
            if (message.equals("2")) {
                if (i % 2 == 0) {
                    gameSession.playerOneRight();
                    if (gameSession.playerOneOnEmergency()) {
                        String gameStage = gameSession.generateGson(gameSession.isPlayerOneOnEmergency());
                        gameSession.getPlayerOneSession().getAsyncRemote().sendText(gameStage);
                        return;
                    }
                } else {
                    gameSession.playerTwoRight();
                    if (gameSession.playerTwoOnEmergency()) {
                        String gameStage = gameSession.generateGson(gameSession.isPlayerTwoOnEmergency());
                        gameSession.getPlayerTwoSession().getAsyncRemote().sendText(gameStage);
                        return;
                    }
                }
            }
            if (message.equals("3")) {
                if (i % 2 == 0) {
                    gameSession.playerOneDown();
                    if (gameSession.playerOneOnEmergency()) {
                        String gameStage = gameSession.generateGson(gameSession.isPlayerOneOnEmergency());
                        gameSession.getPlayerOneSession().getAsyncRemote().sendText(gameStage);
                        return;
                    }
                } else {
                    gameSession.playerTwoDown();
                    if (gameSession.playerTwoOnEmergency()) {
                        String gameStage = gameSession.generateGson(gameSession.isPlayerTwoOnEmergency());
                        gameSession.getPlayerTwoSession().getAsyncRemote().sendText(gameStage);
                        return;
                    }
                }
            }

            if (message.matches("(.*)\\d+\\s\\d+(.*)")) {
                message = message.substring(1, message.length() - 1);
                String[] coords = message.split(" ");
                int X = Integer.valueOf(coords[0]) - 40;
                int Y = Integer.valueOf(coords[1]) - 10;
                System.out.println(X + " " + Y);
                gameSession.mouseClickOnValve(X, Y);
                if (gameSession.getPlayerTwoScore() == 5) {
                    gameSession.getPlayerTwoSession().getAsyncRemote().sendText("5");
                    gameSession.getPlayerOneSession().getAsyncRemote().sendText("-5");
                    return;
                }
                if (gameSession.getPlayerOneScore() == 5) {
                    gameSession.getPlayerTwoSession().getAsyncRemote().sendText("-5");
                    gameSession.getPlayerOneSession().getAsyncRemote().sendText("5");
                    return;
                }
            }

            //gameSession.sendMessageToPlayers(message);

            String gameOneStage = gameSession.generateGson(gameSession.isPlayerOneOnEmergency());
            gameSession.getPlayerOneSession().getAsyncRemote().sendText(gameOneStage);
            String gameTwoStage = gameSession.generateGson(gameSession.isPlayerTwoOnEmergency());
            gameSession.getPlayerTwoSession().getAsyncRemote().sendText(gameTwoStage);
        }
    }

}

