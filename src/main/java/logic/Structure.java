package logic;


import java.util.HashMap;
import java.util.Map;

import static logic.Display.*;


public class Structure {
    private Van van1;
    private Van van2;
//    private static volatile Structure INSTANCE;
//
//    private Structure() {
//    }
//
//    public static Structure getInstance() {
//        Structure structure = INSTANCE;
//        if (structure == null) {
//            synchronized (Structure.class) {
//                structure = INSTANCE;
//                if (structure == null) {
//                    structure = INSTANCE = new Structure();
//                }
//            }
//        }
//        return structure;
//    }

    public static int height = 28;
    public static int width = 60;
    private Node[][] map = new Node[height][width];
    public Map<Node, House> houses = new HashMap<>();


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Node[][] getMap() {
        return map;
    }

    public void bind() {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = new Node(i, j);

            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Node node = map[i][j];
                node.setUpNode((i == 0) ? map[height - 1][j] : map[i - 1][j]);
                node.setDownNode((i == height - 1) ? map[0][j] : map[i + 1][j]);
                node.setLeftNode((j == 0) ? map[i][width - 1] : map[i][j - 1]);
                node.setRightNode((j == width - 1) ? map[i][0] : map[i][j + 1]);
            }
        }
    }


    public void buildHouseBlocks() {
        Node node;
        map[0][0].setType(NodeType.HOUSE_BLOCK);  // set corner nodes type
        map[height - 1][0].setType(NodeType.HOUSE_BLOCK);
        map[0][width - 1].setType(NodeType.HOUSE_BLOCK);
        map[height - 1][width - 1].setType(NodeType.HOUSE_BLOCK);
        map[0][1].setRandomAsHouseBlock();
        map[height - 1][1].setTypeSame(map[0][1]);
        map[1][0].setRandomAsHouseBlock();
        map[1][width - 1].setTypeSame(map[1][0]);

        for (int i = 2; i < width - 1; i++) { // set first & last line
            node = map[0][i];
            if (!node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT)) {
                node.setType(NodeType.HOUSE_BLOCK);
                map[height - 1][i].setTypeSame(node);
                continue;
            }
            if (node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT) && node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.LEFT)) {
                continue;
            }
            if (node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT) && node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.RIGHT)) {
                continue;
            }
            node.setRandomAsHouseBlock();
            map[height - 1][i].setTypeSame(node);
        }

        for (int i = 2; i < height - 1; i++) { //set first and last column
            node = map[i][0];
            if (!node.verify(NodeType.HOUSE_BLOCK, LocType.UP)) {
                node.setType(NodeType.HOUSE_BLOCK);
                map[i][width - 1].setTypeSame(node);
                continue;
            }
            if (node.verify(NodeType.HOUSE_BLOCK, LocType.UP) && node.verify(NodeType.HOUSE_BLOCK, LocType.UP, LocType.UP)) {
                continue;
            }
            if (node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN) && node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN, LocType.DOWN)) {
                continue;
            }
            node.setRandomAsHouseBlock();
            map[i][width - 1].setTypeSame(node);
        }

        for (int i = 1; i < width - 1; i++) { //set second line
            node = map[1][i];
            if (!node.verify(NodeType.HOUSE_BLOCK, LocType.UP) || node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.DOWN) ||
                    node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.DOWN)) {
                continue;
            }
            node.setRandomAsHouseBlock();
        }

        for (int i = 1; i < width - 1; i++) { //set before last line
            node = map[height - 2][i];
            if (!node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN) || node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.UP) ||
                    node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.UP)) {
                continue;
            }
            map[height - 2][i].setRandomAsHouseBlock();
        }

        for (int i = 2; i < height - 2; i++) { //set second column
            node = map[i][1];
            if (check_A(node) || (!node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT))) {
                continue;
            }
            node.setRandomAsHouseBlock();
        }

        for (int i = 2; i < height - 2; i++) {  //set before last column
            node = map[i][width - 2];
            if (check_A(node) || !node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT)) {
                continue;
            }
            node.setRandomAsHouseBlock();
        }

        for (int i = 2; i < height - 2; i++) {  //set the main square
            for (int j = 2; j < width - 2; j++) {
                node = map[i][j];
                if (check_E(node)) {
                    node.setType(NodeType.HOUSE_BLOCK);
                    continue;
                }
                if (check_D(node)) {
                    continue;
                }
                node.setRandomAsHouseBlock();
            }
        }

        for (int i = 0; i < height; i++) {   //fill spaces not surrounded  with HOUSE_BLOCKS
            for (int j = 0; j < width; j++) {
                node = map[i][j];
                if (!node.verify(NodeType.HOUSE_BLOCK)) {
                    if (check_B(node) || check_C(node)) {
                        node.setType(NodeType.HOUSE_BLOCK);
                    }
                }
            }
        }

    }

    public void buildHouses() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Node node = map[i][j];
                if (node.typeIs(NodeType.HOUSE_BLOCK)) {
                    House house = new House();
                    house.add(node, i, j, width, height);
                    houses.put(map[i][j], house);
                }
            }
        }
      //  System.out.println(houses.size());
    }


    private boolean check_A(Node node) {
        return (node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.UP) && node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.UP, LocType.UP)) ||
                (node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.UP) && node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.UP, LocType.UP)) ||
                (node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.DOWN) && node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.DOWN, LocType.DOWN)) ||
                (node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.DOWN) && node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.DOWN, LocType.DOWN));
    }

    private boolean check_B(Node node) {
        if (node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT) && node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.UP) &&
                node.verify(NodeType.HOUSE_BLOCK, LocType.UP) && !node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT) &&
                !node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.DOWN) && !node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN)) {
            return true;
        }

        if (node.verify(NodeType.HOUSE_BLOCK, LocType.UP) && node.verify(NodeType.HOUSE_BLOCK, LocType.UP, LocType.RIGHT) &&
                node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT) && !node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT) &&
                !node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.DOWN) && !node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN)) {
            return true;
        }

        if (node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN) && node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.DOWN) &&
                node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT) && node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT) &&
                node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.UP) && node.verify(NodeType.HOUSE_BLOCK, LocType.UP)) {
            return true;
        }

        if (node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT) && node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.DOWN) &&
                node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN) && node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT) &&
                !node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.UP) && !node.verify(NodeType.HOUSE_BLOCK, LocType.UP)) {
            return true;
        }

        if (node.verify(NodeType.HOUSE_BLOCK, LocType.UP) && node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT) &&
                node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.UP) && node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT) &&
                node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT)) {
            return true;
        }
        if (node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN) && node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.DOWN) &&
                node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT) && node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.UP) &&
                node.verify(NodeType.HOUSE_BLOCK, LocType.UP)) {
            return true;
        }
        return false;
    }


    private boolean check_C(Node node) {
        if (node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN) && !node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.UP) &&
                !node.verify(NodeType.HOUSE_BLOCK, LocType.UP) && !node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.UP)) {
            return true;
        }

        if (node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT) && !node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.UP) &&
                !node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT) && !node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.DOWN)) {
            return true;
        }
        if (node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT) && !node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.UP) &&
                !node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT) && !node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.DOWN)) {
            return true;
        }
        if (node.verify(NodeType.HOUSE_BLOCK, LocType.UP) && !node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.DOWN) &&
                !node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN) && !node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.DOWN)) {
            return true;
        }
        return false;
    }

    private boolean check_D(Node node) {
        return (node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT) && node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.LEFT)) ||
                (node.verify(NodeType.HOUSE_BLOCK, LocType.UP) && node.verify(NodeType.HOUSE_BLOCK, LocType.UP, LocType.UP)) ||
                (node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN) && node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN, LocType.DOWN)) ||
                (node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT) && node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.RIGHT)) ||
                (node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.UP)) ||
                (node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.DOWN)) ||
                (node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.UP)) ||
                (node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.DOWN));
    }

    private boolean check_E(Node node) {
        return (!node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT) && !node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.LEFT)) &&
                (!node.verify(NodeType.HOUSE_BLOCK, LocType.UP) && !node.verify(NodeType.HOUSE_BLOCK, LocType.UP, LocType.UP)) &&
                (!node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN) && !node.verify(NodeType.HOUSE_BLOCK, LocType.DOWN, LocType.DOWN)) &&
                (!node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT) && !node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.RIGHT)) &&
                (!node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.UP)) &&
                (!node.verify(NodeType.HOUSE_BLOCK, LocType.RIGHT, LocType.DOWN)) &&
                (!node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.UP)) &&
                (!node.verify(NodeType.HOUSE_BLOCK, LocType.LEFT, LocType.DOWN));
    }

    public void addVan() {
        int midI = height / 2;
        int midJ = width / 2;
        while (map[midI][midJ].isHouse() && map[midI][midJ+1].isHouse()) {
            midI++;
        }
        van1 = new Van(midI, midJ);
        van2 = new Van(midI, midJ+1);
        van1.setStructure(this);
        van2.setStructure(this);
    }

    public Van getVan1() {
        return van1;
    }

    public Van getVan2() {
        return van2;
    }

    public Node getNodeByCoordinate(int i,int j){
        int nodeI = (i - baseY) / BLOCK;
        int nodeJ = (j - baseX) / BLOCK;
        return  map[nodeI][nodeJ];
    }


    public enum   NodeType {
        HOUSE_BLOCK(1), HOUSE(2), PIPELINE_BLOCK(0), PIPELINE(5) ;
        private int value;

        NodeType(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}

