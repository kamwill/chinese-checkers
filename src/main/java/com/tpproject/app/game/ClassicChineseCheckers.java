package com.tpproject.app.game;

import com.tpproject.app.board.Field;
import com.tpproject.app.board.builder.BoardAssembler;
import com.tpproject.app.board.builder.ClassicBoardBuilder;
import com.tpproject.app.exceptions.FieldAlreadyOccupied;
import com.tpproject.app.exceptions.IncorrectMove;
import com.tpproject.app.exceptions.MaxNumberOfPlayersReached;
import com.tpproject.app.piece.Piece;
import com.tpproject.app.player.PlayerInfo;

import javax.sound.midi.SysexMessage;
import java.awt.*;
import java.util.ArrayList;

/**
 * Contains info about Classic Chinese Checkers game, validates and makes moves
 */
public class ClassicChineseCheckers extends Game {
    private int corner=1;

    public ClassicChineseCheckers(int maxPlayers){
        iterator =0;
        if(!(maxPlayers==2 || maxPlayers==3 || maxPlayers==6)) throw new IllegalArgumentException();
        this.maxPlayers = maxPlayers;
        BoardAssembler assembler = new BoardAssembler();
        this.setBoard(assembler.getBoard(new ClassicBoardBuilder()));
        this.pieces = new ArrayList<Piece>();
        this.orderOfPlayers = new int[maxPlayers];
    }

    /**
     * Checks if the given move is available to make by the player
     * @param playerID identifies the player
     * @param pieceID identifies the piece
     * @param newPoint coordinates to which move is going to be done
     * @param destinationCorner identifies the player's destination corner
     * @param lastMoveWasJump player cannot make move different from jump, after jump
     * @return false if not-jump and correct, true if jump and correct
     * @throws IncorrectMove if move is not correct
     */
    private boolean validateMove(int playerID, int pieceID, Point newPoint, int destinationCorner, boolean lastMoveWasJump) throws IncorrectMove{

        int xOld = this.getPiece(pieceID).getCoordinates().x;
        int yOld = this.getPiece(pieceID).getCoordinates().y;
        int xDiff = (int) newPoint.getX() - xOld;
        int yDiff = (int) newPoint.getY() - yOld;
        boolean jump = false;
        String message;

        System.out.println("Making move");
        /*
         * Sprawdzamy czy pionek należy do gracza
         */

        if(!(playerID ==this.getPiece(pieceID).getPlayerID())){
            message = "To nie twoj pion";
            throw new IncorrectMove(message);
        }

        /*
         * Sprawdzamy czy pole na które chcemy się ruszyć istnieje
         */

        if(this.getBoard().getField((int) newPoint.getX(), (int) newPoint.getY())==null)
            throw new IncorrectMove("Pole nie istnieje");


        /*
         * Sprawdzamy czy pole, na które chcemy się ruszyć jest puste
         */

        if(this.getBoard().isFieldOccupied(newPoint)){
            message = "Pole zajete";
            throw new IncorrectMove(message);
        }


        /*
         * Sprawdzamy poprawność koordynatów ruchu oraz jego rodzaj (skok/zwykły).
         * Jeśli niepoprawny rzucamy wyjątek
         */

        if( (Math.abs(xDiff) < 3) && (Math.abs(yDiff) <3) &&(Math.abs(xDiff) + Math.abs(yDiff)==2) || (Math.abs(xDiff)+ Math.abs(yDiff) ==4)){
            if ( ((Math.abs(xDiff) == 2) && (Math.abs(yDiff) == 2)) || Math.abs(xDiff)==4) jump = true;
        }
        else {
            message = "Niepoprawny ruch";
            throw new IncorrectMove(message);
        }

        /*
         * Jeśli skok, sprawdzamy, czy na przeskakiwanym polu stoi pion
         */

        if(jump){
            Field jumpedField = this.getBoard().getField((xOld + (int) newPoint.getX())/2, (yOld + (int) newPoint.getY())/2);
            if(!jumpedField.isOccupied()){
                message = "Skaczesz nad pustym polem";
                throw new IncorrectMove(message);
            }
        }

        /*
        * Sprawdzamy czy po skoku nie następuje zwykły ruch
         */
        if((lastMoveWasJump && !jump))
            throw new IncorrectMove("Nie możesz wykonać tego ruchu");

        /*
         * Sprawdzamy czy pionek jest w docelowym promieniu, jeśli tak sprawdzamy, czy w skutek
         * ruchu w nim pozostanie.
         *
         * Jeśli coś niepoprawne rzucamy wyjątek.
         */

        if(this.getPiece(pieceID).isInDestination()) {                              //pionek w docelowym promieniu
            if (!this.getBoard().getArea(destinationCorner).containsField(newPoint)) {
                message = "Nie mozesz wyjsc poza docelowy promien";
                throw new IncorrectMove(message);
            }
        }

        return jump;
    }

