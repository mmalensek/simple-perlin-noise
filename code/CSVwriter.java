package procedural_generation.tasks.task2;

import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/* 
 *
 * for the basic implementation 
 * only generating one "octave" 
 * to improve runtime and reduce complexity
 * 
 * using linear interpolation for 
 * lower time complexity
 *
 */

public class CSVwriter {

    public static void writeFloatArrayToCSV(float[][] array, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (float[] row : array) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < row.length; j++) {
                line.append(String.format("%.6f", row[j])); // format for 6 decimals
                if (j < row.length - 1) {
                    line.append(",");
                }
            }
            writer.write(line.toString());
            writer.newLine();
        }
        writer.close();
    }

    public static void main(String[] args) {

        /*
         * TEST FUNCTION
         *
         * 
         * Scanner sc = new Scanner(System.in);
         * // float x = sc.nextFloat();
         * // float y = sc.nextFloat();
         * int seed = sc.nextInt();
         * 
         * int[][] array = Perlin.directionArrayGenerate(10, 10, seed);
         * 
         * for (int i = 0; i + 1 < 10; i++) {
         * for (int j = 0; j + 1 < 10; j++) {
         * float value = Perlin.perlinGenerate(i, j, array);
         * System.out.printf("%.2f ", value);
         * }
         * System.out.println();
         * }
         * // najdi max pa pol s tem z deli?
         * 
         * sc.close();
         * 
         * 
         * END OF TEST FUNCTION
         */

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter numCol, numRow, seed and step (0,01..0,8): ");
        int numCol = sc.nextInt();
        int numRow = sc.nextInt();
        int seed = sc.nextInt();
        float step = sc.nextFloat();

        float[][] array = Perlin.perlinArrayGenerate(numCol, numRow, seed, step);

        try {
            writeFloatArrayToCSV(array, "procedural_generation/tasks/task2/output.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        sc.close();

    }

}
