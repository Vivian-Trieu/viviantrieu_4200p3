package _4_in_a_line;

import java.util.Scanner;

public class ticTacToe {

    static int n = 8;
    static int[][] b = new int[n][n];
    static int maxDepth = 64;
    static final long timeLimit = 5000;

    public static void main(String[] args) throws Exception {
        setup();

        Scanner sc = new Scanner(System.in);
        System.out.println("CS 4200 Project 3");
        boolean playerGoesFirst = chooseStartingPlayer(sc);
       
        if (playerGoesFirst) {
            printBoard();
            while (true) {
                
                getAMove();
                //int score = evaluate();
                //System.out.println(score);
                checkGameOver();
                makeMove();
                checkGameOver();
            }
        } else {
            printBoard();
            while (true) {
                makeMove();
                checkGameOver();
                getAMove();
                checkGameOver();
            }
        }
        
    }


    private static boolean chooseStartingPlayer(Scanner sc) {
        System.out.print("Would you like to go first? (y/n): ");
        String choice = sc.nextLine().trim().toLowerCase();

        while (!choice.equals("y") && !choice.equals("n")) {
            System.out.println("Invalid choice. Please choose 'y' or 'n'.");
            System.out.print("Would you like to go first? (y/n): ");
            choice = sc.nextLine().trim().toLowerCase();
        }
        return choice.equals("y");
    }


    private static void printBoard() {
        System.out.println();
        System.out.println("   1  2  3  4  5  6  7  8");
        for (int i = 0; i < n; i++) {
            System.out.print((char)('a' + i) + "  ");
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
        System.out.println("Player's turn. Enter your move (e.g., a1)");
        String input = sc.nextLine().trim().toLowerCase();
        while (!isValidMove(input)) {
            System.out.println("Invalid move. Please try again.");
            input = sc.nextLine().trim().toLowerCase();
        }
        i = input.charAt(0) - 'a';
        j = input.charAt(1) - '1';
        b[i][j] = 2;
        
    }

    private static boolean isValidMove (String input) {
        if (input.length() != 2) {
            return false;
        }

        char rowChar = input.charAt(0);
        char colChar = input.charAt(1);

        if (rowChar < 'a' || rowChar >= 'a' + n || colChar < '1' || colChar > '0' + n) {
            return false;
        }

        int row = rowChar - 'a';
        int col = colChar - '1';

        if (b[row][col] != 0) {
            return false;
        }

        return true;
    }

    
    private static int evaluate() {
        int score = 0;

        // Checking rows for consecutive marks
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 3; j++) {
                int compConsecutiveMarks = 0;
                int playerConsecutiveMarks = 0;
                for (int k = 0; k < 4; k++) {
                    if (b[i][j + k] == 1) {
                        compConsecutiveMarks++;
                    } else if (b[i][j + k] == 2) {
                        playerConsecutiveMarks++;
                    }
                }
                score += calculateScore(compConsecutiveMarks, playerConsecutiveMarks);
            }
        }

        // Checking columns for consecutive marks
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n - 3; i++) {
                int compConsecutiveMarks = 0;
                int playerConsecutiveMarks = 0;
                for (int k = 0; k < 4; k++) {
                    if (b[i + k][j] == 1) {
                        compConsecutiveMarks++;
                    } else if (b[i + k][j] == 2) {
                        playerConsecutiveMarks++;
                    }
                }
                score += calculateScore(compConsecutiveMarks, playerConsecutiveMarks);
            }
        }

        return score;
    }

    // player is minimizer, comp is maximizer
    private static int calculateScore(int compConsecutiveMarks, int playerConsecutiveMarks) {
        int score = 0;
        if (compConsecutiveMarks >= 3) {
            score += 100;
        } else if (compConsecutiveMarks == 2) {
            score += 10;
        } else if (compConsecutiveMarks == 1) {
            score += 1;
        } 
        if (playerConsecutiveMarks >= 3) {
            score -= 1000;
        } else if (playerConsecutiveMarks == 2) {
            score -= 10;
        } else if (playerConsecutiveMarks == 1) {
            score -= 1;
        }

        return score;
    }

    private static void makeMove() {
        int best = -20000;
        int depth = 1; 
        int score;
        int mi = 0, mj = 0;
    
        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeLimit;
    
        while (true) {
            int tempBest = -20000;
            int tempMi = 0, tempMj = 0;
            boolean cutoff = false;
    
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (b[i][j] == 0) {
                        b[i][j] = 1; // make move on board
                        score = min(depth - 1, best, 20000);
                        if (score > tempBest) {
                            tempMi = i;
                            tempMj = j;
                            tempBest = score;
                        }
                        b[i][j] = 0; // undo move

                        if (tempBest >= 20000) {
                            cutoff = true;
                            break;
                        }
    
                        // Check if we need to stop the search due to time limit
                        if (System.currentTimeMillis() >= endTime) {
                            cutoff = true;
                            break;
                        }
                    }
                }
    
                if (cutoff) {
                    break;
                }
            }
    
            // Update the best move if the search completed within the time limit
            best = tempBest;
            mi = tempMi;
            mj = tempMj;
    
            if (System.currentTimeMillis() >= endTime) {
                break;
            }
    
            depth++;
        }
        
        char rowChar = (char)('a' + mi);
        int colNum = mj + 1;
        System.out.println("My move is " + rowChar + colNum);
        b[mi][mj] = 1;
    }

    private static int min(int depth, int alpha, int beta) {
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
                    score = max(depth - 1, alpha, beta);
                    if (score < best) {
                        best = score;
                    }
                    b[i][j] = 0; // undo move

                    beta = Math.min(beta, best);
                    if (beta <= alpha) {
                        return best;
                    }
                }
            }
        }
        return best;
    }


    private static int max(int depth, int alpha, int beta) {
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
                    score = min(depth - 1, alpha, beta);
                    if (score > best) {
                        best = score;
                    }
                    b[i][j] = 0; // undo move

                    alpha = Math.max(alpha, best);
                    if (alpha >= beta) {
                        return best;
                    }
                }
            }
        }
        return best;
    }


    private static int check4Winner() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int mark = b[i][j];
                if (mark == 0) continue;
    
                // Check for four consecutive marks in a row
                if (j <= n - 4 && b[i][j + 1] == mark && b[i][j + 2] == mark && b[i][j + 3] == mark) {
                    return mark == 1 ? 5000 : -5000; // Computer wins or Player wins
                }
    
                // Check for four consecutive marks in a column
                if (i <= n - 4 && b[i + 1][j] == mark && b[i + 2][j] == mark && b[i + 3][j] == mark) {
                    return mark == 1 ? 5000 : -5000; // Computer wins or Player wins
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
        int check = check4Winner();
        if (check == -5000) {
            System.out.println("You win");
            System.exit(0);
        } else if (check == 5000) {
            System.out.println("I win");
            System.exit(0);
        } else if (check == 1) {
            System.out.println("Draw");
            System.exit(0);
        }
    }

}
