package com.company;

public class turn {
    private boolean whiteTurn;
    private boolean blackTurn;

    public turn() {
        whiteTurn = true;
        blackTurn = false;
    }

    public void changeTurn() {
        if (whiteTurn) {
            whiteTurn = false;
            blackTurn = true;
        } else {
            whiteTurn = true;
            blackTurn = false;
        }
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public boolean isBlackTurn() {
        return blackTurn;
    }
}
