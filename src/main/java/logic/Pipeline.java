package logic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static logic.LocType.*;
import static logic.Structure.NodeType.*;
import static logic.Structure.NodeType.PIPELINE;

public class Pipeline {
    private List<Valve> valves = new LinkedList<>();
    private Map<Pipe, List<LocType>> pipes = new HashMap<>();
    private List<House> houses = new LinkedList<>();
    private boolean open = true;
    private boolean accident;

    public void setStage(boolean stage) {
        this.open = stage;
    }

    public List<Valve> getValves() {
        return valves;
    }

    public void setAccident() {
        this.accident = true;
    }

    public void changeAccidentStage() {
        accident = !accident;
    }

    public boolean isAccident() {
        return accident;
    }

    public Map<Pipe, List<LocType>> getPipes() {
        return pipes;
    }

    public boolean isOpen() {
        return open;
    }

    private List<LocType> locTypesList(LocType... locTypes) {
        List<LocType> locTypeList = new LinkedList<>();
        for (LocType locType : locTypes) {
            locTypeList.add(locType);
        }
        return locTypeList;
    }



    public boolean allButOneIsClose(Valve valve){
        for(Valve valve1:valves){
            if(valve1!=valve && !valve1.isOpen()){
                continue;
            }else return false;
        }
        return true;
    }

    public void updateStatus() {
        Valve valve;
        for (Pipe pipe : pipes.keySet()) {
            if (pipe.containValve()) {
                valve = pipe.getValve();
                if (!valve.isOpen()) {
                    continue;
                }
                open = true;
                return;
            }
        }
        open = false;
    }

    private void addToPipesMap(Pipe pipe, LocType... types) {
        if (pipes.containsKey(pipe)) {
            List<LocType> locTypes = pipes.get(pipe);
            for (LocType locType : types) {
                locTypes.add(locType);
            }
            pipes.put(pipe, locTypes);
        } else {
            pipes.put(pipe, locTypesList(types));
        }
    }

    public void addDepHouses() {
        List<House> newHouses = new LinkedList<>();
        for (House house : houses) {
            newHouses.addAll(house.getAllHousesInGroup());
        }
        houses.addAll(newHouses);
        for (House house : houses) {
            if (!pipes.containsKey(house.getPipe())) {
                pipes.put(house.getPipe(), house.getPipe().getIntake());
            } else {
                List<LocType> types = pipes.get(house.getPipe());
                types.addAll(house.getPipe().getIntake());
                pipes.put(house.getPipe(), types);
            }
        }

    }

    public void generate(Node node, LocType type) {
        Pipe pipe = node.getPipe();
        if (pipe.containValve()) {
            Valve valve = pipe.getValve();
            valve.setPipeline(this);
            valves.add(valve);
            if (getOppositeType(type) == valve.getType()) {
                addToPipesMap(pipe, getOppositeType(type));
                pipe.setMajorPartsIsTaken(true);
                pipe.ifMinorTakenSetPipeline();
            } else if (pipes.size() == 0) {
                pipes.put(pipe, locTypesList(type));
                pipe.setMajorPartsIsTaken(true);
                pipe.ifMinorTakenSetPipeline();
                generate(node.getNodeByType(type), type);
            } else if (valve.getType() == RIGHT || valve.getType() == LEFT) {
                List<LocType> types = new LinkedList<>();
                if (pipe.intakeContain(LEFT)) {
                    houses.add(pipe.getIntakeHouse());
                    types.add(LEFT);
                } else if (pipe.intakeContain(RIGHT)) {
                    houses.add(pipe.getIntakeHouse());
                    types.add(RIGHT);
                }
                if(pipes.containsKey(pipe)){
                    types.addAll(pipes.get(pipe));
                }
                types.add(UP);
                types.add(DOWN);
                pipes.put(pipe, types);
                pipe.setMinorPartsIsTaken(true);
                pipe.ifMajorTakenSetPipeline();
                generate(node.getNodeByType(type), type);
            } else if (valve.getType() == UP || valve.getType() == DOWN) {
                List<LocType> types = new LinkedList<>();
                if (pipe.intakeContain(UP)) {
                    houses.add(pipe.getIntakeHouse());
                    types.add(UP);
                } else if (pipe.intakeContain(DOWN)) {
                    houses.add(pipe.getIntakeHouse());
                    types.add(DOWN);
                }
                if(pipes.containsKey(pipe)){
                    types.addAll(pipes.get(pipe));
                }
                types.add(LEFT);
                types.add(RIGHT);
                pipes.put(pipe, types);
                pipe.setMinorPartsIsTaken(true);
                pipe.ifMajorTakenSetPipeline();
                generate(node.getNodeByType(type), type);
            }
        } else if (!pipe.partsContain(type)) {
            LocType newType = null;
            for (LocType locType : pipe.getParts()) {
                if (locType != getOppositeType(type)) {
                    newType = locType;
                    break;
                }
            }
            List<LocType> types = new LinkedList<>();
            types.add(getOppositeType(type));
            types.add(newType);
            if (!pipe.getIntake().isEmpty()) {
                houses.add(pipe.getIntakeHouse());
                types.add(pipe.getIntake().get(0));
            }
            pipes.put(pipe, types);
            pipe.getNode().setType(PIPELINE);
            generate(node.getNodeByType(newType), newType);
        } else if (pipe.getParts().size() == 4) {
            if (type == LEFT || type == RIGHT) {
                List<LocType> types = new LinkedList<>();
                if(pipes.containsKey(pipe)){
                    types.addAll(pipes.get(pipe));
                }
                types.add(LEFT);
                types.add(RIGHT);
                pipes.put(pipe, types);
                pipe.setMinorPartsIsTaken(true);
                pipe.ifMajorTakenSetPipeline();
                generate(node.getNodeByType(type), type);
            }
            if (type == UP || type == DOWN) {
                List<LocType> types = new LinkedList<>();
                if(pipes.containsKey(pipe)){
                    types.addAll(pipes.get(pipe));
                }
                types.add(UP);
                types.add(DOWN);
                pipes.put(pipe, types);
                pipe.setMajorPartsIsTaken(true);
                pipe.ifMinorTakenSetPipeline();
                generate(node.getNodeByType(type), type);
            }
        } else {
            List<LocType> types = new LinkedList<>(pipe.getParts());
            if (!pipe.getIntake().isEmpty() && pipe.getIntake().size() != 2) {
                houses.add(pipe.getIntakeHouse());
                types.add(pipe.getIntake().get(0));
            }
            if(pipes.containsKey(pipe)){
               types.addAll(pipes.get(pipe));
            }
            pipes.put(pipe, types);
            pipe.getNode().setType(PIPELINE);
            generate(node.getNodeByType(type), type);
        }
    }

    public List<House> getHouses() {
        return houses;
    }


}
