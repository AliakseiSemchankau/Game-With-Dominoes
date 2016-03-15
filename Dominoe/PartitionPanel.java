package dominoe;

import javax.jws.soap.SOAPBinding;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Aliaksei Semchankau on 12.08.2015.
 */

public class PartitionPanel extends JPanel {

    private int width;
    private int height;
    private int cellGap = 1;
    private int cellSize = 47;
    private Color c0 = new Color(0x505050);
    private Color c1 = new Color(0xFFFFFF);
    private Color horColor = Color.GREEN;
    private Color verColor = Color.RED;

    java.util.List<Move> horDominoes = null;
    java.util.List<Move> verDominoes = null;

    public PartitionPanel(final int width, final int height,
                          java.util.List<Move> horizontalDominoes, java.util.List<Move> verticalDominoes,
                          UsefulFunctions.countries countryCode, UsefulFunctions.sizes sizeStyle) {
        this.width = width;
        this.height = height;
        this.horDominoes = horizontalDominoes;
        this.verDominoes = verticalDominoes;
        //System.out.println("here!");
        setBackground(Color.BLACK);
        switch (countryCode) {
            case BLR:
               // c1 = Color.WHITE;
                horColor = Color.RED;
                verColor = Color.GREEN;
                break;
            case RUS:
               // c1 = Color.RED;
                horColor = Color.WHITE;
                verColor = Color.BLUE;
                break;
            case POL:
               //c1 = Color.BLUE;
                horColor = Color.WHITE;
                verColor = Color.RED;
                break;
            case UKR:
                horColor = Color.BLUE;
                verColor = Color.YELLOW;

        }

        switch (sizeStyle) {
            case SMALL: cellSize = UsefulFunctions.smallCell;
                break;
            case MIDDLE: cellSize = UsefulFunctions.middleCell;
                break;
            case LARGE: cellSize = UsefulFunctions.largeCell;
                break;
        }

    }

    @Override
    public Dimension getPreferredSize() {

        Insets b = getInsets();
        return new Dimension((cellSize + cellGap) * width + cellGap + b.left + b.right,
                (cellSize + cellGap) * height + cellGap + b.top + b.bottom);

    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);
        Insets b = getInsets();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                g.setColor(c0);
                g.fillRect(b.left + cellGap + x * (cellSize + cellGap), b.top + cellGap + y
                        * (cellSize + cellGap), cellSize, cellSize);
            }
        }

        for (Move move : horDominoes) {
            int x = move.getX();
            int y = move.getY();
            g.setColor(horColor);
            g.fillRect(b.left + cellGap + x * (cellSize + cellGap), b.top + cellGap + y
                    * (cellSize + cellGap), 2 * cellSize + 1, cellSize);
        }

        for (Move move : verDominoes) {
            int x = move.getX();
            int y = move.getY();
            g.setColor(verColor);
            g.fillRect(b.left + cellGap + x * (cellSize + cellGap), b.top + cellGap + y
                    * (cellSize + cellGap), cellSize, 2 * cellSize + 1);
        }


    }

}
