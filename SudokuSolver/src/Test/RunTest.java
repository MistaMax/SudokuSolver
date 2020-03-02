package Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Controller.SudokuController;

public class RunTest {
	public static void main(String[] args) {
		SudokuController sud = new SudokuController(scanInPuzzle("puzzle.txt"));
		sud.solveEverything();
	}
	private static int[][] scanInPuzzle(String fn) {
		File puzz = new File(fn);
		int [][] sudPuzzle = null;
		sudPuzzle = new int[9][];
		for(int i=0;i<9;i++) {
			sudPuzzle[i] = new int[9];
		}
		try {
			Scanner sc = new Scanner(puzz);
			for(int x = 0; x < 9; x++) {
				for(int y = 0; y < 9; y++) {
					sudPuzzle[x][y] = sc.nextInt();
				}
			}
			sc.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return sudPuzzle;
	}
}
