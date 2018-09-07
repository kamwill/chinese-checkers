package com.tpproject.app.server;

import com.tpproject.app.bot.Bot;
import com.tpproject.app.exceptions.FieldAlreadyOccupied;
import com.tpproject.app.exceptions.IncorrectMove;
import com.tpproject.app.exceptions.MaxNumberOfPlayersReached;
import com.tpproject.app.game.ClassicChineseCheckers;
import com.tpproject.app.game.Game;
import com.tpproject.app.player.PlayerInfo;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class clientThread extends Thread {

    protected BufferedReader is = null;
    protected PrintStream os = null;
    protected Socket clientSocket = null;
    protected ArrayList<clientThread> players;
    protected ArrayList<Game> games;
    protected PlayerInfo playerInfo;


    public clientThread(Socket clientSocket, ArrayList<clientThread> players, ArrayList<Game> games) {
        this.clientSocket = clientSocket;
        this.players = players;
        this.playerInfo = new PlayerInfo(players.size());
        this.games = games;
    }

    public void run() {


        try {
            /*
             * Create input and output streams for this client.
             */
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new PrintStream(clientSocket.getOutputStream());
            this.sendRS("Connection successfull!");

            while (true) {
                String line = is.readLine();
                System.out.println("received: " + line);
                parseRQ(line);

                if (line.startsWith("/quit")) {
                    break;
                }
            }
        } catch (IOException e1) {
            System.out.println("Klient się rozlaczyl.");
        } catch (NullPointerException e2){
            this.executeLeaveGameRQ(new ArrayList<Integer>());
            System.out.println("Klient rozłączył się");
        }

    }

    public void parseRQ(String request) {
        String[] splitRQ = request.split("\\s+");
        ArrayList<Integer> args= new ArrayList<>();
        System.out.println(splitRQ[0]);
        if(splitRQ.length >1){
            System.out.println(splitRQ[1]);
        }
        for(int i =1; i<splitRQ.length; i++){
            args.add(Integer.valueOf(splitRQ[i]));
        }

        try {
            Method method = this.getClass().getMethod("execute"+splitRQ[0], ArrayList.class);
            method.invoke(this, args);
        } catch (NoSuchMethodException e) {
            this.sendRS("ERR Niepoprawne argumenty");
        } catch (IllegalAccessException e) {
            this.sendRS("ERR Niepoprawne argumenty");
        } catch (InvocationTargetException e) {
            this.sendRS("ERR Niepoprawne argumenty");
        }


    }

    @SuppressWarnings("Duplicates")
    public void executeMoveRQ(ArrayList<Integer> arguments) {
        ArrayList<Point> listOfMoves = new ArrayList<Point>();
        int pieceID;
        ClassicChineseCheckers game = (ClassicChineseCheckers) playerInfo.getGame();
        String response;

        try{
            if(arguments.isEmpty()){
                this.sendRS("SUC");
                response = "OtherPlayerMove";
            }else{
                pieceID = playerInfo.getGame().getPieceIDbyCoordinates(arguments.get(0), arguments.get(1));
                for(int i = 2; i<arguments.size(); i+=2){
                    listOfMoves.add(new Point(arguments.get(i), arguments.get(i+1)));
                }
                game.makeMove(this.playerInfo, pieceID, listOfMoves);
                if(game.checkIfPlayerWon(this.playerInfo.getPlayerID())){
                    this.sendRS("SUC You_won");
                }else{
                    this.sendRS("SUC");
                }
                response = "OtherPlayerMove " + arguments.get(0) + " " + arguments.get(1) +
                    " " + arguments.get(arguments.size()-2) + " " + arguments.get(arguments.size()-1);
            }
            int next = game.nextPlayer();
            for(clientThread ct: players){
                if(ct.getPlayerInfo().getGame() == this.getPlayerInfo().getGame() &&
                        ct.getPlayerInfo().getPlayerID() != this.getPlayerInfo().getPlayerID()){
                    if(next == ct.getPlayerInfo().getPlayerID()){
                        if(ct instanceof Bot){
                            System.out.println("Wysylam info do bota");
                        }
                        ct.sendRS(response + " Your_move");
                    }else {
                        ct.sendRS(response);
                    }
                }
            }
        } catch (IncorrectMove incorrectMove) {
            this.sendRS("ERR "+ incorrectMove.getMessage());
        }

    }

    public void executeJoinRQ(ArrayList<Integer> arguments) {
        int gameID = arguments.get(0);

        try {
            ClassicChineseCheckers game = (ClassicChineseCheckers) games.get(gameID);
            game.addPlayer(this.playerInfo);
            playerInfo.setGame(game);
            this.sendRS("JoinSUC " + playerInfo.getGame().getMaxPlayers());
            if(game.getFreeSlots()==0){
                int i = game.nextPlayer();
                for(clientThread ct: players){
                    if(ct.getPlayerInfo().getGame() == game){
                        if(i==ct.getPlayerInfo().getPlayerID()){
                            ct.sendRS("START");
                            System.out.println("START");
                        }
                    }
                }
            }
        } catch (FieldAlreadyOccupied fieldAlreadyOccupied) {
            this.sendRS("JoinERR " + "Joining game failed");
        } catch (MaxNumberOfPlayersReached maxNumberOfPlayersReached) {
            this.sendRS("JoinERR " + "Maximum number of players reached");
        }
    }

    public void executeNewGameRQ(ArrayList<Integer> arguments ) {
        int gameType = arguments.get(0);
        int numberOfPlayers = arguments.get(1);
        int numberOfBots = arguments.get(2) ;
        Game game;

        System.out.println("NewGameRQ");


        switch (gameType) {
            case 1:
                game = new ClassicChineseCheckers(numberOfPlayers);
                try {
                    game.addPlayer(this.playerInfo);

                    int portNumber = 2222;
                    // The default host.
                    String host = "localhost";

                    playerInfo.setGame(game);
                    games.add(game);

                    ArrayList<Integer> list = new ArrayList<>();
                    list.add(games.size()-1);

                    this.sendRS("GameCreated");

                    for(int i = 0; i<numberOfBots; i++){
                        Bot bot = new Bot( this.players, this.games);
                        players.add(bot);
                        bot.executeJoinRQ(list);

                        System.out.println("Utworzono: " + bot.playerInfo.getPlayerID());

                    }

                    for(clientThread ct: players){
                        if(ct instanceof Bot)
                            System.out.println("player " + ct.playerInfo.getPlayerID());
                    }


                } catch (MaxNumberOfPlayersReached e) {
                    this.sendRS("ERR Maximum number of players reached");
                } catch (FieldAlreadyOccupied e) {
                    this.sendRS("ERR Joining game failed");
                }
        }

        System.out.println("Gracz " + playerInfo.getPlayerID()+ " utworzyl nowa gre.");

    }

    public void executeGetGamesRQ(ArrayList<Integer> arguments){
        String games = "ListOfGames ID freeSlots gameType ";

        int freeSlots;
        int i = 0;

        System.out.println(this.games.size());

        for (Game game: this.games) {
            freeSlots = game.getFreeSlots();
            games = games + " " + i + " " + freeSlots + " " + game.getClass().getSimpleName();

            i++;
        }

        this.sendRS(games);


    }

    public void executeLeaveGameRQ(ArrayList<Integer> arguments){

        ClassicChineseCheckers game = (ClassicChineseCheckers) playerInfo.getGame();
        String response = "PlayerLeft Jeden z graczy opuścił grę. Gra zostanie zakończona.";

        for(clientThread ct: players){
            if(ct.getPlayerInfo().getGame() == this.getPlayerInfo().getGame()){
                if(ct.getPlayerInfo().getPlayerID() != this.getPlayerInfo().getPlayerID()){
                    ct.sendRS(response);
                }
                else{
                    ct.sendRS("PlayerLeft Opusciłeś grę");
                }

            }
        }
        games.remove(game);

    }

    public void sendRS(String response){
        this.os.println(response);
    }



    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

}
