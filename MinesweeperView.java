import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author Anoop Jalla
 * @version 1.0
 */
public class MinesweeperView extends Application {
    /**
     * Local method that overrides the start method from Application.
     *
     * @param primaryStage The stage that is passed in
     */
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Minesweeper");

        Text text = new Text(675, 20, "Welcome to Minesweeper!");
        text.setFont(Font.font("Georgia, FontWeight.BOLD, 45"));
        text.setFill(Color.BLACK);

        ComboBox<String> combo = new ComboBox<>();
        combo.setPromptText("Select Difficulty: ");
        combo.getItems().addAll("Easy", "Medium", "Hard");

        FlowPane pane = new FlowPane();
        Insets inset = new Insets(30, 15, 13, 50);
        FlowPane.setMargin(pane, inset);

        pane.setPadding(inset);
        pane.setVgap(5);
        pane.setHgap(5);

        TextField prompt = new TextField();
        prompt.setPromptText("Name...");

        Button start = new Button("Start");
        start.setStyle("-fx-background-color: MediumSeaGreen");
        start.setOnAction((ActionEvent e) -> {
            if ((prompt.getText().isEmpty()) || (combo.getValue() == null)) {
                Alert warning = new Alert(Alert.AlertType.WARNING,
                        "Invalid Inputs, Please select a difficulity and enter your name.");
                warning.showAndWait();
            } else {
                playMinesweeper(primaryStage, Difficulty.valueOf(combo.getValue().toUpperCase()), prompt.getText());
            }
        });

        pane.setOrientation(Orientation.VERTICAL);
        pane.getChildren().add(text);
        pane.getChildren().add(combo);
        pane.getChildren().addAll(prompt);
        pane.getChildren().add(start);

        Scene scene = new Scene(pane, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Local method that creates another scene where the user can play Minesweeper.
     *
     * @param primaryStage   The stage that is passed in
     * @param gameDifficulty The difficulty level of the game
     * @param name           The user's name
     */
    public void playMinesweeper(Stage primaryStage, Difficulty gameDifficulty, String name) {
        MinesweeperGame minesweeper = new MinesweeperGame(gameDifficulty);
        GridPane grid = new GridPane();
        Button[][] gridArr = new Button[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Button button = new Button("X");
                button.setStyle("-fx-background-color: Blue");
                grid.add(button, i, j);
                gridArr[i][j] = button;
                int a = i;
                int b = j;
                button.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        Tile[] tileArr = minesweeper.check(b, a);
                        if ((tileArr[0].isMine()) && (tileArr.length == 1)) {
                            FlowPane pane = new FlowPane();
                            pane.setOrientation(Orientation.VERTICAL);
                            Text loseText = new Text(675, 20, "You Lost!, " + name);
                            Button newGame = new Button("New Game");
                            newGame.setStyle("-fx-background-color: Aquamarine");
                            newGame.setOnAction(new CustomEventHandler(primaryStage));
                            pane.getChildren().add(loseText);
                            pane.getChildren().add(newGame);
                            Scene loseScence = new Scene(pane, 1920, 1080);
                            primaryStage.setScene(loseScence);
                            primaryStage.show();
                        } else {
                            for (Tile tile : tileArr) {
                                gridArr[tile.getX()][tile.getY()].setText("" + tile.getBorderingMines());
                                if (minesweeper.isWon()) {
                                    FlowPane pane = new FlowPane();
                                    pane.setOrientation(Orientation.VERTICAL);
                                    Text winText = new Text(675, 20, "You Won!, " + name);
                                    Button newGame = new Button("New Game");
                                    newGame.setStyle("-fx-background-color: Aquamarine");
                                    newGame.setOnAction(new CustomEventHandler(primaryStage));
                                    pane.getChildren().add(winText);
                                    pane.getChildren().add(newGame);
                                    Scene winScene = new Scene(pane, 1920, 1080);
                                    primaryStage.setScene(winScene);
                                    primaryStage.show();
                                }

                            }
                        }
                    }
                });
            }
        }
        Scene scene2 = new Scene(grid, 1920, 1080);
        primaryStage.setScene(scene2);
        primaryStage.show();
    }

    private class CustomEventHandler implements EventHandler<ActionEvent> {
        private Stage primaryStage;

        CustomEventHandler(Stage primaryStage) {
            this.primaryStage = primaryStage;
        }

        Stage getPrimaryStage() {
            return primaryStage;
        }

        void setPrimaryStage(Stage primaryStage) {
            this.primaryStage = primaryStage;
        }

        public void handle(ActionEvent event) {
            start(primaryStage);
        }
    }
}