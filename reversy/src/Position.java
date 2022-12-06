public interface Position {
    Cell getTurn(); /// whose turn?

    int getIntTurn();

    boolean isValid(Move move); /// is move correct?

    Move[] getPOSSIBLE_TURNs(int num);

    VariableBoard getVariableBoard(Move[] moves);

    double func(int i, int j, int increaseI, int increaseJ);

    boolean getPreviousTurn(int num);

    double checkTurnDanger(Move move);

}