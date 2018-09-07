package com.tpproject.app.clientApp;

import com.tpproject.app.server.ServerClient;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class ClientHelloControllerTest {
    @InjectMocks
    ClientHelloController controller;

    @Mock
    ServerClient serverClient;

    @Mock
    ActionEvent actionEvent;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNewGameOK(){

    }
}
