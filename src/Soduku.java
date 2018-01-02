import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
/**
 * This soduku class is made to solve a soduku puzzle located in a specified file
 * 
 * the file should have the puzzle in text like this:
 * 1 0 0 7 0 8 0 0 0
 * 6 0 5 4 0 0 0 0 1
 * 0 0 0 0 0 1 0 0 9
 * 8 0 4 0 0 0 0 0 7
 * 0 0 1 0 0 0 9 0 0
 * 5 0 0 0 0 0 6 0 3
 * 2 0 0 1 0 0 0 0 0
 * 9 0 0 0 0 4 1 0 6
 * 0 0 0 2 0 7 0 0 8
 * 
 * where 0 is a blank that needs to be filled
 * @author Max Crookshanks
 *
 */
public class Soduku {
	Possibility curr;
	Stack<Possibility> saved;
	
	int stackCount;
	
	long stepCount;
	
	long stacksGenerated;
	
	boolean skipSolve;
	
	boolean solved;
	
	boolean displayPossibilities;
	
	/**
	 * 
	 * @param fn
	 * 		the file name to be read with the input puzzle
	 */
	public Soduku(String fn) {
		skipSolve = false;
		displayPossibilities = true;
		solved = false;
		stepCount = 0;
		stacksGenerated = 0;
		stackCount = 0;
		curr = new Possibility();
		saved = new Stack<Possibility>();
		scanInPuzzle(fn);
	}
	/**
	 * 
	 * @param fn
	 * 		the file name of the puzzle
	 * @param disp
	 * 		determines weather to display the possibilities or not
	 */
	public Soduku(String fn, boolean disp) {
		skipSolve = false;
		displayPossibilities = disp;
		solved = false;
		stepCount = 0;
		stacksGenerated = 0;
		stackCount = 0;
		curr = new Possibility();
		saved = new Stack<Possibility>();
		scanInPuzzle(fn);
	}
	/**
	 * scan in the puzzle from the specified file
	 */
	public void scanInPuzzle(String fn) {
		File puzz = new File(fn);
		
		try {
			Scanner sc = new Scanner(puzz);
			for(int x = 0; x < 9; x++) {
				for(int y = 0; y < 9; y++) {
					curr.sudPuzzle[x][y] = sc.nextInt();
				}
			}
			sc.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * updates all the possibilities for each open square
	 */
	public void updatePoss() {
		//sets the possibilities to 0
		curr.resetPoss();
		//gets all the numbers in the puzzle and links them to the corresponding
		//limitations
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (curr.sudPuzzle[row][col] != 0) {
					curr.vertPoss[col][curr.sudPuzzle[row][col]] = true;
					curr.horizPoss[row][curr.sudPuzzle[row][col]] = true;
					curr.cubePoss[getCube(row, col)][curr.sudPuzzle[row][col]] = true;
				}
			}
		}
		//finds all the possibilities for  the open spaces in the puzzle
		for(int row = 0; row < 9; row++) {
			for(int col = 0; col < 9; col++) {
				if(curr.sudPuzzle[row][col] == 0) {
					genOverPoss(row, col);
				}
			}
		}
	}
	/**
	 * finds all the possibilties for and empty cell at x,y
	 * 
	 * @param x
	 * @param y
	 */
	public void genOverPoss(int x, int y) {
		int count = 0;
		//crosses out any possibility due to the limitations of the vert, horiz, or cube
		for (int i = 1; i < 10; i++) {
			if (curr.vertPoss[y][i]) {
				curr.overPoss[x][y][i] = true;
				count++;
				continue;
			}
			
			if (curr.horizPoss[x][i]) {
				curr.overPoss[x][y][i] = true;
				count++;
				continue;
			}
			
			if (curr.cubePoss[getCube(x, y)][i]) {
				curr.overPoss[x][y][i] = true;
				count++;
			}
		}
		//runs through a series of tests to see if the possibilities are valid
		if (count == 8) {
			//if there is only one possible value for that area then 
			//overpass at val 0 is true
			curr.overPoss[x][y][0] = true;
		} else if (count == 9) {
			//this only happens when the advanced solve guesser has failed
			//if so, then it will recover the previous sud puzzle then will say 
			//that that certain value did not work and crosses it off of the possibility
			//list
			System.out.println("Ending advanced mode");
			//gets the previous graph and possibilities
			recoverPoss();
			//removes the first possibility
			curr.toggleFirstPossValue(curr.tester[0],curr.tester[1]);
			//tests out the next possible value
			curr.sudPuzzle[curr.tester[0]][curr.tester[1]] = curr.getFirstPossValue(curr.tester[0],curr.tester[1]);
			//we dont want to evaluate until the possibilities are updated
			skipSolve = true;
		} else {
			//this is false if there is more than one possible value
			curr.overPoss[x][y][0] = false;
		}
	}
	/**
	 * calls all the other solvers and runs until the puzzle is solved
	 */
	public void solveEverything() {
		while (!solved) {
			updatePoss();
			if(displayPossibilities)printPossibilities(curr);
			if(!skipSolve) {
				if (!baseSolve())
					advSolveInit();
				stepCount++;
			}
			else
				skipSolve = false;
			solved = checkSolved();
		}
		System.out.println("Solved!");
		System.out.println("Num of steps: " + stepCount);
		System.out.println("Stacks Generated: " + stacksGenerated);
		System.out.println("Solution:");
		printPossibilities(curr);
		
	}

