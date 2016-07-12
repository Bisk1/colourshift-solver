package colourshift.gui;

import colourshift.model.blocks.Block;
import colourshift.model.Board;
import colourshift.model.BoardFactory;
import colourshift.model.BoardFactory.Wrap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import javafx.event.ActionEvent;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

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
        grid.add(createMenuNode(primaryStage, board), 1, 2);
        grid.add(createLogNode(), 1, 3);

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
    }

    private Node createBoardNode(Board board) {
        Table<Integer, Integer, ImageView> imageViewTable = HashBasedTable.create();
        GridPane boardScene = new GridPane();
        for (int row = 0; row < board.size(); row++) {
            for (int column = 0; column < board.size(); column++) {
                Block block = board.get(row, column);
                Image image = imageProvider.getImage(block);
                ImageView imageView = new ImageView(image);
                boardScene.add(imageView, column, row);
                attachClickHandler(imageView, board, row, column, imageViewTable);
                imageViewTable.put(row, column, imageView);
            }
        }
        return boardScene;
    }

    private void refreshBoardNode(Board board, Table<Integer, Integer, ImageView> imageViewTable) {
        for (int row = 0; row < board.size(); row++) {
            for (int column = 0; column < board.size(); column++) {
                Block block = board.get(row, column);
                Image newImage = imageProvider.getImage(block);
                imageViewTable.get(row, column).setImage(newImage);
            }
        }
    }

    private void attachClickHandler(ImageView imageView, Board board, int row, int column, Table<Integer, Integer, ImageView> imageViewTable) {
        imageView.setOnMouseClicked((MouseEvent event) -> {
            Block block = board.get(row, column);
            if (event.getButton() == MouseButton.PRIMARY) {
                block.rotate();
            } else {
                block = board.changeBlockType(row, column);
            }
            refreshBoardNode(board, imageViewTable);
        });
    }

    private Node createMenuNode(Stage primaryStage, Board board) {
        Button menuBtn = new Button("Menu");
        menuBtn.setOnAction((ActionEvent e) -> reset(primaryStage));

        Button saveBtn = new Button("Save");
        saveBtn.setOnAction((ActionEvent e) -> save(primaryStage, board));

        Button loadBtn = new Button("Load");
        loadBtn.setOnAction((ActionEvent e) -> load(primaryStage));

        Button solveBtn = new Button("Solve");

        menuBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        loadBtn.setMaxWidth(Double.MAX_VALUE);
        solveBtn.setMaxWidth(Double.MAX_VALUE);

        VBox vbBtn = new VBox();
        vbBtn.getChildren().addAll(menuBtn, saveBtn, loadBtn, solveBtn);
        return vbBtn;
    }

    private void reset(Stage primaryStage) {
        setupMenuScene(primaryStage);
    }

    private void save(Stage primaryStage, Board board) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Board files (*.board)", "*.board");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(primaryStage);
        if(file != null) {
            try {
                if(!file.getName().contains(".")) {
                    file = new File(file.getAbsolutePath() + ".board");
                }
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(board);
                oos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void load(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Board files (*.board)", "*.board");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(primaryStage);
        if(file != null) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Board board = (Board)ois.readObject();
                ois.close();
                setupBoardScene(primaryStage, board);
            } catch (IOException|ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Node createLogNode() {
        logBox = new Text();
        return logBox;
    }

    private void log(String message) {
        logBox.setText(message);
    }
}
