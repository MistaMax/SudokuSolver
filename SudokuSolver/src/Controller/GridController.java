package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

public class GridController implements Initializable {
	private BorderPane rootNode;

	@FXML
	private GridPane puzzleGrid;

	public GridController() {

	}

	public void setRootNode(BorderPane rootNode) {
		this.rootNode = rootNode;
	}

	public GridPane getPuzzleGrid() {
		return puzzleGrid;
	}
	
	public void resetPuzzleGrid() {
		puzzleGrid.getChildren().clear();
	}
	
	public void stylePuzzle() {
		for (Node n : puzzleGrid.getChildren()) {
			if (n instanceof Control) {
				Control c = (Control) n;
				c.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				c.setStyle("-fx-background-color: white;-fx-alignment: center;");
			}
			if (n instanceof Pane) {
				Pane p = (Pane) n;
				p.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				p.setStyle("-fx-background-color: white;-fx-alignment: center;");
			}
		}
	}

	public void setPuzzleGrid(GridPane puzzleGrid) {
		this.puzzleGrid = puzzleGrid;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				TextField l = new TextField();
				l.setText("");
				puzzleGrid.add(l, i, j);
			}
		}
		stylePuzzle();
		puzzleGrid.setStyle("-fx-background-color: black; -fx-padding: 2; -fx-hgap: 1; -fx-vgap: 1;");
		puzzleGrid.setSnapToPixel(false);
		ColumnConstraints oneThird = new ColumnConstraints();
		oneThird.setPercentWidth(puzzleGrid.getWidth() / 9.0);
		oneThird.setHalignment(HPos.CENTER);
		puzzleGrid.getColumnConstraints().addAll(oneThird, oneThird, oneThird, oneThird, oneThird, oneThird, oneThird,
				oneThird, oneThird);
		RowConstraints oneHalf = new RowConstraints();
		oneHalf.setPercentHeight(puzzleGrid.getHeight() / 9.0);
		oneHalf.setValignment(VPos.CENTER);
		puzzleGrid.getRowConstraints().addAll(oneHalf, oneHalf, oneHalf, oneHalf, oneHalf, oneHalf, oneHalf, oneHalf,
				oneHalf);
	}

}
