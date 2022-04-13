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

    private static final Color myBrown = new Color(143, 90, 10);
    private static final Color myLtBrown = new Color(245, 200, 144);
    int size = 8;//x and y dimension of board
    int fields[][] = new int[size][size];//2D array of size x size

    void draw() {

        JFrame frame = new JFrame("Checkers");//        setting up game window
        frame.setSize(620, 810);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JPanel panel = new JPanel(); //        setting up checkers board
        panel.setSize(100,100);
        panel.setLayout(new GridLayout(size, size));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 200, 10));

        for (int i = 0; i < size; i++) { //     adding fields of checkers board
            for (int j = 0; j < size; j++) {
                JButton button = new JButton();
                if (i%2 == 0) {
                    if (j%2 == 0) {
                        button.setBackground(myLtBrown);
                    }else {
                        button.setBackground(myBrown);
                    }
                }else {
                    if (j%2 == 0) {
                        button.setBackground(myBrown);
                    }else {
                        button.setBackground(myLtBrown);
                    }
                }
                panel.add(button);
            }
        }

        frame.setVisible(true);
        frame.add(panel);       //include checkers board in game window
        panel.setVisible(true);


    }

    void initialize(){
        draw();
         }
}
