package com.tpproject.app.game;

import com.tpproject.app.exceptions.FieldAlreadyOccupied;
import com.tpproject.app.exceptions.IncorrectMove;
import com.tpproject.app.exceptions.MaxNumberOfPlayersReached;
import com.tpproject.app.piece.Piece;
import com.tpproject.app.player.PlayerInfo;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class ClassicChineseCheckersTest {
    @Test(expected = IllegalArgumentException.class)
    public void illegalConstructorArgumentTest(){
        ClassicChineseCheckers game = new ClassicChineseCheckers(0);
    }

    @Test
    public void addNewPlayerTest(){
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        PlayerInfo playerInfo = new PlayerInfo(0);
        try{
            game.addPlayer(playerInfo);
        }catch (MaxNumberOfPlayersReached numberOfPlayersReached){
            System.out.println("Max players no. reached");
        }catch (FieldAlreadyOccupied fieldAlreadyOccupied){
            System.out.println("Adding piece failed");
        }
        String actual = playerInfo.getDestinationCorner() + " " + game.getBoard().getArea(1).getFields().size();
        String expected = "4 10";
        assertEquals(expected, actual);
    }

    @Test
    public void addSecondPlayerTest(){
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        PlayerInfo player1 = new PlayerInfo(1);
        PlayerInfo player2 = new PlayerInfo(2);
        try{
            game.addPlayer(player1);
            game.addPlayer(player2);
        }catch (MaxNumberOfPlayersReached numberOfPlayersReached){
            System.out.println("Max players no. reached");
        }catch (FieldAlreadyOccupied fieldAlreadyOccupied){
            System.out.println("Adding piece failed");
        }
        String actual = player1.getDestinationCorner() + " " + game.getBoard().getArea(1).getFields().size() + " " +
                player2.getDestinationCorner() + " " + game.getBoard().getArea(4).getFields().size();
        String expected = "4 10 1 10";
        assertEquals(expected, actual);
    }

    @Test(expected = MaxNumberOfPlayersReached.class)
    public void maxPlayersNumberReachedTest() throws MaxNumberOfPlayersReached, FieldAlreadyOccupied{
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        PlayerInfo player1 = new PlayerInfo(1);
        PlayerInfo player2 = new PlayerInfo(2);
        PlayerInfo player3 = new PlayerInfo(3);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
    }

    @Test(expected = IncorrectMove.class)
    public void notAllowedToMoveOtherPlayersPiecesTest() throws FieldAlreadyOccupied, IncorrectMove{
        PlayerInfo playerInfo = new PlayerInfo(0);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(1,0));
        game.addPiece(new Piece(1, 0,0));
        game.makeMove(playerInfo, 0, listOfMoves);
    }

    @Test(expected = IncorrectMove.class)
    public void IncorrectMoveTest()throws FieldAlreadyOccupied, IncorrectMove{
        PlayerInfo playerInfo = new PlayerInfo(0);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(1,0));
        game.addPiece(new Piece(0,0,0));
        game.makeMove(playerInfo, 0, listOfMoves);

    }

    @Test(expected = IncorrectMove.class)
    public void jumpOverEmptyFieldTest()throws FieldAlreadyOccupied, IncorrectMove{
        PlayerInfo playerInfo = new PlayerInfo(0);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(2,2));
        game.addPiece(new Piece(0, 0,0));
        game.makeMove(playerInfo, 0, listOfMoves);
    }

    @Test
    public void makeOneMoveTest(){
        PlayerInfo playerInfo = new PlayerInfo(0);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(2,0));
        try{
            game.addPiece(new Piece(0,0,0));
            game.makeMove(playerInfo, 0, listOfMoves);
        }catch (IncorrectMove incorrectMove){
            System.out.println(incorrectMove.getMessage());
        }catch (FieldAlreadyOccupied fieldAlreadyOccupied){
            System.out.println("Field is already occupied");
        }
        assertEquals(2, (int) game.getPiece(0).getCoordinates().getX());
        assertEquals(0, (int) game.getPiece(0).getCoordinates().getY());

    }

    @Test(expected = IncorrectMove.class)
    public void makeMultipleMoveTestWithoutJumpTest() throws IncorrectMove, FieldAlreadyOccupied{
        PlayerInfo playerInfo = new PlayerInfo(0);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(2,0));
        listOfMoves.add(new Point(3,1));
        listOfMoves.add(new Point(2, 2));
        game.addPiece(new Piece(0,0,0));
        game.makeMove(playerInfo, 0, listOfMoves);
    }

    @Test
    public void makeMoveJumpTest(){
        PlayerInfo playerInfo = new PlayerInfo(0);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(0,4));
        try{
            game.addPiece(new Piece(0,2,2));
            game.addPiece(new Piece(0,1,3));
            game.makeMove(playerInfo, 0, listOfMoves);
        }catch (IncorrectMove incorrectMove){
            System.out.println(incorrectMove.getMessage());
        }catch (FieldAlreadyOccupied fieldAlreadyOccupied){
            System.out.println("Field occupied");
        }
        assertEquals(0, (int) game.getPiece(0).getCoordinates().getX());
        assertEquals(4, (int) game.getPiece(0).getCoordinates().getY());
    }

    @Test
    public void enteringDestinationCornerTest(){
        PlayerInfo playerInfo = new PlayerInfo(0);
        playerInfo.setDestinationCorner(1);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(1,5));
        try{
            game.addPiece(new Piece(0,0,4));
            game.makeMove(playerInfo, 0, listOfMoves);
        }catch (IncorrectMove incorrectMove){
            System.out.println(incorrectMove.getMessage());
        }catch (FieldAlreadyOccupied fieldAlreadyOccupied){
            System.out.println("Field occupied");
        }
        assertEquals(true, game.getPiece(0).isInDestination());
    }

    @Test(expected = IncorrectMove.class)
    public void notAllowedToLeaveDestinationCornerTest()throws FieldAlreadyOccupied, IncorrectMove{
        PlayerInfo playerInfo = new PlayerInfo(0);
        playerInfo.setDestinationCorner(1);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(0,4));
        game.addPiece(new Piece(0,1,5));
        game.getPiece(0).setInDestination(true);
        game.makeMove(playerInfo, 0, listOfMoves);

    }

    @Test(expected = IncorrectMove.class)
    public void moveToOccupiedField()throws FieldAlreadyOccupied, IncorrectMove{
        PlayerInfo playerInfo = new PlayerInfo(0);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(1,1));
        game.addPiece(new Piece(0,0,0));
        game.addPiece(new Piece(0,1,1));
        game.makeMove(playerInfo,0, listOfMoves);
    }

    @Test(expected = IncorrectMove.class)
    public void moveToNoneExistingFieldTest()throws FieldAlreadyOccupied, IncorrectMove{
        PlayerInfo playerInfo = new PlayerInfo(0);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(10, 0));
        game.addPiece(new Piece(0,8,0));
        game.makeMove(playerInfo,0,listOfMoves);
    }

    @Test
    public void twoJumpsTest(){
        PlayerInfo playerInfo = new PlayerInfo(0);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(2,2));
        listOfMoves.add(new Point(0,4));
        try{
            game.addPiece(new Piece(0,0,0));
            game.addPiece(new Piece(0,1,1));
            game.addPiece(new Piece(0,1,3));
            for(Piece p: game.getPieces()){
                System.out.println(p.getCoordinates().getX() + " " + p.getCoordinates().getY());
            }
            assertEquals(true, game.getBoard().getField(1,1).isOccupied());
            game.makeMove(playerInfo, 0, listOfMoves);
            assertEquals(0, (int) game.getPiece(0).getCoordinates().getX());
            assertEquals(4, (int) game.getPiece(0).getCoordinates().getY());
        }catch (IncorrectMove incorrectMove){
            System.out.println(incorrectMove.getMessage());
        }catch (FieldAlreadyOccupied fieldAlreadyOccupied){
            System.out.println(fieldAlreadyOccupied.getMessage());
        }
    }

    @Test(expected = IncorrectMove.class)
    public void NormalMoveAfterJumpTest()throws IncorrectMove, FieldAlreadyOccupied{
        PlayerInfo playerInfo = new PlayerInfo(0);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        listOfMoves.add(new Point(2,2));
        listOfMoves.add(new Point(3,3));
        game.addPiece(new Piece(0,0,0));
        game.addPiece(new Piece(0,1,1));
        game.makeMove(playerInfo,0, listOfMoves);
    }

    @Test(expected = IncorrectMove.class)
    public void InOutDestinationCorner()throws IncorrectMove, FieldAlreadyOccupied{
        PlayerInfo playerInfo = new PlayerInfo(0);
        playerInfo.setDestinationCorner(1);
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        game.addPiece(new Piece(0, -1,5));
        game.addPiece(new Piece(0,1,5));
        game.addPiece(new Piece(0,-2,4));
        listOfMoves.add(new Point(0,6));
        listOfMoves.add(new Point(2,4));
        game.makeMove(playerInfo, 2, listOfMoves);
    }

    @Test
    public void testNextPlayer() throws FieldAlreadyOccupied, MaxNumberOfPlayersReached{
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        PlayerInfo p1 = new PlayerInfo(1);
        PlayerInfo p2 = new PlayerInfo(2);
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertEquals(game.nextPlayer(), p1.getPlayerID());
    }

    @Test
    public void testSecondRoundNextPlayer() throws FieldAlreadyOccupied, MaxNumberOfPlayersReached{
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        PlayerInfo p1 = new PlayerInfo(1);
        PlayerInfo p2 = new PlayerInfo(2);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.nextPlayer();
        game.nextPlayer();
        assertEquals(game.nextPlayer(), p1.getPlayerID());
    }

    @Test(expected = FieldAlreadyOccupied.class)
    public void testFieldAlreadyOccupied() throws FieldAlreadyOccupied{
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        game.addPiece(new Piece(0,0,0));
        game.addPiece(new Piece(0,0,0));
    }
}
