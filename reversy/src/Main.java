import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static int chooseGameMode() {
        System.out.println("Enter the game mod: \n 1 - Play with computer; \n 2 - Play with friend; \n 3 - End game; \n");
        int gameMode;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                gameMode = sc.nextInt();
                if (gameMode < 1 || gameMode > 3) {
                    throw new NumberFormatException();
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("try again:");
            } catch (InputMismatchException e) {
                System.out.println("try again:");
            } catch (NoSuchElementException e) {
                System.out.println("try again:");
            }
        }
        return gameMode;
    }

    private static int chooseTheFirstTurn() {
        System.out.println("Choose your turn: \n 1 - to make the first turn \n 2 - to give the first turn to the computer \n 3 - to go to beginning; \n");
        int difficulty;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                difficulty = sc.nextInt();
                if (difficulty < 1 || difficulty > 3) {
                    throw new NumberFormatException();
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("try again:");
            } catch (InputMismatchException e) {
                System.out.println("try again:");
            } catch (NoSuchElementException e) {
                System.out.println("try again:");
            }
        }
        return difficulty;
    }

    private static int chooseBotDifficulty() {
        System.out.println("Choose the bot difficulty: \n 1 - to play with easy bot; \n 2 - to play with hard bot; \n 3 - to go to beginning; \n");
        int turn;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                turn = sc.nextInt();
                if (turn < 1 || turn > 3) {
                    throw new NumberFormatException();
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("try again:");
            } catch (InputMismatchException e) {
                System.out.println("try again:");
            } catch (NoSuchElementException e) {
                System.out.println("try again:");
            }
        }
        return turn;
    }

    public static void main(String[] args) {
        while (true) {
            int win1 = 0, win2 = 0, draw = 0;
            int gameMode = chooseGameMode();
            switch (gameMode) {
                case 1:
                    Player bot;
                    int botDifficulty = chooseBotDifficulty();
                    if (botDifficulty == 1) {
                        bot = new EasyAutoPlayer();
                    } else if (botDifficulty == 2) {
                        bot = new HardAutoPlayer();
                    } else {
                        break;
                    }
                    while (true) {
                        int turn = chooseTheFirstTurn();
                        final GameResult result;
                        if (turn == 1) {
                            result = new Game(new ReversyBoard(), new HumanPlayer(), bot).play();
                        } else if (turn == 2) {
                            result = new Game(new ReversyBoard(), bot, new HumanPlayer()).play();
                        } else {
                           break;
                        }
                        switch (result) {
                            case BLACK_WIN -> {
                                if (turn == 1) {
                                    win1++;
                                } else {
                                    win2++;
                                }
                            }
                            case WHITE_WIN -> {
                                if (turn == 1) {
                                    win2++;
                                } else {
                                    win1++;
                                }
                            }
                            case DRAW -> {
                                draw++;
                            }
                        }
                        System.out.println("Total score: " + win1 + " : " + win2 + ", total draws: " + draw + ".");
                    }
                    System.out.println("The game is completed with the score:\nFirst player: " + win1 + "\nSecond player: " + win2 + "\nDraws: " + draw + "\n");
                    break;
                case 2:
                    while (true) {
                        int command = chooseCommand();
                        final GameResult result;
                        if (command == 1) {
                            result = new Game(new ReversyBoard(), new HumanPlayer(), new HumanPlayer()).play();
                        } else {
                            break;
                        }
                        switch (result) {
                            case BLACK_WIN -> System.out.println("Black won!");
                            case WHITE_WIN -> System.out.println("White won!");
                            case DRAW -> System.out.println("Draw!");
                        }
                    }
                    break;
                case 3:
                    return;
            }
        }
    }

    private static int chooseCommand() {
        System.out.println("Choose command: \n 1 - to begin new game \n 2 - to go to beginning; \n");
        int command = 2;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                command = sc.nextInt();
                if (command < 1 || command > 2) {
                    throw new NumberFormatException();
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("try again:");
            } catch (InputMismatchException e) {
                System.out.println("try again:");
            } catch (NoSuchElementException e) {
                System.out.println("try again:");
            }
        }
        return command;
    }
}