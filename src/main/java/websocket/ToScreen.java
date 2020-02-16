package websocket;

import java.util.List;

public class ToScreen{
    private List<Integer> coords;
    private boolean flag;

    public ToScreen(List<Integer> coords, boolean flag){
        this.coords=coords;
        this.flag=flag;
    }

    public List<Integer> getCoords() {
        return coords;
    }

    public void setCoords(List<Integer> coords) {
        this.coords = coords;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}