package com.company;

public class coordinates {
    private int x, y;
    coordinates(){
        x = 0;
        y = 0;
    }
    coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }
    void printCoordinates(){
        System.out.println("(" + x + "," + y + ")");
        return;
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
}
