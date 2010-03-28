package gdcalendar.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
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
    private int orientation;                    //The current orientation of the panel
                                                //can attain the values NORTH,WEST,SOUTH or EAST
    private int buttonSize = 5;                 //The default collapsButton size
    private int arrowSize;                      //The size of the arrow, displayed in the 
    private Polygon downArrow, leftArrow, upArrow, rightArrow;

    /**
     * Create a collapsible panel, with a collapse/expand button located
     * at the given location
     * @param   collapseOrientation the location where the collapse/expand
     *          button should be located
     */
    public CollapsiblePanel(int collapseOrientation) {

        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        orientation = collapseOrientation;

        //Check if the collapseOrientation is in the correct range
        if (collapseOrientation > 4 || collapseOrientation < 1) {
            throw new IllegalArgumentException("collapseOrientation is not in the correct range");
        }
        //Make sure the arrow is large enough
        if (buttonSize < 4) {
            arrowSize = buttonSize;
        } else {
            arrowSize = buttonSize - 1;
        }
        //Create all the arrows, one of them displayed on the collapseButton
        arrowUp(arrowSize);
        arrowDown(arrowSize);
        arrowRight(arrowSize);
        arrowLeft(arrowSize);


        //Set up expand/minimizer component depending on input argument
        switch (collapseOrientation) {

            case NORTH:
                collapseButton = new JButton() {

                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.GRAY);
                        if (expanded) {
                            g.translate(getSize().width / 2, 0);
                            g.fillPolygon(upArrow);
                        } else {
                            g.translate(getSize().width / 2, getSize().height);
                            g.fillPolygon(downArrow);
                        }
                        g.dispose();
                    }
                };

                north = new JPanel(new BorderLayout());
                north.add(collapseButton, BorderLayout.CENTER);
                north.setPreferredSize(new Dimension(0, buttonSize));
                north.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                add(north, BorderLayout.PAGE_START);
                break;

            case EAST:
                collapseButton = new JButton() {

                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.GRAY);
                        if (expanded) {
                            g.translate(0, getSize().height / 2);
                            g.fillPolygon(leftArrow);
                        } else {
                            g.translate(getSize().width, getSize().height / 2);
                            g.fillPolygon(rightArrow);
                        }
                        g.dispose();
                    }
                };
                east = new JPanel(new BorderLayout());
                east.add(collapseButton, BorderLayout.CENTER);
                east.setPreferredSize(new Dimension(buttonSize, 0));
                east.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                add(east, BorderLayout.LINE_END);
                break;

            case SOUTH:
                collapseButton = new JButton() {

                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.GRAY);
                        if (expanded) {
                            g.translate(getSize().width / 2, getSize().height);
                            g.fillPolygon(downArrow);
                        } else {
                            g.translate(getSize().width / 2, 0);
                            g.fillPolygon(upArrow);
                        }
                        g.dispose();
                    }
                };
                south = new JPanel(new BorderLayout());
                south.add(collapseButton, BorderLayout.CENTER);
                south.setPreferredSize(new Dimension(0, buttonSize));
                south.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                add(south, BorderLayout.PAGE_END);
                break;

            case WEST:
                collapseButton = new JButton() {

                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.GRAY);
                        if (expanded) {
                            g.translate(getSize().width, getSize().height / 2);
                            g.fillPolygon(rightArrow);
                        } else {
                            g.translate(0, getSize().height / 2);
                            g.fillPolygon(leftArrow);
                        }
                        g.dispose();
                    }
                };
                west = new JPanel(new BorderLayout());
                west.add(collapseButton, BorderLayout.CENTER);
                west.setPreferredSize(new Dimension(buttonSize, 0));
                west.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                add(west, BorderLayout.LINE_START);
                break;
        }
        //Remove all mouseListners from the button, so that it will not change
        //apperence while hovering over the button
        collapseButton.removeMouseListener(collapseButton.getMouseListeners()[0]);

        collapseButton.addMouseListener(this);
        add(contentPanel, BorderLayout.CENTER);
    }

    //Create a arrow pointing downwards
    private void arrowDown(int size) {
        int[] xPoints = {0, size, -size};
        int[] yPoints = {0, -size, -size};
        downArrow = new Polygon(xPoints, yPoints, 3);
    }

    //Create a arrow pointing upwards
    private void arrowUp(int size) {
        int[] xPoints = {0, -size, size};
        int[] yPoints = {0, size, size};
        upArrow = new Polygon(xPoints, yPoints, 3);
    }

    //Create a arrow pointing to the left
    private void arrowLeft(int size) {
        int[] xPoints = {0, size, size};
        int[] yPoints = {0, size, -size};
        leftArrow = new Polygon(xPoints, yPoints, 3);
    }

    //Create a arrow pointing to the right
    private void arrowRight(int size) {
        int[] xPoints = {0, -size, -size};
        int[] yPoints = {0, -size, size};
        rightArrow = new Polygon(xPoints, yPoints, 3);
    }

    /**
     * Get the panel which is supposed to contain the content of the collapsiblePanel
     * @return The container holding the content of the collapsiblePanel
     */
    public Container getContentPanel() {
        return contentPanel;
    }

    /**
     * Set the size of the collapsible panel button
     * @param size The new size of the button in pixels (0>=)
     */
    public void setCollapsButtonSize(int size) {
        buttonSize = size;
        //Redefine the collapseButton, and add it to the collapsiblePanel
        switch (orientation) {
            case NORTH:
                collapseButton.setPreferredSize(new Dimension(0, buttonSize));
                north.add(collapseButton,BorderLayout.PAGE_START);
                break;
            case WEST:
                collapseButton.setPreferredSize(new Dimension(buttonSize,0));
                west.add(collapseButton,BorderLayout.LINE_START);
                break;
            case SOUTH:
                collapseButton.setPreferredSize(new Dimension(0, buttonSize));
                south.add(collapseButton,BorderLayout.PAGE_END);
                break;
            case EAST:
                collapseButton.setPreferredSize(new Dimension(buttonSize,0));
                east.add(collapseButton,BorderLayout.LINE_END);
                break;
        }
        //Make sure the arrow is large enough
        if (buttonSize < 4) {
            arrowSize = buttonSize;
        } else {
            arrowSize = buttonSize - 1;
        }
        //Recreate the arrows, one of them displayed on the collapseButton
        arrowUp(arrowSize);
        arrowDown(arrowSize);
        arrowRight(arrowSize);
        arrowLeft(arrowSize);
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
