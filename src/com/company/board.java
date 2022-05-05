package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static java.lang.Math.abs;

public class board {
    private int gameType = -1;//incorrect value for used in do while loop in choosegameType()
    private final turn turns = new turn();


    private static final Color myBlack = new Color(143, 90, 10);
    private static final Color myWhite = new Color(245, 200, 144);
    private final int size = 8;//x and y dimension of board
    //JButton fieldsVisual[][] = new JButton[size][size];//2D array for GUI
    field[][] fields = new field[size][size];

    //private int fieldsValues[][] = new int[size][size];//2D array for backend depiction of field. It portrays placement of pawns on the board
    public final int empty = 0;
    public final int blackPawn = 1;
    public final int whitePawn = 2;
    public final int blackQueen = 3;
    public final int whiteQueen = 4;

    public void initialize(){
        drawBoard();         //open window with board GUI
        for (int yAxis = 0; yAxis < size; yAxis++) {
            for (int xAxis = 0; xAxis < size; xAxis++) {
                System.out.print(fields[yAxis][xAxis].getCurrentPawn());
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
                Icon icon = null;
                Color futureColor = new Color(0, 0, 0);
                int fieldValue = -1;
                try {
                    switch (initialFieldValue(xAxis, yAxis)) {
                        case 0: {//empty dark field
                            icon = new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
                            futureColor = myBlack;
                            fieldValue = empty;
                            break;
                        }
                        case 1: {//empty light field
                            icon = new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)); // transparent icon
                            futureColor = myWhite;
                            fieldValue = empty;
                            break;
                        }
                        case 2: {//dark field with dark pawn
                            icon = new ImageIcon(getClass().getResource("icons/blackPawn.png"));
                            futureColor = myBlack;
                            fieldValue = blackPawn;
                            break;
                        }
                        case 3: {//dark field with light pawn
                            icon = new ImageIcon(getClass().getResource("icons/whitePawn.png"));
                            futureColor = myBlack;
                            fieldValue = whitePawn;
                            break;
                        }
                        default: {//default case that SHOULD NOT occur
                            icon = new ImageIcon(getClass().getResource("icons/test.png"));
                        }

                    }
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException in method drawBoard(), check if all icons are in file /src/com.company/icons");
                    System.exit(1);
                }
                fields[yAxis][xAxis] = new field(icon, new Dimension(icon.getIconWidth(), icon.getIconHeight()), futureColor, xAxis, yAxis);
                fields[yAxis][xAxis].setCurrentPawn(fieldValue);
                panel.add(fields[yAxis][xAxis].button);//adding fields of checkers board
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
        for (field[] i : fields) {//iterate over each field and count pawns
            for (field j: i) {
                if(j.getCurrentPawn() == whitePawn)
                    whites++;
                if (j.getCurrentPawn() == blackPawn)
                    blacks++;
            }
        }

        return (blacks != 0 && whites != 0);
    }

    void findMoveAttempt() {
        int pressedButtonsCounter = 0;
        coordinates[] pressedButtons = new coordinates[2];        //array with 2 coordinates
        while(pressedButtonsCounter < 2) {
            for (field[] i : fields) {//iterate over each field and count pawns
                for (field j : i) {
                    if (j.getIsPressed()) {
                        if (pressedButtonsCounter == 0) {//first button pressed
                            //System.out.println("first button pressed");
                            pressedButtons[0] = new coordinates(j.getX(), j.getY());
                            pressedButtonsCounter++;
                        } else if (pressedButtonsCounter == 1 && (j.getX() != pressedButtons[0].getX() || j.getY() != pressedButtons[0].getY())) {//second button pressed and not on the same field
                            //System.out.println("second button pressed");
                            pressedButtons[1] = new coordinates(j.getX(), j.getY());
                            pressedButtonsCounter++;
                        }
                    }
                    if (pressedButtons[0] != null && !fields[pressedButtons[0].getY()][pressedButtons[0].getX()].getIsPressed()) {//button is no longer pressed
                        pressedButtons[0] = null;
                        pressedButtonsCounter = 0;
                    }
                }
            }
        }

        if (pressedButtonsCounter == 2) {//show pressed buttons;
            System.out.println("\nPressed buttons are: " + pressedButtons[0].getY() + " " + pressedButtons[0].getX() + " " + pressedButtons[1].getY() + " " + pressedButtons[1].getX());
            fields[pressedButtons[0].getY()][pressedButtons[0].getX()].setIsPressed(false);
            fields[pressedButtons[1].getY()][pressedButtons[1].getX()].setIsPressed(false);//reset isPressed flag IMPORTANT: IT IS JUST FOR TESTING PURPOSES
            if (move(pressedButtons) != 0) {
                System.out.println("Invalid move attempt");
            }
            refreshIcons();
        }
    }



