package project;

import java.util.*;

// supposed to be search tree node class
class State implements Comparable<State> {
	String[][] grid;
	State parent;
	int pathCost;
	public void setPathCost(int pathCost) {
		this.pathCost = pathCost;
	}

	int depth;

	public State(String[][] grid) {
		this.grid = grid;
	}
	public boolean isGoalState() {
		// compares distance between agent and accepting location
		// if it's zero it should return true
		return (grid[1][grid[1].length - 1].equals("A"));
	}
	@Override
	public int compareTo(State o) {
		if (this.pathCost >= o.pathCost)
			return 1;
		return 0;
	}

	public List<State> getChildren() {
		List<String> seenBricks = new LinkedList<>();
		List<State> children = new LinkedList<>();

		for (int i = 0; i < this.grid.length; ++i) {
			for (int j = 0; j < this.grid[i].length; ++j) {
				boolean isAgent = grid[i][j].equals("A");

				if (seenBricks.contains(grid[i][j]))
					continue;
				seenBricks.add(grid[i][j]);

				if (isAgent) {
					try {
						State s = new State(Telekinesis.moveBrickRight(grid, grid[i][j]));
						children.add(s);
					} catch (Exception e) {}
				}

				boolean isHorizontalBrick = grid[i][j].charAt(0) == 'H';
				if (isHorizontalBrick) {
					try {
						State s = new State(Telekinesis.moveBrickRight(grid, grid[i][j]));
						children.add(s);
					} catch (Exception e) {}
					try {
						State s = new State(Telekinesis.moveBrickLeft(grid, grid[i][j]));
						children.add(s);
					} catch (Exception e) {}
				}

				boolean isVerticalBrick = grid[i][j].charAt(0) == 'V';
				if (isVerticalBrick) {
					try {
						State s = new State(Telekinesis.moveBrickUp(grid, grid[i][j]));
						children.add(s);
					} catch (Exception e) {}
					try {
						State s = new State(Telekinesis.moveBrickDown(grid, grid[i][j]));
						children.add(s);
					} catch (Exception e) {}
				}
			}
		}
		return children;
	}

	@Override
	public String toString() {
		return Telekinesis.showGrid(this.grid);
	}

}

abstract class SearchProblem {
	State initialState;

	// The set of states reachable from the initial state by any sequence of actions
	// Set<State> stateSpace;

	public SearchProblem(State initialState) {
		this.initialState = initialState;
	}

	public abstract boolean goalTest(State state);

}

public class Main {
	public static void main(String[] args) throws Exception {
		 // String[][] grid = Telekinesis.genGrid(4, 4);
		 // Object[] res = tele.search(tele.initialState.grid, Strategy.BF, false);
		// String seqOfMovesForGoal = (String) res[0];
		// int cost = (int) res[1];
		// int numExpandedNodes = (int) res[2];
		// String[][] grid = Telekinesis.genGrid(4, 4);
		String[][] grid = new String[4][4];
		grid = Telekinesis.initializeGridWithBlanks(grid);
		grid = Telekinesis.putAgent(grid);
		grid = Telekinesis.putBricks(grid);	
		System.out.println(Telekinesis.showGrid(grid));

		//Telekinesis tele = new Telekinesis(new State(grid));
		//State sol = tele.search(tele.initialState.grid, Strategy.BF, false, 0);
		//System.out.println(Telekinesis.showGrid(sol.grid));
		
		// List<State> children = new State(grid).getChildren();
		// System.out.println(children);
		// System.out.println(Telekinesis.showGrid(grid));
		// grid = Telekinesis.moveBrickLeft(grid, "H1");
		
		

	}
}
class Telekinesis extends SearchProblem {

	public Telekinesis(State initialState) {
		super(initialState);
	}

	@Override
	public boolean goalTest(State state) {
		return state.isGoalState();
	}

	static String[][] genGrid(int n, int m) throws Exception {
		if (n < 4 || m < 4) {
			throw new Exception("n or m is less than 4");
		}
		String[][] grid = new String[n][m];
		grid = initializeGridWithBlanks(grid);
		grid = putAgent(grid);
		grid = putBricks(grid);

		return grid;
	}

