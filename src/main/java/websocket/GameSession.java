package websocket;

import com.google.gson.Gson;
import logic.*;
import org.codehaus.jackson.map.ObjectMapper;

import javax.websocket.Session;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static logic.Display.BLOCK;

public class GameSession {
    private Session playerOneSession;
    private Session playerTwoSession;
    private WaterSupplyMap waterSupplyMap = new WaterSupplyMap();
    private Structure structure = new Structure();

    public WaterSupplyMap getWaterSupplyMap() {
        return waterSupplyMap;
    }

    public Structure getStructure() {
        return structure;
    }

    public GameSession(Session player1, Session player2) {
        this.playerOneSession = player1;
        this.playerTwoSession = player2;
        waterSupplyMap.setStructure(structure);
        structure.bind();
        structure.buildHouseBlocks();
        structure.buildHouses();
        waterSupplyMap.setMap(structure.getMap());
        waterSupplyMap.addPipes();
        waterSupplyMap.addWaterIntake();
        waterSupplyMap.pipelineUnion();
        waterSupplyMap.generateAccidents();
        structure.addVan();
        System.out.println("New game session with " + player1.getId() + " , " + player2.getId() + " started!");
    }

    public boolean containSession(Session session) {
        if (playerOneSession == session || playerTwoSession == session) {
            return true;
        }
        return false;
    }

    public List<Pipeline> getPipelines() {
        return waterSupplyMap.getPipelines();
    }

    public Session getPlayerOneSession() {
        return playerOneSession;
    }

    public Session getPlayerTwoSession() {
        return playerTwoSession;
    }

    public Session getAnotherPlayerSession(Session session) {
        return playerOneSession == session ? playerTwoSession : playerOneSession;
    }

    public void sendMessageToPlayers(String message) {
        System.out.println("Sending to " + playerOneSession.getId() + " & " + playerTwoSession.getId());
        playerOneSession.getAsyncRemote().sendText(message);
        playerTwoSession.getAsyncRemote().sendText(message);
    }

    public void generateGson() {
//        List<List<Integer>> houses = new LinkedList<>();
//        for (Pipeline pipeline : waterSupplyMap.getPipelines()) {
//            for (House house : pipeline.getHouses()) {
//                for (Node node : house.getHouseFragments()) {
//                    List<Integer> houseCoordinates = new LinkedList<>();
//                    houseCoordinates.add(node.getJ() * BLOCK);
//                    houseCoordinates.add(node.getI() * BLOCK);
//                    houses.add(houseCoordinates);
//                }
//            }
//        }
        Gson gson = new Gson();
//        List<Integer> list = new LinkedList<>();
//        list.add(1);
//        list.add(3);
//        final ObjectMapper mapper = new ObjectMapper();
//        try {
//            System.out.println(mapper.writeValueAsString(list));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String jsonStr = new Gson().toJson(list);
//        System.out.println(jsonStr);
    }


}
