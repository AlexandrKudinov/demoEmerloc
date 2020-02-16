package websocket;


import java.util.LinkedList;
import java.util.List;


class GameStage {
    List<ToScreen> houses = new LinkedList<>();
    List<ToScreen> valves = new LinkedList<>();
    List<ToScreen> emergencies = new LinkedList<>();
    List<ToScreen> dots = new LinkedList<>();
    List<ToScreen> verticalPlumbs = new LinkedList<>();
    List<ToScreen> horizontalPlumbs = new LinkedList<>();
    List<Integer> playerOnePosition = new LinkedList<>();
    List<Integer> playerTwoPosition = new LinkedList<>();


    public void clearAll() {
        houses.clear();
        valves.clear();
        emergencies.clear();
        dots.clear();
        horizontalPlumbs.clear();
        verticalPlumbs.clear();
        playerOnePosition.clear();
        playerTwoPosition.clear();
    }


    public void addHouse(ToScreen house){
        houses.add(house);
    }
    public void addValve(ToScreen valve){
        valves.add(valve);
    }
    public void addEmergency(ToScreen emergency){
        emergencies.add(emergency);
    }
    public void addDot(ToScreen dot){
        dots.add(dot);
    }
    public void addHorizontalPlumb(ToScreen horizontalPlumb){
        horizontalPlumbs.add(horizontalPlumb);
    }
    public void addVerticalPlumb(ToScreen verticalPlumb){
        verticalPlumbs.add(verticalPlumb);
    }



    public List<ToScreen> getHouses() {
        return houses;
    }

    public void setHouses(List<ToScreen> houses) {
        this.houses = houses;
    }

    public List<ToScreen> getValves() {
        return valves;
    }

    public void setValves(List<ToScreen> valves) {
        this.valves = valves;
    }

    public List<ToScreen> getEmergencies() {
        return emergencies;
    }

    public void setEmergencies(List<ToScreen> emergencies) {
        this.emergencies = emergencies;
    }

    public List<ToScreen> getDots() {
        return dots;
    }

    public void setDots(List<ToScreen> dots) {
        this.dots = dots;
    }

    public List<ToScreen> getHorizontalPlumb() {
        return horizontalPlumbs;
    }

    public void setHorizontalPlumb(List<ToScreen> horizontalPlumbs) {
        this.horizontalPlumbs = horizontalPlumbs;
    }

    public List<ToScreen> getVerticalPlumb() {
        return verticalPlumbs;
    }

    public void setVerticalPlumb(List<ToScreen> verticalPlumbs) {
        this.verticalPlumbs = verticalPlumbs;
    }

    public List<Integer> getPlayerOnePosition() {
        return playerOnePosition;
    }

    public void setPlayerOnePosition(List<Integer> playerOnePosition) {
        this.playerOnePosition = playerOnePosition;
    }

    public List<Integer> getPlayerTwoPosition() {
        return playerTwoPosition;
    }

    public void setPlayerTwoPosition(List<Integer> playerTwoPosition) {
        this.playerTwoPosition = playerTwoPosition;
    }
}
