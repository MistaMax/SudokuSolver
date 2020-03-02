package Run;

import java.net.URL;

import Controller.GridController;
import Controller.MainController;
import View.ViewSwitcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Launcher extends Application{
	@Override
	public void start(Stage stage) throws Exception {
		//loads in the Main view that contains the menu at the top of the application
		URL fxmlFile = this.getClass().getResource("../View/MainView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		MainController main = new MainController();
		loader.setController(main);
		BorderPane rootNode = loader.load();
		ViewSwitcher.getInstance().setRootNode(rootNode);
		main.setRootNode((BorderPane) rootNode);
		Scene scene = new Scene(rootNode, 800, 600);
		stage.setTitle("Sudoku");
		stage.setScene(scene);
		stage.show();
		ViewSwitcher.getInstance().setMainStage(stage);
		fxmlFile = this.getClass().getResource("../View/GridView.fxml");
		loader = new FXMLLoader(fxmlFile);
		GridController puzzleGridController = new GridController();
		loader.setController(puzzleGridController);
		Parent contentView = loader.load();
		rootNode.setCenter(contentView);
		main.setGridController(puzzleGridController);
	}

	
	@Override
	public void stop() throws Exception {
		super.stop();
	}


	public static void main(String[] args) {
		launch(args);

	}
}
