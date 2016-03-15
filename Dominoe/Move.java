package dominoe;

/**
 * Created by Aliaksei Semchankau on 10.08.2015.
 */
public class Move implements Comparable{

    private int x;
    private int y;

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Object o) {
        Move move = (Move)o;
        if (x < move.getX()) {
            return -1;
        }
        if (x > move.getX()) {
            return 1;
        }
        if (y < move.getY()) {
            return -1;
        }
        if (y > move.getY()) {
            return 1;
        }
        return 0;
    }

    @Override
    public Move clone() {
        Move move = new Move(x, y);
        return move;
    }

}
