public class VariableBoard extends ReversyBoard {
    VariableBoard(Cell[][] field) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.field[i][j] = field[i][j];
            }
        }
    }

    public void getVariableField(Move[] moves) {
        for (Move el : moves) {
            field[el.getRow()][el.getCol()] = Cell.POSSIBLE_TURN;
        }
    }

}
