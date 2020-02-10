package logic;

import java.util.Objects;
import java.util.Random;

import static logic.LocType.*;


public class Node {
    private Random random = new Random();
    private Node left;
    private Node right;
    private Node up;
    private Node down;
    private House house;
    private Pipeline pipeline;
    private Pipe pipe;
    private Structure.NodeType nodeType = Structure.NodeType.PIPELINE_BLOCK;
    private int i;
    private int j;

    public Node getNodeByType(LocType type) {
        Node node = null;
        switch (type) {
            case DOWN:
                node = getDownNode();
                break;
            case UP:
                node = getUpNode();
                break;
            case RIGHT:
                node = getRightNode();
                break;
            case LEFT:
                node = getLeftNode();
        }
        return node;
    }


    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public Node(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public boolean verify(Structure.NodeType type, LocType... locTypes) {
        Node node = this;
        for (LocType locType : locTypes) {
            if (locType == UP) {
                node = node.getUpNode();
                continue;
            }
            if (locType == LEFT) {
                node = node.getLeftNode();
                continue;
            }
            if (locType == RIGHT) {
                node = node.getRightNode();
                continue;
            }
            if (locType == DOWN) {
                node = node.getDownNode();
            }
        }
        return node.getType() == type;
    }


    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }

    public Pipe getPipe() {
        return pipe;
    }

    public void setType(Structure.NodeType nodeType) {
        this.nodeType = nodeType;
    }


    public void setRandomAsHouseBlock() {
        if (random.nextInt(2) == 1)
            this.nodeType = Structure.NodeType.HOUSE_BLOCK;
    }

    public Structure.NodeType getType() {
        return nodeType;
    }


    public boolean isHouse() {
        return nodeType == Structure.NodeType.HOUSE;
    }

    public boolean isPipelineBlock() {
        return nodeType == Structure.NodeType.PIPELINE_BLOCK ? true : false;
    }

    public boolean isPipeline() {
        return nodeType == Structure.NodeType.PIPELINE ? true : false;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public House getHouse() {
        return house;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public void setDownNode(Node down) {
        this.down = down;
    }

    public void setUpNode(Node up) {
        this.up = up;
    }

    public void setLeftNode(Node left) {
        this.left = left;
    }

    public void setRightNode(Node right) {
        this.right = right;
    }

    public Node getDownNode() {
        return down;
    }

    public Node getLeftNode() {
        return left;
    }

    public Node getRightNode() {
        return right;
    }

    public Node getUpNode() {
        return up;
    }

    public boolean typeIs(Structure.NodeType fragmentType) {
        return this.nodeType == fragmentType ? true : false;
    }

    public void setTypeSame(Node fragment) {
        this.nodeType = fragment.getType();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return i == node.i &&
                j == node.j &&
                nodeType == node.nodeType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeType, i, j);
    }

    @Override
    public String toString() {
        return "( "+i +" , "+j+" )";
    }
}

