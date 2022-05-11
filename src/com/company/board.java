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
            System.out.println("\nPressed buttons are: " + pressedButtons[0].getX() + " " + pressedButtons[0].getY() + " " + pressedButtons[1].getX() + " " + pressedButtons[1].getY());
            fields[pressedButtons[0].getY()][pressedButtons[0].getX()].setIsPressed(false);
            fields[pressedButtons[1].getY()][pressedButtons[1].getX()].setIsPressed(false);//reset isPressed flag IMPORTANT: IT IS JUST FOR TESTING PURPOSES
            if (move(pressedButtons) != 0) {
                System.out.println("Invalid move attempt");
            }
            refreshIcons();
        }
    }


    coordinates pawnBetweenFields(coordinates[] pressedButtons){//method to find pawns when queen is attacking, returns null if it finds more than one pawn
        coordinates pawn = null;

        coordinates attackingPawnField = pressedButtons[0];
        System.out.println("attackingPawnField(x y): " + attackingPawnField.getX() + " " + attackingPawnField.getY());
        boolean attackDown = pressedButtons[0].getY() < pressedButtons[1].getY();//true if moving pawn is above its destination
        boolean attackRight = pressedButtons[0].getX() < pressedButtons[1].getX();//true if moving pawn is left of its destination

        System.out.println("attackUp: " + attackDown + " attackRight: " + attackRight);
        int y = attackingPawnField.getY(), x = attackingPawnField.getX();
        coordinates firstOutOfRangeField = new coordinates(attackRight ? pressedButtons[1].getX() + 1 : pressedButtons[1].getX() - 1, attackDown ? pressedButtons[1].getY() + 1 : pressedButtons[1].getY() - 1);//field used for loop condition below


        y = attackDown ? y + 1 : y - 1;//first increments/decrements to avoid checking attacking field
        x = attackRight ? x + 1 : x - 1;
        while( y != firstOutOfRangeField.getY() || x != firstOutOfRangeField.getX() ){
            System.out.println("Checking field x=" + x + " y=" + y);
            if (pawn != null && fields[y][x].getCurrentPawn() != empty){//another pawn between fields detected
                System.out.println("Pawn detected on field x=" + x + " y=" + y + " called out with fields[y][x]\npawn = " + pawn);
                return null;
            }
            if (fields[y][x].getCurrentPawn() != empty) {//pawn detected
                pawn = new coordinates(x, y);
                System.out.println("Pawn detected on field x=" + x + " y=" + y);
            }
            y = attackDown ? y + 1 : y - 1;
            x = attackRight ? x + 1 : x - 1;
        }
        return pawn != null ? pawn : new coordinates(-1, -1);//return the only found pawn coordinates if not NULL, if null then return coordinates of -1,-1 meaning there is no pawn between fields
    }

    int move(coordinates[] pressedButtons){
        if (isAttack(pressedButtons)
            && fields[pressedButtons[0].getY()][pressedButtons[0].getX()].getCurrentPawn() != whiteQueen
            && fields[pressedButtons[0].getY()][pressedButtons[0].getX()].getCurrentPawn() != blackQueen) {

                System.out.println("IF 0 started");
                if (swapFieldValues(pressedButtons) != 0){
                    System.out.println("pierwszy if");
                    return 1;
                }else if (fields[pressedButtons[0].getY()][pressedButtons[0].getX()].getCurrentPawn() != whiteQueen//attack of pawn
                        && fields[pressedButtons[0].getY()][pressedButtons[0].getX()].getCurrentPawn() != blackQueen){
                    coordinates fieldToRemove = fieldBetween(pressedButtons);
                    fields[fieldToRemove.getY()][fieldToRemove.getX()].setCurrentPawn(empty);
                    return 0;
                }
            }

        if (fields[pressedButtons[0].getY()][pressedButtons[0].getX()].getCurrentPawn() == whiteQueen//move/attack of queen
                || fields[pressedButtons[0].getY()][pressedButtons[0].getX()].getCurrentPawn() == blackQueen){
            System.out.println("IF 1 started: move/attack of queen");
            coordinates fieldToRemove = pawnBetweenFields(pressedButtons);
            if (fieldToRemove == null) {//multiple pawns between fields
                System.out.println("Multiple or no pawns between fields, if 1 (move/attack of queen)");
                //return 1;
            }else if(fieldToRemove.getX() == -1 && fieldToRemove.getY() == -1) {// no pawn between fields
                System.out.println("Zwykly ruch");
                if (swapFieldValues(pressedButtons) != 0) {
                    System.out.println("swapFieldValues failed in if 1 (move/attack of queen)");
                    return 1;
                }
            }else if (fields[fieldToRemove.getY()][fieldToRemove.getX()].getCurrentPawn()%2 != fields[pressedButtons[0].getY()][pressedButtons[0].getX()].getCurrentPawn()%2) {//field in between has got different color than moving pawn
                System.out.println("Mamy bicie prosze panstwa");
                if (swapFieldValues(pressedButtons) != 0) {
                    System.out.println("swapFieldValues failed in if 1 (move/attack of queen)");
                    return 1;
                }
                fields[fieldToRemove.getY()][fieldToRemove.getX()].setCurrentPawn(empty);
            }

            return 0;
        }
        else if(distanceBetweenFields(pressedButtons) == 1){//not attack, validate move distance
            System.out.println("IF 2 started");
            if (swapFieldValues(pressedButtons) != 0) {
                System.out.println("If 2 fail");
                return 1;
            }
            return 0;//all good, normal move
        }

        System.out.println("Mission failed");
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
        return (distanceBetweenFields(fieldCoordinates) >= 2 && fieldsHaveDifferentColorPawns(new coordinates[]{fieldCoordinates[0], fieldBetween(fieldCoordinates)}))
                || ((fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == blackQueen || fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == whiteQueen) && pawnBetweenFields(fieldCoordinates) != null); //if fields are the same color and are not empty OR queen attacks and there's opposite color pawn in between
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
            System.out.println("Wrong number of arguments in swapFieldValues");
            //System.out.println("Backend after fail\n");
            printBackend();
            return 1;
        }

        if (fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == empty){//empty first-pressed field case
            System.out.println("Field " + fieldCoordinates[0].getX() + " " + fieldCoordinates[0].getY() + " is empty");
            //System.out.println("Backend after fail\n");
            printBackend();
            return 2;
        }
        if(fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() != empty && fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn() != empty) {//both fields have pawns
            System.out.println("Unable to swap pawns");
            //System.out.println("Backend after fail\n");
            printBackend();
            return 3;
        }
        if(!colorCanMove(fieldCoordinates)){//color can't move and first field is not empty
            System.out.println("Color can't move");
            return 4;
        }

        if (fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == whiteQueen
                || fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == blackQueen
                && pawnBetweenFields(fieldCoordinates) != null){
            System.out.println("Queen attacks in swapFieldValues()");
            //SWAPPING VALUES!!!
            int tempFieldValue = fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn();
            fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].setCurrentPawn(fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn());
            fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].setCurrentPawn(tempFieldValue);
            turns.changeTurn();
            return 0;
        }

        int distance = distanceBetweenFields(fieldCoordinates);
        switch (distance){                                          //values swapping
            case -1:{
                System.out.println("Fields are not on the same diagonal");
                return 4;
            }
            default:{
                if (distance >= 2){
                    if (fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == blackQueen || fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn() == whiteQueen){
                        System.out.println("Queen detected in swapFieldValues()");
                        break;
                    }
                }
                if (distance > 2){
                    System.out.println("Distance between fields is too big");
                    return 5;
                }
            }
        }
        //SWAPPING VALUES!!!
        int tempFieldValue = fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].getCurrentPawn();
        fields[fieldCoordinates[0].getY()][fieldCoordinates[0].getX()].setCurrentPawn(fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].getCurrentPawn());
        fields[fieldCoordinates[1].getY()][fieldCoordinates[1].getX()].setCurrentPawn(tempFieldValue);

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
                        case blackQueen: {//field with black queen
                            icon = new ImageIcon(getClass().getResource("icons/blackQueen.png"));
                            break;
                        }
                        case whiteQueen: {//field with white queen
                            icon = new ImageIcon(getClass().getResource("icons/whiteQueen.png"));
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
                try {
                    if (j.getCurrentPawn() == whitePawn && j.getY() == 0) {
                        j.setIcon(new ImageIcon(getClass().getResource("icons/whiteQueen.png")));
                        j.setCurrentPawn(whiteQueen);
                    }
                    if (j.getCurrentPawn() == blackPawn && j.getY() == 7) {
                        j.setIcon(new ImageIcon(getClass().getResource("icons/blackQueen.png")));
                        j.setCurrentPawn(blackQueen);
                    }
                }catch (NullPointerException e){
                    System.out.println("NullPointerException in refreshIcons() for field Y:" + j.getY() + " X:" + j.getX());
                }
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



