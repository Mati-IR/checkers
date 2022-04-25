package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static java.lang.Thread.sleep;

public class board {
    private int gameType = -1;//incorrect value for used in do while loop in choosegameType()


    private static final Color myBlack = new Color(143, 90, 10);
    private static final Color myWhite = new Color(245, 200, 144);
    private int size = 8;//x and y dimension of board
    //JButton fieldsVisual[][] = new JButton[size][size];//2D array for GUI
    field fieldsVisual[][] = new field[size][size];

    //private int fieldsValues[][] = new int[size][size];//2D array for backend depiction of field. It portrays placement of pawns on the board
    private final int empty = 0;
    private final int blackPawn = 1;
    private final int whitePawn = 2;

    public void initialize(){
        drawBoard();         //open window with board GUI
        for (int yAxis = 0; yAxis < size; yAxis++) {
            for (int xAxis = 0; xAxis < size; xAxis++) {
                System.out.print(fieldsVisual[yAxis][xAxis].getCurrentPawn());
            }
            System.out.println();

        }
    }
    public void play(){
        System.out.println(bothPlayersHavePawns());
        chooseGameType();
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
                int fieldValue = -1;

                switch (initialFieldValue(xAxis, yAxis)) {
                    case 0: {//empty dark field
                        //icon = new ImageIcon(getClass().getResource("icons/dark_empty.png"));
                        icon = new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
                        futureColor = myBlack;
                        fieldValue = empty;
                        break;
                    }
                    case 1:{//empty light field
                        //icon = new ImageIcon(getClass().getResource("icons/light_empty.png"));
                        //create transparent icon
                        icon = new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)); // transparent icon
                        futureColor = myWhite;
                        fieldValue = empty;
                        break;
                    }
                    case 2:{//dark field with dark pawn
                        icon = new ImageIcon(getClass().getResource("icons/blackPawn.png"));
                        futureColor = myBlack;
                        fieldValue = blackPawn;
                        break;
                    }
                    case 3:{//dark field with light pawn
                        //icon = new ImageIcon(getClass().getResource("icons/dark_lightpawn.png"));
                        icon = new ImageIcon(getClass().getResource("icons/whitePawn.png"));
                        futureColor = myBlack;
                        fieldValue = whitePawn;
                        break;
                    }
                    default:{//default case that SHOULD NOT occur
                        icon = new ImageIcon(getClass().getResource("icons/test.png"));
                    }

                }
                fieldsVisual[yAxis][xAxis] = new field(icon, new Dimension(icon.getIconWidth(), icon.getIconHeight()), futureColor, xAxis, yAxis);
                fieldsVisual[yAxis][xAxis].setCurrentPawn(fieldValue);
                panel.add(fieldsVisual[yAxis][xAxis].button);//adding fields of checkers board
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
    
    boolean bothPlayersHavePawns(){
        int whites = 0, blacks = 0;
        for (field i[]: fieldsVisual) {//iterate over each field and count pawns
            for (field j: i) {
                if(j.getCurrentPawn() == whitePawn)
                    whites++;
                if (j.getCurrentPawn() == blackPawn)
                    blacks++;
            }
        }
        //System.out.println("whites = " + whites + "\nblacks = " + blacks);
        return (blacks != 0 && whites != 0);
    }

    void findMoveAttempt(){
        int pressedButtonsCounter = 0;
        //array with 2 coordinates
        coordinates pressedButtons[] = new coordinates[2];
        for (field i[]: fieldsVisual) {//iterate over each field and count pawns
            for (field j: i) {
                if(j.getIsPressed()){
                    pressedButtonsCounter++;
                    pressedButtons[pressedButtonsCounter-1] = new coordinates(j.getX(), j.getY());//add pressed button coordinates to array
                }
            }
        }
        if(pressedButtonsCounter == 2){//show pressed buttons;
            System.out.println("Pressed buttons are: " + pressedButtons[0].getX() + " " + pressedButtons[0].getY() + " " + pressedButtons[1].getX() + " " + pressedButtons[1].getY());
            fieldsVisual[pressedButtons[0].getY()][pressedButtons[0].getX()].setIsPressed(false);
            fieldsVisual[pressedButtons[1].getY()][pressedButtons[1].getX()].setIsPressed(false);//reset isPressed flag IMPORTANT: IT IS JUST FOR TESTING PURPOSES
            //TODO: make pawn move in this if statement
            move(pressedButtons);
            refreshIcons();
        }
    }
    int move(coordinates pressedButtons[]){
        //TODO: create if that validates move
        if (swapFieldValues(pressedButtons) != 0){
            System.out.println("Move unsuccessful");
            return 1;
        }
        return 0;//everything ok
    }
    int swapFieldValues(coordinates fieldCoordinates[]){//2 values to swap
        if (fieldCoordinates.length != 2){
            System.out.println("Wrong number of arguments in swapFieldValues");
            return 1;
        }
        System.out.println("Current values are: " + fieldsVisual[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() + " " + fieldsVisual[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn());
        int tempFieldValue = fieldsVisual[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn();
        fieldsVisual[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].setCurrentPawn(fieldsVisual[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn());
        fieldsVisual[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].setCurrentPawn(tempFieldValue);
        System.out.println("swapped" + fieldCoordinates[0].getX() + " " + fieldCoordinates[0].getY() + " " + fieldCoordinates[1].getX() + " " + fieldCoordinates[1].getY());
        System.out.println("Values now are: " + fieldsVisual[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() + " " + fieldsVisual[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn());
        return 0;
    }
    private void refreshIcons() {
        System.out.println("Refreshing icons...");
        for (field i[] : fieldsVisual) {//iterate over each field and count pawns
            for (field j : i) {
                Icon icon;
                int fieldValue = -1;

                switch (j.getCurrentPawn()) {
                    case empty: {//empty dark field
                        //icon = new ImageIcon(getClass().getResource("icons/dark_empty.png"));
                        icon = new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
                        fieldValue = empty;
                        break;
                    }
                    case blackPawn: {//empty light field
                        //icon = new ImageIcon(getClass().getResource("icons/light_empty.png"));
                        //create transparent icon
                        icon = new ImageIcon(getClass().getResource("icons/blackPawn.png"));
                        fieldValue = blackPawn;
                        break;
                    }
                    case whitePawn: {//dark field with dark pawn
                        icon = new ImageIcon(getClass().getResource("icons/whitePawn.png"));
                        fieldValue = blackPawn;
                        break;
                    }
                    default: {//default case that SHOULD NOT occur
                        icon = new ImageIcon(getClass().getResource("icons/test.png"));
                        break;
                    }
                }
                j.setIcon(icon);
                j.button.setBackground(j.getFieldColor()); //remove highlight by re-painting the field
            }
        }
    }
    private void chooseGameType(){
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
        System.out.println("Hotseat game");
        while (bothPlayersHavePawns()) {
            findMoveAttempt();
        }
    }



}
