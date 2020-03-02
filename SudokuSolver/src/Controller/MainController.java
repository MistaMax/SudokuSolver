package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import View.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

public class MainController implements Initializable {

	private BorderPane rootNode;

	private GridController gridController;

	private SudokuController sudokuController;

	public MainController() {
		sudokuController = new SudokuController();
	}

	public void setGridController(GridController gridController) {
		this.gridController = gridController;
	}

	public void setSudokuController(SudokuController sudokuController) {
		this.sudokuController = sudokuController;
	}

	@FXML
	private MenuItem openPuzzle;

	@FXML
	private MenuItem closeButton;

	@FXML
	private MenuItem runButton;

	@FXML
	void closeApplication(ActionEvent event) {

	}

	@FXML
	void openPuzzleDialog(ActionEvent event) {
		sudokuController.reset();

		FileChooser fileChooser = new FileChooser();
		File selectedDirectory = fileChooser.showOpenDialog(ViewSwitcher.getInstance().getMainStage());
		try {
			sudokuController.setSudokuPuzzle(scanInPuzzle(selectedDirectory.getAbsolutePath()));
			displayCurrPuzzle();
		} catch (NullPointerException e) {
			System.out.println(e);
		}
	}

	private void displayCurrPuzzle() {
		gridController.resetPuzzleGrid();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				TextField l = new TextField();
				if (sudokuController.curr.sudPuzzle[j][i] != 0)
					l.setText(Integer.toString(sudokuController.curr.sudPuzzle[j][i]));
				gridController.getPuzzleGrid().add(l, j, i);
			}
		}
		gridController.stylePuzzle();
	}
	private boolean isValidCellValue(String s) {
			if(s.compareTo("") == 0)return true;
			if(s.compareTo("1") == 0)return true;
			if(s.compareTo("2") == 0)return true;
			if(s.compareTo("3") == 0)return true;
			if(s.compareTo("4") == 0)return true;
			if(s.compareTo("5") == 0)return true;
			if(s.compareTo("6") == 0)return true;
			if(s.compareTo("7") == 0)return true;
			if(s.compareTo("8") == 0)return true;
			if(s.compareTo("9") == 0)return true;
			return false;
	}
	@FXML
	void runSolver(ActionEvent event) {
		sudokuController.reset();
		//take in inputs from text box
		int i = 0;
		for (Node n : gridController.getPuzzleGrid().getChildren()) {
			if (n instanceof TextField) {
				TextField t = (TextField) n;
				//validate the string to make sure nothing is taken in that I dont want
				if(!isValidCellValue(t.getText())) {
					return;
				}
				int cellVal = 0;
				if(t.getText().compareTo("") != 0)
					cellVal = Integer.parseInt(t.getText());
				sudokuController.curr.sudPuzzle[i%9][i/9] = cellVal;
				i++;
			}
		}
		//runs the solver
		sudokuController.solveEverything();
		displayCurrPuzzle();
	}

	public void setRootNode(BorderPane rootNode) {
		this.rootNode = rootNode;
	}

	private int[][] scanInPuzzle(String fn) {
		File puzz = new File(fn);
		int[][] sudPuzzle = null;
		sudPuzzle = new int[9][];
		for (int i = 0; i < 9; i++) {
			sudPuzzle[i] = new int[9];
		}
		try {
			Scanner sc = new Scanner(puzz);
			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 9; y++) {
					sudPuzzle[x][y] = sc.nextInt();
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return sudPuzzle;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
