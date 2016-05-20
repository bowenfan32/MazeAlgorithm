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

		if (maze.type == 1) {
			visited = new boolean[maze.sizeR][maze.sizeC];
		} else {
			visited = new boolean[maze.sizeR][maze.sizeC + (maze.sizeR + 1) / 2];
		}

		Random random = new Random();
		int r = random.nextInt(sizeR);
		int c = random.nextInt(sizeC);

		Stack<Cell> stack = new Stack();

		Cell cell1 = new Cell(r, c); 
		cell1 = maze.map[r][c];
		stack.push(cell1);


		while (!stack.isEmpty()) {
			Cell cell = stack.peek();
			visited[cell.r][cell.c] = true;
			boolean expandable = false;
			int[] dir = randomDir();
			for (int i = 0; i < 6; i++) {
				Cell next = cell.neigh[dir[i]];

				if ((next != null) && (cell.wall[dir[i]].present) && (!isVisited(next))) {
					stack.push(next);
					cell.wall[dir[i]].present = false;
					expandable = true;
					break;
				}
			}
			if (!expandable)
				stack.pop();

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
	// /*
	// * int sizeC; int sizeR; int deltaR[]; int deltaC[];
	// *
	// * boolean visited;
	// *
	// * boolean marked;
	// *
	// * public boolean isMarked() { return marked; }
	// *
	// * protected boolean isIn(Cell cell) { if (cell == null) return false;
	// * return isIn(cell.r, cell.c); } // end of isIn()
	// *
	// * protected boolean isIn(int r, int c) { return r >= 0 && r < sizeR && c
	// >=
	// * 0 && c < sizeC; } // end of isIn()
	// *
	// * @Override public void generateMaze(Maze maze) { this.sizeC =
	// maze.sizeC;
	// * this.sizeR = maze.sizeR; this.deltaR = Maze.deltaR; this.deltaC =
	// * Maze.deltaC;
	// *
	// * Random random = new Random(); int r = random.nextInt(sizeR); int c =
	// * random.nextInt(sizeC); int dir[] = { Maze.NORTH, Maze.EAST, Maze.SOUTH,
	// * Maze.WEST };
	// *
	// *
	// * LinkedList<Cell> stack = new LinkedList<Cell>();
	// *
	// * Cell cell = new Cell(r, c); // maze.map[cell.r][cell.c] = cell; cell =
	// * maze.map[r][c];
	// *
	// * // visited.add(cell);
	// *
	// * boolean[][] marked = new boolean[sizeR][sizeC]; for (int i = 0; i <
	// * sizeR; i++) { for (int j = 0; j < sizeC; j++) { marked[i][j] = false; }
	// }
	// *
	// * stack.addFirst(cell);
	// *
	// * marked[r][c] = true; System.out.println(cell.r + "+" + cell.c +
	// * "marked");
	// *
	// * int[] neighbours = new int[4];
	// *
	// * do { // Examine the current cell's neighbors int freeNeighbourCount =
	// 0;
	// * for (int i = 0; i < dir.length; i++) {
	// *
	// * switch (i) { case 0: if (cell.r < sizeR && isIn(cell.r +
	// * deltaR[Maze.NORTH], cell.c + deltaC[Maze.NORTH])) { if (!marked[cell.r
	// +
	// * deltaR[Maze.NORTH]][cell.c + deltaC[Maze.NORTH]]) {
	// *
	// * neighbours[freeNeighbourCount++] = i; System.out.println(
	// * "north is added as a free neighbor!!!"); } } break; case 1: if (cell.c
	// <
	// * sizeC && isIn(cell.r + deltaR[Maze.EAST], cell.c + deltaC[Maze.EAST]))
	// {
	// * if (!marked[cell.r + deltaR[Maze.EAST]][cell.c + deltaC[Maze.EAST]]) {
	// * System.out.println("east is added as a free neighbor!!!");
	// *
	// * neighbours[freeNeighbourCount++] = i; } } break; case 2: if (cell.r > 0
	// * && isIn(cell.r + deltaR[Maze.SOUTH], cell.c + deltaC[Maze.SOUTH])) { if
	// * (!marked[cell.r + deltaR[Maze.SOUTH]][cell.c + deltaC[Maze.SOUTH]]) {
	// * neighbours[freeNeighbourCount++] = i; System.out.println(
	// * "south is added as a free neighbor!!!"); } } break; case 3: if (cell.c
	// >
	// * 0 && isIn(cell.r + deltaR[Maze.WEST], cell.c + deltaC[Maze.WEST])) { if
	// * (!marked[cell.r + deltaR[Maze.WEST]][cell.c + deltaC[Maze.WEST]]) {
	// * neighbours[freeNeighbourCount++] = i; System.out.println(
	// * "west is added as a free neighbor!!!"); } } break; } } // Pick a random
	// * free neighbour if (freeNeighbourCount > 0) { stack.addFirst(cell); cell
	// =
	// * new Cell(cell.r, cell.c); switch
	// * (neighbours[random.nextInt(freeNeighbourCount)]) { case 0: //
	// * cell.wall[cell.c--] = null;
	// * maze.map[cell.r][cell.c].wall[Maze.NORTH].present = false; cell.r++;
	// * marked[cell.r][cell.c] = true; System.out.println("north"); break; case
	// * 1: // cell.wall[cell.r++] = null;
	// * maze.map[cell.r][cell.c].wall[Maze.EAST].present = false; cell.c++;
	// * marked[cell.r][cell.c] = true; System.out.println("east"); break; case
	// 2:
	// * // cell.wall[cell.c++] = null;
	// * maze.map[cell.r][cell.c].wall[Maze.SOUTH].present = false; cell.r--;
	// * marked[cell.r][cell.c] = true; System.out.println("south"); break; case
	// * 3: // cell.wall[cell.r--] = null;
	// * maze.map[cell.r][cell.c].wall[Maze.WEST].present = false; cell.c--;
	// * marked[cell.r][cell.c] = true; System.out.println("west"); break; } }
	// * else { cell = stack.removeFirst(); System.out.println("backtrack"); }
	// try
	// * { Thread.sleep(5); } catch (InterruptedException e) { // TODO
	// * Auto-generated catch block e.printStackTrace(); }
	// *
	// * } while (!stack.isEmpty());
	// *
	// * } // end of generateMaze()
	// */
} // end of class RecursiveBacktrackerGenerator
