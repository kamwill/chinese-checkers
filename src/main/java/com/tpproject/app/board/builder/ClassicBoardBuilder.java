package com.tpproject.app.board.builder;

import com.tpproject.app.board.*;

public class ClassicBoardBuilder implements BoardBuilder {

    private ClassicStarBoard board = new ClassicStarBoard();
    private int idIncrement = 0;


    public void createCenter() {
        Centre centre = new Centre(idIncrement, 8, 4);


        board.addArea(centre);
        idIncrement++;
    }

    public void createCorners() {
        board.addArea(new Corner(0, 8, idIncrement%2, idIncrement, 4));
        idIncrement++;
        board.addArea(new Corner(9,1, idIncrement%2, idIncrement, 4));
        idIncrement++;
        board.addArea(new Corner(9,-1, idIncrement%2, idIncrement, 4));
        idIncrement++;
        board.addArea(new Corner(0,-8, idIncrement%2, idIncrement, 4));
        idIncrement++;
        board.addArea(new Corner(-9,-1, idIncrement%2, idIncrement, 4));
        idIncrement++;
        board.addArea(new Corner(-9,1, idIncrement%2, idIncrement, 4));
        idIncrement++;
    }

    public Board getBoard() {
        return board;
    }

}
