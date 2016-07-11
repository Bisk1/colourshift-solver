package colourshift.gui;

import colourshift.model.blocks.Board;
import colourshift.model.blocks.BoardFactory;
import colourshift.model.blocks.BoardFactory.Wrap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Gui {

    @Autowired
    private BoardFactory boardFactory;

    @Autowired
    private ImageProvider imageProvider;

	public void init(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Colourshift solver");
        setupMenuScene(primaryStage);
	}

    public void setupMenuScene(Stage primaryStage) {

        GridPane grid = new GridPane();

        Button newBtn = new Button("New");

        grid.add(newBtn, 0, 0);

        Label sizeLabel = new Label("Size:");
        TextField sizeInput = new TextField();
        HBox sizeBox = new HBox(sizeLabel, sizeInput);
        grid.add(sizeBox, 0, 1);

        Label wrapLabel = new Label("Wrap enabled:");
        CheckBox wrapInput = new CheckBox();
        HBox wrapBox = new HBox(wrapLabel, wrapInput);
        grid.add(wrapBox, 0, 2);

        VBox vBox = new VBox(newBtn, sizeBox, wrapBox);

        newBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int boardSizeInt = Integer.parseInt(sizeInput.getText());
                System.out.println("boardSizeInt: " + boardSizeInt);
                Wrap wrap =  wrapInput.isSelected() ? Wrap.ENABLED : Wrap.DISABLED;
                Board board = boardFactory.createEmpty(boardSizeInt, boardSizeInt, wrap);
                setupBoardScene(primaryStage, board);
            }
        });

        primaryStage.setScene(new Scene(vBox));
        primaryStage.show();

    }

    public void setupBoardScene(Stage primaryStage, Board board) {
        imageProvider.init();

        // Big grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // 2. Title node
        Text sceneTitle = new Text("Welcome");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        // 3. Board node
        GridPane boardScene = new GridPane();

        int boardSize = board.size();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Image image = imageProvider.getImage(board.get(i, j));
                ImageView imageView = new ImageView(image);
                boardScene.add(imageView, i, j);
            }
        }

        grid.add(boardScene, 1, 3);


        // 3. Buttons node
        Button newBtn = new Button("New");
        Button saveBtn = new Button("Save");
        Button loadBtn = new Button("Load");
        Button solveBtn = new Button("Solve");

        newBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        loadBtn.setMaxWidth(Double.MAX_VALUE);
        solveBtn.setMaxWidth(Double.MAX_VALUE);

        VBox vbBtn = new VBox();
        vbBtn.getChildren().addAll(newBtn, saveBtn, loadBtn, solveBtn);
        grid.add(vbBtn, 1, 4);

        // 3. Action node
        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 6);

        newBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                actionTarget.setFill(Color.FIREBRICK);
                actionTarget.setText("New button pressed");
            }
        });

        // 4. Scene node
        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void reset(Stage primaryStage) {

    }
}
