package com.tpproject.app.clientApp;

import com.tpproject.app.clientApp.observer.Observer;
import com.tpproject.app.server.ServerClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClientHelloController implements Observer{
    ObservableList<String> listOfGameTypes = FXCollections.observableArrayList("Klasyczna Trylma");
    ServerClient serverClient = ServerClient.getServerClient();

    @FXML public ChoiceBox chooseGameType;

    @FXML public TextField numberOfPlayers;

    @FXML public TextField numberOfBots;

    @FXML public TextField gameID;

    @FXML private void initialize(){

          ServerClient.setObserver(this);

          chooseGameType.setItems(listOfGameTypes);
          chooseGameType.getSelectionModel().selectFirst();
          String text = serverClient.getResponseLine();
          connection.setText(text);
          this.showGames();


    }

    @FXML public Text response, connection;

    @FXML public TextArea showGames;

    public void newGameOKController(ActionEvent event) throws IOException{

        try {
            int bots = Integer.parseInt(numberOfBots.getText());
            int noPlayers = Integer.parseInt(numberOfPlayers.getText());
            List<Integer> players = new ArrayList<Integer>();
            players.add(1);
            players.add(Integer.parseInt(numberOfPlayers.getText()));
            players.add(bots);
            ServerClient.setNumberOfPlayers(noPlayers);
            serverClient.executeRQ("NewGameRQ", players);

        } catch (IllegalArgumentException e){
            response.setText("Niewłaściwe argumenty");
        }



    }

    public void showGames(){
        serverClient.sendRQ("GetGamesRQ\n");

    }


    public void joinGame(ActionEvent event) throws IOException{
        try{
            ArrayList<Integer> ID = new ArrayList<>();
            ID.add(Integer.parseInt(gameID.getText()));
            serverClient.sendRQ(serverClient.prepareRQ("JoinRQ", ID));



        }catch (NumberFormatException e){
            gameID.clear();
            gameID.setPromptText("Niewłaściwy format argumentu");
        }
    }

    @Override
    public void update(String response){
        String[] args = response.split("\\s+");
        try{
            Method method = this.getClass().getDeclaredMethod("handle"+args[0]+"Response",
                    String.class);
            method.invoke(this, response);
        } catch (NoSuchMethodException ex1){
            this.response.setText("Niewłaściwe argumenty");
            ex1.printStackTrace();
        } catch (InvocationTargetException ex2){
            ex2.printStackTrace();
        } catch (IllegalAccessException ex3){
            ex3.printStackTrace();
        }
    }

    private void showClassicBoard(){
        try{
            Parent parent = FXMLLoader.load(getClass().getResource("/ClassicStarBoardFXML.fxml"));
            final Scene boardScene = new Scene(parent);
            final Stage stage = (Stage) ((Node) gameID).getScene().getWindow();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    stage.setScene(boardScene);
                    stage.show();
                }
            });
        } catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private void handleGameCreatedResponse(String response){
        showClassicBoard();
    }

    private void handleJoinSUCResponse(String response){
        ServerClient.setNumberOfPlayers(Integer.parseInt(serverClient.getResponseLine().substring(8,9)));
        showClassicBoard();
    }

    private void handleListOfGamesResponse(String response){
        String text = response.substring(12);
        String[] split = text.split("\\s+");

        text = "";
        for( int i = 0; i<split.length; i=i+3){
            text=text + split[i]+"\t"+split[i+1]+"\t"+split[i+2]+"\n";
        }

        showGames.setText(text);
    }

    private void handleERRResponse(String response){
        this.response.setText(response.substring(4));
    }
}
