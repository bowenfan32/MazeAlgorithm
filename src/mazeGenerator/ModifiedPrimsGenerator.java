package mazeGenerator;

import maze.Maze;
import maze.Cell;
import java.util.Random;

public class ModifiedPrimsGenerator implements MazeGenerator {

	int sizeC;
	int sizeR;
	int deltaR[];
	int deltaC[];

	private static int IN = 0;
	private static int FRONTIER = 1;
	private static int OUT = 2;


	
	@Override
	public void generateMaze(Maze maze) {
		this.sizeC = maze.sizeC;
		this.sizeR = maze.sizeR;
		this.deltaR = Maze.deltaR;
		this.deltaC = Maze.deltaC;

		int dir[] = {Maze.SOUTH,Maze.EAST,Maze.NORTH,Maze.WEST};
		int randir=(int)(Math.random()*dir.length);
		int randomdir = dir[randir];
		Random random = new Random();
		int r = random.nextInt(sizeR);
		int c = random.nextInt(sizeC);
		Cell cell = new Cell(r, c);
		cell = maze.map[r][c];
		System.out.println("start :" +r +"  "+c);
		
		int[] cells = new int[sizeR*sizeC];
		for (int i = 0; i < sizeR*sizeC; i++) {

			cells[i] = OUT;
			
		}//cells = out
		
		int[] frontierCells = new int[sizeR*sizeC];
		cells[cell.r*sizeC + cell.c] = IN;
		int frontierCount = 0;
		for (int i = 0; i < 4; i++) {
			switch (i) {
			case 0:
				if (r > 0) {
					int index = (r - 1) * sizeC + c;
					cells[(r-1)*sizeC + c] = FRONTIER;
					frontierCells[frontierCount++] = index;
					System.out.println("frontier count: " + frontierCount );
					System.out.println("down frontier");
				}//down
				break;
			case 1:
				if (c < sizeC - 1) {
					int index = r * sizeR + (c + 1);
					cells[r*sizeC+c+1] = FRONTIER;
					frontierCells[frontierCount++] = index;
					System.out.println("frontier count: " + frontierCount );
					System.out.println("right frontier");
				}//right
				break;
			case 2:
				if (r < sizeR - 1) {
					int index = (r + 1) * sizeC + c;
					cells[(r+1)*sizeC+c] = FRONTIER;
					frontierCells[frontierCount++] = index;
					System.out.println("frontier count: " + frontierCount );
					System.out.println("up frontier");
				}//up
				break;
			case 3:
				if (c > 0) {
					int index = r * sizeC + (c - 1);
					cells[r*sizeC+c-1] = FRONTIER;
					frontierCells[frontierCount++] = index;
					System.out.println("left frontier");
				}//left
				break;
			}
		}
		int[] inNeighbours = new int[4];
		while (frontierCount > 0) {
			int frontierCellIndex = random.nextInt(frontierCount);
			int index = frontierCells[frontierCellIndex];
			System.out.println("index : "+index);
			int x = index % sizeC;
			int y = index / sizeC;
			int inNeighbourCount = 0;
			for (int i = 0; i < 4; i++) {
				switch (i) {
				case 0:
					if (y > 0 && cells[(y-1)*sizeC+x] == IN) {
						inNeighbours[inNeighbourCount++] = i;
						System.out.println("inNeighbour : down ");
					}
					break;
				case 1:
					if (x < sizeC - 1 && cells[y*sizeC+x+1] == IN) {
						inNeighbours[inNeighbourCount++] = i;
						System.out.println("inNeighbour : right");
					}
					break;
				case 2:
					if (y < sizeR - 1 && cells[(y+1)*sizeC+x] == IN) {
						inNeighbours[inNeighbourCount++] = i;
						System.out.println("inNeighbour : up");
					}
					break;
				case 3:
					if (x > 0 && cells[y*sizeC+x-1] == IN) {
						inNeighbours[inNeighbourCount++] = i;
						System.out.println("inNeighbour : left");
					}
					break;
				}

			}
			maze.map[y][x].wall[dir[inNeighbours[random.nextInt(inNeighbourCount)]]].present = false;
			cells[index] = IN;
			if (frontierCellIndex < frontierCount - 1) {
				System.arraycopy(frontierCells, frontierCellIndex + 1, frontierCells, frontierCellIndex,
						frontierCount - frontierCellIndex - 1);
			}
			frontierCount--;

			for (int i = 0; i < 4; i++) {
				switch (i) {
				case 0:
					if (y > 0 && cells[index-sizeC] == OUT) {
						cells[index-sizeC] = FRONTIER;
						frontierCells[frontierCount++] = index - sizeC;
						System.out.println("frontierCells : "+ (index-sizeC));
					}
					break;
				case 1:
					if (x < sizeC - 1 && cells[index+1] == OUT) {
						cells[index+1] = FRONTIER;
						frontierCells[frontierCount++] = index+1;
						System.out.println("frontierCells : "+ (index+1));
					}
					break;
				case 2:
					if (y < sizeR - 1 && cells[index+sizeC] == OUT) {
						cells[index+sizeC] = FRONTIER;
						frontierCells[frontierCount++] = index + sizeC;
						System.out.println("frontierCells : "+ (index+sizeC));
						
					}
					break;
				case 3:
					if (x > 0 && cells[index-1] == OUT) {
						cells[index-1] = FRONTIER;
						frontierCells[frontierCount++] = index - 1;
						System.out.println("frontierCells : "+ (index-1));
						
					}
					break;
				}
			}
		}
		// TODO Auto-generated method stub

	} // end of generateMaze()

} // end of class ModifiedPrimsGenerator

