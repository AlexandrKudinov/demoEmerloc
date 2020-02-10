package logic;

import com.google.gson.Gson;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        add(new GameField());
        setTitle("Emerloc.01");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        setResizable(false);
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}