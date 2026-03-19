import java.lang.Math;

public class PathSolver {

    private final char NOCONSTRAINT = '.';
    private final char NODIAGONAL   = 'X';
    private final char NOJUMPS      = 'J';
    private final char NOSTEP       = '#';
    private final int  ROWS         = 3;
    private final int  MOD          = 1000000007;

    private int noRows;
    private int noColumns;
    private int noConsecJumps;
    private int noMaxJumps;

    private char[][] map;
    private int mapLine;

    private int[][][][] noPaths;

    public PathSolver(int noRows, int noColumns, int noConsecJumps, int noMaxJumps) {
        this.noRows = noRows;
        this.noColumns = noColumns;
        this.noConsecJumps = noConsecJumps;
        this.noMaxJumps = noMaxJumps;
        this.map = new char[noRows][];
        
        //int matrix holding number of paths to that tile
        noPaths = new int[ROWS][noColumns][noConsecJumps+1][noMaxJumps+1];
        noPaths[0][0][0][0] = 1; 
    }

    public void addLine(char[] line, int i){
        map[i] = line;
    }

    public int answer() {
        int row = 0;
        int prevRow = ROWS-1; //dummy value (wont be used in the first iteration)
        for (int i = 0; i < noRows; i++) {
            if (i >= ROWS)
                clearRow(row);
            for (int j = 0; j < noColumns; j++) {
                char tile = map[i][j];
                if (tile != NOSTEP && (i != 0 || j != 0))
                    for (int k = 0; k <= noMaxJumps; k++) //jumps done to reach this point
                        for (int c = 0; c <= Math.min(k, noConsecJumps); c++) { //consecutive jumps
                            downRule(row, prevRow, i, j, c, k);                 //done to reach this point
                            rightRule(row, i, j, c, k);
                            leftDownRule(row, prevRow, i, j, c, k);
                            doubleDownRule(row, prevRow, i, j, c, k);
                            rightDownRule(row, prevRow, i, j, c, k);
                    }
            }
            prevRow = row;
            row     = nextRow(row);
        }
        //sum all jump final spaces
        int total = 0;
        for (int i = 0; i <= noMaxJumps; i++) 
            for(int j = 0; j <= noConsecJumps; j++)
                total = (total + noPaths[prevRow][noColumns-1][j][i]) % MOD;
        return total;
    }

    private void downRule(int row, int prevRow, int i, int j, int c, int k){
        if (i > 0) 
            noPaths[row][j][0][k] = (noPaths[row][j][0][k] + noPaths[prevRow][j][c][k]) % MOD;
    }

    private void rightRule(int row, int i, int j, int c, int k){
        if (j > 0) 
            noPaths[row][j][0][k] = (noPaths[row][j][0][k] + noPaths[row][j-1][c][k]) % MOD;
    }

    private void leftDownRule(int row, int prevRow, int i, int j, int c, int k){
        if (i > 0 && j < noColumns-1 && k > 0 && c > 0 && canJump(i-1,j+1) && canDiagonal(i-1, j+1)) 
            noPaths[row][j][c][k] = (noPaths[row][j][c][k] + noPaths[prevRow][j+1][c-1][k-1]) % MOD;
    }

    private void rightDownRule(int row, int prevRow, int i, int j, int c, int k){
        if (i > 0 && j > 0 && k > 0 && c > 0 && canJump(i-1,j-1) && canDiagonal(i-1, j-1)) 
            noPaths[row][j][c][k] = (noPaths[row][j][c][k]+ noPaths[prevRow][j-1][c-1][k-1]) % MOD;
    }

    private void doubleDownRule(int row, int prevRow, int i, int j, int c, int k){
        if (i > 1 && canJump(i-2, j) && k > 0 && c > 0)
            noPaths[row][j][c][k] = (noPaths[row][j][c][k] + noPaths[prevRow(prevRow)][j][c-1][k-1]) % MOD;
    }

    private boolean canJump(int x, int y){
        return map[x][y] != NOJUMPS;
    }

    private boolean canDiagonal(int x, int y){
        return map[x][y] != NODIAGONAL;
    }

    private int nextRow(int i){
        i++;
        if (i >= ROWS)
            i = 0;
        return i;
    }

    private int prevRow(int i){
        i--;
        if (i < 0)
            i = ROWS-1;
        return i;
    }

    private void clearRow(int row){
        for (int j = 0; j < noColumns; j++) 
            for (int k = 0; k <= noMaxJumps; k++) 
                for (int c = 0; c <= Math.min(k, noConsecJumps); c++) 
                    noPaths[row][j][c][k] = 0;
    }
}
