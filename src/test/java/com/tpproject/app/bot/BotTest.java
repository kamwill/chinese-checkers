package com.tpproject.app.bot;

import com.tpproject.app.exceptions.FieldAlreadyOccupied;
import com.tpproject.app.exceptions.MaxNumberOfPlayersReached;
import com.tpproject.app.game.ClassicChineseCheckers;
import com.tpproject.app.game.Game;
import com.tpproject.app.player.PlayerInfo;
import com.tpproject.app.server.clientThread;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.booleanThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BotTest {
    @Mock
    ArrayList<clientThread> players;


    ArrayList<Game> games;
    Bot bot;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        games = new ArrayList<>();
        bot = new Bot(players, games);
    }

    @Test
    public void testFindBotFields() throws FieldAlreadyOccupied, MaxNumberOfPlayersReached{
        ClassicChineseCheckers game = new ClassicChineseCheckers(2);
        bot.setPlayerInfo(new PlayerInfo(1));
        game.addPlayer(bot.getPlayerInfo());
        bot.getPlayerInfo().setGame(game);

        games.add(game);

        assertEquals(10, bot.getPlayerInfo().getGame().getPieces().size());
    }
}
