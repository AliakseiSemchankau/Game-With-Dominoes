package dominoe;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigInteger;

/**
 * Created by Aliaksei Semchankau on 10.08.2015.
 */

public class Dominoe extends JFrame {

    private int width = UsefulFunctions.smallSize;
    private int height = UsefulFunctions.smallSize;

    private Graph graph = null;
    private DominoePanel dominoePanel = null;
    private JButton buttonExit = null;
    private JButton buttonClear = null;
    private JButton buttonConstructGraph = null;
    private JButton buttonRandomPartition = null;
    private JButton buttonUKR = null;
    private JButton buttonRUS = null;
    private JButton buttonBLR = null;
    private JButton buttonPOL = null;
    private JButton buttonSmall = null;
    private JButton buttonMiddle = null;
    private JButton buttonLarge = null;

    private JButton buttonLowAccuracy = null;
    private JButton buttonMiddleAccuracy = null;
    private JButton buttonHighAccuracy = null;

    /**
     * Initial values for style
     */
    private UsefulFunctions.sizes sizeStyle = UsefulFunctions.sizes.SMALL;
    private UsefulFunctions.countries countryCode = UsefulFunctions.countries.BLR;

    /**
     This class provides GUI + table for painting
     * @param title
     */
    public Dominoe(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        dominoePanel = new DominoePanel();
        dominoePanel.initialize(width, height);
        add(dominoePanel);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(true);
        add(toolBar, BorderLayout.NORTH);

        buttonClear = new JButton("Clear");
        toolBar.add(buttonClear);

        buttonExit = new JButton("Exit");
        toolBar.add(buttonExit);

        buttonConstructGraph = new JButton("Count of cuttings");
        toolBar.add(buttonConstructGraph);

        buttonRandomPartition = new JButton("Random partition");
        toolBar.add(buttonRandomPartition);

        toolBar.addSeparator();

        buttonBLR = new JButton("BLR");
        toolBar.add(buttonBLR);

        buttonPOL = new JButton("POL");
        toolBar.add(buttonPOL);

        buttonRUS = new JButton("RUS");
        toolBar.add(buttonRUS);

        buttonUKR = new JButton("UKR");
        toolBar.add(buttonUKR);

        toolBar.addSeparator();

        buttonSmall = new JButton("Small");
        toolBar.add(buttonSmall);

        buttonMiddle = new JButton("Middle");
        toolBar.add(buttonMiddle);

        buttonLarge = new JButton("Large");
        toolBar.add(buttonLarge);

        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dominoePanel.clearGrid();
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonConstructGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph = new Graph(dominoePanel.getDominoeGrid());
                if (!graph.constructMatrix(width, height)) {
                    JOptionPane.showMessageDialog(null, "Can't be cut into dominoes:(");
                    graph = null;
                } else {
                    JOptionPane.showMessageDialog(null, "Count of cuttings into dominoes: " + graph.countDeterminant());
                }
            }
        });

        buttonRandomPartition.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph = new Graph(dominoePanel.getDominoeGrid());
                if (!graph.constructMatrix(width, height) || graph.countDeterminant().equals(BigInteger.ZERO)) {
                    JOptionPane.showMessageDialog(null, "Can't be disjoined into dominoes");
                    graph = null;
                } else {
                    Graph.RandomPartition partition = graph.takeRandomPartition();
                    PartitionFrame partitionFrame = new PartitionFrame(width, height,
                            partition.takeHorizontals(), partition.takeVerticals(),
                            countryCode, sizeStyle);
                    try {
                        PNGWriter.printPNGFile(width, height,
                                partition.takeHorizontals(), partition.takeVerticals(),
                                countryCode, sizeStyle);
                    } catch (Exception exc) {
                        JOptionPane.showMessageDialog(null, "can't write it to file");
                    }
                }
            }
        });

        buttonBLR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countryCode = UsefulFunctions.countries.BLR;
                dominoePanel.setColorStyle(countryCode);
            }
        });

        buttonUKR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countryCode = UsefulFunctions.countries.UKR;
                dominoePanel.setColorStyle(countryCode);
            }
        });

        buttonRUS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countryCode = UsefulFunctions.countries.RUS;
                dominoePanel.setColorStyle(countryCode);
            }
        });

        buttonPOL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countryCode = UsefulFunctions.countries.POL;
                dominoePanel.setColorStyle(countryCode);
            }
        });

        buttonSmall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sizeStyle = UsefulFunctions.sizes.SMALL;
                width = UsefulFunctions.smallSize;
                height = UsefulFunctions.smallSize;
                dominoePanel.setSizeStyle(sizeStyle);
            }
        });

        buttonMiddle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sizeStyle = UsefulFunctions.sizes.MIDDLE;
                width = UsefulFunctions.middleSize;
                height = UsefulFunctions.middleSize;
                dominoePanel.setSizeStyle(sizeStyle);
            }
        });

        buttonLarge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sizeStyle = UsefulFunctions.sizes.LARGE;
                width = UsefulFunctions.largeSize;
                height = UsefulFunctions.largeSize;
                dominoePanel.setSizeStyle(sizeStyle);
            }
        });

        pack();
        setVisible(true);

    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Dominoe("Dominoe");
            }
        });

    }



}
