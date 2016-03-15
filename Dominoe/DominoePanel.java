package dominoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Aliaksei Semchankau on 12.08.2015.
 */

/**
 * This class provides a panel for painting
 */
public class DominoePanel extends JPanel {

    private int cellGap = 1;
    private int cellSize = UsefulFunctions.smallCell;

    private Color c0 = new Color(0x505050);
    private Color c1 = new Color(0xFFFFFF);

    /**
     * initial style
     */
    private UsefulFunctions.sizes sizeStyle = UsefulFunctions.sizes.SMALL;
    private UsefulFunctions.countries countryCode = UsefulFunctions.countries.BLR;

    private DominoeGrid dominoeGrid = null;

    protected void setSizeStyle(UsefulFunctions.sizes sizeStyle) {
        this.sizeStyle = sizeStyle;
        switch (sizeStyle) {
            case SMALL:
                cellSize = UsefulFunctions.smallCell;
                dominoeGrid = new DominoeGrid(UsefulFunctions.smallSize, UsefulFunctions.smallSize);
                break;
            case MIDDLE:
                cellSize = UsefulFunctions.middleCell;
                dominoeGrid = new DominoeGrid(UsefulFunctions.middleSize, UsefulFunctions.middleSize);
                break;
            case LARGE:
                cellSize = UsefulFunctions.largeCell;
                dominoeGrid = new DominoeGrid(UsefulFunctions.largeSize, UsefulFunctions.largeSize);
                break;
        }
        repaint();

    }

    protected void setColorStyle(UsefulFunctions.countries countryCode) {
        this.countryCode = countryCode;
        switch (countryCode) {
            case BLR:
                c1 = Color.WHITE;
                break;
            case RUS:
                c1 = Color.RED;
                break;
            case POL:
                c1 = Color.BLUE;
                break;
            case UKR:
                c1 = Color.WHITE;
                break;
        }
        repaint();
    }

    public DominoePanel() {
        //System.out.println("here!");
        setBackground(Color.BLACK);

        MouseAdapter mAdapter = new MouseAdapter() {

            private boolean pressedLeft = false;
            private boolean pressedRight = false;

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    pressedLeft = true;
                    pressedRight = false;
                    setCell(e);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    pressedLeft = false;
                    pressedRight = true;
                    setCell(e);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                setCell(e);
            }

            private void setCell(MouseEvent e) {
                // рассчитываем координаты клетки, на которую указывает
                // курсор мыши
                int x = e.getX() / (cellSize + cellGap);
                int y = e.getY() / (cellSize + cellGap);
                if (x >= 0 && y >= 0 && x < dominoeGrid.getWidth() && y < dominoeGrid.getHeight()) {
                    if (pressedLeft) {
                        dominoeGrid.setCell(x, y, true);
                        repaint();
                    }
                    if (pressedRight) {
                        dominoeGrid.setCell(x, y, false);
                        repaint();
                    }
                }
            }


        };
        addMouseListener(mAdapter);
        addMouseMotionListener(mAdapter);
    }

    public void initialize(int width, int height) {
        dominoeGrid = new DominoeGrid(width, height);
    }

    @Override
    public Dimension getPreferredSize() {
        if (dominoeGrid != null) {
            Insets b = getInsets();
            return new Dimension((cellSize + cellGap) * dominoeGrid.getWidth() + cellGap + b.left + b.right + 35,
                    (cellSize + cellGap) * dominoeGrid.getHeight() + cellGap + b.top + b.bottom + 35);
        } else
            return new Dimension(100, 100);
    }

    @Override
    public void paint(Graphics g) {
        if (dominoeGrid != null) {
            super.paint(g);
            Insets b = getInsets();
            for (int y = 0; y < dominoeGrid.getHeight(); y++) {
                for (int x = 0; x < dominoeGrid.getWidth(); x++) {
                    boolean cellFlag = dominoeGrid.getCell(x, y);
                    g.setColor((cellFlag) ? c1 : c0);
                    g.fillRect(b.left + cellGap + x * (cellSize + cellGap), b.top + cellGap + y
                            * (cellSize + cellGap), cellSize, cellSize);
                }
            }
        }
    }

    public void clearGrid() {
        for (int i = 0; i < dominoeGrid.getWidth(); ++i) {
            for (int j = 0; j < dominoeGrid.getHeight(); ++j) {
                dominoeGrid.setCell(i, j, false);
                int sz = dominoeGrid.getWidth();
                if (Math.abs(2 * i - sz + 1) + Math.abs(2 * j - sz + 1) <= sz)
                    dominoeGrid.setCell(i, j, true);
            }
        }
        repaint();
    }

    public DominoeGrid getDominoeGrid() {
        return dominoeGrid;
    }

}