	public static String[][] moveBrickUp(String[][] grid, String brick) throws Exception {
		String[][] ret = deepClone(grid);
		for (int i = 0; i < ret.length; ++i) {
			for (int j = 0; j < ret[i].length; ++j) {
				// try {
				boolean foundBrickToBeMoved = ret[i][j].equals(brick);
				boolean sizeThreeBrick = false;
				try {
					sizeThreeBrick = foundBrickToBeMoved && ret[i + 2][j].equals((brick));
				} catch (Exception e) {
					// throw new Exception("Can't move brick up");
				}
				// boolean emptySpaceBelowSizeThreeBrick = sizeThreeBrick && ret[i - 3][j].equals(" ");

				if (foundBrickToBeMoved) {
					boolean emptySpaceBelow = i - 1 >= 0 && ret[i - 1][j].equals(" ");

					if (sizeThreeBrick && emptySpaceBelow) {
						ret[i + 1][j] = " ";
						ret[i - 1][j] = brick;
					}
					else if (emptySpaceBelow) {
						ret[i + 1][j] = " ";
						ret[i - 1][j] = brick;
						return ret;
					}
				}
				// }
				// catch (Exception e) { System.out.println("Can't move brick down"); }
			}
		}
		throw new Exception("Can't move brick up");
	}

	public static String[][] moveBrickDown(String[][] grid, String brick) throws Exception {
		String[][] ret = deepClone(grid);
		for (int i = 0; i < ret.length; ++i) {
			for (int j = 0; j < ret[i].length; ++j) {
				// try {
				boolean foundBrickToBeMoved = ret[i][j].equals(brick);
				boolean sizeThreeBrick = false;
				try {
					sizeThreeBrick = foundBrickToBeMoved && ret[i + 2][j].equals((brick));
				} catch (Exception e) {
					throw new Exception("Can't move brick down");
				}
				boolean emptySpaceBelowSizeThreeBrick = sizeThreeBrick && ret[i + 3][j].equals(" ");

				if (foundBrickToBeMoved) {
					boolean emptySpaceBelow = i + 2 <= ret.length - 1 && ret[i + 2][j].equals(" ");

					if (sizeThreeBrick && emptySpaceBelowSizeThreeBrick) {
						ret[i][j] = " ";
						ret[i + 3][j] = brick;
						return ret;
					}
					else if (emptySpaceBelow) {
						ret[i][j] = " ";
						ret[i + 2][j] = brick;
						return ret;
					}
				}
				// }
				// catch (Exception e) { System.out.println("Can't move brick down"); }
			}
		}
		throw new Exception("Can't move brick down");
	}

	public static String[][] moveBrickRight(String[][] grid, String brick) throws Exception {
		String[][] ret = deepClone(grid);
		for (int i = 0; i < ret.length; ++i) {
			for (int j = 0; j < ret[i].length; ++j) {
				// try {
				boolean foundBrickToBeMoved = ret[i][j].equals(brick);
				boolean sizeThreeBrick = false;
				try {
					sizeThreeBrick = foundBrickToBeMoved && ret[i][j+2].equals((brick));
				} catch (Exception e) {
					// throw new Exception("Can't move brick right");
				}
				boolean emptySpaceBelowSizeThreeBrick = sizeThreeBrick && ret[i][j+3].equals(" ");

				if (foundBrickToBeMoved) {
					boolean emptySpaceBelow = j + 2 <= ret[i].length - 1 && ret[i][j+2].equals(" ");

					if (emptySpaceBelow) {
						ret[i][j] = " ";
						ret[i][j+2] = brick;
						return ret;
					}
					else if (sizeThreeBrick && emptySpaceBelowSizeThreeBrick) {
						ret[i][j] = " ";
						ret[i][j+3] = brick;
						return ret;
					}
				}
				// }
				// catch (Exception e) { System.out.println("Can't move brick down"); }
			}
		}
		throw new Exception("Can't move brick right");
	}

	public static String[][] moveBrickLeft(String[][] grid, String brick) throws Exception {
		String[][] ret = deepClone(grid);
		for (int i = 0; i < ret.length; ++i) {
			for (int j = 0; j < ret[i].length; ++j) {
				// try {
				boolean foundBrickToBeMoved = ret[i][j].equals(brick);
				boolean sizeThreeBrick = false;
				try {
					sizeThreeBrick = foundBrickToBeMoved && ret[i][j+2].equals((brick));
				} catch (Exception e) {
					// throw new Exception("Can't move brick right");
				}
				boolean emptySpaceBelowSizeThreeBrick = sizeThreeBrick && ret[i][j-1].equals(" ");

				if (foundBrickToBeMoved) {
					boolean emptySpaceBelow = j - 1 >= 0 && ret[i][j-1].equals(" ");

					if (emptySpaceBelow) {
						ret[i][j+1] = " ";
						ret[i][j-1] = brick;
						return ret;
					}
					else if (sizeThreeBrick && emptySpaceBelowSizeThreeBrick) {
						ret[i][j+2] = " ";
						ret[i][j-1] = brick;
						return ret;
					}
				}
				// }
				// catch (Exception e) { System.out.println("Can't move brick down"); }
			}
		}
		throw new Exception("Can't move brick left");
	}

