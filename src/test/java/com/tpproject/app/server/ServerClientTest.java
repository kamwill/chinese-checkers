package com.tpproject.app.server;

import com.tpproject.app.clientApp.observer.Observer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ServerClientTest {

    @Mock
    Observer observer;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSingleton(){
        ServerClient expected = ServerClient.getServerClient();
        assertEquals(expected, ServerClient.getServerClient());
    }

    @Test
    public void testPrepareRQ(){
        ServerClient serverClient = ServerClient.getServerClient();
        String expected = "MoveRQ 0 1 2 3 \n";
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        String actual = serverClient.prepareRQ("MoveRQ", list);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddObserver(){
        ServerClient.setObserver(observer);
        assertEquals(observer, ServerClient.getObserver());
    }

    @Test
    public void testNotifyObservers(){
        ServerClient.setObserver(observer);
        ServerClient.notifyObserver(" ");

        verify(observer, times(1)).update(" ");
    }


}
