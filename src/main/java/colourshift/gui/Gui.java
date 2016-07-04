package colourshift.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Gui extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		
		GridPane boardScene = new GridPane();

//		Label userName = new Label("User Name:");
//		grid.add(userName, 0, 1);
//
//		TextField userTextField = new TextField();
//		grid.add(userTextField, 1, 1);
//
//		Label pw = new Label("Password:");
//		grid.add(pw, 0, 2);
//
//		PasswordField pwBox = new PasswordField();
//		grid.add(pwBox, 1, 2);
		
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
		
		final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        
        newBtn.setOnAction(new EventHandler<ActionEvent>() {
        	 
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("New button pressed");
            }
        });
        
		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
