package com.tpproject.app.clientApp;

import com.tpproject.app.clientApp.observer.Observer;
import com.tpproject.app.server.ServerClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class ClassicStarBoardController implements Observer{
    private ArrayList<Point> listOfMoves = null;
    private ArrayList<Circle> changedCircles = null;
    private Point piece = null;
    private ServerClient serverClient;
    private boolean isMoving = false;
    private boolean won = false;

    private Color[] piecesColors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE};

    @FXML public Label gameInfo;

    @FXML public Button makeMoveButton;

    @FXML public AnchorPane boardAnchorPane;

    public void initialize(){
        serverClient = ServerClient.getServerClient();
        ServerClient.setObserver(this);

        makeMoveButton.setDisable(true);

        boolean[] corners = {false, false, false, false, false, false};

        if(ServerClient.getNumberOfPlayers() == 2){
            corners[0] = true;
            corners[3] = true;
        }else if(ServerClient.getNumberOfPlayers() == 3){
            corners[0] = true;
            corners[2] = true;
            corners[4] = true;
        }else if(ServerClient.getNumberOfPlayers() == 6){
            for (int i=0; i<6; i++)
                corners[i]=true;
        }


        for (Node n: boardAnchorPane.getChildren()){
            if(n instanceof Circle){
                Circle c = (Circle) n;
                if(c.getId() != null){
                    if(c.getId().substring(0,6).equals("corner") && corners[Integer.parseInt(c.getId().substring(6,7))-1]){
                        c.setFill(piecesColors[Integer.parseInt(c.getId().substring(6,7))-1]);
                    }
                }
            }
        }
    }

    @FXML public void chooseField(MouseEvent mouseEvent) {
        if(isMoving && !won){

            Circle circle = (Circle) mouseEvent.getSource();
            Color color = (Color) circle.getFill();

            if(piece == null && ! color.equals(Color.rgb(235, 235, 235)) ){
                piece = convertCoordinatesFromGUIToGame((int) circle.getLayoutX(), (int) circle.getLayoutY());
                listOfMoves = new ArrayList<>();
                changedCircles = new ArrayList<>();

                circle.setRadius(18);
                circle.toFront();

                changedCircles.add(circle);
            }else if(piece != null){
                listOfMoves.add(convertCoordinatesFromGUIToGame((int) circle.getLayoutX(),
                        (int) circle.getLayoutY()));
                circle.setStrokeWidth(2);

                changedCircles.add(circle);
                makeMoveButton.setText("Wykonaj ruch");
            }
        }else {
            setErrorMessage("To nie twoja kolej");
        }
    }

    /**
     * Converts GUI's field coordinates to Checkers' field coordinates
     * @param x first GUI's field coordinate
     * @param y second GUI's field coordinate
     * @return Point with Checkers' field coordinate
     */
    Point convertCoordinatesFromGUIToGame(int x, int y){
        return new Point((x-300)/15, (300-y)/30);
    }

    /**
     * Resets the board and lets player choose new piece and new moves
     * @param actionEvent clicking on "Reset" button
     */
    public void resetMoves(ActionEvent actionEvent) {
        listOfMoves = null;
        piece = null;
        if(changedCircles!=null){
            for(Circle c: changedCircles){
                c.setStrokeWidth(1);
                c.setRadius(15);
            }
        }
        changedCircles = null;
        makeMoveButton.setText("Pasuj");
    }

    private void setErrorMessage(final String message){
        gameInfo.setTextFill(Color.CRIMSON);
        gameInfo.setFont(Font.font("System", FontWeight.BOLD, 13));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameInfo.setText(message);
            }
        });

    }

    private void resetGameInfo(){
        gameInfo.setTextFill(Color.BLACK);
        gameInfo.setFont(Font.font("System", FontWeight.NORMAL, 13));
    }

    public void makeMove(ActionEvent actionEvent){
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        String RQ;
        if(isMoving){
            if(button.getText().equals("Pasuj")){
                System.out.println("Pas");
                RQ = "MoveRQ\n";
                serverClient.sendRQ(RQ);
            }else {
                RQ = prepareMoveRQ(this.piece, this.listOfMoves);
                piece = null;
                listOfMoves = null;
                isMoving = false;
                serverClient.sendRQ(RQ);
            }
        }else{
            setErrorMessage("To nie twoja kolej");
        }
        makeMoveButton.setText("Pasuj");
    }

    private Circle findGUIFieldByCoordinates(Point coordinates){
        for(Node n: boardAnchorPane.getChildren()){
            if(n instanceof Circle){
                Circle c = (Circle) n;
                if(c.getLayoutX() == coordinates.getX() && c.getLayoutY() == coordinates.getY())
                    return c;
            }
        }
        return null;
    }

    Point convertCoordinatesFromGameToGUI(Point gameCoordinates){
        return new Point((int) ((gameCoordinates.getX()*15)+300), (int) -((gameCoordinates.getY()*30)-300));
    }

    String prepareMoveRQ(Point piece, ArrayList<Point> listOfMoves){
        String RQ;
        ArrayList<Integer> arguments = new ArrayList<>();
        arguments.add((int) piece.getX());
        arguments.add((int) piece.getY());
        for(Point p: listOfMoves){
            arguments.add((int) p.getX());
            arguments.add((int) p.getY());
        }
        RQ = serverClient.prepareRQ("MoveRQ", arguments);
        return RQ;
    }

    //TODO: Coś do obserwowania serverClient i jego responseLine

    @Override
    public void update(String response) {
        String[] args = response.split("\\s+");
        try{
            Method method = this.getClass().getDeclaredMethod("handle"+args[0]+"Response", String.class);
            method.invoke(this, response);
        }catch (NoSuchMethodException ex1){
            setErrorMessage("Niewłaściwe argumenty");
            ex1.printStackTrace();
        }catch (InvocationTargetException ex2){
            setErrorMessage("Niewłaściwe argumenty");
            ex2.printStackTrace();
        }catch (IllegalAccessException ex3){
            setErrorMessage("Niewłaściwe argumenty");
            ex3.printStackTrace();
        }
    }

    @FXML private void leaveGame(ActionEvent event) throws IOException{
        String RQ = "LeaveGameRQ\n";
        serverClient.sendRQ(RQ);
    }

    void handleSTARTResponse(String response){
        resetGameInfo();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameInfo.setText("Twój ruch");
            }
        });
        isMoving = true;
        makeMoveButton.setDisable(false);
    }

    void handleOtherPlayerMoveResponse(String response){
        String args[] = response.split("\\s+");

        if(response.equals("OtherPlayerMove Your_move")){
            if(!won){
                resetGameInfo();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameInfo.setText("Twój ruch");
                    }
                });
                isMoving=true;
                makeMoveButton.setDisable(false);
            }else{
                this.serverClient.sendRQ("MoveRQ\n");
            }
        } else if(args[0].equals("OtherPlayerMove")){
            Point piece = convertCoordinatesFromGameToGUI(new Point(
                    Integer.parseInt(args[1]), Integer.parseInt(args[2])));
            Point newField = convertCoordinatesFromGameToGUI(new Point(
                    Integer.parseInt(args[3]), Integer.parseInt(args[4])
            ));
            Circle pieceC = findGUIFieldByCoordinates(piece);
            Circle newFieldC = findGUIFieldByCoordinates(newField);

            Color color = (Color) pieceC.getFill();

            pieceC.setFill(Color.rgb(235,235,235));
            newFieldC.setFill(color);

            if(args.length>=6){
                if(args[5].equals("Your_move") && !won){
                    resetGameInfo();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            gameInfo.setText("Twój ruch");
                        }
                    });
                    isMoving = true;
                    makeMoveButton.setDisable(false);
                }else{
                    this.serverClient.sendRQ("MoveRQ\n");
                }
            }
        }
    }

    void handleERRResponse(String response){
        setErrorMessage(response.substring(3));
        isMoving = true;
        for(Circle c: changedCircles){
            c.setRadius(15);
            c.setStrokeWidth(1);
        }
        changedCircles = null;
        makeMoveButton.setDisable(false);
    }

    void handleSUCResponse(String response){
        if(changedCircles!=null){
            Color color = (Color) changedCircles.get(0).getFill();
            changedCircles.get(0).setFill(Color.rgb(235,235,235));
            changedCircles.get(changedCircles.size()-1).setFill(color);
            for(Circle c: changedCircles){
                c.setRadius(15);
                c.setStrokeWidth(1);
            }
            changedCircles=null;

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    resetGameInfo();
                    gameInfo.setText("Wykonałeś ruch");
                }
            });
        }
        if(response.equals("SUC You_won")){
            this.won = true;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gameInfo.setTextFill(Color.GOLD);
                    gameInfo.setFont(Font.font("System", FontWeight.BOLD, 13));
                    gameInfo.setText("WYGRAŁEŚ! Gratulacje");
                }
            });
        }
    }

    void handlePlayerLeftResponse(String response) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/ClientHelloFXML.fxml"));
        final Scene boardScene = new Scene(parent);
        final Stage stage = (Stage) ((Node) boardAnchorPane).getScene().getWindow();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.setScene(boardScene);
                stage.show();
            }
        });
    }
}