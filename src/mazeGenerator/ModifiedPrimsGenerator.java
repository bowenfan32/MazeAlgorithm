package mazeGenerator;

import maze.Maze;
import maze.Cell;
import java.util.Random;
import java.util.Stack;

public class ModifiedPrimsGenerator implements MazeGenerator {
	int sizeC;
	int sizeR;
	int deltaR[];
	int deltaC[];
	Random random = new Random();

	@Override
	public void generateMaze(Maze maze) {
		this.sizeC = maze.sizeC;
		this.sizeR = maze.sizeR;
		this.deltaR = Maze.deltaR;
		this.deltaC = Maze.deltaC;

		Stack<Cell> setZ = new Stack();
		Stack<Cell> setF = new Stack();

		// Step 1
		// Randomly pick a cell
		int r = random.nextInt(sizeR);
		int c = random.nextInt(sizeC);
		if (maze.type == 2) { // ensure hex random do not go out of bound
			r = random.nextInt(sizeR);
			c = random.nextInt((sizeC - (sizeR + 1) / 2)) + (sizeR + 1) / 2;
		}
		Cell startCell = new Cell(r, c);
		startCell = maze.map[r][c];
		// and add it to stack
		setZ.push(startCell);
		
		int[] dir = randomDir();
		// put neighboring cell into frontier set F
		for (int i = 0; i < 6; i++) {
			Cell cell = setZ.peek();
			Cell neigh = cell.neigh[dir[i]];
			if ((neigh != null) && (cell.wall[dir[i]].present) && !setF.contains(neigh) && !setZ.contains(neigh)) {
				setF.push(neigh);
			}

		}
		while (!setF.isEmpty()) { // loop until no frontier cells 
			// Step 2
			// randomly select a cell form set F and remove it
			int randomCellIndex = random.nextInt(setF.size());
			Cell randomCell = setF.get(randomCellIndex);
			
			// randomly select a cell from a neighbor thats in set z and
			// adjacent
			for (int i = 0; i < 6; i++) {
				Cell randomCellNeigh = randomCell.neigh[dir[i]];
				if (setZ.contains(randomCellNeigh)) {
					// carve a path
					randomCell.wall[dir[i]].present = false;
					break;
				}
			}
			setF.remove(randomCellIndex);

			// Step 3
			// add cell c to set Z
			setZ.push(randomCell);
			// Add its neighbor to frontier set
			for (int i = 0; i < 6; i++) {
				Cell cell = setZ.peek();
				Cell neigh = cell.neigh[dir[i]];
				if ((neigh != null) && (cell.wall[dir[i]].present) && !setF.contains(neigh) && !setZ.contains(neigh)) {
					setF.push(neigh);
				}
			}
		}
	} // end of generateMaze()

	// randomizes direction
	protected int[] randomDir() {
		int[] dir = new int[6];
		boolean[] present = new boolean[6];
		for (int i = 0; i < 6; i++) {
			do {
				dir[i] = random.nextInt(6);
			} while (present[dir[i]] != false);
			present[dir[i]] = true;
		}
		return dir;
	}


} // end of class ModifiedPrimsGenerator
