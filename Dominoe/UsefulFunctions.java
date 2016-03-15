package dominoe;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

/**
 * Created by Aliaksei Semchankau on 12.08.2015.
 */
public class UsefulFunctions {

    //static BigInteger prime = null;

    static Random rand = new Random();

    public final static int smallCell = 47;
    public final static int middleCell = 25;
    public final static int largeCell = 15;

    public final static int smallSize = 16;
    public final static int middleSize = 30;
    public final static int largeSize = 50;

    public final static BigInteger NOTHING = BigInteger.ONE.negate();

    public static enum countries {BLR, RUS, UKR, POL}

    ;

    public static enum sizes {SMALL, MIDDLE, LARGE}

    ;



    public static BigInteger determinant(final int[][] matr) {


        int n = matr.length;
        BigInteger[][] a = new BigInteger[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                a[i][j] = BigInteger.valueOf(matr[i][j]);
            }
        }


 /*
        System.out.println("matrix:");
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                System.out.print(matr[i][j] + " ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
   */


        BigInteger prime = BigInteger.probablePrime(n + 4, new Random());

        //System.out.println("prime = " + prime);

        BigInteger det = BigInteger.ONE;

        for (int row = 0; row < n; ++row) {
            int currentRow = row;
            while (currentRow < n && a[currentRow][row].equals(BigInteger.ZERO)) {
                ++currentRow;
            }
            if (currentRow == n) {
                return BigInteger.ZERO;
            }

            if (currentRow != row) {
                det = det.negate();
                BigInteger[] tmp = a[currentRow];
                a[currentRow] = a[row];
                a[row] = tmp;
            }

            BigInteger inverse = a[row][row].modInverse(prime);

            for (currentRow = row + 1; currentRow < n; ++currentRow) {
                if (a[currentRow][row].equals(BigInteger.ZERO)) {
                    continue;
                }
                BigInteger coefficient = a[currentRow][row].multiply(inverse).remainder(prime);
                for (int column = row; column < n; ++column) {
                    a[currentRow][column] = a[currentRow][column].subtract(a[row][column].multiply(coefficient).remainder(prime)).remainder(prime);
                }
            }
        }

        for (int i = 0; i < n; ++i) {
            det = det.multiply(a[i][i]).remainder(prime);
        }
        det = det.add(prime);
        det = det.remainder(prime);
        if (det.multiply(BigInteger.valueOf(2)).compareTo(prime) > 0) {
            det = prime.subtract(det).remainder(prime);
        }
        return det;
    }

    public static BigInteger determinant(final int[][] matr, final boolean[] usedRow, final boolean[] usedColumn) {
        return determinant(substruct(matr, usedRow, usedColumn));
    }

    public static int chooseRandom(final Map<Integer, BigInteger> variants) {

        Integer someKey = new Integer(-1);
        BigInteger sum = BigInteger.valueOf(0);
        for (Map.Entry<Integer, BigInteger> entry : variants.entrySet()) {
            //System.out.println(entry.getKey() + " " + entry.getValue() + "  - entry");
            sum = sum.add(entry.getValue());
        }
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        BigDecimal randValue = new BigDecimal(rand.nextDouble());
       //randValue = new BigDecimal(0.5);
        // System.out.println("randValue = " + randValue);
        randValue.setScale(10, BigDecimal.ROUND_HALF_UP);
        BigDecimal subSum = new BigDecimal(0.0);
        subSum.setScale(10, BigDecimal.ROUND_HALF_UP);
        for (Map.Entry<Integer, BigInteger> entry : variants.entrySet()) {
            BigDecimal num = new BigDecimal(entry.getValue());
            BigDecimal den = new BigDecimal(sum);
            BigDecimal frac = num.divide(den, 10, BigDecimal.ROUND_HALF_UP);
            subSum = subSum.add(frac);
            //System.out.println("subSum = " + subSum);
            if (subSum.compareTo(randValue) > 0) {
                //System.out.println("okey!!");
                someKey = entry.getKey();
                break;
            }
        }
        return someKey;
    }

    public static int[][] substruct(final int[][] matr, final boolean[] usedRow, final boolean[] usedColumn) {

        int usedInRow = 0;
        int usedInColumn = 0;

        for (int i = 0; i < usedRow.length; ++i) {
            if (usedRow[i]) {
                ++usedInRow;
            }
        }
        for (int i = 0; i < usedColumn.length; ++i) {
            if (usedColumn[i]) {
                ++usedInColumn;
            }
        }

        int[][] substructMatrix = new int[matr.length - usedInRow][matr.length - usedInColumn];

        int currentRow = 0;
        int oldRow = 0;

        while (currentRow < substructMatrix.length) {
            while (usedRow[oldRow]) {
                ++oldRow;
            }
            int currentColumn = 0;
            int oldColumn = 0;
            while (currentColumn < substructMatrix.length) {
                while (usedColumn[oldColumn]) {
                    ++oldColumn;
                }
                substructMatrix[currentRow][currentColumn] = matr[oldRow][oldColumn];
                ++oldColumn;
                ++currentColumn;
            }
            ++oldRow;
            ++currentRow;
        }

        return substructMatrix;

    }

    public static boolean bipatrite(final int[][] matr, final int delimeter) {
        for (int i = 0; i < delimeter; ++i)
            for (int j = delimeter; j < matr.length; ++j) {
                if (matr[i][j] != 0) {
                    return false;
                }
            }
        for (int j = 0; j < delimeter; ++j)
            for (int i = delimeter; i < matr.length; ++i) {
                if (matr[i][j] != 0) {
                    return false;
                }
            }
        return true;
    }

    public static int[][] substruct(int[][] matr, int low, int high) {

        int len = high - low;
        int[][] substr = new int[len][len];

        for (int i = low; i < high; ++i)
            for (int j = low; j < high; ++j) {
                substr[i - low][j - low] = matr[i][j];
            }

        return substr;

    }

    public static int[] reduce(boolean[] usedColumn) {

        int len = usedColumn.length;
        for (int i = 0; i < usedColumn.length; ++i)
            if (usedColumn[i])
                --len;

        int[] reduced = new int[len];

        int cur = 0;
        int i = -1;
        while (i < len - 1) {
            while (cur < usedColumn.length && usedColumn[cur])
                ++cur;
            ++i;
            reduced[i] = cur;
            ++cur;
        }

        return reduced;
    }

}
