package com.tpproject.app.game;

import com.tpproject.app.board.Board;
import com.tpproject.app.exceptions.FieldAlreadyOccupied;
import com.tpproject.app.exceptions.IncorrectMove;
import com.tpproject.app.exceptions.MaxNumberOfPlayersReached;
import com.tpproject.app.piece.Piece;
import com.tpproject.app.player.PlayerInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Game {

    List<Piece> pieces;
    Board board;
    int maxPlayers;
    int iterator;
    int[] orderOfPlayers;
    int nextPlayerIterator = 0;

    public void setBoard(Board temp){
        this.board = temp;
    }

    public Board getBoard() {
        return board;
    }

    public abstract void makeMove(PlayerInfo playerInfo, int pieceID, ArrayList<Point> listOfMoves) throws IncorrectMove;

    public Piece getPiece(int ID){
        return this.pieces.get(ID);
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    /**
     * Adds piece to the game, and sets the field occupied
     * @param piece Piece to be added
     * @throws FieldAlreadyOccupied if the field is already occupied
     */
    public void addPiece(Piece piece) throws FieldAlreadyOccupied{
        if(!this.board.getField((int) piece.getCoordinates().getX(), (int) piece.getCoordinates().getY()).isOccupied()){
            this.pieces.add(piece);
            this.board.getField((int) piece.getCoordinates().getX(), (int) piece.getCoordinates().getY()).changeOccupancy();
        } else{
            System.out.println("FieldOcc");
            throw new FieldAlreadyOccupied();
        }
    }

    abstract boolean checkIfPlayerWon(int playerID);

    public abstract void addPlayer(PlayerInfo playerInfo) throws MaxNumberOfPlayersReached, FieldAlreadyOccupied;

    public int getFreeSlots(){
        return maxPlayers - iterator;

    }

    public int getMaxPlayers(){
        return this.maxPlayers;
    }

    public int getPieceIDbyCoordinates(int x, int y){
        for(int countID = 0; countID<pieces.size(); countID++){
            if((int) pieces.get(countID).getCoordinates().getX() == x &&
                    (int) pieces.get(countID).getCoordinates().getY() == y){
                return countID;
            }
        }

        return -1;
    }

    abstract int nextPlayer();

}
