package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Thread.sleep;

public class board {
    int gameType = -1;//incorrect value for used in do while loop in choosegameType()
    private final int A = 1;
    private final int B = 2;
    private final int C = 3;
    private final int D = 4;
    private final int E = 5;
    private final int F = 6;
    private final int G = 7;
    private final int H = 8;

    private static final Color myBrown = new Color(143, 90, 10);
    private static final Color myLtBrown = new Color(245, 200, 144);
    private int size = 8;//x and y dimension of board
    JButton fieldsVisual[][] = new JButton[size][size];//2D array for GUI

    private int fieldsValues[][] = new int[size][size];//2D array for backend depiction of field. It portrays placement of pawns on the board
    private final int empty = 0;
    private final int darkPawn = 1;
    private final int lightPawn = 2;

    private int initialFieldValue(int xAxis, int yAxis) {
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

    private void drawBoard() {

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
                    case 0: {//empty dark field
                        icon = new ImageIcon(getClass().getResource("icons/dark_empty.png"));
                        futureColor = myBrown;
                        break;
                    }
                    case 1:{//empty light field
                        icon = new ImageIcon(getClass().getResource("icons/light_empty.png"));
                        futureColor = myLtBrown;
                        break;
                    }
                    case 2:{//dark field with dark pawn
                        icon = new ImageIcon(getClass().getResource("icons/dark_darkpawn.png"));
                        futureColor = myBrown;
                        break;
                    }
                    case 3:{//dark field with light pawn
                        icon = new ImageIcon(getClass().getResource("icons/dark_lightpawn.png"));
                        futureColor = myBrown;
                        break;
                    }
                    default:{//default case that SHOULD NOT occur
                        icon = new ImageIcon(getClass().getResource("icons/test.png"));
                    }

                }
                fieldsVisual[yAxis][xAxis] = new JButton(icon);   //create button with icon image on it
                fieldsVisual[yAxis][xAxis].setSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
                fieldsVisual[yAxis][xAxis].setBackground(futureColor);
                panel.add(fieldsVisual[yAxis][xAxis]);//adding fields of checkers board
            }
        }

        frame.setVisible(true);
        frame.add(panel);       //include checkers board in game window
        panel.setVisible(true);


    }

    int getSize(){
        return size;
    }
    int getGameType(){
        return gameType;
    }

    private void choosegameType(){
        //set up choice window
        JFrame frame = new JFrame("Choose game type");
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JLabel label = new JLabel("Choose game type:");

        //setup buttons
        JButton choice0 = new JButton("Hotseat");
        JButton choice1 = new JButton("LAN");
        JButton choice2 = new JButton("PvE");

        //add action listeners
        choice0.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                gameType = 0;
                frame.dispose();
            }
        });
        choice1.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                gameType = 1;
                frame.dispose();
            }
        });
        choice2.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                gameType = 2;
                frame.dispose();
            }
        });

        //add buttons to frame
        frame.add(label);
        frame.add(choice0);
        frame.add(choice1);
        frame.add(choice2);


        frame.setVisible(true);

        do {
            System.out.print("");//without this statement program will not see user input
        }while(getGameType() == -1);//wait for input

        return;
    }

    private void hotseatGame(){

    }

    public void initialize(){
        drawBoard();         //open window with board GUI
    }
    public void play(){
        choosegameType();
        System.out.println("gameType = " + getGameType());
        /*
        0 - hotseat
        1 - LAN
        2 - PvE
         */
        switch (getGameType()){
            case 0:{
                hotseatGame();
            }
        }
    }

}
