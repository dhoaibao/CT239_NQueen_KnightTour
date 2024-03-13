import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class NQueen extends JFrame implements ActionListener {
    int N;
    int[][] board;
    JButton[][] squares;
    JMenuBar menuBar;
    JMenu Menu;
    JMenuItem continueItem, newItem;

    NQueen() {
        N = 0;
        board = new int[N][N];
        squares = new JButton[N][N];
    }

    void readFile() {
        try (Scanner myReader = new Scanner(new File("./src/txt/NQueen.txt"))) {
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

    void writeFile() {
        try (FileWriter myWriter = new FileWriter("./src/txt/NQueen.txt")) {
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

    void printSolution(int[][] board) {
        System.out.println(N);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(board[i][j] + " ");
            System.out.println();
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

    void timeDelay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    boolean solveNQUtil(int[][] board, int col) {
        if (col >= N) return true;

        for (int i = 0; i < N; i++) {
            if (!isSafe(board, i, col)) continue;

            if (board[i][col] == 1) {
                if (solveNQUtil(board, col + 1)) return true;
            } else {
                board[i][col] = 1;
                updateBoard(i, col, true);
                if (solveNQUtil(board, col + 1)) return true;
                updateBoard(i, col, false);
            }
        }
        return false;
    }

    void updateBoard(int row, int col, boolean placeQueen) {
        timeDelay(200);
        if (placeQueen) {
            placeQueen(row, col);
        } else {
            board[row][col] = 0;
            squares[row][col].setBackground(Color.RED);
            timeDelay(200);
            squares[row][col].setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.BLACK);
            squares[row][col].setIcon(null);
        }
    }

    void solveNQ() {
        if (!solveNQUtil(board, 0)) {
            JOptionPane.showMessageDialog(null, "Không tìm được lời giải!");
            return;
        }
        printSolution(board);
    }

    void createMenu() {
        menuBar = new JMenuBar();
        Menu = new JMenu("Menu");

        continueItem = createMenuItem("Tiếp tục");
        newItem = createMenuItem("Tạo mới");

        Menu.add(continueItem);
        Menu.add(newItem);

        menuBar.add(Menu);
        setJMenuBar(menuBar);
    }

    JMenuItem createMenuItem(String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(this);
        return item;
    }

    void createChessBoard() {
        setTitle("Bàn cờ vua " + N + "x" + N);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(N, N));
        createMenu();

        squares = new JButton[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                JButton square = new JButton();
                square.setPreferredSize(new Dimension(75, 75));
                square.setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);
                square.addActionListener(this);
                squares[i][j] = square;
                if (board[i][j] == 1) placeQueen(i, j);
                add(square);
            }
        }
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void placeQueen(int row, int col) {
        Icon icon = new ImageIcon(new ImageIcon("./src/icons/queen.png").getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
        squares[row][col].setIcon(icon);
    }

    void resetChessBoard() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                squares[i][j].setIcon(null);
                board[i][j] = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == continueItem) {
            solveNQ();
//            writeFile();
        } else if (e.getSource() == newItem) {
            JOptionPane.showConfirmDialog(null, "Bạn có muốn tạo bàn cờ mới?", "Tạo mới", JOptionPane.YES_NO_OPTION);
            resetChessBoard();
            solveNQ();
//            writeFile();
        } else {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (e.getSource() == squares[i][j]) {
                        board[i][j] = Math.abs(board[i][j] - 1);
                        if (board[i][j] == 1) placeQueen(i, j);
                        else squares[i][j].setIcon(null);
                        return;
                    }
                }
            }
        }
    }

    void start() {
        readFile();
        createChessBoard();
        solveNQ();
    }

    public static void main(String[] args) {
        NQueen Queen = new NQueen();
        Queen.start();
    }
}