	/**
	 * gets rid of a single possibility number and saves it to working soduku
	 * returns true if there is something that has been replaced
	 * 
	 * @return
	 */
	public boolean baseSolve() {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (curr.sudPuzzle[row][col] == 0) {
					if (curr.overPoss[row][col][0]) {
						curr.sudPuzzle[row][col] = curr.getFirstPossValue(row, col);
						System.out.println("Adding " + curr.getFirstPossValue(row, col) + " to (" + row + ", " + col + ")");
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * tests out a possibility in a square with the lowest ammount of possibilities
	 */
	public void runAdvTester() {
		int count = 0, min = 9, minx = 0, miny = 0;
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				count = 0;
				if(curr.sudPuzzle[x][y] == 0) {
					for(int i = 1; i < 10; i++) {
						if(!curr.overPoss[x][y][i]) {
							count++;
						}
					}
					if(count < min) {
						min = count;
						minx = x;
						miny = y;
					}
				}
			}
		}
		
		saved.peek().tester[0] = minx;
		saved.peek().tester[1] = miny;
		
		curr.sudPuzzle[minx][miny] = curr.getFirstPossValue(minx,miny);
	}
	
	
	/**
	 * if there are no more single possibility spaces, then this will save the working
	 * puzzle then test out a number within the given possibilities
	 */
	public void advSolveInit() {
		System.out.println("Running advanced solve");
		setSave();
		runAdvTester();
	}
	/**
	 * saves the current values of the possibilities and pushes them onto the stack before
	 * testing and replacing values in sudoku
	 */
	public void setSave() {
		Possibility tmp = new Possibility();
		//saves the limitations
		for(int t = 0; t < 9; t++) {
			for(int i = 0; i < 10; i++) {
				tmp.cubePoss[t][i] = curr.cubePoss[t][i];
				tmp.horizPoss[t][i] = curr.horizPoss[t][i];
				tmp.vertPoss[t][i] = curr.vertPoss[t][i];
			}
		}
		//saves the possibilities
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				for(int i = 0; i < 10; i++) {
					tmp.overPoss[x][y][i] = curr.overPoss[x][y][i];
				}
			}
		}
		//saves the sudoku puzzle
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				tmp.sudPuzzle[x][y] = curr.sudPuzzle[x][y];
			}
		}
		//saves the tester
		for(int i = 0; i < 2; i++)
			tmp.tester[i] = curr.tester[i];
		//pushes the saved Possibility object to the saved stack of possibilities
		saved.push(tmp);
		
		stackCount++;
		stacksGenerated++;
	}
	/**
	 * recovers the data from the top of the stack if the tested value did not work
	 */
	public void recoverPoss() {
		if(stackCount != 0) {
			//recovering the limitations
			for(int t = 0; t < 9; t++) {
				for(int i = 0; i < 10; i++) {
					curr.cubePoss[t][i] = saved.peek().cubePoss[t][i];
					curr.horizPoss[t][i] = saved.peek().horizPoss[t][i];
					curr.vertPoss[t][i] = saved.peek().vertPoss[t][i];
				}
			}
			//recovering the possibilities
			for(int x = 0; x < 9; x++) {
				for(int y = 0; y < 9; y++) {
					for(int i = 0; i < 10; i++) {
						curr.overPoss[x][y][i] = saved.peek().overPoss[x][y][i];
					}
				}
			}
			//recovering the puzzle
			for(int x = 0; x < 9; x++) {
				for(int y = 0; y < 9; y++) {
					curr.sudPuzzle[x][y] = saved.peek().sudPuzzle[x][y];
				}
			}
			//recovering the tester
			for(int i = 0; i < 2; i++)
				curr.tester[i] = saved.peek().tester[i];
			//gets rid of the Possiblity on top of the stack
			saved.pop();
			stackCount--;
		}
		else {
			System.out.println("This puzzle is unsolvable");
			System.exit(0);
		}
	}
	/**
	 * checks to see if the puzzle has been solved returns true if it is solved uses
	 * the working soduku array
	 * 
	 * @return
	 */
	public boolean checkSolved() {
		int checkHoriz[][] = new int[9][10];
		int checkVert[][] = new int[9][10];
		int checkCube[][] = new int [9][10];
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (curr.sudPuzzle[x][y] == 0)
					return false;
				else{
					if(checkHoriz[x][curr.sudPuzzle[x][y]] == 1 || (checkVert[y][curr.sudPuzzle[x][y]] == 1 || checkCube[getCube(x,y)][curr.sudPuzzle[x][y]] == 1))
						return false;
					checkHoriz[x][curr.sudPuzzle[x][y]]++;
					checkVert[y][curr.sudPuzzle[x][y]]++;
					checkCube[getCube(x,y)][curr.sudPuzzle[x][y]]++;
				}
			}
		}
		
		return true;
	}

	/**
	 * returns the current cube based on the x(row) and the y(collumn) 
	 * 0 1 2 
	 * 3 4 5 
	 * 6 7 8
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getCube(int x, int y) {
		int xd3 = x / 3;
		int yd3 = y / 3;
		yd3 *= 3;
		return xd3 + yd3;
	}
	/**
	 * prints all the possibilities and values on the sudoku array
	 * @param tmp
	 */
	public void printPossibilities(Possibility tmp) {
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				if(tmp.sudPuzzle[x][y] == 0) {
					System.out.print("(");
					for(int i = 1; i < 10; i++) {
						if(!tmp.overPoss[x][y][i])
							System.out.print(i);
					}
					System.out.print(")");
				}
				else {
					System.out.print(tmp.sudPuzzle[x][y]);
				}
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println("\n");
	}
}
