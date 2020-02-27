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

    // private boolean onEmergency = false;
    private boolean playerOneOnEmergency = false;
    private boolean playerTwoOnEmergency = false;

    private Session playerOneSession;
    private Session playerTwoSession;
    private WaterSupplyMap waterSupplyMap = new WaterSupplyMap();
    private Structure structure = new Structure();
    private GameStage gameStage = new GameStage();
    private ToScreen toScreen;
    private Van van1;
    private Van van2;
    private int playerOneScore = 0;
    private int playerTwoScore = 0;

    public int getPlayerTwoScore() {
        return playerTwoScore;
    }

    public int getPlayerOneScore() {
        return playerOneScore;
    }

    public void setPlayerOneScore(int playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    public void setPlayerOneOnEmergency(boolean playerOneOnEmergency) {
        this.playerOneOnEmergency = playerOneOnEmergency;
    }

    public void setPlayerTwoOnEmergency(boolean playerTwoOnEmergency) {
        this.playerTwoOnEmergency = playerTwoOnEmergency;
    }


    public boolean isPlayerOneOnEmergency() {
        return playerOneOnEmergency;
    }

    public boolean isPlayerTwoOnEmergency() {
        return playerTwoOnEmergency;
    }

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

    public boolean playerTwoOnEmergency() {
        if (van2.onEmergency()) {
            playerTwoOnEmergency = true;
            return true;
        } else {
            playerTwoOnEmergency = false;
            return false;
        }
    }

    public boolean playerOneOnEmergency() {
        if (van1.onEmergency()) {
            playerOneOnEmergency = true;
            return true;
        } else {
            playerOneOnEmergency = false;
            return false;
        }
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

    public String generateGson(boolean onEmergency) {
        gameStage.clearAll();
       // waterSupplyMap.check();
        for (Pipeline pipeline : waterSupplyMap.getPipelines()) {
            boolean open = pipeline.isOpen();

            for (House house : pipeline.getHouses()) {
                for (Node node : house.getHouseFragments()) {
                    List<Integer> houseCoords = new LinkedList<>();
                    houseCoords.add(node.getJ() * BLOCK);
                    houseCoords.add(node.getI() * BLOCK);
                    toScreen = new ToScreen(houseCoords, open);
                    gameStage.addHouse(toScreen);
                }
            }
            if (pipeline.isAccident()) {
                for (Pipe pipe : pipeline.getPipes().keySet()) {
                    if (pipe.isAccident()) {
                        Node node = pipe.getNode();
                        //  showAccident(g, baseX + node.getJ() * BLOCK, baseY + node.getI() * BLOCK);
                        List<Integer> accidentCoords = new LinkedList<>();
                        accidentCoords.add(node.getJ() * BLOCK);
                        accidentCoords.add(node.getI() * BLOCK);
                        ToScreen accidentToScreen = new ToScreen(accidentCoords, open);
                        gameStage.addEmergency(accidentToScreen);
                        break;
                    }
                }
            }
            if (onEmergency) {
                Map<Pipe, List<LocType>> pipelineMap = pipeline.getPipes();
                //pipeline.updateStatus();

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
                            toScreen = new ToScreen(upDotCoords, open);
                            gameStage.addDot(toScreen);

                        } else {
                            //  showUpPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                            List<Integer> upPlumbCoords = new LinkedList<>();
                            upPlumbCoords.add(j * BLOCK + BLOCK / 5 * 2);
                            upPlumbCoords.add(i * BLOCK);
                            toScreen = new ToScreen(upPlumbCoords, open);
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
                            toScreen = new ToScreen(leftDotCoords, open);
                            gameStage.addDot(toScreen);


                        } else {
                            // showLeftPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                            List<Integer> leftPlumbCoords = new LinkedList<>();
                            leftPlumbCoords.add(j * BLOCK);
                            leftPlumbCoords.add(i * BLOCK + BLOCK / 5 * 2);
                            toScreen = new ToScreen(leftPlumbCoords, open);
                            gameStage.addHorizontalPlumb(toScreen);

                        }
                    }
                    if (listEntry.getValue().contains(DOWN)) {
                        if (pipe.containValve() && pipe.getValve().getType() == DOWN) {
                            //  showDownDot(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                            List<Integer> downDotCoords = new LinkedList<>();
                            downDotCoords.add(j * BLOCK + BLOCK / 5 * 2);
                            downDotCoords.add(i * BLOCK + BLOCK / 5 * 4);
                            toScreen = new ToScreen(downDotCoords, open);
                            gameStage.addDot(toScreen);
                        } else {
                            // showDownPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                            List<Integer> downPlumbCoords = new LinkedList<>();
                            downPlumbCoords.add(j * BLOCK + BLOCK / 5 * 2);
                            downPlumbCoords.add(i * BLOCK + BLOCK / 5 * 2);
                            toScreen = new ToScreen(downPlumbCoords, open);
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
                            toScreen = new ToScreen(rightDotCoords, open);
                            gameStage.addDot(toScreen);

                        } else {
                            //showRightPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                            List<Integer> rightPlumbCoords = new LinkedList<>();
                            rightPlumbCoords.add(j * BLOCK + BLOCK / 5 * 2);
                            rightPlumbCoords.add(i * BLOCK + BLOCK / 5 * 2);
                            toScreen = new ToScreen(rightPlumbCoords, open);
                            gameStage.addHorizontalPlumb(toScreen);
                        }
                    }

                }
                for (Valve valve : waterSupplyMap.getValves()) {
                    List<Integer> valveCoords = new LinkedList<>();
                    valveCoords.add(valve.getJ());
                    valveCoords.add(valve.getI());
                    toScreen = new ToScreen(valveCoords, valve.isOpen());
                    gameStage.addValve(toScreen);
                    // showValve(g, valve.getJ(), valve.getI(), valve.isOpen());
                }
            }

//            for (House house : pipeline.getHouses()) {
//                for (Node node : house.getHouseFragments()) {
//                    showHouseBlock(g, baseX + node.getJ() * BLOCK, baseY + node.getI() * BLOCK, open);
//                }
//            }


        }


        List<Integer> playerOneCoords = new LinkedList<>();
        playerOneCoords.add(structure.getVan1().getJ());
        playerOneCoords.add(structure.getVan1().getI());
        gameStage.setPlayerOnePosition(playerOneCoords);

        List<Integer> playerTwoCoords = new LinkedList<>();
        playerTwoCoords.add(structure.getVan2().getJ());
        playerTwoCoords.add(structure.getVan2().getI());
        gameStage.setPlayerTwoPosition(playerTwoCoords);

        gameStage.setPlayerOneScore(playerOneScore);
        gameStage.setPlayerTwoScore(playerTwoScore);

        Gson gson = new Gson();
        return gson.toJson(gameStage);
    }

    public void mouseClickOnValve(int X, int Y) {
        for (Valve valve : waterSupplyMap.getValves()) {
            if ((X >= valve.getJ() && X <= valve.getJ() + BLOCK / 5) &&
                    ((Y >= valve.getI() && Y <= valve.getI() + BLOCK / 5))) {

                valve.changeStage();
                checkPipelineStage(valve.getFirstPipeline());
                checkPipelineStage(valve.getSecondPipeline());
                //     waterSupplyMap.check()
                if ((!valve. getFirstPipeline().isOpen() && valve.getFirstPipeline().isAccident())||
                        (!valve.getSecondPipeline().isOpen() && valve.getSecondPipeline().isAccident())){
                    if(playerOneOnEmergency){
                        playerOneScore++;
                    }
                    if(playerTwoOnEmergency){
                        playerTwoScore++;
                    }
                }
                return;
            }
        }
    }

    private void checkPipelineStage(Pipeline pipeline){
        for (Valve valve:pipeline.getValves()){
            if(valve.isOpen()){
                return;
            }
        }
        pipeline.setStage(false);
    }




}

