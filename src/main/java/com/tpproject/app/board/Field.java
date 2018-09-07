package com.tpproject.app.board;

import java.awt.*;

/**
 * Contains info about Board's field
 */
public class Field {

    private Point coordinates;
    private boolean isOccupied;


    public Field(int coordinateX, int coordinateY){
        this.isOccupied = false;
        this.coordinates = new Point(coordinateX, coordinateY);
    }

    public boolean isOccupied(){
        return isOccupied;
    }

    public void changeOccupancy(){
        this.isOccupied = !this.isOccupied;
    }

    public Point getCoordinates() { return coordinates; }


}
