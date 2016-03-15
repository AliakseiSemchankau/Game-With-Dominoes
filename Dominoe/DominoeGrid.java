package dominoe;

/**
 * Created by Aliaksei Semchankau on 12.08.2015.
 */

public class DominoeGrid {

    public int width;
    public int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private boolean[][] table;



    public DominoeGrid(int width, int height) {
        this.width = width;
        this.height = height;
        table = new boolean[width][height];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                table[i][j] = false;
            }
        }
    }

    public void setCell(int i, int j, boolean flag) {
        if (i < 0 || i >= width) {
            return;
        }
        if (j < 0 || j >= height) {
            return;
        }

        table[i][j] = flag;
    }

    boolean getCell(int i, int j) {
        return table[i][j];
    }

}