    int move(coordinates[] pressedButtons){
        if (isAttack(pressedButtons)) {//attack
            if (swapFieldValues(pressedButtons) != 0){
                return 1;
            }else {
                coordinates fieldToRemove = fieldBetween(pressedButtons);
                fields[fieldToRemove.getY()][fieldToRemove.getX()].setCurrentPawn(empty);
                return 0;
            }
        }else if(distanceBetweenFields(pressedButtons) == 1){//not attack, validate move distance
            if (swapFieldValues(pressedButtons) != 0) {
                return 1;
            }
            return 0;//all good, normal move
        }
        return 1;//failure
    }
    void printBackend(){
        for (field[] i : fields) {//iterate over each field and count pawns
            for (field j: i) {
                System.out.print(j.getCurrentPawn());
            }
            System.out.println();
        }
    }
    int distanceBetweenFields(coordinates[] fieldCoordinates){
        if (abs(fieldCoordinates[0].getX() - fieldCoordinates[1].getX()) == abs(fieldCoordinates[0].getY() - fieldCoordinates[1].getY())) {//if fields are on the diagonal
            return abs(fieldCoordinates[0].getX() - fieldCoordinates[1].getX());
        }
        return -1;
    }
    coordinates fieldBetween(coordinates[] fieldCoordinates){// method to help isAttack() method
        return new coordinates((fieldCoordinates[0].getX() + fieldCoordinates[1].getX()) / 2, (fieldCoordinates[0].getY() + fieldCoordinates[1].getY()) / 2);
    }
    boolean fieldsHaveDifferentColorPawns(coordinates[] fieldCoordinates){//if fields are the same color
        //System.out.println("fieldsHaveDifferentColorPawns is checking fields " + fieldCoordinates[0].getY() + " " + fieldCoordinates[0].getX() + " and " + fieldCoordinates[1].getY() + " " + fieldCoordinates[1].getX());
        return (fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() != fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn())//if pawns are different color
            && (fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() != 0) && (fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn() != 0);//if fields are not empty
    }
    boolean isAttack(coordinates[] fieldCoordinates){
        //System.out.println("distanceBetweenFields " + distanceBetweenFields(fieldCoordinates));
        //System.out.println("fieldsHaveDifferentColorPawns " + fieldsHaveDifferentColorPawns(new coordinates[]{fieldCoordinates[0], fieldBetween(fieldCoordinates)}));
        return distanceBetweenFields(fieldCoordinates) >= 2 && fieldsHaveDifferentColorPawns(new coordinates[]{fieldCoordinates[0], fieldBetween(fieldCoordinates)}); //if fields are the same color and are not empty
    }
    boolean colorCanMove(coordinates[] fieldCoordinates){//if color can move
        return ((fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == whitePawn || fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == whiteQueen)
                && turns.isWhiteTurn())
                || ((fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == blackPawn || fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == blackQueen)
                && turns.isBlackTurn());
    }
    int swapFieldValues(coordinates[] fieldCoordinates){//2 values to swap
        //System.out.println("*********** NEW SWAP ***********");
        fieldCoordinates[0].printCoordinates();
        fieldCoordinates[1].printCoordinates();

        if (fieldCoordinates.length != 2 ){//bad parameter passed
            //System.out.println("Wrong number of arguments in swapFieldValues");
            //System.out.println("Backend after fail\n");
            printBackend();
            return 1;
        }

        if (fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == empty){//empty first-pressed field case
            //System.out.println("Field " + fieldCoordinates[0].getX() + " " + fieldCoordinates[0].getY() + " is empty");
            //System.out.println("Backend after fail\n");
            printBackend();
            return 2;
        }
        if(fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() != empty && fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn() != empty) {//both fields have pawns
            //System.out.println("Unable to swap pawns");
            //System.out.println("Backend after fail\n");
            printBackend();
            return 3;
        }
        if(!colorCanMove(fieldCoordinates)){
            System.out.println("Color can't move");
            return 4;
        }

        //System.out.println("*********** SUCCESS ***********");
        //System.out.println("Current values are: " + fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() + " " + fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn());


        int distance = distanceBetweenFields(fieldCoordinates);
        switch (distance){                                          //values swapping
            case -1:{
                System.out.println("Fields are not on the same diagonal");
                return 4;
            }
            default:{
                if (distance > 2){//TODO: add exception for queen and attack
                    System.out.println("Distance between fields is too big");
                    return 5;
                }
                int tempFieldValue = fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn();
                fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].setCurrentPawn(fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn());
                fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].setCurrentPawn(tempFieldValue);
            }
        }



        //System.out.println("swapped " + fieldCoordinates[0].getX() + " " + fieldCoordinates[0].getY() + " and " + fieldCoordinates[1].getX() + " " + fieldCoordinates[1].getY());
        //System.out.println("Values now are: " + fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() + " " + fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn() + "\n");

        /*System.out.println("Backend after swap:\n");
        printBackend();*/
        turns.changeTurn();
        return 0;
    }
    private void refreshIcons() {
        //System.out.println("Refreshing icons...");
        for (field[] i : fields) {//iterate over each field and count pawns
            for (field j : i) {
                Icon icon = null;
                j.setIsPressed(false);
                try {
                    switch (j.getCurrentPawn()) {
                        case empty: {//empty field
                            icon = new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
                            break;
                        }
                        case blackPawn: {//field with black pawn
                            icon = new ImageIcon(getClass().getResource("icons/blackPawn.png"));
                            break;
                        }
                        case whitePawn: {//field with white pawn
                            icon = new ImageIcon(getClass().getResource("icons/whitePawn.png"));
                            break;
                        }
                        default: {//default case that SHOULD NOT occur
                            icon = new ImageIcon(getClass().getResource("icons/test.png"));
                            System.out.println("Default case in refreshIcons() for field Y:" + j.getY() + " X:" + j.getX());
                            break;
                        }
                    }
                }catch (NullPointerException e){
                    System.out.println("NullPointerException in refreshIcons() for field Y:" + j.getY() + " X:" + j.getX());
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
    }

    public int whoWon(){
        for (field[] i : fields) {//iterate over each field and count pawns
            for (field j: i) {
                if(j.getCurrentPawn() == whitePawn)
                    return 0;//white won
                if (j.getCurrentPawn() == blackPawn)
                    return 1;//black won
            }
        }

        return -1;//no one won
    }

    private void hotseatGame(){
        System.out.println("Hotseat game");
        while (bothPlayersHavePawns()) {
            findMoveAttempt();
        }
        switch (whoWon()) {
            case 0: {
                System.out.println("White won");
                break;
            }
            case 1: {
                System.out.println("Black won");
                break;
            }
            default: {
                System.out.println("Error in function hotseatGame()");
                break;
            }
        }
    }
}

