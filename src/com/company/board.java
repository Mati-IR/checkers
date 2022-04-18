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
    JButton fields[][] = new JButton[size][size];//2D array of size x size

    int initialFieldValue(int xAxis, int yAxis) {
        //0 dark_empty.png
        //1 light_empty.png
        //2 dark_darkpawn.png
        //3 dark_lightpawn.png
        //4 light_darkpawn.png
        //5 light_lightpawn.png
        //6 move_empty.png
        //7 attack_empty.png

        if (yAxis == 3 || yAxis == 4) {//middle fields, empty on start
            return xAxis%2 != yAxis%2 ? 1 : 0;
        }
        if (yAxis < 3 )//top fields, dark pawns on start
            return xAxis%2 != yAxis%2 ? 1 : 2;

        return xAxis%2 != yAxis%2 ? 1 : 3;//bottom fields, light pawns on start
    }

    void draw() {

        JFrame frame = new JFrame("Checkers");//        setting up game window
        frame.setSize(620, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JPanel panel = new JPanel(); //        setting up checkers board
        panel.setSize(100,100);
        panel.setLayout(new GridLayout(size, size));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 200, 10));

        for (int yAxis = 0; yAxis < size; yAxis++) { //     loop for adding fields of checkers board
            for (int xAxis = 0; xAxis < size; xAxis++) {
                Icon icon;
                Color futureColor = new Color(0, 0, 0);

                switch (initialFieldValue(xAxis, yAxis)) {
                    case 0: {
                        icon = new ImageIcon(getClass().getResource("icons/dark_empty.png"));
                        futureColor = myBrown;
                        break;
                    }
                    case 1:{
                        icon = new ImageIcon(getClass().getResource("icons/light_empty.png"));
                        futureColor = myLtBrown;
                        break;
                    }
                    case 2:{
                        icon = new ImageIcon(getClass().getResource("icons/dark_darkpawn.png"));
                        futureColor = myBrown;
                        break;
                    }
                    case 3:{
                        icon = new ImageIcon(getClass().getResource("icons/dark_lightpawn.png"));
                        futureColor = myLtBrown;
                        break;
                    }
                    default:{
                        icon = new ImageIcon(getClass().getResource("icons/test.png"));
                    }

                }
                fields[yAxis][xAxis] = new JButton(icon);   //create button with icon image on it
                fields[yAxis][xAxis].setSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
                fields[yAxis][xAxis].setBackground(futureColor);
                panel.add(fields[yAxis][xAxis]);//adding fields of checkers board
            }
        }

        frame.setVisible(true);
        frame.add(panel);       //include checkers board in game window
        panel.setVisible(true);


    }



    void initialize(){
        draw();         //open window with board GUI
    }
}
