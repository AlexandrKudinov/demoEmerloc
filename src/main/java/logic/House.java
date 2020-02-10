package logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static logic.LocType.*;


public class House {

    private List<Node> houseFragments = new LinkedList<>();
    private List<House> allHousesInGroup = new ArrayList<>();
    private List<House> intakeHouses = new LinkedList<>();
    private List<House> outtakeHouses = new LinkedList<>();

    private Pipe pipe;

    public List<House> getOuttakeHouses() {
        return outtakeHouses;
    }


    public List<House> getAllHousesInGroup() {
        return allHousesInGroup;
    }

    public Pipe getPipe() {
        return pipe;
    }

    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }


    public List<House> getIntakeHouses() {
        return intakeHouses;
    }

    public List<Node> getHouseFragments() {
        return houseFragments;
    }

    public void add(Node node, int i, int j, int width, int height) {
        this.houseFragments.add(node);
        node.setType(Structure.NodeType.HOUSE);
        node.setHouse(this);
        if (node.verify(Structure.NodeType.HOUSE_BLOCK, LEFT) && j != 0) {
            add(node.getLeftNode(), i, j - 1, width, height);
        }
        if (node.verify(Structure.NodeType.HOUSE_BLOCK, RIGHT) && j != width - 1) {
            add(node.getRightNode(), i, j + 1, width, height);
        }
        if (node.verify(Structure.NodeType.HOUSE_BLOCK, UP) && i != 0) {
            add(node.getUpNode(), i - 1, j, width, height);
        }
        if (node.verify(Structure.NodeType.HOUSE_BLOCK, DOWN) && i != height - 1) {
            add(node.getDownNode(), i + 1, j, width, height);
        }
    }


    public void addHouseInGroup(House newHouse) {
        List<House> firstHouseList = new LinkedList<>(newHouse.getAllHousesInGroup());
        firstHouseList.add(newHouse);

        List<House> secondHouseList = new LinkedList<>(this.getAllHousesInGroup());
        secondHouseList.add(this);

        for (int i = 0; i < firstHouseList.size(); i++) {
            firstHouseList.get(i).getAllHousesInGroup().addAll(secondHouseList);
        }
        for (int i = 0; i < secondHouseList.size(); i++) {
            secondHouseList.get(i).getAllHousesInGroup().addAll(firstHouseList);
        }

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : houseFragments) {
            stringBuilder.append(node.getI() + "," + node.getJ() + " ; ");
        }
        return "house contain: " + stringBuilder.toString();
    }


}
