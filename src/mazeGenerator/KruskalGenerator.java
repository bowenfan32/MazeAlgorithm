package mazeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import maze.Cell;
import maze.Maze;
import maze.TunnelMaze;
import maze.Wall;

public class KruskalGenerator implements MazeGenerator {

	int sizeC;
	int sizeR;
	int deltaR[];
	int deltaC[];

	private List<List<Tree>> sets;
	private Stack<Edge> edges;
	Random random = new Random();

	@Override
	public void generateMaze(Maze maze) {
		this.sizeC = maze.sizeC;
		this.sizeR = maze.sizeR;
		this.deltaR = Maze.deltaR;
		this.deltaC = Maze.deltaC;

		Cell cell = new Cell(0, 0);
		sets = new ArrayList<List<Tree>>();
		// create a set for each cell
		if (maze.type == 2) {
			for (cell.r = 0; cell.r < sizeR; cell.r++) {
				List<Tree> tree = new ArrayList<Tree>();
				for (cell.c = 0; cell.c < sizeC + (cell.r + 1) / 2; cell.c++) {
					tree.add(new Tree());
				}
				sets.add(tree);
			}
		} else {
			for (cell.r = 0; cell.r < sizeR; cell.r++) {
				List<Tree> tree = new ArrayList<Tree>();
				for (cell.c = 0; cell.c < sizeC; cell.c++) {
					tree.add(new Tree());
				}
				sets.add(tree);
			}
		}

		// Build the collection of edges and randomize.
		edges = new Stack<Edge>();
		if (maze.type == 2) {
			// Edges are "south-west","south-east" and "west" sides of cell,
			for (cell.r = 0; cell.r < sizeR; cell.r++) {
				for (cell.c = (cell.r + 1) / 2; cell.c < sizeC + (cell.r + 1) / 2; cell.c++) {
					// for every odd row
					if (cell.r > 0 && cell.r % 2 == 1) {
						edges.add(new Edge(cell.c, cell.r, Maze.SOUTHWEST));
					}
					// for every even row
					if (cell.r > 0 && cell.r % 2 == 0) {
						edges.add(new Edge(cell.c, cell.r, Maze.SOUTHEAST));
					}
					if (cell.c > (cell.r + 1) / 2) {
						edges.add(new Edge(cell.c, cell.r, Maze.WEST));
					}
				}
			}
		} else {
			// Edges are "north" and "west" sides of cell,
			for (cell.r = 0; cell.r < sizeR; cell.r++) {
				for (cell.c = 0; cell.c < sizeC; cell.c++) {
					if (cell.r > 0) {
						edges.add(new Edge(cell.c, cell.r, Maze.SOUTH));
					}
					if (cell.c > 0) {
						edges.add(new Edge(cell.c, cell.r, Maze.WEST));
					}
				}
			}
		}
		shuffle(edges);

		while (edges.size() > 0) {
			// Select the next edge, and decide which direction we are going in.
			Edge tmp = edges.pop();
			int x = tmp.getX();
			int y = tmp.getY();
			cell = maze.map[y][x];
			int direction = tmp.getDirection();

			if (maze.type == 2) {
				if (cell.c + deltaC[direction] < sizeC + (cell.r + 1) / 2 && cell.r + deltaR[direction] < sizeR) {
					int dx = cell.c + deltaC[direction];
					int dy = cell.r + deltaR[direction];

					// Pluck out the corresponding sets
					Tree set1 = (sets.get(y)).get(x);
					Tree set2 = (sets.get(dy)).get(dx);

					// Connect two sets and remove the wall in between
					if (!set1.connected(set2)) {
						set1.connect(set2);
						cell.wall[direction].present = false;
					}
				}
			} else {
				if (cell.c + deltaC[direction] < sizeC && cell.r + deltaR[direction] < sizeR) {

					if (cell.tunnelTo != null) {
						Tree set1 = (sets.get(cell.r)).get(cell.c);
						Tree set2 = (sets.get(cell.tunnelTo.r)).get(cell.tunnelTo.c);
						if (!set1.connected(set2)) {
							set1.connect(set2);
						}
					} else {
						int dx = cell.c + deltaC[direction];
						int dy = cell.r + deltaR[direction];

						// Pluck out the corresponding sets
						Tree set1 = (sets.get(y)).get(x);
						Tree set2 = (sets.get(dy)).get(dx);

						if (!set1.connected(set2)) {
							// Connect two sets and remove the wall in between
							set1.connect(set2);
							cell.wall[direction].present = false;
						}
					}
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
			int pos = random.nextInt(args.size());
			Edge tmp1 = args.get(i);
			Edge tmp2 = args.get(pos);
			args.set(i, tmp2);
			args.set(pos, tmp1);
		}
	}

	class Tree {

		private Tree parent = null;

		// Build a new tree object
		public Tree() {

		}

		// If we are joined, return the root. Otherwise, return this object
		// instance.
		public Tree root() {
			return parent != null ? parent.root() : this;
		}

		// Check whether the trees are connected
		public boolean connected(Tree tree) {
			return this.root() == tree.root();
		}

		// Connect to the tree
		public void connect(Tree tree) {
			tree.root().setParent(this);
		}

		// Set the parent of the object instance
		public void setParent(Tree parent) {
			this.parent = parent;
		}
	}

	class Edge {
		private int x;
		private int y;
		private int direction;

		public Edge(int x, int y, int direction) {
			this.x = x;
			this.y = y;
			this.direction = direction;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}

		public int getDirection() {
			return this.direction;
		}
	}

} // end of class KruskalGenerator
