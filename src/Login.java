import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 *
 * @author Cooledge
 */
public class Login extends Application {

    private Scanner scan = new Scanner(System.in);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Crouching Tiger Hidden Battleship");
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("BattleShip");

        scenetitle.setId("welcome-text");
        grid.add(scenetitle, 0, 0, 2, 1);

        Label player1 = new Label("Player 1:");
        grid.add(player1, 0, 1);

        player1.getText();

        TextField p1 = new TextField();
        grid.add(p1, 1, 1);

        //Label player2 = new Label("Player 2:");
        Label player2 = new Label("Player 2:");
        grid.add(player2, 0, 2);

        player2.getText();

        TextField p2 = new TextField();
        grid.add(p2, 1, 2);

        Button btn = new Button("let's play!");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {

                    System.out.println("Welcome:" + " " + p1.getText()+" "+"&" + " " + p2.getText());
                    System.out.println();
                    System.out.println("Place your battle ships!");
                    System.out.println();
                    //actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setId("actiontarget");
                    actiontarget.setText("Welcome" + " " +p1.getText() + " " + "&" + " " + p2.getText());
                    //BattleshipModel game =  new BattleshipModel ("Joe","Johnny");
                    // PlayGame.startBattleship(p1.getText(), p2.getText());

                    BattleshipModel game = new BattleshipModel(p1.getText(), p2.getText());
                    //BattleshipModel game = new BattleshipModel(player1,player2);
                    Login userLogin = new Login();  // instantiate Login to be able to print board
                    PlayGame playGame = new PlayGame();

                    playGame.runGame(p1.getText(), p2.getText(), game, playGame);

                    btn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                primaryStage.hide();

                                for(int clear = 0 ; clear < 1000; clear++) {
                                    System.out.println("\b");
                                }

                                
                            }
                        });
                }

            });

        Scene scene = new Scene(grid, 734, 400);
        primaryStage.setScene(scene);
        scene.getStylesheets().add
        (Login.class.getResource("login.css").toExternalForm());
        primaryStage.show();

    }  

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        String player1 = "Joe";  // got an error when I tried using p1.getText()
        String player2 = "Johnny"; // got an error when I tried using p2.getText()

        BattleshipModel game = new BattleshipModel(player1, player2);
        PlayGame playGame = new PlayGame();
        playGame.runGame(player1, player2, game, playGame);
    }

}