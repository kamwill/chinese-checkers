package com.tpproject.app.board.builder;

import com.tpproject.app.board.Area;
import org.junit.Test;
import com.tpproject.app.board.Board;

import static junit.framework.Assert.assertEquals;

public class BoardAssemblerTest {

    @Test
    public void testAssembleClassicsStarBoard(){
        BoardAssembler assembler = new BoardAssembler();
        Board board = assembler.getBoard(new ClassicBoardBuilder());
        int expected = 7;
        assertEquals(expected, board.getAreas().size());
    }

    @Test
    public void testClassicBoardFieldsNumber(){
        BoardAssembler assembler = new BoardAssembler();
        Board board = assembler.getBoard(new ClassicBoardBuilder());
        int fieldsNumber = 0;
        for(Area a: board.getAreas()){
            fieldsNumber += a.getFields().size();
        }
        int expected = 121;

        assertEquals(expected, fieldsNumber);
    }
}
