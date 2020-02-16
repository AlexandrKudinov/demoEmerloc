package websocket;

import com.google.gson.Gson;
import logic.*;

import javax.websocket.Session;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static logic.Display.*;
import static logic.LocType.*;

public class GameSession {
    private Session playerOneSession;
    private Session playerTwoSession;
    private WaterSupplyMap waterSupplyMap = new WaterSupplyMap();
    private Structure structure = new Structure();
    GameStage gameStage = new GameStage();
    ToScreen toScreen;
    Van van1;
    Van van2;

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
        van1 = structure.getVan1();
        van2 = structure.getVan2();
    }

    public void playerOneUp() {
        van1.turnUp();
    }

    public void playerTwoUp() {
        van2.turnUp();
    }

    public void playerOneDown() {
        van1.turnDown();
    }

    public void playerTwoDown() {
        van2.turnDown();
    }

    public void playerOneLeft() {
        van1.turnLeft();
    }

    public void playerTwoLeft() {
        van2.turnLeft();
    }

    public void playerOneRight() {
        van1.turnRight();
    }

    public void playerTwoRight() {
        van2.turnRight();
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

    public String generateGson() {
        gameStage.clearAll();
        //  List<ToScreen> houses = new LinkedList<>();
        for (Pipeline pipeline : waterSupplyMap.getPipelines()) {
            if (pipeline.isAccident()) {
                for (Pipe pipe : pipeline.getPipes().keySet()) {
                    if (pipe.isAccident()) {
                        Node node = pipe.getNode();
                        //  showAccident(g, baseX + node.getJ() * BLOCK, baseY + node.getI() * BLOCK);
                        List<Integer> accidentCoords = new LinkedList<>();
                        accidentCoords.add(node.getJ() * BLOCK);
                        accidentCoords.add(node.getI() * BLOCK);
                        ToScreen accidentToScreen = new ToScreen(accidentCoords, true);
                        gameStage.addEmergency(accidentToScreen);
                        break;
                    }
                }
            }

            for (House house : pipeline.getHouses()) {
                for (Node node : house.getHouseFragments()) {
                    List<Integer> houseCoords = new LinkedList<>();
                    houseCoords.add(node.getJ() * BLOCK);
                    houseCoords.add(node.getI() * BLOCK);
                    toScreen = new ToScreen(houseCoords, true);
                    gameStage.addHouse(toScreen);
                }
            }
            Map<Pipe, List<LocType>> pipelineMap = pipeline.getPipes();
            //pipeline.updateStatus();
            boolean open = pipeline.isOpen();
            for (Map.Entry<Pipe, List<LocType>> listEntry : pipelineMap.entrySet()) {
                int i = listEntry.getKey().getNode().getI();
                int j = listEntry.getKey().getNode().getJ();
                Pipe pipe = listEntry.getKey();

                if (listEntry.getValue().contains(UP)) {
                    if (pipe.containValve() && pipe.getValve().getType() == UP) {
                        // showUpDot(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        List<Integer> upDotCoords = new LinkedList<>();
                        upDotCoords.add(j * BLOCK + BLOCK / 5 * 2);
                        upDotCoords.add(i * BLOCK);
                        toScreen = new ToScreen(upDotCoords, true);
                        gameStage.addDot(toScreen);

                    } else {
                        //  showUpPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        List<Integer> upPlumbCoords = new LinkedList<>();
                        upPlumbCoords.add(j * BLOCK + BLOCK / 5 * 2);
                        upPlumbCoords.add(i * BLOCK);
                        toScreen = new ToScreen(upPlumbCoords, true);
                        gameStage.addVerticalPlumb(toScreen);
                    }
                }
                if (listEntry.getValue().contains(LEFT)) {
                    if (pipe.containValve() && pipe.getValve().getType() == LEFT ||
                            pipe.containFourParts()) {
                        // showLeftDot(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        List<Integer> leftDotCoords = new LinkedList<>();
                        leftDotCoords.add(j * BLOCK);
                        leftDotCoords.add(i * BLOCK + BLOCK / 5 * 2);
                        toScreen = new ToScreen(leftDotCoords, true);
                        gameStage.addDot(toScreen);


                    } else {
                        // showLeftPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        List<Integer> leftPlumbCoords = new LinkedList<>();
                        leftPlumbCoords.add(j * BLOCK);
                        leftPlumbCoords.add(i * BLOCK + BLOCK / 5 * 2);
                        toScreen = new ToScreen(leftPlumbCoords, true);
                        gameStage.addHorizontalPlumb(toScreen);

                    }
                }
                if (listEntry.getValue().contains(DOWN)) {
                    if (pipe.containValve() && pipe.getValve().getType() == DOWN) {
                        //  showDownDot(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        List<Integer> downDotCoords = new LinkedList<>();
                        downDotCoords.add(j * BLOCK + BLOCK / 5 * 2);
                        downDotCoords.add(i * BLOCK + BLOCK / 5 * 4);
                        toScreen = new ToScreen(downDotCoords, true);
                        gameStage.addDot(toScreen);
                    } else {
                        // showDownPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        List<Integer> downPlumbCoords = new LinkedList<>();
                        downPlumbCoords.add(j * BLOCK + BLOCK / 5 * 2);
                        downPlumbCoords.add(i * BLOCK + BLOCK / 5 * 2);
                        toScreen = new ToScreen(downPlumbCoords, true);
                        gameStage.addVerticalPlumb(toScreen);

                    }
                }
                if (listEntry.getValue().contains(RIGHT)) {
                    if (pipe.containValve() && pipe.getValve().getType() == RIGHT ||
                            pipe.containFourParts()) {
                        //  showRightDot(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        List<Integer> rightDotCoords = new LinkedList<>();
                        rightDotCoords.add(j * BLOCK + BLOCK / 5 * 4);
                        rightDotCoords.add(i * BLOCK + BLOCK / 5 * 2);
                        toScreen = new ToScreen(rightDotCoords, true);
                        gameStage.addDot(toScreen);

                    } else {
                        //showRightPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        List<Integer> rightPlumbCoords = new LinkedList<>();
                        rightPlumbCoords.add(j * BLOCK + BLOCK / 5 * 2);
                        rightPlumbCoords.add(i * BLOCK + BLOCK / 5 * 2);
                        toScreen = new ToScreen(rightPlumbCoords, true);
                        gameStage.addHorizontalPlumb(toScreen);
                    }
                }

            }
//            for (House house : pipeline.getHouses()) {
//                for (Node node : house.getHouseFragments()) {
//                    showHouseBlock(g, baseX + node.getJ() * BLOCK, baseY + node.getI() * BLOCK, open);
//                }
//            }
        }
        //  gameStage.setHouses(houses);
        for (Valve valve : waterSupplyMap.getValves()) {
            List<Integer> valveCoords = new LinkedList<>();
            valveCoords.add(valve.getJ());
            valveCoords.add(valve.getI());
            toScreen = new ToScreen(valveCoords, true);
            gameStage.addValve(toScreen);
            // showValve(g, valve.getJ(), valve.getI(), valve.isOpen());
        }

        List<Integer> playerOneCoords = new LinkedList<>();
        playerOneCoords.add(structure.getVan1().getJ());
        playerOneCoords.add(structure.getVan1().getI());
        gameStage.setPlayerOnePosition(playerOneCoords);

        List<Integer> playerTwoCoords = new LinkedList<>();
        playerTwoCoords.add(structure.getVan2().getJ());
        playerTwoCoords.add(structure.getVan2().getI());
        gameStage.setPlayerTwoPosition(playerTwoCoords);

        Gson gson = new Gson();
        return gson.toJson(gameStage);
    }


}
