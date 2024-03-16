import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessBoard extends JFrame implements ActionListener {
    NQueen Queen;
    KnightTour Knight;
    String fileName;
    int N;
    int[][] board;
    JButton[][] squares;
    JMenuBar menuBar;
    JMenu Menu;
    JMenuItem continueItem, newItem;

    public ChessBoard(String fileName, int N, int[][] board) {
        this.fileName = fileName;
        this.N = N;
        this.board = board;
    }

    void createMenu() {
        menuBar = new JMenuBar();
        Menu = new JMenu("Menu");

        continueItem = createMenuItem("Continue");
        newItem = createMenuItem("New");

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

    void timeDelay() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void updateBoard(int row, int col, boolean place) {
        timeDelay();
        if (place) {
            board[row][col] = 1;
            placeQueen(row, col);
        } else {
            board[row][col] = 0;
            squares[row][col].setBackground(Color.RED);
            timeDelay();
            squares[row][col].setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.BLACK);
            squares[row][col].setIcon(null);
        }
    }

    void placeQueen(int row, int col) {
        Icon icon = new ImageIcon(new ImageIcon("./src/icons/" + fileName + ".png").getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
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

    void solve() {
        if (fileName.equals("Queen")) {
            Queen.solveNQ();
        } else {
            Knight.solveKT();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == continueItem) {
            solve();
        } else if (e.getSource() == newItem) {
            JOptionPane.showConfirmDialog(null, "Bạn có muốn tạo bàn cờ mới?", "Tạo mới", JOptionPane.YES_NO_OPTION);
            resetChessBoard();
        } else {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (e.getSource() == squares[i][j]) {
                        board[i][j] = Math.abs(board[i][j] - 1);
//                        Queen.updateBoard(board, i, j);
                        if (board[i][j] == 1) placeQueen(i, j);
                        else squares[i][j].setIcon(null);
                        return;
                    }
                }
            }
        }
    }
}