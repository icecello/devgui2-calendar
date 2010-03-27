package gdcalendar.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A custom panel, showing a button which makes the panel collapse/expand.
 * The button can be chosen to be shown at the follwing locations:
 * NORTH,WEST,SOUTH and EAST
 * @author Tomas
 */
public class CollapsiblePanel extends JPanel implements MouseListener {

    /**
     * Indicate where what orientation the collapsisble panel has.
     * I.e. NORTH indicate that the expand/minimizer button is located at
     * the very top of the panel, WEST indicate that the button is located to
     * the very left of the panel.
     */
    public static final int NORTH = 1, WEST = 2, SOUTH = 3, EAST = 4;
    private JPanel contentPanel;                //Content panel, available to the user
    private JPanel north, south, west, east;    //Panels containing exp/min button
    private boolean expanded = true;            //Indicate if panel expanded
    private JButton collapseButton;             //Button making panel expand/collapse
    private int buttonSize = 10;            

    /**
     * Create a collapsible panel, with a collapse/expand button located
     * at the given location
     * @param   collapseOrientation the location where the collapse/expand
     *          button should be located
     */
    public CollapsiblePanel(int collapseOrientation) {
        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        //Button used for minimizing/expanding the panel

        //Check if the collapseOrientation is in the correct range
        if(collapseOrientation>4 || collapseOrientation<1)
            throw new IllegalArgumentException("collapseOrientation is not in the correct range");


        //Set up expand/minimizer component depending on input argument
        switch (collapseOrientation) {
            case NORTH:
                collapseButton = new JButton() {

                    int arcSize = 16;

                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.GRAY);
                        if (expanded) {
                            g.fillArc((getSize().width- arcSize)/2, -5, arcSize, arcSize, 225, 90);
                        } else {
                            g.fillArc((getSize().width- arcSize)/2, -2, arcSize, arcSize, 45, 90);
                        }
                    }
                };
                north = new JPanel(new BorderLayout());
                north.add(collapseButton, BorderLayout.CENTER);
                north.setPreferredSize(new Dimension(0, buttonSize));
                north.setMinimumSize(new Dimension(0, buttonSize));
                north.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                add(north, BorderLayout.PAGE_START);
                break;

            case EAST:
                collapseButton = new JButton() {

                    int arcSize = 16;

                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.GRAY);
                        if (expanded) {
                            g.fillArc(-4, (getSize().height - arcSize) / 2, arcSize, arcSize, -45, 90);
                        } else {
                            g.fillArc(-2, (getSize().height - arcSize) / 2, arcSize, arcSize, 135, 90);
                        }
                    }
                };
                east = new JPanel(new BorderLayout());
                east.add(collapseButton, BorderLayout.CENTER);
                east.setPreferredSize(new Dimension(buttonSize, 0));
                east.setMinimumSize(new Dimension(buttonSize, 0));
                east.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                add(east, BorderLayout.LINE_END);
                break;

            case SOUTH:
                collapseButton = new JButton() {

                    int arcSize = 16;

                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.GRAY);
                        if (expanded) {
                            g.fillArc((getSize().width- arcSize)/2, -2, arcSize, arcSize, 45, 90);
                        } else {
                            g.fillArc((getSize().width- arcSize)/2, -5, arcSize, arcSize, 225, 90);
                        }
                    }
                };
                south = new JPanel(new BorderLayout());
                south.add(collapseButton, BorderLayout.CENTER);
                south.setPreferredSize(new Dimension(0, buttonSize));
                south.setMinimumSize(new Dimension(0, buttonSize));
                south.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                add(south, BorderLayout.PAGE_END);
                break;

            case WEST:
                collapseButton = new JButton() {

                    int arcSize = 16;

                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.GRAY);
                        if (expanded) {
                            g.fillArc(-2, (getSize().height - arcSize) / 2, arcSize, arcSize, 135, 90);
                        } else {
                            g.fillArc(-4, (getSize().height - arcSize) / 2, arcSize, arcSize, -45, 90);
                        }
                    }
                };
                west = new JPanel(new BorderLayout());
                west.add(collapseButton, BorderLayout.CENTER);
                west.setPreferredSize(new Dimension(buttonSize, 0));
                west.setMinimumSize(new Dimension(buttonSize, 0));
                west.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                add(west, BorderLayout.LINE_START);
                break;
        }
        //Remove all mouseListners from the button, so that it will not change
        //apperence while hoovering over the button
        collapseButton.removeMouseListener(collapseButton.getMouseListeners()[0]);
        
        collapseButton.addMouseListener(this);
        add(contentPanel, BorderLayout.CENTER);
    }

    public Container getContentPane() {
        return contentPanel;
    }

    public void mouseClicked(MouseEvent e) {
        if (expanded) {
            contentPanel.setVisible(false);
        } else {
            contentPanel.setVisible(true);
        }
        expanded = !expanded;
        repaint();
    }

    public void mousePressed(MouseEvent e) {
    //Don't do anything for this event
    }

    public void mouseReleased(MouseEvent e) {
    //Don't do anything for this event
    }

    public void mouseEntered(MouseEvent e) {
    //Don't do anything for this event
    }

    public void mouseExited(MouseEvent e) {
    //Don't do anything for this event
    }
}
