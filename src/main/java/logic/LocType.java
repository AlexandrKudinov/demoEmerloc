package logic;

public enum LocType {
    UP, DOWN, LEFT, RIGHT;

    public static LocType getOppositeType(LocType type) {
        LocType oppositeType = null;
        switch (type) {
            case LEFT:
                oppositeType = RIGHT;
                break;
            case RIGHT:
                oppositeType = LEFT;
                break;
            case UP:
                oppositeType = DOWN;
                break;
            case DOWN:
                oppositeType = UP;
                break;
        }
        return oppositeType;
    }
}
