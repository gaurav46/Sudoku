import com.google.common.base.Strings;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class SudokuPuzzleSolver {

    private static int[][] inputPuzzle = new int[9][9];

    /**
     * @param args The command-line arguments to get csv file path
     */
    public static void main(String[] args) {

        // Parse input CSV file
        parseCSV(args[0]);

        // Solve Sudoku Puzzle
        if (solve(0, 0, inputPuzzle)) {
            System.out.println("Sudoku Solved");
            System.out.println("--------------------");
            printTwoDimArray(inputPuzzle);
        } else {
            System.out.println("NONE");
        }
    }

    private static boolean parseCSV(String inputCsvFileWithFullPath) {
        try {

            if (Strings.isNullOrEmpty(inputCsvFileWithFullPath)) {
                System.out.println("Please provide valid absolute csv file path.");
                return false;
            }
            File file = new File(inputCsvFileWithFullPath);
            if (!file.exists()) {
                System.out.println("File does not exist. Please provide valid csv file.");
                return false;
            }

            CSVParser parser = CSVParser.parse(file, StandardCharsets.US_ASCII, CSVFormat.EXCEL);
            int i = 0;
            for (CSVRecord record : parser) {
                for (int j = 0; j < 9; j++) {
                    inputPuzzle[i][j] = Integer.parseInt(record.get(j));
                }
                i++;
            }
            parser.close();

            System.out.println("Sudoku Puzzle");
            System.out.println("--------------------");
            printTwoDimArray(inputPuzzle);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static void printTwoDimArray(int[][] puzzle) {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (j % 9 != 0) System.out.print(",");
                System.out.print(Integer.toString(puzzle[i][j]));
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private static boolean solve(int i, int j, int[][] cells) {

        if (i == 9) {
            i = 0;
            if (++j == 9) {
                return true;
            }
        }

        // skip filled cells
        if (cells[i][j] != 0) {
            return solve(i + 1, j, cells);
        }

        for (int val = 1; val <= 9; ++val) {
            if (legal(i, j, val, cells)) {
                cells[i][j] = val;
                if (solve(i + 1, j, cells)) {
                    return true;
                }
            }
        }

        // reset on backtrack
        cells[i][j] = 0;

        return false;
    }

    private static boolean legal(int i, int j, int val, int[][] cells) {

        // Row
        for (int k = 0; k < 9; ++k) {
            if (val == cells[k][j]) {
                return false;
            }
        }

        // Column
        for (int k = 0; k < 9; ++k) {
            if (val == cells[i][k]) {
                return false;
            }
        }

        int boxRowOffset = (i / 3) * 3;
        int boxColOffset = (j / 3) * 3;

        // Box
        for (int k = 0; k < 3; ++k) {
            for (int m = 0; m < 3; ++m) {
                if (val == cells[boxRowOffset + k][boxColOffset + m]) {
                    return false;
                }
            }
        }

        // No violations, so it's legal
        return true;
    }

}
