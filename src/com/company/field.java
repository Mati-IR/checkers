package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

public class field {
    private int x;
    private int y;
    public final int empty = 0;
    public final int blackPawn = 1;
    public final int whitePawn = 2;
    private int currentPawn;
    private boolean isPressed;
    public JButton button;


    field(Icon icon, Dimension dimension, Color color, int x, int y){//initializer
        button = new JButton(icon);
        button.setPreferredSize(dimension);
        button.setBackground(color);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button "+ getX() + " " + getY() + " clicked");
                JButton button = (JButton) e.getSource();//returns the button that was clicked
                button.setBackground(Color.BLUE);
                setIsPressed(true);
            }
        });
        setX(x);//set point coordinates
        setY(y);
        isPressed = false;
    }
    void setIcon(Icon icon){
        button.setIcon(icon);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCurrentPawn() {
        return currentPawn;
    }

    public void setCurrentPawn(int currentPawn) {
        this.currentPawn = currentPawn;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setIsPressed(boolean pressed) {
        isPressed = pressed;
    }
}
