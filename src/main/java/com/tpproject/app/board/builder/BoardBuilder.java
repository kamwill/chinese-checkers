package com.tpproject.app.board.builder;

import com.tpproject.app.board.Board;

public interface BoardBuilder {

        void createCenter();
        void createCorners();
        Board getBoard();
}
