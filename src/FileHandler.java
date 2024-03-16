import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {

    String fileName;
    private int N;
    private int[][] board;

    public FileHandler(String fileName) {
        this.fileName = fileName;
    }

    public int getN() {
        return N;
    }

    public int[][] getBoard() {
        return board;
    }

    public void readFile() {
        try (Scanner myReader = new Scanner(new File("./src/txt/" + fileName + ".txt"))) {
            N = Integer.parseInt(myReader.nextLine());
            board = new int[N][N];
            for (int i = 0; myReader.hasNextLine(); i++) {
                String[] tokens = myReader.nextLine().split(" ");
                for (int j = 0; j < N; j++) board[i][j] = Integer.parseInt(tokens[j]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Lỗi đọc file!");
        }
    }

    public void writeFile(int N, int[][] board) {
        try (FileWriter myWriter = new FileWriter("./src/txt/" + fileName + "txt")) {
            myWriter.write(N + "\n");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    myWriter.write(board[i][j] + " ");
                }
                myWriter.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Lỗi ghi file!");
        }
    }
}