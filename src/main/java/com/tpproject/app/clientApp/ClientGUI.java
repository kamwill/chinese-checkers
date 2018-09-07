package com.tpproject.app.clientApp;

import com.tpproject.app.server.ServerClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGUI extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException{

        ServerClient serverClient = ServerClient.getServerClient();
        serverClient.start();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientHelloFXML.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Trylma");
            primaryStage.show();
        } catch (LoadException ex){
            System.out.println("Nie udało się wyświetlić aplikacji.\n" +
                    "Może to być spowodowane nieudanym połączeniem");
            System.exit(1);
        }
    }
}
