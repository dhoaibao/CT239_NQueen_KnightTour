import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class NQueen {
    int N;
    int[][] board;
    JButton[][] squares;
    NQueen() {
        N = 0;
        board = new int[N][N];
        squares = new JButton[N][N];
    }
    void readFile() {
        try {
            File myObj = new File("./src/NQueen.txt");
            Scanner myReader = new Scanner(myObj);
            N = Integer.parseInt(myReader.nextLine());
            int i = 0;
            while (myReader.hasNextLine()) {
                String[] tokens = myReader.nextLine().split(" ");
                for (int j = 0; j < N; j++) board[i][j] = Integer.parseInt(tokens[j]); // Parsing each token as integer
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Lỗi đọc file!");
        }
    }
    void writeFile() {
        try {
            FileWriter myWriter = new FileWriter("./src/NQueen.txt");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    myWriter.write(board[i][j] + " ");
                }
                myWriter.write("\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Lỗi ghi file!");
        }
    }
    void printSolution(int[][] board) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(board[i][j]
                        + " ");
        }
    }
    boolean isSafe(int[][] board, int row, int col) {
        int i, j;

        for (i = 0; i < N; i++)
            if ((board[i][col] == 1 && i != row) || (board[row][i] == 1 && i != col))
                return false;

        for (i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;

        for (i = row + 1, j = col - 1; j >= 0 && i < N; i++, j--)
            if (board[i][j] == 1)
                return false;

        return true;
    }
    boolean solveNQUtil(int[][] board, int col) {
        if (col >= N)
            return true;

        for (int i = 0; i < N; i++) {
            if (isSafe(board, i, col)) {
                board[i][col] = 1;
                if (solveNQUtil(board, col + 1))
                    return true;
                board[i][col] = 0;
            }
        }
        return false;
    }
    boolean solveNQ() {
        if (!solveNQUtil(board, 0)) {
            System.out.print("Solution does not exist");
            return false;
        }
        return true;
    }
    void createAndShowGUI() {
        JFrame frame = new JFrame("NQueen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new GridLayout(N, N));
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                squares[i][j] = new JButton();
                squares[i][j].setBackground(Color.WHITE);
                frame.add(squares[i][j]);
            }
        frame.setVisible(true);
    }
    void placeQueen(int row, int col) {
        squares[row][col].setIcon(new ImageIcon("./src/queen.png"));
    }
    void timeDelay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void solveNQGUI() {
        if (!solveNQUtil(board, 0)) {
            System.out.print("Solution does not exist");
            return;
        }
        createAndShowGUI();
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                squares[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);
        if (solveNQUtil(board, 0)) {
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    if (board[i][j] == 1) {
                        placeQueen(i, j);
                        timeDelay(200);
                    }
        }
    }

    public static void main(String[] args) {
        NQueen nq = new NQueen();
        nq.readFile();
        nq.solveNQGUI();
        nq.writeFile();
    }
}


