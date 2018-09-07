package com.tpproject.app.piece;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;

public class PieceTest {

    @Test
    public void setCoordinateXTest(){
        Piece piece = new Piece(0, 0, 0);
        piece.setCoordinates(new Point(1,0));
        assertEquals((int) piece.getCoordinates().getX(), 1);
    }

    @Test
    public void setCoordinateTest(){
        Piece piece = new Piece(0, 0, 0);
        piece.setCoordinates(new Point(0,1));
        assertEquals((int) piece.getCoordinates().getY(), 1);
    }
}
