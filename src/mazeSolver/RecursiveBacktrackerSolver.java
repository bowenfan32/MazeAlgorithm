package mazeSolver;

import java.util.Random;
import java.util.Stack;

import maze.Cell;
import maze.Maze;

/**
 * Implements the recursive backtracking maze solving algorithm.
 */
public class RecursiveBacktrackerSolver implements MazeSolver {

	boolean solved = false;
	boolean[][] visited = null;
	Random ran = new Random();

	@Override
	public void solveMaze(Maze maze) {
		if (maze.type == 1) {
			visited = new boolean[maze.sizeR][maze.sizeC];
		} else {
			visited = new boolean[maze.sizeR][maze.sizeC + (maze.sizeR + 1) / 2];
		}

		Stack<Cell> stack = new Stack();
		stack.push(maze.entrance);

		while (!stack.isEmpty()) {
			Cell cell = stack.peek();
			visited[cell.r][cell.c] = true;
			maze.drawFtPrt(cell);

			if (cell == maze.exit) {
				solved = true;
				return;
			}

			if ((maze.type == 1) && (cell.tunnelTo != null) && (!isVisited(cell.tunnelTo))) {
				stack.push(cell.tunnelTo);
			} else {
				boolean expandable = false;
				int[] dir = randomDir();
				for (int i = 0; i < 6; i++) {
					Cell next = cell.neigh[dir[i]];
					if ((next != null) && (!cell.wall[dir[i]].present) && (!isVisited(next))) {
						stack.push(next);
						expandable = true;
						break;
					}
				}
				if (!expandable) {
					stack.pop();
				}
			}
		}

	} // end of solveMaze()

	boolean isVisited(Cell cell) {
		return this.visited[cell.r][cell.c];
	}

	protected int[] randomDir() {
		int[] dir = new int[6];
		boolean[] present = new boolean[6];
		for (int i = 0; i < 6; i++) {
			do {
				dir[i] = ran.nextInt(6);
			} while (present[dir[i]] != false);
			present[dir[i]] = true;
		}
		return dir;
	}

	@Override
	public boolean isSolved() {
		return solved;
	} // end if isSolved()

	@Override
	public int cellsExplored() {
		// TODO Auto-generated method stub
		return 0;
	} // end of cellsExplored()

} // end of class RecursiveBackTrackerSolver
