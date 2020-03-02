package View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import Controller.*;

public class ViewSwitcher {
	private static ViewSwitcher instance = null;
	private BorderPane rootNode;
	private Stage mainStage;
	
	private ViewSwitcher() {
		rootNode = null;
	}
	
	public void switchView(int viewType, String title) {
		switch(viewType) {
		case 1:
			try {
				GridController controller = new GridController();
				controller.setRootNode(rootNode);
				
				URL fxmlFile = this.getClass().getResource("BookDetailView.fxml");
				FXMLLoader loader = new FXMLLoader(fxmlFile);
			
				loader.setController(controller);
			
				Parent contentView = loader.load();

				//get rid of reference to previous content view
				rootNode.setCenter(null);
				
				rootNode.setCenter(contentView);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	public BorderPane getRootNode() {
		return rootNode;
	}

	public void setRootNode(BorderPane rootNode) {
		this.rootNode = rootNode;
	}

	public static ViewSwitcher getInstance() {
		if(instance == null) {
			instance = new ViewSwitcher();
		}
		return instance;
	}

	public Stage getMainStage() {
		return mainStage;
	}

	public void setMainStage(Stage mainStage) {
		this.mainStage = mainStage;
	}
}
