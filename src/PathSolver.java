public class PathSolver {

    private final char NOCONSTRAINT = '#';
    private final char NODIAGONAL   = 'X';
    private final char NOJUMPS      = 'J';
    private final char NOSTEP       = ;

    private int noRows;
    private int noColumns;
    private int noConsecJumps;
    private int noMaxJumps;

    private char[][] map;
    private int mapLine;

    private int[][][] noPaths;

    public PathSolver(int noRows, int noColumns, int noConsecJumps, int noMaxJumps) {
        this.noRows = noRows;
        this.noColumns = noColumns;
        this.noConsecJumps = noConsecJumps;
        this.noMaxJumps = noMaxJumps;
        this.map = new char[noRows][];

        //int matrix holding number of paths to that tile
        noPaths = new int[noRows][noColumns][noConsecJumps + 1][noMaxJumps + 1];
        noPaths[0][0][0][noMaxJumps] = 1; // all jumps?
    }

    public void addLine(char[] line, int i){
        map[i] = line;
    }

    public int answer() {
        for (int i = 0; i < noRows; i++) {
            for (int j = 0; j < noColumns; j++) {
                for (int k = 0; k < noMaxJumps + 1; k++) {
                    if (i == 0 && j == 0) 
                        continue;
                    //down rule
                    if (i > 0) 
                        noPaths[i][j][k] += noPaths[i - 1][j][k];
                    //right rule
                    if (j > 0) 
                        noPaths[i][j][k] += noPaths[i][j - 1][k];
                    //left-down rule
                    if (i > 0 && j < noColumns - 1 && k != 0) 
                        noPaths[i][j][k] += noPaths[i - 1][j + 1][k - 1];
                    //down-down rule
                    if (i > 1 && k != 0) 
                        noPaths[i][j][k] += noPaths[i - 2][j][k - 1];
                    //right-down rule
                    if (i > 0 && j > 0 && k != 0) 
                        noPaths[i][j][k] += noPaths[i - 1][j - 1][k - 1];
                }
            }
        }

        //sum all jump final spaces
        int total = 0;
        for (int i = 0; i < noMaxJumps + 1; i++) {
            total = total + noPaths[noRows - 1][noColumns - 1][i];
        }
        //output total
        return total;
    }
}