	public static String showGrid(String[][] grid) {	
		StringBuilder ret = new StringBuilder();	
		for (int i = 0; i < grid.length; ++i) {	
			for (int j = 0; j < grid[i].length; ++j)	
				ret.append(grid[i][j] + ", ");	
			ret.append("\n");	
		}	
		return ret.append("\n").toString();	
	}

	public static String[][] initializeGridWithBlanks(String[][] grid) {
		String[][] ret = deepClone(grid);
		for (int i = 0; i < ret.length; ++i) {
			for (int j = 0; j < ret[i].length; ++j) {
				ret[i][j] = " ";
			}
		}
		return ret;
	}

	public static String[][] putBricks(String[][] grid) {
		String[][] ret = deepClone(grid);
		ret = Telekinesis.initializeGridWithBlanks(ret);
		ret = Telekinesis.putAgent(ret);
		ret = Telekinesis.putHorizontalBrick(ret, 1, 0, Size.TWO);
		ret = Telekinesis.putHorizontalBrick(ret, 0, 2, Size.TWO);
		ret = Telekinesis.putHorizontalBrick(ret, 0, 3, Size.TWO);
		ret = Telekinesis.putVerticalBrick(ret, 3, 0, Size.TWO);
		return ret;
	}

	public static String[][] putHorizontalBrick(String[][] grid, int x, int y, Size size) {
		String[][] ret = deepClone(grid);
		String validBrickNum = getValidBrickNum(grid);

		if (ret[y][x] == " " && ret[y][x + 1] == " ") {
			ret[y][x] = "H" + validBrickNum;
			ret[y][x + 1] = "H" + validBrickNum;;
			if (size == Size.THREE && ret[y][x + 2] == " ")
				ret[y][x + 2] = "H" + validBrickNum;;
		}


		return ret;
	}

	public static String[][] putVerticalBrick(String[][] grid, int x, int y, Size size) {
		String[][] ret = deepClone(grid);
		String validBrickNum = getValidBrickNum(grid);

		if (ret[y][x] == " " && ret[y + 1][x] == " ") {
			ret[y][x] = "V" + validBrickNum;
			ret[y + 1][x] = "V" + validBrickNum;;
			if (size == Size.THREE && ret[y + 2][x] == " ")
				ret[y + 2][x] = "V" + validBrickNum;;
		}

		return ret;
	}

	public static String getValidBrickNum(String[][] grid) {
		int max = -1;
		int current = -1;

		for (int i = 0; i < grid.length; ++i)
			for (int j = 0; j < grid[i].length; ++j)
				if (grid[i][j].charAt(0) == 'V' || grid[i][j].charAt(0) == 'H') {
					current = Integer.parseInt(grid[i][j].charAt(1) + "");
					if (current > max)
						max = current;
				}

		return (max == - 1) ? Integer.toString(1) : Integer.toString(max + 1);
	}

	public static String[][] putAgent(String[][] grid) {
		String[][] ret = deepClone(grid);
		ret[1][0] = "A";
		ret[1][1] = "A";
		return ret;
	}


	public static String[][] deepClone(String[][] arr) {
		String[][] clone = new String[arr.length][];

		for (int i = 0; i < arr.length; ++i) {
			clone[i] = new String[arr[i].length];

			for (int j = 0; j < arr[i].length; ++j) {
				clone[i][j] = arr[i][j];
			}
		}
		return clone;
	}

	public Object[] search(String[][] grid, Strategy strategy, boolean visualize,int heuristic) throws Exception {
		String seqOfMovesForGoal = ""; // if possible
		int cost = 0;
		int numExpandedNodes = 0;

		switch (strategy) {
		case BF:
			Search.bfs(this);
			break;
		case AS:
			Search.as(this,heuristic);
			break;
		case DF:
			Search.dfs(this);
			break;
		case GR:
			Search.grs(this,heuristic);
			break;
		case ID:
			Search.ids(this);
			break;
		case UC:
			Search.ucs(this);
			break;
		default:
			break;
		}
		return new Object[] { seqOfMovesForGoal, cost, numExpandedNodes };
	}

}

