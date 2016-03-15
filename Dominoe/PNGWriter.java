package dominoe;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Aliaksei Semchankau on 14.08.2015.
 */
public class PNGWriter {

    private static int width;
    private static int height;
    private static int cellGap = 1;
    private static int cellSize = 47;
    private static Color c0 = new Color(0x505050);
    private static Color c1 = new Color(0xFFFFFF);
    private static Color horColor = Color.GREEN;
    private static Color verColor = Color.RED;

    java.util.List<Move> horDominoes = null;
    java.util.List<Move> verDominoes = null;




    public static void printPNGFile(final int w, final int h,
                                    final List<Move> horizontalDominoes, final List<Move> verticalDominoes,
                                    final UsefulFunctions.countries countryCode, final UsefulFunctions.sizes sizeStyle) throws Exception {
        width = w;
        height = h;

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
            case SMALL:
                cellSize = UsefulFunctions.smallCell;
                break;
            case MIDDLE:
                cellSize = UsefulFunctions.middleCell;
                break;
            case LARGE:
                cellSize = UsefulFunctions.largeCell;
                break;
        }

        try {

            int imageWidth = (cellSize + cellGap) * width + cellGap;
            int imageHeight = (cellSize + cellGap) * height + cellGap;

            BufferedImage bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D ig2 = bi.createGraphics();

            ig2.setPaint(Color.BLACK);
            ig2.setBackground(Color.BLACK);
            ig2.setColor(Color.BLACK);
            ig2.fillRect(0, 0, imageWidth, imageHeight);
           /* for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    ig2.setColor(c0);
                    ig2.fillRect(cellGap + x * (cellSize + cellGap), cellGap + y
                            * (cellSize + cellGap), cellSize, cellSize);
                }
            }*/
            for (Move move : horizontalDominoes) {
                int x = move.getX();
                int y = move.getY();
                ig2.setColor(horColor);
                ig2.fillRect(cellGap + x * (cellSize + cellGap), cellGap + y
                        * (cellSize + cellGap), 2 * cellSize + 1, cellSize);
            }

            for (Move move : verticalDominoes) {
                int x = move.getX();
                int y = move.getY();
                ig2.setColor(verColor);
                ig2.fillRect(cellGap + x * (cellSize + cellGap), cellGap + y
                        * (cellSize + cellGap), cellSize, 2 * cellSize + 1);
            }

            String dir = System.getProperty("user.dir");
            dir += "\\Pictures\\";

            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy-hh-mm-ss");
            String fileName = sdf.format(new Date());


            //System.out.println(dir + fileName);

            ImageIO.write(bi, "PNG", new File(dir + fileName + ".PNG"));
            ImageIO.write(bi, "JPEG", new File(dir + fileName + ".JPG"));
            ImageIO.write(bi, "gif", new File(dir + fileName + ".GIF"));
            ///ImageIO.write(bi, "BMP", new File(dir + fileName + ".BMP"));

        } catch (IOException ie) {
            ie.printStackTrace();
        }

    }


}
