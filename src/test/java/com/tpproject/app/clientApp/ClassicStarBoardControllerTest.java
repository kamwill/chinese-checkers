package com.tpproject.app.clientApp;

import com.tpproject.app.server.Server;
import com.tpproject.app.server.ServerClient;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClassicStarBoardControllerTest {
    @InjectMocks
    ClassicStarBoardController controller;

    @Mock
    ServerClient serverClient;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        ServerClient.setObserver(controller);
    }

    @Test
    public void testConvertGameToGUI(){
        Point coordinates = controller.convertCoordinatesFromGameToGUI(new Point(0,0));
        assertEquals(coordinates.getX(), 300.0);
        assertEquals(coordinates.getY(), 300.0);
    }

    @Test
    public void testConvertGUIToGame(){
        Point coordinates = controller.convertCoordinatesFromGUIToGame(300, 300);
        assertEquals(coordinates.getX(), 0.0);
        assertEquals(coordinates.getY(), 0.0);
    }

    @Test
    public void testPrepareMoveRQ(){
        ArrayList<Point> list = new ArrayList<>();
        list.add(new Point(1,1));
        String request = controller.prepareMoveRQ(new Point(0,0), list);
        verify(serverClient, times(1)).prepareRQ(anyString(), any(ArrayList.class));
    }
}
