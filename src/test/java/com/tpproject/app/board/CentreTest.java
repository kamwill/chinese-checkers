package com.tpproject.app.board;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CentreTest {
    @Test(expected = IllegalArgumentException.class)
    public void testCentreException(){
        Centre centre = new Centre(0, -1, 1);
    }

    @Test
    public void testNumberOfFields(){
        Centre centre = new Centre(0, 8, 4);
        assertEquals(61, centre.getFields().size());
    }

    @Test
    public void testID(){
        Centre centre = new Centre(1, 1,1);
        assertEquals(1, centre.getAreaID());
    }
}
