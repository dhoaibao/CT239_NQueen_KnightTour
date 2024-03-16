import javax.swing.*;

public class NQueen {
    int N;
    int[][] board;
    ChessBoard chess;

    NQueen() {
        N = 0;
        board = new int[N][N];
        chess = new ChessBoard("Queen", N, board);
    }

    void updateBoard(int [][] board, int i, int j) {
        this.board[i][j] = board[i][j];
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

    boolean solveNQUtil(int[][] board, int col) {
        System.out.println(N);
        if (col >= N) return true;
        for (int i = 0; i < N; i++) {
            if (!isSafe(board, i, col)) {
                if (board[i][col] == 1) return false;
                else continue;
            }

            if (board[i][col] == 1) {
                if (solveNQUtil(board, col + 1)) return true;
            } else {
                board[i][col] = 1;
                chess.updateBoard(i, col, true);
                if (solveNQUtil(board, col + 1)) return true;
                board[i][col] = 0;
                chess.updateBoard(i, col, false);
            }
        }
        return false;
    }

    void solveNQ() {
        if (!solveNQUtil(board, 0)) {
            JOptionPane.showMessageDialog(null, "Không tìm được lời giải!");
            return;
        }
        printSolution(board);
    }

    void start() {
        FileHandler fileHandler = new FileHandler("NQueen");
        fileHandler.readFile();
        N = fileHandler.getN();
        board = fileHandler.getBoard();
        chess = new ChessBoard("NQueen", N, board);
        chess.createChessBoard();
        solveNQ();
    }
}