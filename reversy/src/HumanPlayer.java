import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HumanPlayer extends Player {

    private boolean cancelTurn(Position position) {
        return position.getPreviousTurn(2);
    }

    @Override
    Move makeMove(Position position) {
        try{
            //System.out.println(board.getPosition().getIntTurn());
            Move[] moves = position.getPOSSIBLE_TURNs(position.getIntTurn());
            //System.out.println("Available positions:");
            VariableBoard changeBord = position.getVariableBoard(moves);
            changeBord.getVariableField(moves);
            //for (Move el : moves) {
              //  System.out.println(el);
            //}
            System.out.println("Make turn!");
            System.out.println(changeBord);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        Move result = null;
        System.out.println("Enter the coordinates of the new point; \nTo cancel your last turn enter -1 -1:");
        Scanner sc = new Scanner(System.in);
        while (true) {
            try{

                int i = sc.nextInt();
                int j = sc.nextInt();
                if (i == -1 && j == -1) {
                    if (cancelTurn(position)) {
                        System.out.println("Turn canceled:");
                        return makeMove(position);
                    } else {
                        System.out.println("Can't canceled turn");
                    }
                }
                result = new Move(i, j, position.getTurn());
                if (!position.isValid(result)) {
                    throw new NumberFormatException();
                } else {
                    return result;
                }
            } catch (NumberFormatException e) {
                System.out.println("try again:");
                sc = new Scanner(System.in);
            } catch (InputMismatchException e) {
                System.out.println("try again:");
                sc = new Scanner(System.in);
            } catch (NoSuchElementException e) {
                System.out.println("try again:");
                sc = new Scanner(System.in);
            }
        }
    }
}
