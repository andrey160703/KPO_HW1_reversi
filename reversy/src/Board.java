public interface Board {

    Cell[][] getField();

    Position getPosition();

    void makeMove(Move move);

    GameResult getWinner();

    void changePlayersTurn();

    int cellToInt(Cell cell);

    boolean canMakeTurn(int num);

}