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

            //character matrix holding map information
            char[][] map = new char[noRows][noColumns];
            for (int j = 0; j < noRows; j++) {
                map[j] = input.readLine().toCharArray();
            }

            //int matrix holding number of paths to that tile
            int[][] noPaths = new int[noRows][noColumns];
            noPaths[0][0] = 1;

            for (int k = 0; k < noRows; k++) {
                for (int l = 0; l < noColumns; l++) {
                    //down rule
                    if (k + 1 < noRows) {
                        noPaths[k + 1][l] += noPaths[k][l];
                    }
                    //right rule
                    if (l +  1 < noColumns) {
                        noPaths[k][l + 1] += noPaths[k][l];
                    }

                }
            }
            //output last element
            System.out.println(noPaths[noRows - 1][noColumns - 1]);
        }
    }
}