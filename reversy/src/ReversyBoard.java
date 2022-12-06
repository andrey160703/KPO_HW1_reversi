import java.util.*;

public class ReversyBoard implements Board, Position{
    protected static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.EMPTY, "\u001B[46m  ",
            Cell.BLACK, "\u001B[40m ▫",
            Cell.WHITE, "\u001B[47m ▪",
            Cell.POSSIBLE_TURN, "\u001B[43m ?" // поставить фишку
    );

    protected final Cell[][] field;

    private int turnNumber = 0;

    private final ArrayList<Cell[][]> boardHistory = new ArrayList<>(70);

    protected final int n = 8;

    private Cell turn;

    public ReversyBoard() {
        field = new Cell[8][8];
        try {
            for (Cell[] row : field) {
                Arrays.fill(row, Cell.EMPTY);
            }
            field[3][3] = Cell.WHITE;
            field[4][4] = Cell.WHITE;
            field[3][4] = Cell.BLACK;
            field[4][3] = Cell.BLACK;
            turn = Cell.BLACK;
            for (int i = 0; i < n * n; i++) {
                boardHistory.add(new Cell[n][n]);
            }
            boardHistory.set(turnNumber, getField());
        } catch (Exception ex) {
                System.out.println(ex.getMessage());
        }
    }

    @Override
    public GameResult getWinner() {
        int white = 0, black = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0;j < n; j++) {
                if (field[i][j] == Cell.BLACK) {
                    black++;
                }
                if (field[i][j] == Cell.WHITE) {
                    white++;
                }
            }
        }
        System.out.println("Game score: " + black + " : " + white);
        if (black > white) {
            return GameResult.BLACK_WIN;
        }
        if (black < white) {
            return GameResult.WHITE_WIN;
        }
        return GameResult.DRAW;
    }

    @Override
    public void changePlayersTurn() {
        turn = turn == Cell.BLACK ? Cell.WHITE : Cell.BLACK;
    }

    @Override
    public Cell[][] getField() {
        Cell[][] result = new Cell[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = field[i][j];
            }
        }
        return result;
    }

    @Override
    public double func(int i, int j, int increaseI, int increaseJ) { /// считает ценность закрываемых ячеек
        double result = 0;
        boolean isGood = false;
        try {
            if (3 - cellToInt(field[i + increaseI][j + increaseJ]) == getIntTurn()) { /// если в направлении следущая ячейка другого цвета
                isGood = true;
            }
            if (isGood) {
                for (int l = i + increaseI, k = j + increaseJ; l < n && k < n && l >= 0 && k >= 0; l += increaseI, k += increaseJ) {
                    if (cellToInt(field[l][k]) == getIntTurn()) { /// встретили свой цвет - закончили
                        return result;
                    } else if (cellToInt(field[l][k]) == 0) { /// встретили пустоту - проиграли
                        return 0;
                    } else {
                        if (l == n - 1 || k == n - 1 || l == 0 || k == 0) { /// закрываем ячейку - считаем ответ
                            result += 2;
                        } else {
                            result++;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            isGood = false;
        }
        return 0; /// если мы не вернулись в свой цвет - закрасили 0 ячеек - ответ - 0
    }

    @Override
    public boolean getPreviousTurn(int num) {
        if (turnNumber - num >= 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    field[i][j] = boardHistory.get(turnNumber - num)[i][j];
                }
            }
            turnNumber -= num;
            if (num % 2 == 1) {
                changePlayersTurn();
            }
            return true;
        }
        return false;
    }

    @Override
    public double checkTurnDanger(Move move) {
        //System.out.println("------------------------------------");
        //System.out.println("Поле до хода:");
        //System.out.println(this);
        if (isValid(move)) {
            makeMove(move); /// пробуем сделать ход
        } else {
            return 100000; // если тяжко, возвращаем много чтобы при сравнении число было отрицательным
        }
        //System.out.println(move);
        //System.out.println("Поле после хода:");
        //System.out.println(this);
        //System.out.println("------------------------------------");
        EasyAutoPlayer player = new EasyAutoPlayer(); /// создаем игрока который по легкой формуле сходит
        Move possibleMove = player.makeMove(getPosition()); /// находим самый опасный ход
        double result = player.calculateFunc(possibleMove, getPosition()); /// вычисляем опасность хода
        getPreviousTurn(1); /// возвращаем доску обратно на 1 ход
        return result;
    }

    @Override
    public Position getPosition() {
        return this;
    }

    private void changeField(int i, int j, int increaseI, int increaseJ, int num) {
        boolean isGood = false;
        try{
            if (3 - cellToInt(field[i + increaseI][j + increaseJ]) == num) {
                isGood = true;
            }
            if (isGood) {
                for (int l = i + 2 * increaseI, k = j + 2 * increaseJ; l < n && k < n && l >= 0 && k >= 0; l += increaseI, k += increaseJ) {
                    if (cellToInt(field[l][k]) == 0) {
                        break;
                    }
                    if (cellToInt(field[l][k]) == num) {
                        while (l != i || k != j) {
                            //System.out.println("Change color: " + l + " " + k);
                            field[l][k] = turn;
                            l -= increaseI;
                            k -= increaseJ;
                        }
                        field[l][k] = turn;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            isGood = false;
        }
    }

    @Override
    public void makeMove(Move move) {
        for (int l = -1; l < 2; l++) {
            for (int k = -1; k < 2; k++) {
                if (l == k && k == 0) {
                    continue;
                }
                changeField(move.getRow(), move.getCol(), l, k, getIntTurn());
            }
        }
        field[move.getRow()][move.getCol()] = move.getValue();
        changePlayersTurn();
        turnNumber++;
        boardHistory.set(turnNumber, getField());
    }


    public int cellToInt(Cell color) {
        return switch (color) {
            case WHITE -> 2;
            case BLACK -> 1;
            default -> 0;
        };
    }

    private Move getMove(int i, int j, int increaseI, int increaseJ, int num) { /// иду от цвета к пустой
        boolean isGood = false;
        try{
            if (3 - cellToInt(field[i + increaseI][j + increaseJ]) == num) { /// первый цвет по направлению отличается
                isGood = true;
            }
            if (isGood) {
                for (int l = i + 2 * increaseI, k = j + 2 * increaseJ; l < n && k < n && l >= 0 && k >= 0; l += increaseI, k += increaseJ) {
                    //System.out.println("" + i + " " + j + " " + l + " " + k + " " + increaseI + " " + increaseJ);
                    if (cellToInt(field[l][k]) == 0) { /// встретили пустоту - можем сюда сходить
                        return new Move(l, k, field[l][k]);
                    }
                    if (cellToInt(field[l][k]) == num) { /// встретили свой же цвет - не можем
                        return null;
                    }
                }
            }
        } catch (Exception ex) {
            isGood = false;
        }
        return null;
    }

    @Override
    public int getIntTurn() {
        return turn == Cell.BLACK ? 1 : 2;
    }

    @Override
    public boolean canMakeTurn(int num) {
        return getPOSSIBLE_TURNs(num).length > 0;
    }

    @Override
    public Cell getTurn() {
        return turn;
    }

    @Override
    public boolean isValid(Move move) {
        //System.out.println("123123123123");
        if (move.getRow() >= n || move.getRow() < 0 || move.getCol() >= n || move.getCol() < 0) {
            return false;
        }
        Move[] moves = getPOSSIBLE_TURNs(getIntTurn());
        //System.out.println("Check:");
        for (Move value : moves) {
            //System.out.println("" + value.getCol() + " != " + move.getCol() + " && " + value.getRow() + " != " + move.getRow());
            if (value.getCol() == move.getCol() && value.getRow() == move.getRow()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Move[] getPOSSIBLE_TURNs(int num) {
        Move[] result = new Move[0];
        try {
            Cell currentColor = num == 1 ? Cell.BLACK : Cell.WHITE; //кто ходит
            //System.out.println(num);
            Set<Move> s = new HashSet<Move>();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (field[i][j] == currentColor) {
                        for (int l = -1; l < 2; l++) {
                            for (int k = -1; k < 2; k++) {
                                if (l == k && k == 0) {
                                    continue;
                                }
                                Move curr = getMove(i, j, l, k, num);
                                if (curr != null) {
                                    //System.out.println("added" + s.size());
                                    s.add(new Move(curr.getRow(), curr.getCol(), currentColor));
                                }
                            }
                        }
                    }
                }
            }
            result = new Move[s.size()];
            int ch = 0;
            for (Move el : s) {
                result[ch] = el;
                //System.out.println("" + ch + "Test " + result[ch] + " endTest");
                ch++;
            }
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            result = new Move[0];
        }
        return result;
    }

    @Override
    public VariableBoard getVariableBoard(Move[] moves) {
        Cell[][] newField = new Cell[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newField[i][j] = field[i][j];
            }
        }
        return new VariableBoard(newField);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("   ");
        for (int r = 0; r < n; r++) {
            sb.append(r).append("  ");
        }
        sb.append("\n");
        for (int r = 0; r < n; r++) {
            sb.append(r).append(" ");
            for (Cell cell : field[r]) {
                sb.append(CELL_TO_STRING.get(cell)).append(" ");
            }
            sb.append("\u001B[0m");
            sb.append(System.lineSeparator());
        }
        sb.setLength(sb.length() - System.lineSeparator().length());
        return sb.toString();
    }
}
