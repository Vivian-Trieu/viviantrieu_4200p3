package _4_in_a_line;

import java.util.Scanner;

public class ticTacToe {

    static int n = 8;
    static int[][] b = new int[n][n];
    static int maxDepth = 64;

    public static void main(String[] args) throws Exception {
        setup();
        printBoard();

        while (true) {
            getAMove();
            checkGameOver();
            makeMove();
            checkGameOver();
        }
    }


    private static void printBoard() {
        System.out.println();
        System.out.println("   1  2  3  4  5  6  7  8");
        for (int i = 0; i < n; i++) {
            System.out.print((char)('A' + i) + "  ");
            for (int j = 0; j < n; j++) {
                if (b[i][j] == 0) {
                    System.out.print("-  ");
                } else if (b[i][j] == 1) {
                    System.out.print("X  ");
                } else {
                    System.out.print("O  ");
                }
                
            }
            System.out.println();
        }
    }

    private static void setup() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                b[i][j] = 0;
            }
        }
    }

    private static void getAMove() {
        int i, j;
        Scanner sc = new Scanner(System.in);
        System.out.println("Player's turn. Enter your move (e.g., A1)");
        String input = sc.nextLine().trim().toUpperCase();
        while (!isValidMove(input)) {
            System.out.println("Invalid move. Please try again.");
            input = sc.nextLine().trim().toUpperCase();
        }
        i = input.charAt(0) - 'A';
        j = input.charAt(1) - '1';
        b[i][j] = 2;
    }

    private static boolean isValidMove (String input) {
        if (input.length() != 2) {
            return false;
        }

        char rowChar = input.charAt(0);
        char colChar = input.charAt(1);

        if (rowChar < 'A' || rowChar >= 'A' + n || colChar < '1' || colChar > '0' + n) {
            return false;
        }

        int row = rowChar - 'A';
        int col = colChar - '1';

        if (b[row][col] != 0) {
            return false;
        }

        return true;
    }

    private static int evaluate() {
        return 0;
    }

    private static void makeMove() {
        int best = -20000;
        int depth = maxDepth; 
        int score;
        int mi = 0, mj = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (b[i][j] == 0) {
                    b[i][j] = 1; // make move on board
                    score = min(depth - 1);
                    if (score > best) {
                        mi = i;
                        mj = j;
                        best = score;
                    }
                    b[i][j] = 0; // undo move
                }
            }
        }
        System.out.println("My move is " + mi + mj);
        b[mi][mj] = 1;
    }

    private static int min(int depth) {
        int best = 20000;
        int score;

        if (check4Winner() != 0) {
            return check4Winner();
        }

        if (depth == 0) {
            return evaluate();
        }

        for (int i = 0; i < n; i++) {
            for (int j =0; j < n; j++) {
                if (b[i][j] == 0) {
                    b[i][j] = 2; // make move on board
                    score = max(depth - 1);
                    if (score < best) {
                        best = score;
                    }
                    b[i][j] = 0; // undo move
                }
            }
        }
        return best;
    }


    private static int max(int depth) {
        int best = -20000;
        int score;
        if (check4Winner() != 0) {
            return check4Winner();
        } 

        if (depth == 0) {
            return evaluate();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (b[i][j] == 0) {
                    b[i][j] = 1; // make move on board
                    score = min(depth - 1);
                    if (score > best) {
                        best = score;
                    }
                    b[i][j] = 0; // undo move
                }
            }
        }
        return best;
    }


    private static int check4Winner() {
        int[] rowSum = new int[n];
        int[] colSum = new int[n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int mark = b[i][j];
                if (mark == 0) continue;

                rowSum[i] += mark;
                colSum[j] += mark;

                if (rowSum[i] == 4 || colSum[j] == 4) {
                    if (mark == 1) {
                        return 5000; // Computer wins
                    } else { 
                        return -5000; // Player wins
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (b[i][j] == 0) {
                    return 0; // Game still ongoing
                }
            }
        }

        return 1; // Draw
    }


    private static void checkGameOver() {
        printBoard();
        if (check4Winner() == -5000) {
            System.out.println("You win");
            System.exit(0);
        } else if (check4Winner() == 5000) {
            System.out.println("I win");
            System.exit(0);
        } else if (check4Winner() == 1) {
            System.out.println("Draw");
            System.exit(0);
        }
    }

}