class Search {
	static Object generalSearch(Telekinesis problem, Strategy strategy) throws Exception {
		// returns solution or a failure
		List<State> nodes = new ArrayList<State>();
		nodes.add(problem.initialState);


		for (int i = 0; i < 10000; ++i) {
			if ((nodes.isEmpty()) || (i==10000-1)) 
				//10000 is for handling that the search problem would be infinite or to be exact not computable
				throw new Exception("FAILURE!");

			State node = nodes.get(0);
			List<State> expansion = node.getChildren();

			if (problem.goalTest(node))
				return node;

			switch(strategy) {
			case AS1:
				for (State state : expansion) {
					state.setPathCost(furnitureCountHeuristic(state));
				}
				expansion.sort((a, b) -> a.compareTo(b));
				nodes.add(0, expansion.get(0));
				break;
			case AS2:
				for (State state : expansion) {
					state.setPathCost(blockingFurnitureCountHeuristic(state));
				}
				expansion.sort((a, b) -> a.compareTo(b));
				nodes.add(0, expansion.get(0));
				break;
			case BF:
				nodes.add(nodes.size() - 1, expansion.get(0));
				break;
			case DF:
				nodes.add(0, expansion.get(0));
				break;
			case GR1:
				for (State state : expansion) {
					state.setPathCost(furnitureCountHeuristic(state));
				}
				expansion.sort((a, b) -> a.compareTo(b));
				nodes.add(0, expansion.get(0));
				break;
			case GR2:
				for (State state : expansion) {
					state.setPathCost(blockingFurnitureCountHeuristic(state));
				}
				expansion.sort((a, b) -> a.compareTo(b));
				nodes.add(0, expansion.get(0));
				break;
			case ID:
				nodes.add(nodes.size() - 1, expansion.get(0));
				break;
			case UC:
				expansion.sort((a, b) -> a.compareTo(b));
				nodes.add(nodes.size() - 1, expansion.get(0));
				break;
			default:
				break;
			}
			expansion.remove(0);
		}
		return null; 
	}
	//first heuristic function
	public static int furnitureCountHeuristic(State state) {
		int count = 0;
		int distanceTogoal = state.grid[1].length-2; //we subtract 2 for agent's position
		boolean test = false;

		//we will check if the path to goal is clear or not for the heuristic function to satisfy the centering property h(n)=0
		for (int z = 2 ; z < state.grid[1].length; z++) {
			if (state.grid[1][z]!=" "){
				test=true;
				break;
			}
		}
		if(!test) {
			//check for furniture pieces in the whole grid and assume every furniture would make at least 1 move and add to them the distance to goal too
			for (int i = 0 ; i < state.grid.length; i++) {
				for (int j = 0 ; i < state.grid[i].length; j++) {
					if ((state.grid[i][j]!=" ")&&((state.grid[i][j]!="A1")||((state.grid[i][j]!="A2")))) count++;
				}
			}
		}

		return (count + distanceTogoal) ;
	}

	//second heuristic function
	public static int blockingFurnitureCountHeuristic(State state) {
		int exitCol = state.grid[1].length;
		int count = 0;
		//check for furniture pieces in the agent's row or column between it and the exit
		for (int i = 2 ; i < exitCol; i++) {
			if (state.grid[1][i]!=" "){
				count++;
			}
		}

		return count;
	}


	static Object ucs(Telekinesis problem) throws Exception {
		return generalSearch(problem, Strategy.UC);
	}
	static Object ids(Telekinesis problem) throws Exception {
		return generalSearch(problem, Strategy.ID);
	}
	static Object grs(Telekinesis problem, int heuristic) throws Exception {
		switch (heuristic) {
		case 1:
			return generalSearch(problem, Strategy.GR1);
		case 2:
			return generalSearch(problem, Strategy.GR2);
		}
		return null;
	}
	static Object dfs(Telekinesis problem) throws Exception {
		return generalSearch(problem, Strategy.DF);
	}
	static Object as(Telekinesis problem,int heuristic) throws Exception {
		switch (heuristic) {
		case 1:
			return generalSearch(problem, Strategy.AS1);
		case 2:
			return generalSearch(problem, Strategy.AS2);
		}
		return null;
	}
	static Object bfs(Telekinesis problem) throws Exception {
		return generalSearch(problem, Strategy.BF);
	}
}

enum Size {
	TWO, THREE
}

enum Strategy {
	BF, DF, ID, UC, GR1,GR2, AS1,AS2,AS,GR
}