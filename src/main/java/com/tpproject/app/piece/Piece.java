package com.tpproject.app.piece;

import java.awt.*;

/**
 * Contains info about piece used to play Game
 */
public class Piece {
    private Point coordinates;
    private int playerID;
    private boolean inDestination = false;

    public Piece(int playerID, int coordinateX, int coordinateY){
        this.playerID = playerID;
        this.coordinates = new Point(coordinateX, coordinateY);
    }

    public int getPlayerID() {
        return playerID;
    }



    public Point getCoordinates(){ return this.coordinates;}

    public void setCoordinates(Point point){
        this.coordinates.setLocation(point);
    }

    public boolean isInDestination() { return inDestination; }

    public void setInDestination(boolean newValue){this.inDestination=newValue; }
}
