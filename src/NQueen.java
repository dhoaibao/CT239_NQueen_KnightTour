import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NQueen extends JFrame implements ActionListener {
    int N;
    int[][] board;
    JButton[][] squares;
    JMenuBar menuBar;
    JMenu Menu;
    JMenuItem continueItem;
    JMenuItem newItem;

    NQueen() {
        N = 0;
        board = new int[N][N];
        squares = new JButton[N][N];
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
            System.out.println("An error occurred.");
        }
    }

    void readFile() {
        try {
            File myObj = new File("./src/NQueen.txt");
            Scanner myReader = new Scanner(myObj);
            int i = 0;
            while (myReader.hasNextLine()) {
                String[] tokens = myReader.nextLine().split(" ");
                for (int j = 0; j < N; j++) board[i][j] = Integer.parseInt(tokens[j]); // Parsing each token as integer
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }

    void printSolution(int[][] board) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(board[i][j]
                        + " ");
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
            throw new RuntimeException(e);
        }
    }

    boolean solveNQUtil(int[][] board, int col) {
        if (col >= N)
            return true;
        for (int i = 0; i < N; i++) {
            if (isSafe(board, i, col)) {
                if (board[i][col] == 1) {
                    if (solveNQUtil(board, col + 1))
                        return true;
                } else {
                    board[i][col] = 1;
                    timeDelay(200);
                    placeQueen(i, col);
                    if (solveNQUtil(board, col + 1))
                        return true;
                    board[i][col] = 0;
                    squares[i][col].setBackground(Color.RED);
                    timeDelay(200);
                    squares[i][col].setBackground((i + col) % 2 == 0 ? Color.WHITE : Color.BLACK);
                    squares[i][col].setIcon(null);
                }
            }
        }
        return false;
    }

    void solveNQ() {
        if (!solveNQUtil(board, 0)) {
            JOptionPane.showMessageDialog(  null, "Không tìm được lời giải!");
            return;
        }

        printSolution(board);
    }

    void createMenu() {
        menuBar = new JMenuBar();
        Menu = new JMenu("Menu");
        newItem = new JMenuItem("New");
        continueItem = new JMenuItem("Continue");

        newItem.addActionListener(this);
        continueItem.addActionListener(this);

        Menu.add(continueItem);
        Menu.add(newItem);
        menuBar.add(Menu);
        setJMenuBar(menuBar);
    }

    void initGUI() {
        JFrame f = new JFrame("B2103488 - Dương Hoài Bảo");
        JPanel panel = new JPanel();
        JLabel l = new JLabel("CT239");
        l.setBounds(50, 50, 100, 30);
        panel.setBounds(175, 175, 90, 60);
        panel.setBackground(Color.gray);
        JButton continueButton = new JButton("Continue");
        JButton newButton = new JButton("New");
        panel.add(continueButton);
        panel.add(newButton);
        panel.setLayout(new GridLayout(2, 1));
        f.add(l);
        f.add(panel);
        f.setSize(500, 500);
        f.setLayout(null);
        f.setVisible(true);
    }

    void initChessBoard() {
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
        ImageIcon imageIcon = new ImageIcon("./src/icon.png");
        Image image = imageIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        Icon icon = new ImageIcon(image);
        squares[row][col].setIcon(icon);
    }

    void resetChessBoard() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                squares[i][j].setIcon(null);
                board[i][j] = 0;
            }
        }
        writeFile();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == continueItem) {
            readFile();
            solveNQ();
        } else if (e.getSource() == newItem) {
            JOptionPane.showConfirmDialog(this, "Are you sure to create a new game?");
            resetChessBoard();
            readFile();
//            solveNQ();
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

    void startNQueen() {
//        N = Integer.parseInt(JOptionPane.showInputDialog("Nhập kích thước bàn cờ (N x N):"));
        N = 8;
        board = new int[N][N];
        readFile();
//        initGUI();
        initChessBoard();
//        solveNQ();

    }

    public static void main(String[] args) {
        NQueen Queen = new NQueen();
        Queen.startNQueen();
    }
}