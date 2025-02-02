package server;

import java.io.*;
import java.net.*;

class ClientHandler extends Thread {
    private final Socket player1Socket;
    private final Socket player2Socket;
    private GameLogic gameLogic; // Allows resetting for new games

    public ClientHandler(Socket player1, Socket player2) {
        this.player1Socket = player1;
        this.player2Socket = player2;
        this.gameLogic = new GameLogic(); // Generates a random secret number
    }

    public void run() {
        try (
                BufferedReader in1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
                PrintWriter out1 = new PrintWriter(player1Socket.getOutputStream(), true);
                BufferedReader in2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
                PrintWriter out2 = new PrintWriter(player2Socket.getOutputStream(), true)
        ) {
            boolean playAgain = true; // Controls the game loop

            while (playAgain) {
                gameLogic = new GameLogic(); // Reset game logic for a new round
                out1.println("New game started! Player 1, start guessing.");
                out2.println("New game started! Player 2, start guessing.");

                boolean guessedCorrectly = false;
                while (!guessedCorrectly) {
                    int currentPlayer = gameLogic.getCurrentPlayer();
                    Socket currentSocket = (currentPlayer == 1) ? player1Socket : player2Socket;
                    PrintWriter currentOut = (currentPlayer == 1) ? out1 : out2;
                    BufferedReader currentIn = (currentPlayer == 1) ? in1 : in2;

                    currentOut.println("Your turn to guess:");
                    String guessStr = currentIn.readLine();
                    try {
                        int guess = gameLogic.validateGuess(guessStr);
                        guessedCorrectly = gameLogic.checkGuessCorrectness(guess);
                        String prefix = gameLogic.generatePrefix(guess);

                        if (guessedCorrectly) {
                            currentOut.println(prefix + " Congratulations! You guessed correctly!");
                            if (currentPlayer == 1) {
                                out2.println("Player 1 guessed correctly! Game over.");
                            } else {
                                out1.println("Player 2 guessed correctly! Game over.");
                            }
                        } else {
                            currentOut.println(prefix + " Incorrect guess. Try again.");
                            if (currentPlayer == 1) {
                                out2.println("Player 1 guessed: " + guess);
                            } else {
                                out1.println("Player 2 guessed: " + guess);
                            }
                        }
                        gameLogic.updateState();
                    } catch (IllegalArgumentException e) {
                        currentOut.println(e.getMessage());
                    }
                }

                // Ask both players if they want to play again
                out1.println("Do you want to play again? (yes/no)");
                out2.println("Do you want to play again? (yes/no)");

                String response1 = in1.readLine().trim().toLowerCase();
                String response2 = in2.readLine().trim().toLowerCase();

                if (!response1.equals("yes") || !response2.equals("yes")) {
                    playAgain = false;
                    out1.println("Thanks for playing!");
                    out2.println("Thanks for playing!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                player1Socket.close();
                player2Socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
