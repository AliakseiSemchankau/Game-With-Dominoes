package dominoe;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.*;
import java.util.List;

/**
 * Created by Aliaksei Semchankau on 10.08.2015.
 */

/**
 * This class constructs graphs for DominoeGrid and performs different useful operations for that
 */
public class Graph {

    private int matrixSize;
    public int[][] matrix = null;

    int width;
    int height;

    private boolean[][] gridMatrix = null;

    private Map<Integer, Move> blackCells = new TreeMap<Integer, Move>();
    private Map<Integer, Move> whiteCells = new TreeMap<Integer, Move>();

    private Map<Move, Integer> blackList = new TreeMap<Move, Integer>();
    private Map<Move, Integer> whiteList = new TreeMap<Move, Integer>();

    private List<Move> listOfSquares = new ArrayList<Move>();

    public Graph(final DominoeGrid grid) {
        width = grid.getWidth();
        height = grid.getHeight();
        gridMatrix = new boolean[width][height];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                gridMatrix[i][j] = grid.getCell(i, j);
            }
        }
    }

    public boolean constructMatrix(final int width, final int height) {

        int blacksCount = -1;
        int whiteCount = -1;

        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                if (!gridMatrix[i][j]) {
                    continue;
                }
                if ((i + j) % 2 == 0) {
                    ++blacksCount;
                    blackCells.put(blacksCount, new Move(i, j));
                    blackList.put(new Move(i, j), blacksCount);
                } else {
                    ++whiteCount;
                    whiteCells.put(whiteCount, new Move(i, j));
                    whiteList.put(new Move(i, j), whiteCount);
                }
            }
        }

        if (blacksCount != whiteCount) {
            return false;
        }

        matrixSize = blacksCount + 1;

        matrix = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; ++i) {
            for (int j = 0; j < matrixSize; ++j) {
                matrix[i][j] = 0;
            }
        }

        for (int i = 0; i < matrixSize; ++i)
            for (int j = 0; j < matrixSize; ++j) {
                Move blackCell = blackCells.get(new Integer(i));
                Move whiteCell = whiteCells.get(new Integer(j));
                if (Math.abs(blackCell.getX() - whiteCell.getX())
                        + Math.abs(blackCell.getY() - whiteCell.getY()) == 1) {
                    //System.out.println("i, j = " + i + " " + j);
                    //System.out.println(blackCell.getX() + " " + blackCell.getY());
                    //System.out.println(whiteCell.getX() + " " + whiteCell.getY());
                    matrix[i][j] = 1;
                }
            }

        cutIntoSquares();
        makeColors(width, height);

        return true;
    }

    private void cutIntoSquares() {

        for (int i = 0; i < width - 1; ++i) {
            for (int j = 0; j < height - 1; ++j) {
                if (gridMatrix[i][j]
                        && gridMatrix[i + 1][j]
                        && gridMatrix[i][j + 1]
                        && gridMatrix[i + 1][j + 1]) {
                    listOfSquares.add(new Move(i, j));
                }
            }
        }
    }

    private void makeColors(final int width, final int height) {

        Vector<Vector<Integer>> columns = new Vector<Vector<Integer>>();

        for (int cur = 0; cur < width + height; ++cur) {
            columns.add(new Vector<Integer>());
        }

        for (Move move : listOfSquares) {
            //cout << "square: " << square.first << " " << square.second << "\n";
            columns.get(move.getX()).add(move.getY());
        }

        for (int cur = 0; cur < columns.size(); ++cur) {
            Collections.sort(columns.get(cur));
        }

        for (int i = 0; i < columns.size(); ++i) {
            int prev = -3;
            boolean flag = false;
            for (int j : columns.get(i)) {
                //System.out.println("squares, i, j = " + i + " " + j);
                if (j != prev + 1) {
                    flag = false;
                }
                if (flag) {
                    colorEdge(new Move(i, j), new Move(i + 1, j), 1);
                    colorEdge(new Move(i, j + 1), new Move(i + 1, j + 1), -1);
                    //edgeColor[mp(mp(j, i), mp(j, i + 1))] = 1;
                    flag = false;
                } else {
                    //System.out.println("mineus! " + i + " " + j);
                    colorEdge(new Move(i, j + 1), new Move(i + 1, j + 1), 1);
                    colorEdge(new Move(i, j), new Move(i + 1, j), -1);
                    //edgeColor[mp(mp(i, j), mp(i, j + 1))] = -1;
                    flag = true;
                }
                colorEdge(new Move(i, j), new Move(i, j + 1), 1);

                colorEdge(new Move(i + 1, j), new Move(i + 1, j + 1), 1);

                //edgeColor[mp(mp(i, j), mp(i + 1, j))] = 1;
                //edgeColor[mp(mp(i + 1, j), mp(i + 1, j + 1))] = 1;
                //edgeColor[mp(mp(i, j + 1), mp(i + 1, j + 1))] = 1;
                prev = j;
            }
        }
    }

    private void colorEdge(Move move1, Move move2, int color) {

        if ((move1.getX() + move1.getY()) % 2 != 0) {
            Move tmp = move1;
            move1 = move2;
            move2 = tmp;
        }

        // System.out.println("move1 : " + move1.getX() + " " + move1.getY());
        // System.out.println("move2 : " + move2.getX() + " " + move2.getY());

        int blackIndex = blackList.get(move1);
        int whiteIndex = whiteList.get(move2);

        matrix[blackIndex][whiteIndex] = color;

    }

    public BigInteger countDeterminant() {

       /* System.out.println("{");
        for (int i = 0; i < matrixSize; ++i) {
            System.out.print("{ ");
            for (int j = 0; j < matrixSize - 1; ++j) {
                System.out.print(matrix[i][j] + ", ");
            }
            System.out.println(matrix[i][matrixSize - 1] + " },");
        }
        System.out.println("}");*/

        return UsefulFunctions.determinant(matrix);
    }

    public RandomPartition takeRandomPartition() {
        return new RandomPartition();
    }

    public class RandomPartition {

        LoadingFrame loadingFrame = null;
        private List<Move> verticalDomino = null;
        private List<Move> horizontalDomino = null;
        Map<Integer, BigInteger> variants = null;

        private void constructVariants(final int[][] matr, final int row, boolean[] usedRow, boolean[] usedColumn, BigInteger bigDeterminant) {

            variants = new TreeMap<Integer, BigInteger>();
            int mSize = matr.length;
            Vector<Thread> threads = new Vector<Thread>();

            int lastNonZero = mSize - 1;
            while (lastNonZero >= 0 && matr[row][lastNonZero] == 0) {
                --lastNonZero;
            }
            if (lastNonZero < 0 || bigDeterminant.equals(UsefulFunctions.NOTHING)){
                lastNonZero = mSize;
            }



            for (int column = 0; column < mSize; ++column) {
                if (usedColumn[column] || matr[row][column] == 0 || column == lastNonZero) {
                    continue;
                }
                //System.out.println("column = " + column);
                usedColumn[column] = true;
                final int[][] subMatrix = UsefulFunctions.substruct(matr, usedRow, usedColumn);
                usedColumn[column] = false;
                final Integer  curColumn = new Integer(column);
                Thread detCalculateThread = new Thread(new Runnable() {

                    private int[][] subMatr = subMatrix.clone();
                    private BigInteger det = null;

                    @Override
                    public void run() {
                        BigInteger det = UsefulFunctions.determinant(subMatr);
                        //System.out.println("det = " + det);
                        synchronized (variants) {
                            variants.put(curColumn, det);
                        }
                    }
                });
                threads.add(detCalculateThread);
                detCalculateThread.start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException iexc) {
                    JOptionPane.showMessageDialog(null, "Something has interrupted calculating determinant");
                }
            }

            if (lastNonZero == mSize) {
                return;
            }

            BigInteger remainingDeterminant = bigDeterminant;
            for (Map.Entry<Integer, BigInteger> entry : variants.entrySet()) {
                remainingDeterminant = remainingDeterminant.subtract(entry.getValue());
            }
            variants.put(lastNonZero, remainingDeterminant);
/*
            System.out.println("bigDet = " + bigDeterminant);

           for (Map.Entry<Integer, BigInteger> entry : variants.entrySet()) {
                System.out.print(entry.getValue() + " ");
            }
            System.out.println(" ");
*/
        }

        private Vector<Move> divideAndConqueror(final int[][] matr) {
            Vector<Move> matching = new Vector<Move>();
            int mSize = matr.length;

            if (mSize < 30) {
                matching = hard(matr);
                return matching;
            }

            boolean[] usedColumn = new boolean[mSize];
            boolean[] usedRow = new boolean[mSize];

            for (int i = 0; i < mSize; ++i) {
                usedColumn[i] = false;
                usedRow[i] = false;
            }

            int low = mSize / 2 - 7;
            int high = mSize / 2 + 7;

            BigInteger bigDeterminant = UsefulFunctions.determinant(matr);

            for (int row = low; row < high; ++row) {
                System.out.println(row + " / " + mSize);
                loadingFrame.increment();

               usedRow[row] = true;
               constructVariants(matr, row, usedRow, usedColumn, bigDeterminant);

/*
                usedRow[row] = true;
                 variants = new TreeMap<Integer, BigInteger>();
                for (int column = 0; column < mSize; ++column) {
                    if (usedColumn[column] || matr[row][column] == 0) {
                        continue;
                    }
                    usedColumn[column] = true;
                    variants.put(new Integer(column), UsefulFunctions.determinant(matr, usedRow, usedColumn));
                    usedColumn[column] = false;
                }
*/

                int theMostInterestingColumn = UsefulFunctions.chooseRandom(variants);
                // System.out.println("the most interesting column = " + theMostInterestingColumn);
                matching.add(new Move(row, theMostInterestingColumn));
                usedColumn[theMostInterestingColumn] = true;
                bigDeterminant = variants.get(theMostInterestingColumn);
            }



            int[] mapa = UsefulFunctions.reduce(usedColumn);
            int[][] substrMatr = UsefulFunctions.substruct(matr, usedRow, usedColumn);

            if (UsefulFunctions.bipatrite(substrMatr, low)) {
                System.out.println("reduced");
                int[][] matr1 = UsefulFunctions.substruct(substrMatr, 0, low);
                int[][] matr2 = UsefulFunctions.substruct(substrMatr, low, substrMatr.length);
                //Vector<Move> matching1 = hard(matr1);
                //Vector<Move> matching2 = hard(matr2);
                Vector<Move> matching1 = divideAndConqueror(matr1);
                Vector<Move> matching2 = divideAndConqueror(matr2);

                Vector<Move> finalMatching = new Vector<Move>();
                for (Move move : matching1) {
                    int incKey = move.getX();
                    int incVal = mapa[move.getY()];
                    finalMatching.add(new Move(incKey, incVal));
                   // System.out.print(incKey + "|" + incVal + " ");
                }
                System.out.println(" ");
                for (Move move : matching) {
                    finalMatching.add(move);
                   // System.out.print(move.getX() + "|" + move.getY() + " ");
                }
                System.out.println(" ");
                for (Move move : matching2) {
                    int incKey = move.getX() + high;
                    int incVal = mapa[move.getY() + low];
                    finalMatching.add(new Move(incKey, incVal));
                   // System.out.print(incKey + "|" + incVal + " ");
                }
               // System.out.println(" ");
                return finalMatching;
            } else {
                System.out.println("fail");
                Vector<Move> submatching = divideAndConqueror(substrMatr);
                Vector<Move> finalMatching = new Vector<Move>();

                for (Move move : submatching) {
                    int incKey = move.getX();
                    if (incKey >= low)
                        incKey += (high - low);
                    int incVal = mapa[move.getY()];
                    finalMatching.add(new Move(incKey, incVal));
                   // System.out.print(incKey + "|" + incVal + " ");
                }
                for (Move move : matching) {
                    finalMatching.add(move);
                }

                return finalMatching;

            }

        }

        private Vector<Move> hard(final int[][] matr) {
            int mSize = matr.length;
            Vector<Move> matching = new Vector<Move>();
            boolean[] usedColumn = new boolean[mSize];
            boolean[] usedRow = new boolean[mSize];

            for (int i = 0; i < mSize; ++i) {
                usedColumn[i] = false;
                usedRow[i] = false;
            }

            for (int row = 0; row < mSize; ++row) {
                System.out.println(row + " / " + mSize);
                loadingFrame.increment();

                usedRow[row] = true;
                constructVariants(matr, row, usedRow, usedColumn, UsefulFunctions.NOTHING);

/*
                usedRow[row] = true;
                 variants = new TreeMap<Integer, BigInteger>();
                for (int column = 0; column < mSize; ++column) {
                    if (usedColumn[column] || matr[row][column] == 0) {
                        continue;
                    }
                    usedColumn[column] = true;
                    variants.put(new Integer(column), UsefulFunctions.determinant(matr, usedRow, usedColumn));
                    usedColumn[column] = false;
                }
*/

                int theMostInterestingColumn = UsefulFunctions.chooseRandom(variants);
                // System.out.println("the most interesting column = " + theMostInterestingColumn);
                matching.add(new Move(row, theMostInterestingColumn));
                usedColumn[theMostInterestingColumn] = true;
            }

            return matching;
        }

        private RandomPartition() {
            loadingFrame = new LoadingFrame(matrixSize);
            Vector<Move> matching = divideAndConqueror(matrix);
            //Vector<Move> matching = hard(matrix);
            loadingFrame.setVisible(false);
            constructLists(matching);
        }



        private void constructLists(final Vector<Move> matching) {

            //   for (Move move : matching) {
            //      System.out.println(move.getX() + " " + move.getY() + " - dominoe");
            //   }

            verticalDomino = new ArrayList<Move>();
            horizontalDomino = new ArrayList<Move>();

            for (Move move : matching) {
                Move firstCell = blackCells.get(move.getX()).clone();
                Move secondCell = whiteCells.get(move.getY()).clone();
                if (firstCell.compareTo(secondCell) > 0) {
                    Move tmp = firstCell;
                    firstCell = secondCell;
                    secondCell = tmp;
                }
                if (secondCell.getX() == firstCell.getX()) {
                    verticalDomino.add(firstCell);
                } else {
                    horizontalDomino.add(firstCell);
                }
            }

          /*  System.out.println("verticals");
            for (Move move : verticalDomino) {
                System.out.println(move.getX() + " " + move.getY());
            }
            System.out.println(" ");

            System.out.println("horizontals");
            for (Move move : horizontalDomino) {
                System.out.println(move.getX() + " " + move.getY());
            }
            System.out.println(" ");*/

        }

        public List<Move> takeHorizontals() {
            return horizontalDomino;
        }

        public List<Move> takeVerticals() {
            return verticalDomino;
        }

        private class LoadingFrame extends JFrame {

            private int loaded = 0;
            private int length;

            private LoadingPanel loadingPanel = null;

            public void increment() {
                ++loaded;
                loadingPanel.setLoaded(loaded);
            }

            LoadingFrame(final int length) {

                this.length = length;

                loadingPanel = new LoadingPanel(length);
                add(loadingPanel);
                setResizable(false);

                pack();
                setVisible(true);

            }

            private class LoadingPanel extends JPanel {

                private int width = 750;
                private int height = 70;

                private boolean flag = false;
                private int length;

                private int loaded;

                public void setLoaded(final int x) {
                    loaded = x;
                    paintComponent(this.getGraphics());
                }

                public LoadingPanel(final int length) {
                    this.length = length;
                    setBackground(Color.BLACK);
                    //width = 5*length;
                    repaint();
                }

                @Override
                public void paintComponent(Graphics g) {

                    if (!flag) {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, width + 200, height + 200);
                        flag = true;
                    }
                    g.setColor(Color.GREEN);
                    g.fillRect(0, 0, (loaded * width) / length, height);

                }

                @Override
                public Dimension getPreferredSize() {

                    Insets b = getInsets();
                    return new Dimension(width + b.left + b.right,
                            height + b.top + b.bottom - 20);

                }

            }

        }

    }

}
