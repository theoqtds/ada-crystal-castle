import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        int noTests = Integer.parseInt(input.readLine());

        for (int i = 0; i < noTests; i++) {
            String[] tokens = input.readLine().split(" ");
            //not sure if int is the most memory efficient type for these
            int noRows = Integer.parseInt(tokens[0]);
            int noColumns = Integer.parseInt(tokens[1]);
            int noConsecJumps = Integer.parseInt(tokens[2]);
            int noMaxJumps = Integer.parseInt(tokens[3]);

            // IS IT BETTER TO HAVE ONE PATHSOLVER OR RESET
            PathSolver pathSolver = new 
                                PathSolver(noRows, noColumns, noConsecJumps, noMaxJumps, map);

            //character matrix holding map information
            for (int j = 0; j < noRows; j++) 
                pathSolver.addLine(input.readLine().toCharArray(), j);

            System.out.println(pathSolver.answer());
        }
    }
}
