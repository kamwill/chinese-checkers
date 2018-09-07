package com.tpproject.app.bot;

import com.tpproject.app.board.Field;
import com.tpproject.app.exceptions.IncorrectMove;
import com.tpproject.app.game.ClassicChineseCheckers;
import com.tpproject.app.game.Game;
import com.tpproject.app.piece.Piece;
import com.tpproject.app.player.PlayerInfo;
import com.tpproject.app.server.clientThread;
import org.junit.Before;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class Bot extends clientThread {

    private ArrayList<Piece> botPieces;

    public Bot(ArrayList<clientThread> players, ArrayList<Game> games) {
        super(null, players, games);
    }

    public void run(){
        try{
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new PrintStream(clientSocket.getOutputStream());

            while (true){
                String line = is.readLine();
                System.out.println("BOT received: " + line);

                this.handleRS(line);
            }
        } catch (IOException io){
            System.out.println("BOT: IOException");
        } catch (NullPointerException nullEx){
            System.out.println("BOT: NullPointerException");
        }
    }

    void handleRS(String response) {
        if(response.contains("Your_move")){
            makeMove();
        }
    }

    @SuppressWarnings("Duplicates")
    void makeMove(){
        try{
            int[] args = findFieldToMove(playerInfo.getGame());
            if(args!=null){
                ArrayList<Point> listOfMoves = new ArrayList<>();
                listOfMoves.add(new Point(args[2], args[3]));
                ClassicChineseCheckers game = (ClassicChineseCheckers) playerInfo.getGame();
                game.makeMove(this.playerInfo,
                        game.getPieceIDbyCoordinates(args[0], args[1]),
                        listOfMoves
                        );
                String response = "OtherPlayerMove " + args[0] + " " + args[1] +
                        " " + args[2] + " " + args[3];
                int next = game.nextPlayer();
                for(clientThread ct: players){
                    if(ct.getPlayerInfo().getGame() == this.getPlayerInfo().getGame() &&
                            ct.getPlayerInfo().getPlayerID() != this.getPlayerInfo().getPlayerID()){
                        System.out.println("Znalazłem gracza");
                        if(next == ct.getPlayerInfo().getPlayerID()){
                            System.out.println("Wysyłam info do " + ct.getPlayerInfo().getPlayerID());
                            if(ct instanceof Bot){
                                System.out.println("Wysyłam info do bota");
                            }
                            ct.sendRS(response + " Your_move");
                        }else {
                            System.out.println("Wysyłam info do " + ct.getPlayerInfo().getPlayerID());
                            ct.sendRS(response);
                        }
                    }
                }

            }
        }catch (IncorrectMove moveEx){
            System.out.println("BOT: nieprawidłowy ruch");
        }

    }

    private int[] findFieldToMove(Game game){
        Field[][] neighbours;

        if(botPieces == null)
            botPieces = findBotPieces();

        for(Piece p: botPieces){
            neighbours = new Field[6][2];

            int x = (int) p.getCoordinates().getX();
            int y = (int) p.getCoordinates().getY();
            neighbours[0][0] = game.getBoard().getField(x+1, y+1);
            neighbours[0][1] = game.getBoard().getField(x+2, y+2);

            neighbours[1][0] = game.getBoard().getField(x+2, y+0);

            neighbours[2][0] = game.getBoard().getField(x+1, y-1);
            neighbours[2][1] = game.getBoard().getField(x+2, y-2);

            neighbours[3][0] = game.getBoard().getField(x-1, y-1);
            neighbours[3][1] = game.getBoard().getField(x-2, y-2);

            neighbours[4][0] = game.getBoard().getField(x-2, y+0);

            neighbours[5][0] = game.getBoard().getField(x-1, y+1);
            neighbours[5][1] = game.getBoard().getField(x-2, y+2);

            for(int i=0; i<6; i++){
                if(neighbours[i][0]!=null){
                    if(neighbours[i][0].isOccupied()){
                        if(neighbours[i][1]!=null){
                            if (!neighbours[i][1].isOccupied()){
                                int[] result = {
                                        (int) p.getCoordinates().getX(),
                                        (int) p.getCoordinates().getY(),
                                        (int) neighbours[i][1].getCoordinates().getX(),
                                        (int) neighbours[i][1].getCoordinates().getY(),
                                };
                                return result;
                            }
                        }
                    }else{
                        int[] result = {
                                (int) p.getCoordinates().getX(),
                                (int) p.getCoordinates().getY(),
                                (int) neighbours[i][0].getCoordinates().getX(),
                                (int) neighbours[i][0].getCoordinates().getY(),
                        };
                        return result;
                    }
                }
            }
        }

        return null;
    }

    public void sendRS(String response){
        handleRS(response);
    }

    ArrayList<Piece> findBotPieces(){
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        int ID = this.playerInfo.getPlayerID();

        for(Piece p: this.playerInfo.getGame().getPieces()){
            if(p.getPlayerID() == ID)
                pieces.add(p);
        }

        return pieces;
    }

    void setPlayerInfo(PlayerInfo playerInfo){
        this.playerInfo = playerInfo;
    }
}
