package logic;

import static logic.Display.*;

public class Valve {
    private LocType type;
    private boolean open = true;
    private int i;
    private int j;
    private Pipeline firstPipeline;
    private Pipeline secondPipeline;
    private boolean mysticOpen = true;

    public boolean isMysticOpen() {
        return mysticOpen;
    }

    public void setMysticOpen(boolean mysticOpen) {
        this.mysticOpen = mysticOpen;
    }

    public Pipeline getFirstPipeline() {
        return firstPipeline;
    }


    public boolean allButOneIsClose(Pipeline pipeline) {
        if(pipeline==firstPipeline || pipeline==secondPipeline) {
            for (Valve valve : pipeline.getValves()) {
                if (valve != this && (!valve.isOpen() || !valve.isMysticOpen())) {
                    continue;
                } else return false;
            }
        }
        return true;
    }


    public Pipeline getAnother(Pipeline pipeline) {
        if (pipeline == firstPipeline) {
            return secondPipeline;
        } else if (pipeline == secondPipeline) {
            return firstPipeline;
        } else return null;
    }

    public void setPipeline(Pipeline pipeline) {
        if (pipeline == secondPipeline || pipeline == firstPipeline) {
            return;
        }
        if (firstPipeline == null) {
            firstPipeline = pipeline;
        } else if (secondPipeline == null) {
            secondPipeline = pipeline;
        }
    }

    public Pipeline getSecondPipeline() {
        return secondPipeline;
    }

    public void setFirstPipeline(Pipeline firstPipeline) {
        this.firstPipeline = firstPipeline;
    }

    public void setSecondPipeline(Pipeline secondPipeline) {
        this.secondPipeline = secondPipeline;
    }

    public Valve(LocType type, int i, int j) {
        this.type = type;
        this.i = i;
        this.j = j;

        switch (type) {
            case UP:
                this.j =  j * Display.BLOCK + BLOCK / 5 * 2;  //+baseX
                this.i =  i * Display.BLOCK + BLOCK / 5;     //+baseY
                break;
            case LEFT:
                this.j =  j * Display.BLOCK + BLOCK / 5;
                this.i =  i * Display.BLOCK + BLOCK / 5 * 2;
                break;
            case RIGHT:
                this.i =  i * Display.BLOCK + BLOCK / 5 * 2;
                this.j =  j * Display.BLOCK + BLOCK / 5 * 3;
                break;

            case DOWN:
                this.i =  i * Display.BLOCK + BLOCK / 5 * 3;
                this.j =  j * Display.BLOCK + BLOCK / 5 * 2;
        }


    }

    public LocType getType() {
        return type;
    }

    public int getJ() {
        return j;
    }

    public int getI() {
        return i;
    }

    public boolean isOpen() {
        return open;
    }

    public void changeStage() {
        open = !open;
    }
}
