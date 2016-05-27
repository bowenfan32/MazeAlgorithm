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
		// If maze type is 'normal', use standard coordinates
		if (maze.type == 2) {
			visited = new boolean[maze.sizeR][maze.sizeC + (maze.sizeR + 1) / 2];
		} else {
			visited = new boolean[maze.sizeR][maze.sizeC];
		}

		Stack<Cell> stack = new Stack();
		// Add the entrance to the stack
		stack.push(maze.entrance);

		while (!stack.isEmpty()) {
			// Mark first cell in stack as the current cell, and mark it as visited
			Cell cell = stack.peek();
			visited[cell.r][cell.c] = true;
			maze.drawFtPrt(cell);

			// If cell reaches exit, return solved
			if (cell == maze.exit) {
				solved = true;
				return;
			}

			// If cell has an unvisited tunnel, go to the other end of tunnel
			if ((maze.type == 1) && (cell.tunnelTo != null) && (!isVisited(cell.tunnelTo))) {
				stack.push(cell.tunnelTo);
			} else {
				boolean expandable = false;
				// Randomly picks a direction
				int[] dir = randomDir();
				for (int i = 0; i < 6; i++) {
					Cell next = cell.neigh[dir[i]];
					// Check whether next cell is visited or has wall present
					if ((next != null) && (!cell.wall[dir[i]].present) && (!isVisited(next))) {
						// If so, add to the stack and removes wall in between
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
