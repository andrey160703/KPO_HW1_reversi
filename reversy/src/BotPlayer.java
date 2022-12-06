abstract public class BotPlayer extends Player {
    protected double calculateFunc(Move move, Position position) { // считает общую ценность хода
        double ans = 0;
        int r = move.getRow();
        int c = move.getCol();
        if (
                r == 7 && c == 7 ||
                r == 7 && c == 0 ||
                r == 0 && c == 7 ||
                r == 0 && c == 0
        ) {
            ans = 0.8;
        } else if (r == 7 || c == 7 || r == 0 || c == 0) {
            ans = 0.4;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(i == j && i == 0)) {
                    ans += position.func(move.getRow(), move.getCol(), i, j); /// считаем сумму всех закрываемых ячеек
                }
            }
        }
        return ans;
    }

    @Override
    Move makeMove(Position position) {
        Move[] moves = position.getPOSSIBLE_TURNs(position.getIntTurn());
        double mx = -100;
        int ansIndex = 0;
        for (int i = 0; i < moves.length; i++) {
            double func = calculateFunc(moves[i], position); //посчитать функцию для i го хода
            if (func > mx) {
                mx = func;
                ansIndex = i;
            }
//            if (func >= mx) {
//                System.out.println(moves[i] + " --- " + func);
//            }
        }
        return moves[ansIndex];
    }
}
