package com.tpproject.app.board;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class FieldTest {
    @Test
    public void testGetCoordinateX(){
        Field field = new Field(2,3);
        assertEquals(2, (int) field.getCoordinates().getX());
    }

    @Test
    public void testGetCoordinateY(){
        Field field = new Field(2,3);
        assertEquals(3, (int) field.getCoordinates().getY());
    }

    @Test
    public void testIsOccupied(){
        Field field = new Field(0,0);
        assertEquals(false, field.isOccupied());
    }

    @Test
    public void testChangeOccupancy(){
        Field field = new Field(0,0);
        field.changeOccupancy();
        assertEquals(true, field.isOccupied());
    }

}
