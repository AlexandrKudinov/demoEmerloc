package logic;


public enum  NodeType {
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