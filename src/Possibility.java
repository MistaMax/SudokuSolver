/**
 * This class holds the soduku puzzle and all the possibility booleans needed to calculate
 * the value in the blanks
 * @author Max Crookshanks
 *
 */
public class Possibility {
	public boolean horizPoss[][];
	public boolean vertPoss[][];
	public boolean cubePoss[][];
	public boolean overPoss[][][];
	public int sudPuzzle[][];
	public int tester[] = {0,0};
	
	public Possibility() {
		initPoss();
		sudPuzzle = new int[9][9];
	}
	/**
	 * initializes all the possibility arrays to false
	 */
	public void initPoss() {
		overPoss = new boolean[9][9][10];
		vertPoss = new boolean[9][10];// index at 0 should say one possible value
		horizPoss = new boolean[9][10];
		cubePoss = new boolean[9][10];

		resetPoss();
	}
	/**
	 * sets all the possibility values to false
	 */
	public void resetPoss() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				for (int i = 0; i < 10; i++) {
					overPoss[x][y][i] = false;
				}
			}
		}

		for (int t = 0; t < 9; t++) {
			for (int i = 0; i < 10; i++) {
				vertPoss[t][i] = false;
				horizPoss[t][i] = false;
				cubePoss[t][i] = false;
			}
		}
	}
	
	public void toggleFirstPossValue(int x, int y) {
		int i;
		for (i = 1; i < 10; i++) {
			if (!overPoss[x][y][i]) {
				overPoss[x][y][i] = !overPoss[x][y][i];
				break;
			}
		}
	}
	
	public int getFirstPossValue(int x, int y) {
		int i;
		for (i = 1; i < 10; i++) {
			if (!overPoss[x][y][i])
				return i;
		}
		return 0;
	}
}
