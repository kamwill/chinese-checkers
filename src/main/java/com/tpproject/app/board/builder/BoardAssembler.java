package com.tpproject.app.board.builder;

import com.tpproject.app.board.Board;

public class BoardAssembler {

    public Board getBoard(BoardBuilder builder){
        builder.createCenter();
        builder.createCorners();

        return builder.getBoard();

    }
}
