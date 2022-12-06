public class HardAutoPlayer extends BotPlayer {
    @Override
    protected double calculateFunc(Move move, Position position) {
        try{
            double ans = new EasyAutoPlayer().calculateFunc(move, position); /// считаем обычную функцию
            double costOfTheMostDangerousTurn = position.checkTurnDanger(move);
            //System.out.println("Самый опасный ход противника стоит:" + costOfTheMostDangerousTurn);
            ans -= costOfTheMostDangerousTurn; /// вычитаем самый опасный ход противника
            //System.out.println(move + " стоит " + ans);}
            return ans;
        } catch (Exception ex) {
            System.out.println("Hard bot was broken with trying to check " + move + "((");
        }
        return -10;
    }
}
