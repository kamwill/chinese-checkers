package com.tpproject.app.board;

import com.tpproject.app.exceptions.FieldAlreadyOccupied;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class TestBoard {
    @Test
    public void testAddArea(){
        Board board = new Board();
        Corner corner = new Corner(0,0,0,0, 1);
        board.addArea(corner);
        assertEquals(corner, board.getAreas().get(0));
    }

    @Test
    public void testSetAreas(){
        Board board = new Board();
        List<Area> areaList = new ArrayList<Area>();
        areaList.add(new Corner(0,0,0,0,1));
        board.setAreas(areaList);
        assertEquals(areaList, board.getAreas());
    }
}
