package logic;



import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static logic.Display.BLOCK;
import static logic.Display.van2;
import static logic.Display.van1;


public class GameField extends JPanel {
    Display display = new Display();
    private static int xPosition;
    private static int yPosition;

    public  Structure structure = new Structure();
    public  WaterSupplyMap waterSupplyMap = new WaterSupplyMap();


    public GameField() {
        display.setWaterSupplyMap(waterSupplyMap);
        setFocusable(true);
        waterSupplyMap.setStructure(structure);
        structure.bind();
        structure.buildHouseBlocks();
        structure.buildHouses();
        setBackground(display.background);
        waterSupplyMap.setMap(structure.getMap());
        waterSupplyMap.addPipes();
        waterSupplyMap.addWaterIntake();
        waterSupplyMap.pipelineUnion();
        waterSupplyMap.generateAccidents();
        structure.addVan();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                xPosition = e.getX();
                yPosition = e.getY();
                if (mouseClickOnValve(xPosition, yPosition) && display.onEmergency) {
                    changeValveStage(xPosition, yPosition);
                    GameField.super.repaint();
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_A) {
                    structure.getVan1().turnLeft();
                    GameField.super.repaint();
                }
                if (key == KeyEvent.VK_D) {
                    structure.getVan1().turnRight();
                    GameField.super.repaint();
                }

                if (key == KeyEvent.VK_W) {
                    structure.getVan1().turnUp();
                    GameField.super.repaint();
                }
                if (key == KeyEvent.VK_S) {
                   structure.getVan1().turnDown();
                    GameField.super.repaint();
                }
//------------------------------------------------------------
                if (key == KeyEvent.VK_F) {
                    structure.getVan2().turnLeft();
                    GameField.super.repaint();
                }
                if (key == KeyEvent.VK_H) {
                    structure.getVan2().turnRight();
                    GameField.super.repaint();
                }

                if (key == KeyEvent.VK_T) {
                    structure.getVan2().turnUp();
                    GameField.super.repaint();
                }
                if (key == KeyEvent.VK_G) {
                    structure.getVan2().turnDown();
                    GameField.super.repaint();
                }

                if (key == KeyEvent.VK_R) {
                    display.changeStage();
                    GameField.super.repaint();
                }
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        display.showHousesMap(g);
        if (!Display.onEmergency) {
            g.drawImage(van2, structure.getVan2().getJ(), structure.getVan2().getI(),BLOCK,BLOCK,  this);
            g.drawImage(van1, structure.getVan1().getJ(), structure.getVan1().getI(),BLOCK,BLOCK,  this);
        }
    }


    public boolean mouseClickOnValve(int X, int Y) {
        for (Valve valve : waterSupplyMap.getValves()) {
            if ((X >= valve.getJ() && X <= valve.getJ() + BLOCK/5) &&
                    ((Y >= valve.getI() && Y <= valve.getI() + BLOCK/5))) {
                return true;
            }
        }
        return false;
    }

    public void changeValveStage(int X, int Y) {
        for (Valve valve : waterSupplyMap.getValves()) {
            if ((X >= valve.getJ() && X <= valve.getJ() + BLOCK/5) &&
                    ((Y >= valve.getI() && Y <= valve.getI() + BLOCK/5))) {
                valve.changeStage();
                return;
            }
        }
    }


}
