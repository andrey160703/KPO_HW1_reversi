public class Game {
    private final Board board;
    private final Player player1;
    private final Player player2;

    public Game(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
    }

    public GameResult play() {
        try {
            int turn = 0;
            while (board.canMakeTurn(1) || board.canMakeTurn(2)) {
                if (turn % 2 == 0) {
                    makeMove(player1, 1);
                } else {
                    makeMove(player2, 2);
                }
                turn++;
            }
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return board.getWinner();
    }

    private void makeMove(Player player, int num) {
        try {
            if (board.canMakeTurn(num)) {
                final Move move = player.makeMove(board.getPosition());
                board.makeMove(move);
                System.out.println();
                System.out.println("Player: " + num);
                System.out.println(move);
                System.out.println(board);
            } else {
                board.changePlayersTurn();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