    /**
     * Makes move (after validation)
     * @param playerInfo info about the player that makes the move
     * @param pieceID identifies the piece to be moved
     * @param listOfMoves list of points to be visited during the move
     * @throws IncorrectMove if the move is someway incorrect
     */
    public void makeMove(PlayerInfo playerInfo, int pieceID, ArrayList<Point> listOfMoves) throws IncorrectMove {

        Point temp = new Point((int) this.getPiece(pieceID).getCoordinates().getX(),
                (int )this.getPiece(pieceID).getCoordinates().getY());
        boolean startInDestin = this.getPiece(pieceID).isInDestination();
        boolean lastMoveWasJump = false;
        boolean firstMove = true;

        try{
            for(Point p: listOfMoves){
                if(!lastMoveWasJump && !firstMove) throw new IncorrectMove("Nie możesz wykonać więcej ruchów");
                lastMoveWasJump = validateMove(playerInfo.getPlayerID(), pieceID, p, playerInfo.getDestinationCorner(), lastMoveWasJump);

                if(!this.getBoard().getArea(playerInfo.getDestinationCorner()).containsField(this.getPiece(pieceID).getCoordinates())
                        && this.getBoard().getArea(playerInfo.getDestinationCorner()).containsField(p))
                    this.getPiece(pieceID).setInDestination(true); //zmiana flagi gdy wchodzi do promienia
                this.getBoard().getField((int) this.getPiece(pieceID).getCoordinates().getX(),
                        (int) this.getPiece(pieceID).getCoordinates().getY()).changeOccupancy(); //Zwalniane pole nie jest już zajęte
                this.getPiece(pieceID).setCoordinates(p);
                this.getBoard().getField((int) this.getPiece(pieceID).getCoordinates().getX(),
                        (int) this.getPiece(pieceID).getCoordinates().getY()).changeOccupancy(); //Zajmowane pole nie jest już wolne
                firstMove = false;
            }
        }catch (IncorrectMove incorrectMove){
            this.getBoard().getField((int) this.getPiece(pieceID).getCoordinates().getX(),
                    (int) this.getPiece(pieceID).getCoordinates().getY()).changeOccupancy();
            this.getPiece(pieceID).setCoordinates(temp);
            this.getBoard().getField((int) temp.getX(), (int) temp.getY()).changeOccupancy();
            this.getPiece(pieceID).setInDestination(startInDestin);//cofamy zmiany
            throw new IncorrectMove(incorrectMove.getMessage());
        }
    }

    /**
     * Checks if player's winning conditions are fulfilled
     * @param playerID identifies the player and is winning conditions
     * @return true if player won, false otherwise
     */
    public boolean checkIfPlayerWon(int playerID) {
        int counter = 0;
        for(Piece p: this.getPieces()){
            if(p.getPlayerID()==playerID && p.isInDestination()) counter++;
        }

        return counter == 10;
    }

    public boolean checkIfPieceBelongsToPlayer(int playerID, int pieceID){
        return this.getPiece(pieceID).getPlayerID() == playerID;
    }

    /**
     * Adds player's pieces to the game, and sets player's winning conditions
     * @param playerInfo identifies the player
     * @throws MaxNumberOfPlayersReached if there's no room for a new player
     * @throws FieldAlreadyOccupied if new Piece is being placed on already occupied field
     */
    public void addPlayer(PlayerInfo playerInfo) throws MaxNumberOfPlayersReached, FieldAlreadyOccupied{
        if(this.iterator>=this.maxPlayers) throw new MaxNumberOfPlayersReached();

        orderOfPlayers[iterator] = playerInfo.getPlayerID();

        iterator++;

        if((corner+3)<=6) playerInfo.setDestinationCorner(corner+3);
        else playerInfo.setDestinationCorner((corner+3)%6);

        for(Field f: getBoard().getArea(corner).getFields()){
            this.addPiece(new Piece(playerInfo.getPlayerID(), (int) f.getCoordinates().getX(), (int) f.getCoordinates().getY()));
        }
        this.corner += (6/maxPlayers);


    }

    @Override
    public int nextPlayer() {
        int nextID = orderOfPlayers[nextPlayerIterator];
        nextPlayerIterator=(nextPlayerIterator+1)%maxPlayers;
        return nextID;
    }

    public int[] getOrder(){
        return this.orderOfPlayers;
    }

}
