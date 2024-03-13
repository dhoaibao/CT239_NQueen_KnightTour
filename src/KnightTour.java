import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class KnightTour extends JFrame implements ActionListener {
    int N;
    int[] xMove = {2, 1, -1, -2, -2, -1, 1, 2};
    int[] yMove = {1, 2, 2, 1, -1, -2, -2, -1};
    int[][] board;
    JButton[][] squares;
    JMenuBar menuBar;
    JMenu Menu;
    JMenuItem continueItem, newItem;

    KnightTour() {
        N = 0;
        board = new int[N][N];
        squares = new JButton[N][N];
    }

    void readFile() {
        try (Scanner myReader = new Scanner(new File("./src/txt/KnightTour.txt"))) {
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
        try (FileWriter myWriter = new FileWriter("./src/txt/KnightTour.txt")) {
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

    boolean isSafe(int x, int y, int[][] board) {
        return (x >= 0 && x < N && y >= 0 && y < N
                && board[x][y] == 0);
    }

    void timeDelay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    boolean solveKTUtil(int x, int y, int movei,
                        int[][] board, int[] xMove,
                        int[] yMove) {
        int k, next_x, next_y;
        if (movei == N * N)
            return true;

        for (k = 0; k < 8; k++) {
            next_x = x + xMove[k];
            next_y = y + yMove[k];
            if (!isSafe(next_x, next_y, board)) continue;

            if (board[next_x][next_y] == 1) {
                if (solveKTUtil(next_x, next_y, movei + 1,
                        board, xMove, yMove)) return true;
            } else {
                board[next_x][next_y] = 1;
                updateBoard(next_x, next_y, true);
                if (solveKTUtil(next_x, next_y, movei + 1,
                        board, xMove, yMove)) return true;
                updateBoard(next_x, next_y, false);
            }
        }
        return false;
    }

    void updateBoard(int row, int col, boolean placeKnight) {
        timeDelay(200);
        if (placeKnight) {
            placeKnight(row, col);
        } else {
            board[row][col] = 0;
            squares[row][col].setBackground(Color.RED);
            timeDelay(200);
            squares[row][col].setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.BLACK);
            squares[row][col].setIcon(null);
        }
    }

    void solveKT() {
        if (!solveKTUtil(0, 0, 1,board, xMove, yMove)) {
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
                if (board[i][j] == 1) placeKnight(i, j);
                add(square);
            }
        }
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void placeKnight(int row, int col) {
        Icon icon = new ImageIcon(new ImageIcon("./src/icons/horse.png").getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
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
            solveKT();
//            writeFile();
        } else if (e.getSource() == newItem) {
            JOptionPane.showConfirmDialog(null, "Bạn có muốn tạo bàn cờ mới?", "Tạo mới", JOptionPane.YES_NO_OPTION);
            resetChessBoard();
            solveKT();
//            writeFile();
        } else {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (e.getSource() == squares[i][j]) {
                        board[i][j] = Math.abs(board[i][j] - 1);
                        if (board[i][j] == 1) placeKnight(i, j);
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
        solveKT();
    }

    public static void main(String[] args) {
        KnightTour Knight = new KnightTour();
        Knight.start();
    }
}