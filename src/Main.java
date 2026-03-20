import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static final String SEPARATOR   = " ";
    private static final int    ROWS        = 0;
    private static final int    COLUMNS     = 1;
    private static final int    CONSECJUMPS = 2;
    private static final int    MAXJUMPS    = 3;

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        // The first line indicates how many test cases/grids will follow
        int noTests = Integer.parseInt(input.readLine());

        // Process each test case individually
        for (int i = 0; i < noTests; i++) {
            String[] tokens = input.readLine().split(SEPARATOR);
            int noRows = Integer.parseInt(tokens[ROWS]);
            int noColumns = Integer.parseInt(tokens[COLUMNS]);
            int noConsecJumps = Integer.parseInt(tokens[CONSECJUMPS]);
            int noMaxJumps = Integer.parseInt(tokens[MAXJUMPS]);
            
            // Initialize a new solver for this specific test case
            PathSolver pathSolver = new PathSolver(noRows, noColumns, noConsecJumps, noMaxJumps);

            // Read the grid row by row and add it to the solver's map
            for (int j = 0; j < noRows; j++) 
                pathSolver.addLine(input.readLine().toCharArray(), j);

            // Calculate the result using DP and print it to standard output
            System.out.println(pathSolver.answer());
        }
    }
}
