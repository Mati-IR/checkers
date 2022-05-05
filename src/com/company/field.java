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
    public final int blackQueen = 3;
    public final int whiteQueen = 4;
    private Color fieldColor;
    private int currentPawn;
    private boolean isPressed;
    public JButton button;


    field(Icon icon, Dimension dimension, Color FieldBackgroundcolor, int x, int y){//initializer
        initializeButton(icon, dimension, FieldBackgroundcolor);
        setX(x);//set point coordinates
        setY(y);
        isPressed = false;
        fieldColor = FieldBackgroundcolor;
    }

    void initializeButton(Icon icon, Dimension dimension, Color color){
        button = new JButton(icon);
        button.setPreferredSize(dimension);
        button.setBackground(color);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();//returns the button that was clicked
                if (getIsPressed()){//if button already pressed
                    setIsPressed(false);//disable the button
                    button.setBackground(fieldColor);//restore original field color
                    System.out.println("Button "+ getX() + " " + getY() + " UN-PRESSED");
                    return;
                }
                System.out.println("Button "+ getX() + " " + getY() + " pressed");
                button.setBackground(Color.BLUE);
                setIsPressed(true);
            }
        });
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

    public Color getFieldColor() {
        return fieldColor;
    }

    public void setFieldColor(Color fieldColor) {
        this.fieldColor = fieldColor;
    }
}
