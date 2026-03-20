import java.lang.Math;

/**
 * PathSolver calculates the number of paths across a grid with specific movement constraints.
 * It uses a 4D Dynamic Programming table with a sliding window for memory optimization.
 */
public class PathSolver {

    // Tile Constraint Constants
    private final char NOCONSTRAINT = '.'; // Standard tile
    private final char NODIAGONAL   = 'X'; // Cannot move diagonally FROM this tile
    private final char NOJUMPS      = 'J'; // Cannot perform a jump FROM this tile
    private final char NOSTEP       = '#'; // Wall/Obstacle: Cannot step on this tile
    
    private final int  MIN_ROWS      = 3;          // Buffer size for row-based sliding window
    private final int  MOD           = 1000000007; // Modulo of 10^9 + 7

    private int noRows;
    private int noColumns;
    private int noConsecJumps; 
    private int noMaxJumps;    

    /**
     * map containing the characters for each tile
     * map[row][column]
     */
    private char[][] map;

    /**
     * DP Table: noPaths[row_index][column][consecutive_jumps][jumps_executed]
     * row_index uses a sliding window (0 to 2) to save memory.
     */
    private int[][][][] noPaths;

    public PathSolver(int noRows, int noColumns, int noConsecJumps, int noMaxJumps) {
        this.noRows = noRows;
        this.noColumns = noColumns;
        this.noConsecJumps = noConsecJumps;
        this.noMaxJumps = noMaxJumps;
        this.map = new char[noRows][];
        
        // Initialize DP table: 3 rows (circular buffer) x columns x current_consecutive x total_jumps
        noPaths = new int[MIN_ROWS][noColumns][noConsecJumps + 1][noMaxJumps + 1];
        
        // Base case: 1 way to be at the start with 0 jumps
        noPaths[0][0][0][0] = 1; 
    }

    public void addLine(char[] line, int i) {
        map[i] = line;
    }

    /**
     * Processes the grid row by row to fill the DP table.
     * @return Total valid paths to the bottom-right tile.
     */
    public int answer() {
        int row = 0;           // Current row index in the 3-row buffer
        int prevRow = MIN_ROWS - 1; // Theorical correct value that doesnt affect anything
                                    // because it will be overwritten before being used
        for (int i = 0; i < noRows; i++) {
            // clear the "old" row data if looped back in our buffer
            if (i >= MIN_ROWS) 
                clearRow(row);
            for (int j = 0; j < noColumns; j++) {
                char tile = map[i][j];
                // Skip "#" tiles or the starting tile (already initialized)
                if (tile != NOSTEP && (i != 0 || j != 0)) 
                    for (int k = 0; k <= noMaxJumps; k++) 
                        for (int c = 0; c <= Math.min(k, noConsecJumps); c++) {
                            // Try to reach this tile via all possible moves
                            // and sum the ways to reach those tiles
                            downRule(row, prevRow, i, j, c, k);      // Normal move down
                            rightRule(row, i, j, c, k);              // Normal move right
                            leftDownRule(row, prevRow, i, j, c, k);  // Jump: Diagonal Down-Left
                            doubleDownRule(row, prevRow, i, j, c, k);// Jump: Two steps Down
                            rightDownRule(row, prevRow, i, j, c, k); // Jump: Diagonal Down-Right
                        }
            }
            // Advance the sliding window indices
            prevRow = row;
            row = nextRow(row);
        }
        // Final Result: Sum all paths ending at the last column/row with any jump count
        int total = 0;
        int lastRowIndex = prevRow; 
        for (int i = 0; i <= noMaxJumps; i++) 
            for (int j = 0; j <= noConsecJumps; j++) 
                total = sum(total, noPaths[lastRowIndex][noColumns - 1][j][i]);
        return total;
    }

    // Move: (i-1, j) -> (i, j). Resets consecutive jumps to 0.
    private void downRule(int row, int prevRow, int i, int j, int c, int k) {
        if (i > 0) 
            noPaths[row][j][0][k] = sum(noPaths[row][j][0][k], noPaths[prevRow][j][c][k]);
    }

    // Move: (i, j-1) -> (i, j). Resets consecutive jumps to 0.
    private void rightRule(int row, int i, int j, int c, int k) {
        if (j > 0) 
            noPaths[row][j][0][k] = sum(noPaths[row][j][0][k], noPaths[row][j-1][c][k]);
    }

    // Jump: (i-1, j+1) -> (i, j). Increments jump counts.
    private void leftDownRule(int row, int prevRow, int i, int j, int c, int k) {
        if (i > 0 && j < noColumns - 1 && k > 0 && c > 0 && canJump(i - 1, j + 1) && canDiagonal(i - 1, j + 1)) 
            noPaths[row][j][c][k] = sum(noPaths[row][j][c][k], noPaths[prevRow][j + 1][c - 1][k - 1]);
    }

    // Jump: (i-1, j-1) -> (i, j). Increments jump counts.
    private void rightDownRule(int row, int prevRow, int i, int j, int c, int k) {
        if (i > 0 && j > 0 && k > 0 && c > 0 && canJump(i - 1, j - 1) && canDiagonal(i - 1, j - 1)) 
            noPaths[row][j][c][k] = sum(noPaths[row][j][c][k], noPaths[prevRow][j - 1][c - 1][k - 1]);
    }

    // Jump: (i-2, j) -> (i, j). Increments jump counts.
    private void doubleDownRule(int row, int prevRow, int i, int j, int c, int k) {
        // Needs i > 1 because it jumps 2 rows back
        if (i > 1 && canJump(i - 2, j) && k > 0 && c > 0)
            noPaths[row][j][c][k] = sum(noPaths[row][j][c][k], noPaths[prevRow(prevRow)][j][c - 1][k - 1]);
    }

    private boolean canJump(int x, int y) {
        return map[x][y] != NOJUMPS;
    }

    private boolean canDiagonal(int x, int y) {
        return map[x][y] != NODIAGONAL;
    }

    // Helper for circular buffer indexing (0, 1, 2, 0, 1, 2...)
    private int nextRow(int i) {
        i++;
        if (i >= MIN_ROWS)
            i = 0;
        return i;
    }

    // Helper for circular buffer indexing backwards
    private int prevRow(int i) {
        i--;
        if (i < 0)
            i = MIN_ROWS-1;
        return i; 
    }

    // Resets the data in the DP table for a row before it is reused
    private void clearRow(int row) {
        for (int j = 0; j < noColumns; j++) 
            for (int k = 0; k <= noMaxJumps; k++) 
                for (int c = 0; c <= Math.min(k, noConsecJumps); c++) 
                    noPaths[row][j][c][k] = 0;
    }

    /**
     * @pre a < MOD && b < MOD
     * This is fine because we use it everytime we sum 2 numbers that are < MOD
     * the only way a + b > 2MOD is if a > MOD or b > MOD
     * therefore the result is always < MOD and all the numbers kept in the table will then be < MOD
     *
     * doing a % operation is expensive so thats a way to go around it
     */
    private int sum(int a, int b){
        int result = a + b;
        if (result >= MOD)
            result -= MOD;
        return result;
    }

}
