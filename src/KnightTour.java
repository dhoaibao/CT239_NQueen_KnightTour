import javax.swing.*;

public class KnightTour {
    int N;
    int[] xMove = {2, 1, -1, -2, -2, -1, 1, 2};
    int[] yMove = {1, 2, 2, 1, -1, -2, -2, -1};
    int[][] board;
    ChessBoard chess;

    KnightTour() {
        N = 0;
        board = new int[N][N];
        chess = new ChessBoard("KnightTour", N, board);
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
                chess.updateBoard(next_x, next_y, true);
                if (solveKTUtil(next_x, next_y, movei + 1,
                        board, xMove, yMove)) return true;
                chess.updateBoard(next_x, next_y, false);
            }
        }
        return false;
    }

    void solveKT() {
        if (!solveKTUtil(0, 0, 1,board, xMove, yMove)) {
            JOptionPane.showMessageDialog(null, "Không tìm được lời giải!");
            return;
        }
        printSolution(board);
    }

    void start() {
        FileHandler fileHandler = new FileHandler("KnightTour");
        fileHandler.readFile();
        N = fileHandler.getN();
        board = fileHandler.getBoard();
        chess = new ChessBoard("KnightTour", N, board);
        chess.createChessBoard();
        solveKT();
    }
}