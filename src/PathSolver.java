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
        noPaths = new int[3][noColumns][noConsecJumps+1][noMaxJumps+1];
        noPaths[0][0][0][0] = 1; 
    }

    private void printMap(){
        System.out.println("---------------");
        for (char[] line : map){
            for (char c : line)
                System.out.print(c);
            System.out.println();
        }
        System.out.println("---------------");
    }

    private void printPaths(){ // this shit is lowkey wrong af
        System.out.println("---------------");
        for (int i = 0; i < noRows; i++) {
            for (int j = 0; j < noColumns; j++) {
                int total = 0;
                for (int k = 0; k <= noConsecJumps; k++) {
                    for (int l = 0; l <= noMaxJumps; l++) {
                        total = (total + noPaths[i][j][k][l]) % MOD;
                    }
                }
                System.out.print(total + "\t");
            }
            System.out.println();
        }
        System.out.println("---------------");
    }

    private void printall(int x, int y, int k, int c){
        System.out.println("x: " + x + "; ");
        System.out.println("y: " + y + "; ");
        System.out.println("k: " + k + "; ");
        System.out.println("c: " + c + "; ");
        System.out.println("----------");
    }

    public void addLine(char[] line, int i){
        map[i] = line;
    }

    public int answer() {
        int row = 0;
        int prevRow = 0; //dummy value (wont be used in the first iteration)
        for (int i = 0; i < noRows; i++) {
            //Clear row before being reused
            for (int j = 0; j < noColumns; j++) {
                char tile = map[i][j];
                if (tile != NOSTEP && (i != 0 || j != 0)){
                    for (int k = 0; k <= noMaxJumps; k++) //used to reach this point
                        for (int c = 0; c <= Math.min(k, noConsecJumps); c++) {
                            //consecutive jumps executed
                            if (i >= ROWS)
                                noPaths[row][j][c][k] = 0;
                            downRule(row, prevRow, i, j, k, c);       //to reach this point 
                            rightRule(row, prevRow, i, j, k, c);
                            leftDownRule(row, prevRow, i, j, k, c);
                            doubleDownRule(row, prevRow, i, j, k, c);
                            rightDownRule(row, prevRow, i, j, k, c);
                        }
                }
                else { // clear the data on NOSTEP and (0,0) tiles
                    if (i >= ROWS)
                        for (int k = 0; k <= noMaxJumps; k++) //used to reach this point
                            for (int c = 0; c <= Math.min(k, noConsecJumps); c++) 
                                noPaths[row][j][c][k] = 0;
                }
            }
            prevRow = row;
            row     = nextRow(row);
        }
        //sum all jump final spaces
        int total = 0;
        for (int i = 0; i <= noMaxJumps; i++) 
            for(int j = 0; j <= noConsecJumps; j++)
                total = (total + noPaths[(noRows-1) % 3][noColumns-1][j][i]) % MOD;
        //printMap();
        //printPaths();
        //output total
        return total;
    }

    private void downRule(int row, int prevRow, int i, int j, int k, int c){
        if (i > 0) 
            noPaths[row][j][0][k] = (noPaths[row][j][0][k] + noPaths[prevRow][j][c][k]) % MOD;
    }

    private void rightRule(int row, int prevRow, int i, int j, int k, int c){
        if (j > 0) 
            noPaths[row][j][0][k] = (noPaths[row][j][0][k] + noPaths[row][j-1][c][k]) % MOD;
    }

    private void leftDownRule(int row, int prevRow, int i, int j, int k, int c){
        if (i > 0 && j < noColumns-1 && k > 0 && c > 0 && canJump(i-1,j+1) && canDiagonal(i-1, j+1)) 
            // test if it is noColumns-1 or not noColumns
            noPaths[row][j][c][k] = (noPaths[row][j][c][k] + noPaths[prevRow][j+1][c-1][k-1]) % MOD;
    }

    private void rightDownRule(int row, int prevRow, int i, int j, int k, int c){
        if (i > 0 && j > 0 && k > 0 && c > 0 && canJump(i-1,j-1) && canDiagonal(i-1, j-1)) 
            noPaths[row][j][c][k] = (noPaths[row][j][c][k]+ noPaths[prevRow][j-1][c-1][k-1]) % MOD;
    }

    private void doubleDownRule(int row, int prevRow, int i, int j, int k, int c){
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
}
