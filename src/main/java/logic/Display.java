package logic;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

import static logic.LocType.*;


public class Display {
   private WaterSupplyMap waterSupplyMap;

    public void setWaterSupplyMap(WaterSupplyMap waterSupplyMap) {
        this.waterSupplyMap = waterSupplyMap;
    }

    public static boolean onEmergency = false;
    public static Image van2 = new ImageIcon("Van2.png").getImage();
    public static Image van1 = new ImageIcon("Van1.png").getImage();
    public static final int BLOCK = 20;
    public static int baseX = 40;
    public static int baseY = 10;
    public static Color background = new Color(24, 24, 24);
    public static Color houseOpen = new Color(65, 65, 65);
    public static Color houseClose = new Color(45, 45, 45);
    public static Color plumbingOpen = new Color(0, 168, 243);
    public static Color plumbingClose = new Color(0, 67, 114);
    public static Color valveOpen = Color.white;
    public static Color valveClose = Color.red;


    public static void changeStage() {
        onEmergency = !onEmergency;
    }

    public static void showAccident(Graphics g, int x, int y) {
        g.setColor(Color.red);
        g.fillRect(x + BLOCK/10*3, y + BLOCK/10*3, BLOCK/5*2, BLOCK/5*2);
    }

    public static void showHouseBlock(Graphics g, int x, int y, boolean isOpen) {
        if (isOpen) {
            g.setColor(houseOpen);
        } else {
            g.setColor(houseClose);
        }
        g.fillRect(x, y, BLOCK, BLOCK);
    }


    public static void showUpDot(Graphics g, int x, int y, boolean isOpen) {
        if (isOpen) {
            g.setColor(plumbingOpen);
        } else {
            g.setColor(plumbingClose);
        }
        g.fillRect(x + BLOCK/5*2, y, BLOCK/5, BLOCK/5);
    }


    public static void showUpPlumb(Graphics g, int x, int y, boolean isOpen) {
        if (isOpen) {
            g.setColor(plumbingOpen);
        } else {
            g.setColor(plumbingClose);
        }
        g.fillRect(x + BLOCK/5*2, y, BLOCK/5, BLOCK/5*3);
    }


    public static void showDownDot(Graphics g, int x, int y, boolean isOpen) {
        if (isOpen) {
            g.setColor(plumbingOpen);
        } else {
            g.setColor(plumbingClose);
        }
        g.fillRect(x + BLOCK/5*2, y + BLOCK/5*4, BLOCK/5, BLOCK/5);
    }

    public static void showDownPlumb(Graphics g, int x, int y, boolean isOpen) {
        if (isOpen) {
            g.setColor(plumbingOpen);
        } else {
            g.setColor(plumbingClose);
        }
        g.fillRect(x + BLOCK/5*2, y + BLOCK/5*2, BLOCK/5, BLOCK/5*3);
    }

    public static void showLeftDot(Graphics g, int x, int y, boolean isOpen) {
        if (isOpen) {
            g.setColor(plumbingOpen);
        } else {
            g.setColor(plumbingClose);
        }
        g.fillRect(x, y + BLOCK/5*2, BLOCK/5, BLOCK/5);
    }

    public static void showLeftPlumb(Graphics g, int x, int y, boolean isOpen) {
        if (isOpen) {
            g.setColor(plumbingOpen);
        } else {
            g.setColor(plumbingClose);
        }
        g.fillRect(x, y + BLOCK/5*2, BLOCK/5*3, BLOCK/5);
    }


    public static void showRightDot(Graphics g, int x, int y, boolean isOpen) {
        if (isOpen) {
            g.setColor(plumbingOpen);
        } else {
            g.setColor(plumbingClose);
        }
        g.fillRect(x + BLOCK/5*4, y + BLOCK/5*2, BLOCK/5, BLOCK/5);
    }

    public static void showRightPlumb(Graphics g, int x, int y, boolean isOpen) {
        if (isOpen) {
            g.setColor(plumbingOpen);
        } else {
            g.setColor(plumbingClose);
        }
        g.fillRect(x + BLOCK/5*2, y + BLOCK/5*2, BLOCK/5*3, BLOCK/5);
    }




