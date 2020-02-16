package logic;

import static logic.Display.*;

public class Van {
private Structure structure;

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    int i;
    int j;

    int MAX_J = (Structure.width-1)* BLOCK;//-1   //+baseY
    int MAX_I = (Structure.height-1) * BLOCK;//-1   //+baseX

    public Van(int i, int j) {
        this.i =  i * BLOCK;//+baseY
        this.j =  j * BLOCK;//+baseX
    }

    public boolean canMove(int i, int j) {
        int nodeI = (i) / BLOCK;
        int nodeJ = (j) / BLOCK;
        Node node = structure.getMap()[nodeI][nodeJ];
        return !node.isHouse();
    }

    private boolean onAccident() {
        int nodeI = (i ) / BLOCK;
        int nodeJ = (j ) / BLOCK;
        Node node = structure.getMap()[nodeI][nodeJ];
        return node.getPipe().isAccident();
    }


    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void turnLeft() {
        if (j == 0) {
            j = MAX_J;
            return;
        }
        if (canMove(i + 1, j - 1) && canMove(i + BLOCK - 1, j - 1)) {
            j -= BLOCK/2;
        }

        if (onAccident()) {
            onEmergency = true;
        } else onEmergency = false;
    }

    public void turnRight() {
        if (j == MAX_J) {
            j = 0;
            return;
        }
        if (canMove(i + 1, j + BLOCK + 1) && canMove(i + BLOCK - 1, j + BLOCK + 1)) {
            j += BLOCK/2;
        }
        if (onAccident()) {
            onEmergency = true;
        } else onEmergency = false;
    }

    public void turnUp() {
        if (i == 0) {
            i = MAX_I;
            return;
        }
        if (canMove(i - 1, j + 1) && canMove(i - 1, j + BLOCK - 1)) {
            i -= BLOCK/2;
        }
        if (onAccident()) {
            onEmergency = true;
        } else onEmergency = false;
    }

    public void turnDown() {
        if (i == MAX_I) {
            i = 0;
            return;
        }
        if (canMove(i + BLOCK + 1, j + 1) && canMove(i + BLOCK + 1, j + BLOCK - 1)) {
            i += BLOCK/2;
        }
        if (onAccident()) {
            onEmergency = true;
        } else onEmergency = false;

    }


}
