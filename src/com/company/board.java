package com.company;

import javax.swing.*;
import java.awt.*;

public class board {
    public static final int A = 1;
    public static final int B = 2;
    public static final int C = 3;
    public static final int D = 4;
    public static final int E = 5;
    public static final int F = 6;
    public static final int G = 7;
    public static final int H = 8;

    int size = 8;//x and y dimension of board
    int fields[][] = new int[size][size];//2D array of size x size

    void initialize(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setTitle("Russian checkers :)");
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridLayout(0,1));



    }
}
