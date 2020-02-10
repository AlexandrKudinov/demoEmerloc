package logic;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static logic.Structure.NodeType.*;
import static logic.Structure.NodeType.PIPELINE;

public class Pipe {
    private List<LocType> parts = new LinkedList<>();
    private List<LocType> intake = new LinkedList<>();
    private Valve valve;
    private Node node;
    private House intakeHouse;
    private boolean majorPartsIsTaken;
    private boolean minorPartsIsTaken;
    private boolean accident;

    public boolean isAccident() {
        return accident;
    }

    public void setAccident() {
        this.accident = true;
    }

    public void changeAccidentStage() {
        accident = !accident;
    }


    public void ifMajorTakenSetPipeline() {
        if (getMajorPartsIsTaken()) {
            getNode().setType(PIPELINE);
        }
    }


    public boolean containFourParts() {
        return (parts.size() == 4) || (parts.size() == 2 && intake.size() == 2);
    }

    public void ifMinorTakenSetPipeline() {
        if (getMinorPartsIsTaken()) {
            getNode().setType(PIPELINE);
        }
    }

    public boolean intakeContain(LocType type) {
        return intake.contains(type);
    }


    public boolean partsContain(LocType type) {
        return parts.contains(type);
    }


    public void setMinorPartsIsTaken(boolean minorPartsIsTaken) {
        this.minorPartsIsTaken = minorPartsIsTaken;
    }

    public boolean getMajorPartsIsTaken() {
        return majorPartsIsTaken;
    }

    public boolean getMinorPartsIsTaken() {
        return minorPartsIsTaken;
    }

    public void setMajorPartsIsTaken(boolean majorPartsIsTaken) {
        this.majorPartsIsTaken = majorPartsIsTaken;
    }

    public House getIntakeHouse() {
        return intakeHouse;
    }

    public void setIntakeHouse(House intakeHouse) {
        this.intakeHouse = intakeHouse;
    }


    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setValve(Valve valve) {
        this.valve = valve;
    }

    public boolean containValve() {
        return valve != null;
    }

    public boolean containPart(LocType type) {
        return (parts.contains(type) || intake.contains(type));
    }

    public void addParts(LocType... types) {
        this.parts.addAll(Arrays.asList(types));
    }

    public void addIntakes(LocType... types) {
        this.intake.addAll(Arrays.asList(types));
    }

    public void setIntake(List<LocType> intake) {
        this.intake = intake;
    }

    public void setParts(List<LocType> parts) {
        this.parts = parts;
    }

    public List<LocType> getIntake() {
        return intake;
    }

    public List<LocType> getParts() {
        return parts;
    }

    public Valve getValve() {
        return valve;
    }
}
