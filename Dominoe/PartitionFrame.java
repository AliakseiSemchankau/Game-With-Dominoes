package dominoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Aliaksei Semchankau on 12.08.2015.
 */

public class PartitionFrame extends JFrame {

    private PartitionPanel partitionPanel = null;

    public PartitionFrame(final int width, final int height,
                          final List<Move> horizontalDominoes, final List<Move> verticalDominoes,
                          UsefulFunctions.countries countryCode, UsefulFunctions.sizes sizeStyle){
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        partitionPanel = new PartitionPanel(width, height, horizontalDominoes, verticalDominoes, countryCode, sizeStyle);

        add(partitionPanel);

        setLocationRelativeTo(null);

        pack();
        setVisible(true);
    }

}
