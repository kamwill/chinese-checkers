package com.tpproject.app.player;

import com.tpproject.app.game.Game;

/**
 * Contains info about the player and winning conditions
 */
public class PlayerInfo {
    private int playerID;
    private int destinationCorner;
    private Game game;

    public PlayerInfo(int playerID){
        this.playerID = playerID;

    }

    public void setDestinationCorner(int destination){ this.destinationCorner = destination;}

    public void setGame(Game game){ this.game = game;}

    public int getDestinationCorner(){ return this.destinationCorner; }

    public int getPlayerID() {
        return playerID;
    }

    public Game getGame(){
        return this.game;
    }

}
