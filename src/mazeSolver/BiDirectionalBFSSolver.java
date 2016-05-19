package mazeSolver;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import maze.Cell;
import maze.Maze;

/**
 * Implements Bi-directional BFS maze solving algorithm.
 */
public class BiDirectionalBFSSolver implements MazeSolver {

	boolean solved = false;
	boolean[][] visited = null;
	Cell[] edgeTo;
	Random ran = new Random();

	@Override
	public void solveMaze(Maze maze) {
		if (maze.type == 1) {
			visited = new boolean[maze.sizeR][maze.sizeC];
		} else {
			visited = new boolean[maze.sizeR][maze.sizeC + (maze.sizeR + 1) / 2];
		}
		Queue<Cell> q = new ArrayDeque<Cell>();
		Queue<Cell> q2 = new ArrayDeque<Cell>();

		q.add(maze.entrance);
		q2.add(maze.exit);

		while (!q.isEmpty()) {
			Cell cell = q.remove();
			Cell cell2 = q2.remove();
			visited[cell.r][cell.c] = true;
			visited[cell2.r][cell2.c] = true;
			q.add(cell);
			q2.add(cell2);
			maze.drawFtPrt(cell);
			maze.drawFtPrt(cell2);
			
			if (cell == cell2) {
				solved = true;
				return;
			}
			if ((maze.type == 1) && (cell.tunnelTo != null) && (!isVisited(cell.tunnelTo))) {
				q.add(cell.tunnelTo);
				q2.add(cell2.tunnelTo);
			} else {
				int[] dir = randomDir();
				for (int i = 0; i < 6; i++) {
					Cell next = cell.neigh[dir[i]];
					if ((next != null) && (!cell.wall[dir[i]].present) && (!isVisited(next))) {
						q.add(next);
					}
				}
				for (int i = 0; i < 6; i++) {
					Cell next2 = cell2.neigh[dir[i]];
					if ((next2 != null) && (!cell2.wall[dir[i]].present) && (!isVisited(next2))) {
						q2.add(next2);
					}
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
	} // end of isSolved()

	@Override
	public int cellsExplored() {
		// TODO Auto-generated method stub
		return 0;
	} // end of cellsExplored()

} // end of class BiDirectionalBFSSolver
