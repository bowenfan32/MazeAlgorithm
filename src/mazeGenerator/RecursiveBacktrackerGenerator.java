package mazeGenerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import maze.Cell;
import maze.Maze;

public class RecursiveBacktrackerGenerator implements MazeGenerator {
	int sizeC;
	int sizeR;
	int deltaR[];
	int deltaC[];

	boolean marked;
	boolean solved = false;
	boolean[][] visited = null;
	Random ran = new Random();

	@Override
	public void generateMaze(Maze maze) {
		this.sizeC = maze.sizeC;
		this.sizeR = maze.sizeR;
		this.deltaR = Maze.deltaR;
		this.deltaC = Maze.deltaC;

		// If maze type is 'normal', use standard coordinates
		// if (maze.type == 1) {
		// visited = new boolean[maze.sizeR][maze.sizeC];
		// } else {
		// visited = new boolean[maze.sizeR][maze.sizeC + (maze.sizeR + 1) / 2];
		// }

		if (maze.type == 2) {
			visited = new boolean[maze.sizeR][maze.sizeC + (maze.sizeR + 1) / 2];
		} else {
			visited = new boolean[maze.sizeR][maze.sizeC];
		}

		Stack<Cell> stack = new Stack();

		// Randomly pick a cell and add it to stack
		Random random = new Random();
		int r = random.nextInt(sizeR);
		int c = random.nextInt(sizeC);
		Cell tmp = new Cell(r, c);
		tmp = maze.map[r][c];
		stack.push(tmp);

		while (!stack.isEmpty()) {
			// Mark first cell in stack as the current cell, and mark it as
			// visited
			Cell cell = stack.peek();
			visited[cell.r][cell.c] = true;

			// If maze has tunnel
			if ((maze.type == 1) && (!isVisited(cell.tunnelTo) && (cell.tunnelTo != null))) {
				stack.push(cell.tunnelTo);
			} else {
				boolean expandable = false;
				// Randomly picks a direction
				int[] dir = randomDir();
				for (int i = 0; i < 6; i++) {
					Cell next = cell.neigh[dir[i]];
					// Check whether next cell is visited or has wall present

					if ((next != null) && (cell.wall[dir[i]].present) && (!isVisited(next))) {
						// If so, add to the stack and removes wall in between
						stack.push(next);
						cell.wall[dir[i]].present = false;
						expandable = true;
						break;
					}
				}
				if (!expandable)
					stack.pop();
			}

		}

	} // end of generateMaze()

	boolean isVisited(Cell cell) {
		return visited[cell.r][cell.c];
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
} // end of class RecursiveBacktrackerGenerator
