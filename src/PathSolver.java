public class PathSolver {

    int noRows;
    int noColumns;
    int noConsecJumps;
    int noMaxJumps;

    char[][] map;

    int[][] noPaths;

    public PathSolver(int noRows, int noColumns, int noConsecJumps, int noMaxJumps, char[][] map) {
        this.noRows = noRows;
        this.noColumns = noColumns;
        this.noConsecJumps = noConsecJumps;
        this.noMaxJumps = noMaxJumps;

        this.map = map;

        //int matrix holding number of paths to that tile
        noPaths = new int[noRows][noColumns];
        noPaths[0][0] = 1;
    }

    public int answer() {
        for (int k = 0; k < noRows; k++) {
            for (int l = 0; l < noColumns; l++) {
                if (k == 0 && l == 0) {
                    continue;
                }
                //down rule
                if (k > 0) {
                    noPaths[k][l] += noPaths[k - 1][l];
                }

                //right rule
                if (l > 0) {
                    noPaths[k][l] += noPaths[k][l - 1];
                }

            }
        }
        //output last element
        return noPaths[noRows - 1][noColumns - 1];
    }
}
