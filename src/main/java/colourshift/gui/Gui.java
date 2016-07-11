package colourshift.gui;

import colourshift.model.blocks.Block;
import colourshift.model.blocks.Board;
import colourshift.model.blocks.BoardFactory;
import colourshift.model.blocks.BoardFactory.Wrap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    private Text logBox;

    public void init(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Colourshift solver");
        setupMenuScene(primaryStage);
	}

    private void setupMenuScene(Stage primaryStage) {

        GridPane grid = new GridPane();

        Button newBtn = new Button("New");

        grid.add(newBtn, 0, 0);

        Label sizeLabel = new Label("Size:");
        TextField sizeInput = new TextField("5");
        HBox sizeBox = new HBox(sizeLabel, sizeInput);
        grid.add(sizeBox, 0, 1);

        Label wrapLabel = new Label("Wrap enabled:");
        CheckBox wrapInput = new CheckBox();
        HBox wrapBox = new HBox(wrapLabel, wrapInput);
        grid.add(wrapBox, 0, 2);

        VBox vBox = new VBox(newBtn, sizeBox, wrapBox);

        newBtn.setOnAction((ActionEvent e) -> {
                int boardSizeInt = Integer.parseInt(sizeInput.getText());
                Wrap wrap =  wrapInput.isSelected() ? Wrap.ENABLED : Wrap.DISABLED;
                Board board = boardFactory.createEmpty(boardSizeInt, boardSizeInt, wrap);
                setupBoardScene(primaryStage, board);
            }
        );

        primaryStage.setScene(new Scene(vBox));
        primaryStage.show();

    }

    private void setupBoardScene(Stage primaryStage, Board board) {
        imageProvider.init();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(createBoardNode(board), 1, 1);
        grid.add(createMenuNode(primaryStage), 1, 2);
        grid.add(createLogNode(), 1, 3);

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
    }



    private void reset(Stage primaryStage) {
        setupMenuScene(primaryStage);
    }

    private Node createBoardNode(Board board) {
        GridPane boardScene = new GridPane();
        int boardSize = board.size();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Block block = board.get(i, j);
                Image image = imageProvider.getImage(block);
                ImageView imageView = new ImageView(image);
                boardScene.add(imageView, i, j);
                attachClickHandler(imageView, board, block, i, j);
            }
        }
        return boardScene;
    }

    private void attachClickHandler(ImageView imageView, Board board, Block block, int i, int j) {
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    handleLeftClick(imageView, board.get(i, j), i, j);
                } else {
                    handleRightClick(imageView, board, i, j);
                }
            }
        });
    }

    private void handleLeftClick(ImageView imageView, Block block, int i, int j) {
        System.out.println("left clicked: " + i + " " + j);
        System.out.println("Block:  " + block.getClass());
        block.rotate();
        System.out.println("New angle: " + block.getAngle());
        Image newImage = imageProvider.getImage(block);
        System.out.println("New image: " + newImage);
        imageView.setImage(newImage);
    }

    private void handleRightClick(ImageView imageView, Board board, int i, int j) {
        System.out.println("right clicked: " + i + " " + j);
        Block newBlock = board.changeBlockType(i, j);
        System.out.println("New block type: " + newBlock.getClass().getSimpleName());
        Image newImage = imageProvider.getImage(newBlock);
        imageView.setImage(newImage);
    }

    private Node createMenuNode(Stage primaryStage) {
        Button menuBtn = new Button("Menu");
        menuBtn.setOnAction((ActionEvent e) -> reset(primaryStage));

        Button saveBtn = new Button("Save");
        Button loadBtn = new Button("Load");
        Button solveBtn = new Button("Solve");

        menuBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        loadBtn.setMaxWidth(Double.MAX_VALUE);
        solveBtn.setMaxWidth(Double.MAX_VALUE);

        VBox vbBtn = new VBox();
        vbBtn.getChildren().addAll(menuBtn, saveBtn, loadBtn, solveBtn);
        return vbBtn;
    }

    private Node createLogNode() {
        logBox = new Text();
        return logBox;
    }

    private void log(String message) {
        logBox.setText(message);
    }
}