    public static void showValve(Graphics g, int i, int j, boolean isOpen) {
        if (isOpen) {
            g.setColor(valveOpen);
        } else {
            g.setColor(valveClose);
        }
        g.fillRect(i, j, BLOCK/5, BLOCK/5);
    }


    public  void showHousesMap(Graphics g) {
        waterSupplyMap.check();
        /*
        int I = GameField.structure.getVan().getI() - 75;
        int J = GameField.structure.getVan().getJ() - 150;
        System.out.println(I + "  ,  " + J);
        for (int i = 0; i < 150; i++) {
            for (int j = 0; j < 300; j++) {
                Node node = GameField.structure.getNodeByCoordinate(I, J);
                if (node.isHouse()) {
                    g.setColor(houseOpen);
                    g.fillRect(j * 4+baseX, i * 4+baseY, 4, 4);
                }
                //  int nodeI = (I - baseY) / BLOCK;
                //  int nodeJ = (J - baseX) / BLOCK;
                // System.out.println(nodeI+"..."+nodeJ);

                J++;
            }
            J = GameField.structure.getVan().getJ() - 150;
            I++;
        }

         */

        for (Pipeline pipeline : waterSupplyMap.getPipelines()) {
            if (!onEmergency) {
                if (pipeline.isAccident()) {
                    for (Pipe pipe : pipeline.getPipes().keySet()) {
                        if (pipe.isAccident()) {
                            Node node = pipe.getNode();
                            showAccident(g, baseX + node.getJ() * BLOCK, baseY + node.getI() * BLOCK);
                            break;
                        }
                    }
                }
            }
            for (House house : pipeline.getHouses()) {
                for (Node node : house.getHouseFragments()) {
                    showHouseBlock(g, baseX + node.getJ() * BLOCK, baseY + node.getI() * BLOCK, true);
                }
            }
        }

        if (onEmergency) {
           //GameField.waterSupplyMap.checkPipelines();
            for (Pipeline pipeline : waterSupplyMap.getPipelines()) {
                Map<Pipe, List<LocType>> pipelineMap = pipeline.getPipes();
                //pipeline.updateStatus();
                boolean open = pipeline.isOpen();
                for (Map.Entry<Pipe, List<LocType>> listEntry : pipelineMap.entrySet()) {
                    int i = listEntry.getKey().getNode().getI();
                    int j = listEntry.getKey().getNode().getJ();
                    Pipe pipe = listEntry.getKey();
                    if (listEntry.getValue().contains(UP)) {
                        if (pipe.containValve() && pipe.getValve().getType() == UP) {
                            showUpDot(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        } else {
                            showUpPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        }
                    }
                    if (listEntry.getValue().contains(LEFT)) {
                        if (pipe.containValve() && pipe.getValve().getType() == LEFT ||
                                pipe.containFourParts()) {
                            showLeftDot(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        } else {
                            showLeftPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        }
                    }
                    if (listEntry.getValue().contains(DOWN)) {
                        if (pipe.containValve() && pipe.getValve().getType() == DOWN) {
                            showDownDot(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        } else {
                            showDownPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        }
                    }
                    if (listEntry.getValue().contains(RIGHT)) {
                        if (pipe.containValve() && pipe.getValve().getType() == RIGHT ||
                                pipe.containFourParts()) {
                            showRightDot(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        } else {
                            showRightPlumb(g, baseX + j * BLOCK, baseY + i * BLOCK, open);
                        }
                    }

                }
                for (House house : pipeline.getHouses()) {
                    for (Node node : house.getHouseFragments()) {
                        showHouseBlock(g, baseX + node.getJ() * BLOCK, baseY + node.getI() * BLOCK, open);
                    }
                }

            }

            for (Valve valve : waterSupplyMap.getValves()) {
                showValve(g, valve.getJ(), valve.getI(), valve.isOpen());
            }


        }



    }
}
