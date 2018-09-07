package com.tpproject.app.board;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class CornerTest {
    @Test(expected = IllegalArgumentException.class)
    public void testCornerException(){
        Corner corner = new Corner(0, 0, 0, 0, -1);
    }

    @Test
    public void testNumberOfFields(){
        Corner corner = new Corner(0, 0, 0, 0, 4);
        int expected = 10;
        assertEquals(expected, corner.getFields().size());
    }

    @Test
    public void testID(){
        Corner corner = new Corner(0, 0, 0, 1, 1);
        assertEquals(1, corner.getAreaID());
    }

    @Test
    public void testStartingPoint(){
        Corner corner = new Corner(2, 3, 0, 0, 4);
        String actual = (int) corner.getFields().get(0).getCoordinates().getX() + " " +
                (int) corner.getFields().get(0).getCoordinates().getY();
        assertEquals("2 3", actual);
    }
}
