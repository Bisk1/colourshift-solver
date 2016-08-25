package colourshift.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class App extends Application {

	public static void main(String[] args) {
        launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        Gui gui = ctx.getBean(Gui.class);
        gui.init(primaryStage);
}
}
