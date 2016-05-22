package mazeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import maze.Cell;
import maze.Maze;
import maze.Wall;
import sun.reflect.generics.tree.Tree;

public class KruskalGenerator implements MazeGenerator {

	int sizeC;
	int sizeR;
	int deltaR[];
	int deltaC[];

	Maze maze;

	List<List<Tree>> _sets;
	private Stack<Edge> _edges;

	Random _random = new Random();

	int a = 1;

	boolean[][] visited = null;
	Cell[] edgeTo;
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

		_sets = new ArrayList<List<Tree>>();

		int r = 0;
		int c = 0;
		Cell cell = new Cell(r, c);

		// create a set for each cell
		for (cell.r = 0; cell.r < sizeR; cell.r++) {
			List<Tree> tmp = new ArrayList<Tree>();
			for (cell.c = 0; cell.c < sizeC; cell.c++) {
				tmp.add(new Tree());
			}
			_sets.add(tmp);
		}

		// Build the collection of edges and randomize.
		// Edges are "north" and "west" sides of cell,
		// if index is greater than 0.
		_edges = new Stack<Edge>();
//		if (maze.type==1){
		for (cell.r = 0; cell.r < sizeR; cell.r++) {
			for (cell.c = 0; cell.c < sizeC; cell.c++) {
				if (cell.r > 0) {
					_edges.add(new Edge(cell.c, cell.r, Maze.SOUTH));
				}
				if (cell.c > 0) {
					_edges.add(new Edge(cell.c, cell.r, Maze.WEST));
				}
			}
		}
//		} 
//		else {
//				for (cell.r = 0; cell.r < sizeR; cell.r++) {
//					for (cell.c = 0; cell.c < sizeC + (sizeR + 1) / 2; cell.c++) {
//						if (cell.r > 0) {
//							_edges.add(new Edge(cell.c, cell.r, Maze.SOUTH));
//						}
//						if (cell.c > 0) {
//							_edges.add(new Edge(cell.c, cell.r, Maze.WEST));
//						}
//					}
//				}
//		}
		shuffle(_edges);

		// carvePassages();
		while (_edges.size() > 0) {
			// Select the next edge, and decide which direction we are going in.
			Edge tmp = _edges.pop();

			int x = tmp.getX();
			int y = tmp.getY();
			cell = maze.map[y][x];
			int direction = tmp.getDirection();

			System.out.println(a++);

			if (cell.c + deltaC[direction] < sizeC + (sizeR + 1) / 2 && cell.r + deltaR[direction] < sizeR) {
				Cell next = cell.neigh[direction];
				int dx = cell.c + deltaC[direction];
				int dy = cell.r + deltaR[direction];

				// Pluck out the corresponding sets
				Tree set1 = (_sets.get(y)).get(x);
				Tree set2 = (_sets.get(dy)).get(dx);

				if (!set1.connected(set2)) {

					// Connect the two sets and "knock down" the wall between
					// them.
					set1.connect(set2);
					System.out.println(cell.wall[direction]);
					cell.wall[direction].present = false;
					// cell.wall[Maze.NORTH].present = false;
					// _grid[dy][dx] |= Maze.OPPOSITE(direction);
				}
			}
		}

	} // end of generateMaze()


	/**
	 * Randomly shuffle a List.
	 * 
	 * @param args
	 *            List (of Edges) to be randomly shuffled.
	 */
	private void shuffle(List<Edge> args) {
		for (int i = 0; i < args.size(); ++i) {
			int pos = _random.nextInt(args.size());
			Edge tmp1 = args.get(i);
			Edge tmp2 = args.get(pos);
			args.set(i, tmp2);
			args.set(pos, tmp1);
		}
	}

	class Tree {

		private Tree _parent = null;

		//
		// Build a new tree object
		//
		public Tree() {

		}

		//
		// If we are joined, return the root. Otherwise, return this object
		// instance.
		//
		public Tree root() {
			return _parent != null ? _parent.root() : this;
		}

		//
		// Are we connected to this tree?
		//
		public boolean connected(Tree tree) {
			return this.root() == tree.root();
		}

		//
		// Connect to the tree
		//
		public void connect(Tree tree) {
			tree.root().setParent(this);
		}

		//
		// Set the parent of the object instance
		public void setParent(Tree parent) {
			this._parent = parent;
		}
	}

	class Edge {
		private int _x;
		private int _y;
		private int _direction;

		public Edge(int x, int y, int direction) {
			_x = x;
			_y = y;
			_direction = direction;
		}

		public int getX() {
			return _x;
		}

		public int getY() {
			return _y;
		}

		public int getDirection() {
			return _direction;
		}
	}

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



} // end of class KruskalGenerator
